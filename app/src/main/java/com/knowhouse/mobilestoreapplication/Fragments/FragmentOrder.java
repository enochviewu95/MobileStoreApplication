package com.knowhouse.mobilestoreapplication.Fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.knowhouse.mobilestoreapplication.DataSettersAndGetters.CFood;
import com.knowhouse.mobilestoreapplication.DataSettersAndGetters.CFoodDetails;
import com.knowhouse.mobilestoreapplication.R;
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

public class FragmentOrder extends Fragment {

    public static final String FOOD_OBJ = "food_obj";
    private ArrayList<CFoodDetails> foodDetailsList;
    private int position;
    private RadioGroup optionsLayout;
    private EditText foodQuantityValue;
    private TextView foodTotalPrice;
    private int checkedRadioButton;
    private int foodQuantityInt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_order, container, false);
        optionsLayout = view.findViewById(R.id.optionsLayout);

        TextView foodNameValue = view.findViewById(R.id.foodNameValue);
        TextView foodDescriptionValue = view.findViewById(R.id.foodDescriptionValue);
        foodQuantityValue = view.findViewById(R.id.foodQuantityValue);
        foodTotalPrice = view.findViewById(R.id.foodPriceValue);

        Bundle args = getArguments();
        assert args != null;
        CFood foodObj = (CFood) args.getSerializable(FragmentOrder.FOOD_OBJ);
        assert foodObj != null;
        position = foodObj.getFoodId();
        foodNameValue.setText(foodObj.getFoodName());
        foodDescriptionValue.setText(foodObj.getFoodDescription());
        getFoodDetails();
        // Inflate the layout for this fragment

        foodQuantityValue.addTextChangedListener(foodQuantityTextWatcher);
        optionsLayout.setOnCheckedChangeListener(changeListener);

        return view;
    }

    private void getFoodDetails(){
        String url = ConstantURL.SITE_URL+"FoodDetails.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    JSONObject content = null;
                    JSONArray foodDetailsArray = null;
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


    private void dynamicLayout(ArrayList<CFoodDetails> foodDetailsList){ ;
        final RadioButton[] radioButtons = new RadioButton[foodDetailsList.size()];
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
        StringBuilder builder = new StringBuilder("GHS")
                .append(productBigDecimal);
        foodTotalPrice.setText(builder.toString());
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

}
