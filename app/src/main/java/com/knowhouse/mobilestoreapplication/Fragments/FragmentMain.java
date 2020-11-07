package com.knowhouse.mobilestoreapplication.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.knowhouse.mobilestoreapplication.Adapters.RecyclerAdapter;
import com.knowhouse.mobilestoreapplication.R;
import com.knowhouse.mobilestoreapplication.RecyclerViewClickInterface;

import java.util.Objects;

public class FragmentMain extends Fragment implements RecyclerViewClickInterface {

    private static final String LOG_TAG = "Refreshing" ;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main,container,false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        String[] myDataSet = new String[]{"Enoch","Viewu","Kojo","Deladem"};

        //use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        //specify an adapter
        mAdapter = new RecyclerAdapter(myDataSet,this);
        recyclerView.setAdapter(mAdapter);

        SwipeRefreshLayout mySwipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i(LOG_TAG,"onRefresh called from SwipeRefreshLayout");

                        //This method performs the actual data-refresh operation.
                        //This method calls setRefreshing(false) when it's finished
                    }
                }
        );

        return view;
    }

    @Override
    public void onItemClick(int position) {
        FragmentCollection fragmentCollection = new FragmentCollection();
        Bundle args = new Bundle();
        args.putInt(FragmentCollection.ARGS_POSITION,position);
        fragmentCollection.setArguments(args);

        if(getActivity() != null){
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_switch,fragmentCollection);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}