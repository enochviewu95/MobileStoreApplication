package com.knowhouse.mobilestoreapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

public class SearchableActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
    }

    private void doMySearch(String query){
        //TODO: Process storing and searching of data using FTS3 for sqlite
        //TODO: Return search with a CursorAdapter
    }
}