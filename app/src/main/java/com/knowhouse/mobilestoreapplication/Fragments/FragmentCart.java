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

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    private FloatingActionButton makeOrderFAB;
    private ArrayList<CCart> items;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cart, container, false);
        recyclerView = view.findViewById(R.id.fragmentCart);
        makeOrderFAB = view.findViewById(R.id.makeOrder);

        context = getContext();
        recyclerViewClickInterface = this;
        layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManager);

        makeOrderFAB.setOnClickListener(fabClickLister);

        new CLoadCartDetails().execute();

        return view;
    }

    @Override
    public void onItemClick(int position, String url) {
        new CLoadCartDetails().execute(position);
    }

    private FloatingActionButton.OnClickListener fabClickLister = new FloatingActionButton.OnClickListener() {
        @Override
        public void onClick(View v) {
            DialogFragment dialogFragment = new FragmentConfirmation();
            Bundle args = new Bundle();
            args.putParcelableArrayList(FragmentConfirmation.ORDER_ARRAY_LIST,items);
            dialogFragment.setArguments(args);
            dialogFragment.show(requireActivity().getSupportFragmentManager(),"FragmentConfirmation");
        }
    };

    private class CLoadCartDetails extends AsyncTask<Object,Void,ArrayList<CCart>>{

        String[] cartColumns;
        SQLiteOpenHelper sqLiteOpenHelper;
        String sortOrder;
        int position;

        @Override
        protected void onPreExecute() {
            cartColumns = new String[]{
                    BaseColumns._ID,COLUMN_IMAGE_URL,COLUMN_FOOD_VALUE_TEXT,
                    COLUMN_DESCRIPTION_VALUE_TEXT, COLUMN_QUANTITY_VALUE_TEXT,
                    COLUMN_OPTION_VALUE_TEXT,COLUMN_PRICE_VALUE_TEXT
            };
            sqLiteOpenHelper = new CartReaderDbHelper(getContext());
            sortOrder = BaseColumns._ID;
        }

        @Override
        protected ArrayList<CCart> doInBackground(Object... objects) {
            try {
                SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();

                if(objects.length > 0){
                    position = (int) objects[0] + 1;
                    if(position > 0){
                        String query =
                                "UPDATE "+ TABLE_NAME
                                +" SET "+ CartReaderContract.CartEntry._ID + " = " +
                                        CartReaderContract.CartEntry._ID +" -" + " 1 "+
                                "WHERE "+CartReaderContract.CartEntry._ID +" > "+ position;
                        String selection = CartReaderContract.CartEntry._ID + " LIKE ?";
                        String[] selectionArgs = {String.valueOf(position)};
                        db.delete(TABLE_NAME,selection,selectionArgs);
                        db.execSQL(query);
                    }
                }

                Cursor cursor = db.query(
                        TABLE_NAME,cartColumns,null,null,
                        null,null,sortOrder);
                items = new ArrayList<>();
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