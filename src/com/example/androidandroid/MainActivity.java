package com.example.androidandroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.*;
import com.example.androidandroid.dao.DatabaseHandler;
import com.example.androidandroid.dao.FarmersMarketsDatabaseHandler;
import com.example.androidandroid.dao.ZipcodesDatabaseHandler;
import com.example.androidandroid.dataprocessing.DataLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends Activity {

    private ArrayAdapter<String> listAdapter;
    private ListView mainListView;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Find the ListView resource.
        mainListView = (ListView) findViewById(R.id.listView);

        String[] planets = new String[]{"Mercury", "Venus", "Earth", "Mars",
                "Jupiter", "Saturn", "Uranus", "Neptune"};

        ArrayList<String> planetList = new ArrayList<String>();
        planetList.addAll(Arrays.asList(planets));

        // Create ArrayAdapter using the planet list.
        listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, planetList);

/*        try {

            //    DatabaseHandler dbHandler   = new FarmersMarketsDatabaseHandler(getBaseContext());
            DatabaseHandler dbHandler = new ZipcodesDatabaseHandler(getBaseContext());

            String assetName = "zip_codes_city_state_data.csv";              //farmers_market.csv   //zip_codes_city_state_data

            InputStream inputStream = getAssets().open(assetName);
            DataLoader dbLoader = new DataLoader();
            dbLoader.reloadDbFromScratchHelper(inputStream, dbHandler, ZipcodesDatabaseHandler.getSchema());
        } catch (IOException e) {
            e.printStackTrace();
        }*/


        // Set the ArrayAdapter as the ListView's adapter.
        //  mainListView.setAdapter(listAdapter);

        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                Intent i = new Intent(getApplicationContext(), Draw.class);
                startActivity(i);

           /*     String item = ((TextView) view).getText().toString();

               Toast toast = Toast.makeText(getBaseContext(), item, 50);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();*/

       /*         Toast toast = new Toast(getBaseContext());

                toast.setDuration(Toast.LENGTH_LONG);
                       toast.setText(item);
                //  toast.setView(layout);
                toast.show();*/

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);

        return true;

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        //   WelcomeActivity.super.onBackPressed();
                    }
                }).create().show();
    }
}
