package com.knowhouse.mobilestoreapplication.Adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.knowhouse.mobilestoreapplication.R;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private CardView cardView;

        public MyViewHolder(@NonNull CardView itemView) {
            super(itemView);
            cardView = itemView;
        }
    }

    private Object[] dataSet;

    public RecyclerAdapter(Object[] myDataSet) {
        dataSet = myDataSet;
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
    }

    @Override
    public int getItemCount() {
        return 5;
    }



}
