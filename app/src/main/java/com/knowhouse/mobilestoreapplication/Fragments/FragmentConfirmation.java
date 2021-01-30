package com.knowhouse.mobilestoreapplication.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.knowhouse.mobilestoreapplication.DataSettersAndGetters.CCart;
import com.knowhouse.mobilestoreapplication.R;
import com.knowhouse.mobilestoreapplication.StorageUtilities.CartReaderContract;
import com.knowhouse.mobilestoreapplication.StorageUtilities.CartReaderDbHelper;
import com.knowhouse.mobilestoreapplication.VolleyRequests.ConstantURL;
import com.knowhouse.mobilestoreapplication.VolleyRequests.MySingleton;
import com.knowhouse.mobilestoreapplication.VolleyRequests.SharedPrefManager;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FragmentConfirmation extends DialogFragment {

    private String fullName;
    private String email;
    private String phoneNumber;
    private int totalFoodQuantity;
    private int customerId;
    private BigDecimal totalFoodPrice;

    private TextView totalPriceText;
    private TextView totalQuantityText;
    private TextView totalPriceValue;
    private TextView totalQuantityValue;
    private TextView priceDottedLineOne;
    private TextView priceDottedLineTwo;

    private ArrayList<CCart> myDetails;

    private GridLayout layout;
    private GridLayout.LayoutParams gridLayout;

    private boolean isSavedInDatabase;

    private LinearLayout linearLayout;

    private boolean makeOrderReturnedValue;

    private ProgressDialog progressDialog;



    public static final String ORDER_ARRAY_LIST = "orderArrayList" ;


    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(getContext());
        fullName = sharedPrefManager.getUserFullName();
        email = sharedPrefManager.getUserEmail();
        phoneNumber = sharedPrefManager.getPhoneNumber();
        totalFoodPrice = new BigDecimal("0.00");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        assert getArguments() != null;
        Bundle bundle = getArguments();
        myDetails = bundle.getParcelableArrayList(FragmentConfirmation.ORDER_ARRAY_LIST);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_confirmation,null);
        linearLayout = view.findViewById(R.id.order_detail_layout);
        progressDialog = new ProgressDialog(getContext());


        View title = inflater.inflate(R.layout.title_layout,null);
        TextView fullNameTextView = view.findViewById(R.id.customer_name);
        TextView emailTextView = view.findViewById(R.id.customer_email);
        TextView phoneNumberTextView = view.findViewById(R.id.customer_phone);


        layout = new GridLayout(getContext());
        gridLayout = new GridLayout.LayoutParams();
        totalPriceText = new TextView(getContext());
        totalQuantityText = new TextView(getContext());
        totalPriceValue = new TextView(getContext());
        totalQuantityValue = new TextView(getContext());
        priceDottedLineOne = new TextView(getContext());
        priceDottedLineTwo = new TextView(getContext());

        fullNameTextView.setText(fullName);
        emailTextView.setText(email);
        phoneNumberTextView.setText(phoneNumber);

        populateDialog();

        builder.setCustomTitle(title)
                .setView(view)
                .setPositiveButton("Place Order", (dialog, which) -> {

                    progressDialog.setMessage("Sending Order");
                    progressDialog.show();
                    makeOrder();
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.cancel(); });

        return builder.create();
    }


    private void populateDialog(){
        assert myDetails != null;
        for (int i = 0; i <myDetails.size() ; i++) {

            CCart cartItem = myDetails.get(i);
            String foodName = cartItem.getFoodName();
            String foodOption = cartItem.getFoodOption();
            String foodQuantity = cartItem.getFoodQuantity();
            String foodPrice = cartItem.getFoodPrice();
            String foodPriceGhanaCedis = "GHS"+foodPrice;

            GridLayout temp = initializeViewLayout(foodName,foodOption,foodPriceGhanaCedis,foodQuantity);

            int foodQuantityNum = Integer.parseInt(foodQuantity);
            double foodPriceNum = Double.parseDouble(foodPrice);
            BigDecimal bigDecimal = new BigDecimal(foodPriceNum);

            totalFoodQuantity += foodQuantityNum;
            totalFoodPrice = totalFoodPrice.add(bigDecimal);

            linearLayout.addView(temp);

        }

        totalQuantityText.setText(getResources().getText(R.string.total_quantity));

        String totalFoodQuantityOrder;

        if(totalFoodQuantity == 1){
            totalFoodQuantityOrder = totalFoodQuantity + " order";
        }else{
            totalFoodQuantityOrder = totalFoodQuantity + " orders";
        }

        totalQuantityValue.setText(totalFoodQuantityOrder);

        totalPriceText.setText(getResources().getText(R.string.sum_total));

        String totalFoodPriceGhanaCedis = "GHS"+totalFoodPrice;
        totalPriceValue.setText(totalFoodPriceGhanaCedis);

        priceDottedLineOne.setText("");
        priceDottedLineTwo.setText("");

        gridLayout.setMargins(0,50,0,0);
        layout.setColumnCount(3);
        layout.setUseDefaultMargins(true);
        layout.addView(totalQuantityText,0);
        layout.addView(priceDottedLineOne,1);
        layout.addView(totalQuantityValue,2);
        layout.addView(totalPriceText,3);
        layout.addView(priceDottedLineTwo,4);
        layout.addView(totalPriceValue,5);
        layout.setLayoutParams(gridLayout);

        linearLayout.addView(layout);

    }

    private GridLayout initializeViewLayout(String foodName, String foodOption,
                                            String foodPriceGhanaCedis, String foodQuantity){

        GridLayout temp = new GridLayout(getContext());
        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
        layoutParams.setMargins(0,42,0,0);

        TextView foodNameText = new TextView(getContext());
        TextView foodOptionText = new TextView(getContext());
        TextView foodQuantityText = new TextView(getContext());
        TextView foodPriceText = new TextView(getContext());

        TextView foodNameTextTitle = new TextView(getContext());
        TextView foodOptionTextTitle = new TextView(getContext());
        TextView foodQuantityTextTitle = new TextView(getContext());
        TextView foodPriceTextTitle = new TextView(getContext());
        TextView priceDottedLine = new TextView(getContext());

        foodNameTextTitle.setText(getResources().getText(R.string.food));
        foodOptionTextTitle.setText(getResources().getText(R.string.options));
        foodQuantityTextTitle.setText(getResources().getText(R.string.quantity));
        foodPriceTextTitle.setText(getResources().getText(R.string.price));

        priceDottedLine.setText("");

        foodNameText.setText(foodName);
        foodOptionText.setText(foodOption);
        foodQuantityText.setText(foodQuantity);
        foodPriceText.setText(foodPriceGhanaCedis);

        foodOptionText.setEms(9);

        temp.setColumnCount(3);
        temp.setUseDefaultMargins(true);
        temp.addView(foodNameTextTitle,0);
        temp.addView(foodNameText,1);
        temp.addView(new TextView(getContext()),2);
        temp.addView(foodOptionTextTitle,3);
        temp.addView(foodOptionText,4);
        temp.addView(new TextView(getContext()),5);
        temp.addView(foodQuantityTextTitle,6);
        temp.addView(foodQuantityText,7);
        temp.addView(new TextView(getContext()),8);
        temp.addView(foodPriceTextTitle,9);
        temp.addView(priceDottedLine,10);
        temp.addView(foodPriceText,11);
        temp.setLayoutParams(layoutParams);

        return temp;
    }

    private void makeOrder(){
        for (int i = 0; i <myDetails.size() ; i++) {
            CCart cartItem = myDetails.get(i);
            String foodName = cartItem.getFoodName();
            String foodOption = cartItem.getFoodOption();
            String foodQuantity = cartItem.getFoodQuantity();
            String foodPrice = cartItem.getFoodPrice();
            String foodSizes = foodOption.replaceAll("(GHS\\d*.\\d*)","").trim();
            uploadOrder(foodName,foodSizes,foodQuantity,foodPrice);
        }

        if(makeOrderReturnedValue) isSavedInDatabase = true;
    }


    private void uploadOrder(String foodName, String foodOption,
                             String foodQuantity, String foodPrice) {
        String url = ConstantURL.SITE_URL+"UploadCart.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    if(response.equals("1")){
                        makeOrderReturnedValue = true;
                    }
                }, Throwable::printStackTrace
        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("customer_name",fullName);
                params.put("customer_email",email);
                params.put("customer_phone",phoneNumber);
                params.put("food_name",foodName);
                params.put("food_option",foodOption);
                params.put("food_quantity",foodQuantity);
                params.put("food_price",foodPrice);
                return params;
            }
        };


        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        new ClearSQLiteAsyncTask().execute();
    }

    private class ClearSQLiteAsyncTask extends AsyncTask<Object,Integer,Boolean>{


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            for(int value : values){
                progressDialog.setProgress(value);
            }
        }

        @Override
        protected Boolean doInBackground(Object... objects) {

                SQLiteOpenHelper sqLiteOpenHelper = new CartReaderDbHelper(getContext());
                try{
                    SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
                    db.delete(CartReaderContract.CartEntry.TABLE_NAME,null,null);
                    db.close();
                    return true;
                }catch (SQLException e){
                    e.printStackTrace();
                    return false;
                }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            progressDialog.dismiss();
            BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottom_nav); //Reference to bottom navigation view
            bottomNav.getMenu().findItem(R.id.fragmentContentSwitch).setEnabled(true);//link the controller to the bottom navigation

            Toast.makeText(getContext(),"Your order has been sent",Toast.LENGTH_SHORT).show();
        }
    }

}
