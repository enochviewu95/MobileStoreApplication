package com.knowhouse.mobilestoreapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.knowhouse.mobilestoreapplication.DataSettersAndGetters.CFoodCategory;
import com.knowhouse.mobilestoreapplication.Interfaces.RecyclerViewClickInterface;
import com.knowhouse.mobilestoreapplication.R;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private ArrayList<CFoodCategory> category;
    private Context context;
    private RecyclerViewClickInterface recyclerViewClickInterface;

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private CardView cardView;

        public ViewHolder(CardView view){
            super(view);
            cardView = view;
        }
    }

    public CategoryAdapter(ArrayList<CFoodCategory> category, Context context, RecyclerViewClickInterface recyclerViewClickInterface){
        this.category = category;
        this.context = context;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_food_category,parent,false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        CFoodCategory categoryItems = category.get(position);
        ImageView categoryImage = (ImageView) cardView.findViewById(R.id.category_image);
        TextView categoryName = (TextView) cardView.findViewById(R.id.category_name);


        categoryName.setText(categoryItems.getCategoryName());
        Glide.with(context)
                .load(categoryItems.getCategoryImageUrl())
                .placeholder(R.drawable.blank_background)
                .into(categoryImage);

        cardView.setOnClickListener(v->{
            recyclerViewClickInterface.onItemClick(position,null);
        });
    }

    @Override
    public int getItemCount() {
        return category.size();
    }
}
