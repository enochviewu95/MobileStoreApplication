package com.knowhouse.mobilestoreapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.knowhouse.mobilestoreapplication.DataSettersAndGetters.CFood;
import com.knowhouse.mobilestoreapplication.Interfaces.RecyclerViewClickInterface;
import com.knowhouse.mobilestoreapplication.R;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private static final String TAG = "RecyclerAdapter";
    private Context context;
    private RecyclerViewClickInterface recyclerViewClickInterface;

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private CardView cardView;

        public MyViewHolder(@NonNull CardView itemView) {
            super(itemView);
            cardView = itemView;
        }

    }

    private ArrayList<CFood> dataSet;

    public RecyclerAdapter(ArrayList<CFood> dataSet,
                           Context context,RecyclerViewClickInterface recyclerViewClickInterface) {
        this.dataSet = dataSet;
        this.context = context;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
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
        CFood foodItems = dataSet.get(position);
        ImageView foodImage = (ImageView) cardView.findViewById(R.id.food_image);
        TextView foodName = (TextView) cardView.findViewById(R.id.food_name);
        TextView shop = (TextView) cardView.findViewById(R.id.seller);
        TextView location = (TextView) cardView.findViewById(R.id.location);
        RatingBar foodRating = (RatingBar) cardView.findViewById(R.id.food_rating);

        foodName.setText(foodItems.getFoodName());
        shop.setText("God Be with us");
        location.setText(new StringBuilder(", ").append("Kumasi"));
        foodRating.setRating(foodItems.getFoodRating());

        String url = foodItems.getFoodImageUrl();
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.blank_background)
                .into(foodImage);

        cardView.setOnClickListener(v -> {
            recyclerViewClickInterface.onItemClick(position,url);
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}
