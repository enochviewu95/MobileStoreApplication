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
import com.knowhouse.mobilestoreapplication.Adapters.CategoryAdapter;
import com.knowhouse.mobilestoreapplication.DataSettersAndGetters.CFoodCategory;
import com.knowhouse.mobilestoreapplication.Interfaces.RecyclerViewClickInterface;
import com.knowhouse.mobilestoreapplication.R;
import com.knowhouse.mobilestoreapplication.VolleyRequests.ConstantURL;
import com.knowhouse.mobilestoreapplication.VolleyRequests.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentCategories extends Fragment implements RecyclerViewClickInterface {

    private ArrayList<CFoodCategory> categoryArrayList;
    private CategoryAdapter mAdapter;
    private Context context;
    private RecyclerView categoryRecyclerView;
    private RecyclerViewClickInterface recyclerViewClickInterface;
    private RecyclerView.LayoutManager layoutManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        if(getActivity() != null)
            fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

        context = getContext();
        recyclerViewClickInterface = this;
        categoryRecyclerView = view.findViewById(R.id.category_recylerview);
        layoutManager = new LinearLayoutManager(getActivity());
        categoryRecyclerView.setLayoutManager(layoutManager);
        categoryArrayList = new ArrayList<>();
        getCategoryList();
        return view;
    }

    private void getCategoryList(){
        String url = ConstantURL.SITE_URL+"CategoryDescription.php";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    JSONObject content = null;
                    JSONArray categoryArray = null;
                    try{
                        content = new JSONObject(response);
                        if(!content.getBoolean("error")){
                            categoryArray = new JSONArray(content.
                                    getString("content"));

                            for(int i = 0; i<categoryArray.length();i++){
                                JSONObject categoryList = categoryArray.getJSONObject(i);
                                int id = categoryList.getInt("id");
                                String categoryName = categoryList.getString("category_name");
                                String categoryImageUrl = categoryList.getString("category_image_url");
                                CFoodCategory foodCategory = new CFoodCategory(id,categoryName,categoryImageUrl);

                                categoryArrayList.add(foodCategory);
                            }

                            //specify an adapter
                            mAdapter = new CategoryAdapter(categoryArrayList,context,recyclerViewClickInterface);
                            categoryRecyclerView.setAdapter(mAdapter);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {

                }
        );

        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    @Override
    public void onItemClick(int position, String url) {
        FragmentMain fragmentMain = new FragmentMain();
        Bundle args = new Bundle();
        args.putInt(FragmentMain.ARGS_POSITION,position);
        fragmentMain.setArguments(args);

        FragmentTransaction transaction = fragmentTransaction;
        transaction.replace(R.id.content_switch,fragmentMain);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}