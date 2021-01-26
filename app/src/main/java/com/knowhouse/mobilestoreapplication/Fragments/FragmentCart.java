package com.knowhouse.mobilestoreapplication.Fragments;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.knowhouse.mobilestoreapplication.Adapters.CartAdapter;
import com.knowhouse.mobilestoreapplication.DataSettersAndGetters.CCart;
import com.knowhouse.mobilestoreapplication.Interfaces.RecyclerViewClickInterface;
import com.knowhouse.mobilestoreapplication.R;
import com.knowhouse.mobilestoreapplication.StorageUtilities.CartReaderContract;
import com.knowhouse.mobilestoreapplication.StorageUtilities.CartReaderDbHelper;

import java.util.ArrayList;

import static com.knowhouse.mobilestoreapplication.StorageUtilities.CartReaderContract.CartEntry.COLUMN_DESCRIPTION_VALUE_TEXT;
import static com.knowhouse.mobilestoreapplication.StorageUtilities.CartReaderContract.CartEntry.COLUMN_FOOD_VALUE_TEXT;
import static com.knowhouse.mobilestoreapplication.StorageUtilities.CartReaderContract.CartEntry.COLUMN_IMAGE_URL;
import static com.knowhouse.mobilestoreapplication.StorageUtilities.CartReaderContract.CartEntry.COLUMN_OPTION_VALUE_TEXT;
import static com.knowhouse.mobilestoreapplication.StorageUtilities.CartReaderContract.CartEntry.COLUMN_PRICE_VALUE_TEXT;
import static com.knowhouse.mobilestoreapplication.StorageUtilities.CartReaderContract.CartEntry.COLUMN_QUANTITY_VALUE_TEXT;
import static com.knowhouse.mobilestoreapplication.StorageUtilities.CartReaderContract.CartEntry.TABLE_NAME;

public class FragmentCart extends Fragment implements RecyclerViewClickInterface{

    private View view;
    private RecyclerViewClickInterface recyclerViewClickInterface;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cart, container, false);
        recyclerView = view.findViewById(R.id.fragmentCart);
        context = getContext();
        recyclerViewClickInterface = this;
        layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManager);

        new CLoadCartDetails().execute();

        return view;
    }

    @Override
    public void onItemClick(int position, String url) {
        Toast.makeText(getContext(),"Remove button Tapped",Toast.LENGTH_SHORT).show();
    }

    private class CLoadCartDetails extends AsyncTask<Object,Void,ArrayList<CCart>>{

        String[] cartColumns;
        SQLiteOpenHelper sqLiteOpenHelper;
        String sortOrder;

        @Override
        protected void onPreExecute() {
            cartColumns = new String[]{
                    BaseColumns._ID,COLUMN_IMAGE_URL,COLUMN_FOOD_VALUE_TEXT,
                    COLUMN_DESCRIPTION_VALUE_TEXT, COLUMN_QUANTITY_VALUE_TEXT,
                    COLUMN_OPTION_VALUE_TEXT,COLUMN_PRICE_VALUE_TEXT
            };
            sqLiteOpenHelper = new CartReaderDbHelper(getContext());
            sortOrder = BaseColumns._ID + " DESC";
        }

        @Override
        protected ArrayList<CCart> doInBackground(Object... objects) {
            try {
                SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
                Cursor cursor = db.query(
                        TABLE_NAME,cartColumns,null,null,
                        null,null,sortOrder);
               ArrayList<CCart> items = new ArrayList<>();
                while (cursor.moveToNext()){
                    int itemId = cursor.getInt(cursor.getColumnIndexOrThrow(CartReaderContract.CartEntry._ID));
                    String foodImageUrl = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_URL));
                    String foodName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FOOD_VALUE_TEXT));
                    String foodDescription = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION_VALUE_TEXT));
                    String foodQuantity = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY_VALUE_TEXT));
                    String foodOption = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OPTION_VALUE_TEXT));
                    String foodPrice = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRICE_VALUE_TEXT));
                    CCart cCart = new CCart(itemId,foodImageUrl,foodName,foodDescription,foodQuantity,
                            foodOption,foodPrice);
                    items.add(cCart);
                }
                cursor.close();
                db.close();
                return items;
            }catch (SQLException e){
                e.printStackTrace();
                return  null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<CCart> cCarts) {
            CartAdapter mAdapter = new CartAdapter(cCarts,context,recyclerViewClickInterface);
            recyclerView.setAdapter(mAdapter);
        }
    }
}