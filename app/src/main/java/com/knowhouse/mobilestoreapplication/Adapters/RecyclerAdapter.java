package com.knowhouse.mobilestoreapplication.Adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.knowhouse.mobilestoreapplication.DataSettersAndGetters.FoodDescription;
import com.knowhouse.mobilestoreapplication.Fragments.FragmentCollection;
import com.knowhouse.mobilestoreapplication.R;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private static final String TAG = "RecyclerAdapter";
    private FragmentTransaction fragmentTransaction;


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private CardView cardView;

        public MyViewHolder(@NonNull CardView itemView) {
            super(itemView);
            cardView = itemView;
        }

    }

    private ArrayList<FoodDescription> dataSet;

    public RecyclerAdapter(ArrayList<FoodDescription> dataSet, FragmentTransaction fragmentTransaction) {
        this.dataSet = dataSet;
        this.fragmentTransaction = fragmentTransaction;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_food,parent,false);
        return new MyViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        FoodDescription foodDescription = dataSet.get(position);
        ImageView foodImage = (ImageView) cardView.findViewById(R.id.food_image);
        TextView foodName = (TextView) cardView.findViewById(R.id.food_name);
        TextView shop = (TextView) cardView.findViewById(R.id.seller);
        TextView location = (TextView) cardView.findViewById(R.id.location);
        RatingBar foodRating = (RatingBar) cardView.findViewById(R.id.food_rating);

        foodName.setText(foodDescription.getFoodName());
        shop.setText("God Be with us");
        location.setText(new StringBuilder(", ").append("Kumasi"));
        foodRating.setRating(foodDescription.getFoodRating());

        cardView.setOnClickListener(v -> {
            FragmentCollection fragmentCollection = new FragmentCollection();
            Bundle args = new Bundle();
            args.putInt(FragmentCollection.ARGS_POSITION,position);
            fragmentCollection.setArguments(args);

            FragmentTransaction transaction = fragmentTransaction;
            transaction.replace(R.id.content_switch,fragmentCollection);
            transaction.addToBackStack(null);
            transaction.commit();

        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}
