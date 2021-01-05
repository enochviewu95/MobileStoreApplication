package com.knowhouse.mobilestoreapplication.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.knowhouse.mobilestoreapplication.Adapters.RecyclerAdapter;
import com.knowhouse.mobilestoreapplication.DataSettersAndGetters.FoodDescription;
import com.knowhouse.mobilestoreapplication.R;
import com.knowhouse.mobilestoreapplication.VolleyRequests.ConstantURL;
import com.knowhouse.mobilestoreapplication.VolleyRequests.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentMain extends Fragment{

    private static final String LOG_TAG = "FragmentMain" ;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<FoodDescription> foodArrayList;
    private FragmentTransaction fragmentTransaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main,container,false);

        recyclerView = view.findViewById(R.id.recycler_view);

        recyclerView.setHasFixedSize(true);
        if(getActivity() != null)
            fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        getFoodList();
        foodArrayList = new ArrayList<>();       //Array list to hold the individual food object

        //use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        return view;
    }


    private void getFoodList(){
       String url = ConstantURL.SITE_URL+"FoodDescription.php";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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
                                    String foodImageUrl = foodList.getString("food_image_url");
                                    int foodRating = foodList.getInt("food_rating");
                                    String foodName = foodList.getString("food_name");
                                    int storeDetailsID = foodList.getInt("store_details_id");
                                    int foodPrice = foodList.getInt("food_price");

                                    FoodDescription foodDescription = new FoodDescription(id,foodImageUrl,
                                            foodRating,foodName,storeDetailsID,foodPrice);

                                    foodArrayList.add(foodDescription);
                                }

                                //specify an adapter
                                mAdapter = new RecyclerAdapter(foodArrayList,fragmentTransaction);
                                recyclerView.setAdapter(mAdapter);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }
}