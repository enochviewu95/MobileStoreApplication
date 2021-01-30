package com.knowhouse.mobilestoreapplication.Fragments;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.knowhouse.mobilestoreapplication.DataSettersAndGetters.CFood;
import com.knowhouse.mobilestoreapplication.DataSettersAndGetters.CFoodDetails;
import com.knowhouse.mobilestoreapplication.R;
import com.knowhouse.mobilestoreapplication.StorageUtilities.CartReaderContract;
import com.knowhouse.mobilestoreapplication.StorageUtilities.CartReaderDbHelper;
import com.knowhouse.mobilestoreapplication.VolleyRequests.ConstantURL;
import com.knowhouse.mobilestoreapplication.VolleyRequests.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FragmentOrder extends Fragment{

    public static final String FOOD_OBJ = "food_obj";
    private RadioGroup optionsLayout;
    private TextView foodTotalPrice;
    private RadioButton[] radioButtons;

    private int position;
    private int checkedRadioButton;
    private int foodQuantityInt;

    private ArrayList<CFoodDetails> foodDetailsList;
    private  CFood foodObj;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_order, container, false);

        TextView foodNameValue = view.findViewById(R.id.foodNameValue);
        TextView foodDescriptionValue = view.findViewById(R.id.foodDescriptionValue);
        EditText foodQuantityValue = view.findViewById(R.id.foodQuantityValue);
        Button addToCart = view.findViewById(R.id.add_to_cart_button);

        optionsLayout = view.findViewById(R.id.optionsLayout);
        foodTotalPrice = view.findViewById(R.id.foodPriceValue);

        Bundle args = getArguments();

        assert args != null;
        foodObj = (CFood) args.getSerializable(FragmentOrder.FOOD_OBJ);
        assert foodObj != null;
        position = foodObj.getFoodId();

        foodNameValue.setText(foodObj.getFoodName());
        foodDescriptionValue.setText(foodObj.getFoodDescription());
        getFoodDetails();

        foodQuantityValue.addTextChangedListener(foodQuantityTextWatcher);
        optionsLayout.setOnCheckedChangeListener(changeListener);
        addToCart.setOnClickListener(clickListener);

        return view;
    }

    private void getFoodDetails(){
        String url = ConstantURL.SITE_URL+"FoodDetails.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    JSONObject content;
                    JSONArray foodDetailsArray;
                    foodDetailsList = new ArrayList<>();
                    try{
                        content = new JSONObject(response);
                        if(!content.getBoolean("error")){
                            foodDetailsArray = new JSONArray(content.
                                    getString("content"));

                            for(int i = 0; i<foodDetailsArray.length();i++){
                                JSONObject foodDetails = foodDetailsArray.getJSONObject(i);
                                int id = foodDetails.getInt("id");
                                double foodPrices = foodDetails.getDouble("food_prices");
                                String foodSizes = foodDetails.getString("food_sizes");
                                int foodId = foodDetails.getInt("food_id");
                                CFoodDetails myFoodDetails = new CFoodDetails(id,foodPrices,foodSizes,foodId);
                                foodDetailsList.add(myFoodDetails);
                            }

                            dynamicLayout(foodDetailsList);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {

                }
        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("food_id",String.valueOf(position));
                return params;
            }
        };

        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }


    private void dynamicLayout(ArrayList<CFoodDetails> foodDetailsList){
        radioButtons = new RadioButton[foodDetailsList.size()];
        for (int i = 0; i <foodDetailsList.size() ; i++) {
            radioButtons[i] = new RadioButton(getContext());
            CFoodDetails foodDetails = foodDetailsList.get(i);
            StringBuilder builder = new StringBuilder(" ")
                .append(foodDetails.getFoodSizes())
                .append(" ")
                .append("GHS")
                .append(foodDetails.getFoodPrice());
            radioButtons[i].setText(builder);
            radioButtons[i].setEms(11);
            radioButtons[i].setTextAppearance(android.R.style.TextAppearance_Medium);
            radioButtons[i].setTextColor(getResources().getColor(android.R.color.darker_gray));
            radioButtons[i].setId(i);
            optionsLayout.addView(radioButtons[i]);
        }
        radioButtons[0].setChecked(true);

    }


    private final RadioGroup.OnCheckedChangeListener changeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            checkedRadioButton = checkedId;
            calculate();
        }
    };

    private void calculate(){
        BigDecimal foodQuantityBigDecimal = new BigDecimal(foodQuantityInt);
        BigDecimal foodQuantity = foodQuantityBigDecimal.setScale(2,RoundingMode.HALF_EVEN);
        CFoodDetails foodDetails = foodDetailsList.get(checkedRadioButton);
        BigDecimal product = foodQuantity.multiply(foodDetails.getFoodPrice());
        BigDecimal productBigDecimal = product.setScale(2,RoundingMode.HALF_EVEN);
        String builder = "GHS" +
                productBigDecimal;
        foodTotalPrice.setText(builder);
    }

    private final TextWatcher foodQuantityTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            try{
                foodQuantityInt = Integer.parseInt(s.toString());
            }catch (NumberFormatException e){
                foodQuantityInt = 0;
            }
            calculate();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private final Button.OnClickListener clickListener = v -> new CGetCartDetailsTask().execute();




    private class CGetCartDetailsTask extends AsyncTask<Object,Void,Boolean> {

        ContentValues values;
        boolean isQuantitySet;

        @Override
        protected void onPreExecute() {

            TextView foodNameValue = view.findViewById(R.id.foodNameValue);
            TextView foodDescriptionValue = view.findViewById(R.id.foodDescriptionValue);
            EditText foodQuantityValue = view.findViewById(R.id.foodQuantityValue);
            RadioGroup foodOptionsLayout = view.findViewById(R.id.optionsLayout);
            TextView foodPriceValue = view.findViewById(R.id.foodPriceValue);


            if(foodQuantityValue.getText().toString().isEmpty()){
                isQuantitySet = false;
            }else{

                int checkedRadioButtonId = foodOptionsLayout.getCheckedRadioButtonId();
                RadioButton radioButton = radioButtons[checkedRadioButtonId];

                String foodUrl = foodObj.getFoodImageUrl();
                String foodName = foodNameValue.getText().toString();
                String foodDescription = foodDescriptionValue.getText().toString();
                String foodQuantity = foodQuantityValue.getText().toString();
                String foodOption = radioButton.getText().toString();
                String foodPrice = foodPriceValue.getText().toString()
                        .replace("GHS","");

                values = new ContentValues();
                values.put(CartReaderContract.CartEntry.COLUMN_IMAGE_URL,foodUrl);
                values.put(CartReaderContract.CartEntry.COLUMN_FOOD_VALUE_TEXT,foodName);
                values.put(CartReaderContract.CartEntry.COLUMN_DESCRIPTION_VALUE_TEXT,foodDescription);
                values.put(CartReaderContract.CartEntry.COLUMN_QUANTITY_VALUE_TEXT,foodQuantity);
                values.put(CartReaderContract.CartEntry.COLUMN_OPTION_VALUE_TEXT,foodOption);
                values.put(CartReaderContract.CartEntry.COLUMN_PRICE_VALUE_TEXT,foodPrice);
                isQuantitySet = true;
            }

        }

        @Override
        protected Boolean doInBackground(Object... objects) {
            SQLiteOpenHelper sqLiteOpenHelper = new CartReaderDbHelper(getContext());
            try{
                if(isQuantitySet){
                    SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
                    db.insert(CartReaderContract.CartEntry.TABLE_NAME,null,values);
                    db.close();
                    return true;
                }
                return false;
            }catch (SQLException e){
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(!isQuantitySet){
                Toast.makeText(getContext(),"Please specify the quantity of orders",Toast.LENGTH_LONG)
                        .show();
            } else{
                if(!aBoolean){
                    Toast.makeText(getContext(),"Couldn't insert value",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getContext(),"Order inserted into cart",Toast.LENGTH_LONG).show();
                }
            }

        }
    }




}
