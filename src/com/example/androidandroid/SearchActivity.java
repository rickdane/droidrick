package com.example.androidandroid;

import android.app.Activity;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import com.example.androidandroid.dao.FarmersMarketGeoSearchDao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 2/17/13
 * Time: 3:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class SearchActivity extends Activity {

    private FarmersMarketGeoSearchDao searchDao;
    private ArrayAdapter<String> listAdapter;
    private ListView mainListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (searchDao == null) {
            searchDao = new FarmersMarketGeoSearchDao(getApplicationContext());

        }

        handleIntent(getIntent());
    }


    @Override
    protected void onNewIntent(Intent intent) {

        //   handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            Collection<Map<String, String>> responses = searchDao.searchByCityStateorZipInput(query);


            // Create ArrayAdapter using the planet list.
            listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, prepareDisplayFromResults(responses));

            setContentView(R.layout.main);

            // Find the ListView resource.
            mainListView = (ListView) findViewById(R.id.listView);
            mainListView.setAdapter(listAdapter);

        }
    }

    //TODO figure out where to put this, probably doesn't belong here
    protected List<String> prepareDisplayFromResults(Collection<Map<String, String>> responses) {
        List<String> displayList = new ArrayList<String>();

        for (Map<String, String> map : responses) {
            StringBuilder title = new StringBuilder().
                    append(map.get("name")).
                    append(" - ").
                    append(map.get("city")).
                    append(", ").
                    append(map.get("state"));
            displayList.add(title.toString());
        }

        return displayList;
    }

}