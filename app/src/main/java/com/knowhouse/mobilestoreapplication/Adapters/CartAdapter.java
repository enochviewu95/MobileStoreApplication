package com.knowhouse.mobilestoreapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.knowhouse.mobilestoreapplication.DataSettersAndGetters.CCart;
import com.knowhouse.mobilestoreapplication.Interfaces.RecyclerViewClickInterface;
import com.knowhouse.mobilestoreapplication.R;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder>{

    private ArrayList<CCart> cCarts;
    private Context context;
    private RecyclerViewClickInterface recyclerViewClickInterface;

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private CardView cardView;

        public ViewHolder(@NonNull CardView itemView) {
            super(itemView);
            cardView = itemView;
        }
    }

    public CartAdapter(ArrayList<CCart> cCarts, Context context,
                       RecyclerViewClickInterface recyclerViewClickInterface) {
        this.cCarts = cCarts;
        this.context = context;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView)LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_cardview,parent,false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        CCart cartItem = cCarts.get(position);
        ImageView foodImage = cardView.findViewById(R.id.cartFoodImage);
        TextView foodValueText = cardView.findViewById(R.id.foodValueTextView);
        TextView descriptionValueText = cardView.findViewById(R.id.descriptionValueTextView);
        TextView quantityValueTextView = cardView.findViewById(R.id.quantityValueTextView);
        TextView optionsValueTextView = cardView.findViewById(R.id.optionValueTextView);
        TextView priceValueTextView = cardView.findViewById(R.id.priceValueTextView);
        ImageButton removeButton = cardView.findViewById(R.id.remove_button);

        String totalPrice = "GHS"+ cartItem.getFoodPrice();

        Glide.with(context)
                .load(cartItem.getFoodImageUrl())
                .placeholder(R.drawable.blank_background)
                .into(foodImage);

        foodValueText.setText(cartItem.getFoodName());
        descriptionValueText.setText(cartItem.getFoodDescription());
        quantityValueTextView.setText(cartItem.getFoodQuantity());
        optionsValueTextView.setText(cartItem.getFoodOption());
        priceValueTextView.setText(totalPrice);

        removeButton.setOnClickListener(v -> recyclerViewClickInterface.onItemClick(position,null));
    }

    @Override
    public int getItemCount() {
        return cCarts.size();
    }
}
