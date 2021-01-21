package com.knowhouse.mobilestoreapplication.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.knowhouse.mobilestoreapplication.Adapters.RecyclerAdapter;
import com.knowhouse.mobilestoreapplication.DataSettersAndGetters.CFood;
import com.knowhouse.mobilestoreapplication.Interfaces.RecyclerViewClickInterface;
import com.knowhouse.mobilestoreapplication.R;
import com.knowhouse.mobilestoreapplication.VolleyRequests.ConstantURL;
import com.knowhouse.mobilestoreapplication.VolleyRequests.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FragmentMain extends Fragment implements RecyclerViewClickInterface {

    public static final String ARGS_POSITION = "position" ;
    private static final String LOG_TAG = "FragmentMain" ;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView recyclerView;
    private ArrayList<CFood> foodArrayList;
    private FragmentTransaction fragmentTransaction;
    private Context context;
    private RecyclerViewClickInterface recyclerViewClickInterface;
    private int foodCategoryPosition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main,container,false);

        Bundle position = getArguments();

        recyclerView = view.findViewById(R.id.recycler_view);

        if(position != null)
            foodCategoryPosition = position.getInt(FragmentMain.ARGS_POSITION);

        context = getContext();
        recyclerViewClickInterface = this;

        recyclerView.setHasFixedSize(true);
        if(getActivity() != null)
            fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        getFoodList();
        foodArrayList = new ArrayList<>();       //Array list to hold the individual food object

        //use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        return view;
    }

    @Override
    public void onItemClick(int position,String url) {
        FragmentCollection fragmentCollection = new FragmentCollection();
        Bundle args = new Bundle();
        args.putSerializable(FragmentCollection.ARGS_FOOD_OBJECT,foodArrayList.get(position));
        fragmentCollection.setArguments(args);
        FragmentTransaction transaction = fragmentTransaction;
        transaction.replace(R.id.content_switch,fragmentCollection);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    private void getFoodList(){
       String url = ConstantURL.SITE_URL+"FoodDescription.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    JSONObject content = null;
                    JSONArray foodArray = null;
                    try{
                        content = new JSONObject(response);
                        if(!content.getBoolean("error")){
                            foodArray = new JSONArray(content.
                                    getString("content"));

                            for(int i = 0; i<foodArray.length();i++){
                                JSONObject foodList = foodArray.getJSONObject(i);
                                int id = foodList.getInt("food_id");
                                String foodName = foodList.getString("food_name");
                                String foodImageUrl = foodList.getString("food_image_url");
                                int foodRating = foodList.getInt("food_rating");
                                String foodDescription = foodList.getString("food_description");
                                int foodCategoryId = foodList.getInt("food_category_id");
                                CFood foodListing = new CFood(id,foodName,
                                        foodImageUrl,foodRating,foodDescription,foodCategoryId);

                                foodArrayList.add(foodListing);
                            }

                            //specify an adapter
                            mAdapter = new RecyclerAdapter(foodArrayList,context,recyclerViewClickInterface);
                            recyclerView.setAdapter(mAdapter);

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
                params.put("food_category_id",String.valueOf(foodCategoryPosition+1));
                return params;
            }
        };

        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }


}