package com.knowhouse.mobilestoreapplication.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.knowhouse.mobilestoreapplication.DataSettersAndGetters.CCart;
import com.knowhouse.mobilestoreapplication.R;
import com.knowhouse.mobilestoreapplication.VolleyRequests.SharedPrefManager;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;

public class FragmentConfirmation extends DialogFragment {

    private String fullName;
    private String email;
    private String phoneNumber;
    private int totalFoodQuantity;
    private BigDecimal totalFoodPrice;

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
        ArrayList<CCart> myDetails = bundle.getParcelableArrayList(FragmentConfirmation.ORDER_ARRAY_LIST);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_confirmation,null);
        LinearLayout linearLayout = view.findViewById(R.id.order_detail_layout);
        View title = inflater.inflate(R.layout.title_layout,null);
        TextView fullNameTextView = view.findViewById(R.id.customer_name);
        TextView emailTextView = view.findViewById(R.id.customer_email);
        TextView phoneNumberTextView = view.findViewById(R.id.customer_phone);
        GridLayout layout = new GridLayout(getContext());
        GridLayout.LayoutParams gridLayout = new GridLayout.LayoutParams();
        TextView totalPriceText = new TextView(getContext());
        TextView totalQuantityText = new TextView(getContext());
        TextView totalPriceValue = new TextView(getContext());
        TextView totalQuantityValue = new TextView(getContext());
        TextView priceDottedLineOne = new TextView(getContext());
        TextView priceDottedLineTwo = new TextView(getContext());

        builder.setCustomTitle(title)
                .setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(),"Okay button clicked",Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(),"Okay button clicked",Toast.LENGTH_SHORT).show();
                    }
                });

        fullNameTextView.setText(fullName);
        emailTextView.setText(email);
        phoneNumberTextView.setText(phoneNumber);

        assert myDetails != null;
        for (int i = 0; i <myDetails.size() ; i++) {
            CCart cartItem = myDetails.get(i);
            String foodName = cartItem.getFoodName();
            String foodOption = cartItem.getFoodOption();
            String foodQuantity = cartItem.getFoodQuantity();
            String foodPrice = cartItem.getFoodPrice();
            String foodPriceGhanaCedis = "GHS"+foodPrice;

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

            int foodQuantityNum = Integer.parseInt(foodQuantity);
            double foodPriceNum = Double.parseDouble(foodPrice);
            BigDecimal bigDecimal = new BigDecimal(foodPriceNum);

            totalFoodQuantity += foodQuantityNum;
            totalFoodPrice = totalFoodPrice.add(bigDecimal);

            linearLayout.addView(temp);

        }

        totalQuantityText.setText(getResources().getText(R.string.total_quantity));
        totalQuantityValue.setText(String.valueOf(totalFoodQuantity));

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


        return builder.create();
    }
}
