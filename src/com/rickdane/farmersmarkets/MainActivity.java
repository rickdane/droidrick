package com.rickdane.farmersmarkets;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.*;
import com.rickdane.farmersmarkets.dao.DatabaseHandler;
import com.rickdane.farmersmarkets.dao.DbUtils;
import com.rickdane.farmersmarkets.dao.FarmersMarketsDatabaseHandler;
import com.rickdane.farmersmarkets.dao.ZipcodesDatabaseHandler;
import com.rickdane.farmersmarkets.dataprocessing.DataLoader;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends Activity {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    View introText = (View) findViewById(R.id.intro_text);
    introText.setVisibility(View.VISIBLE);

    //load databases, if needed, as we are using pre-loaded databases
    try {
      DbUtils.createDatabaseIfNotExists(getApplicationContext(), "farmersMarkets.db");
      DbUtils.createDatabaseIfNotExists(getApplicationContext(), "zipcodecitystate.db");
    } catch (Exception e) {
      Log.w("--Exception trying to create DB", e);
    }


    //to load database from csv file
/*        try {

                DatabaseHandler dbHandler   = new FarmersMarketsDatabaseHandler(getBaseContext());
          //  DatabaseHandler dbHandler = new ZipcodesDatabaseHandler(getBaseContext());

            String assetName = "farmers_market.csv";              //farmers_market.csv   //zip_codes_city_state_data

            InputStream inputStream = getAssets().open(assetName);
            DataLoader dbLoader = new DataLoader();
            dbLoader.reloadDbFromScratchHelper(inputStream, dbHandler, FarmersMarketsDatabaseHandler.getSchema());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("finished");*/


/*        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                Intent i = new Intent(getApplicationContext(), Draw.class);
                startActivity(i);

           *//*     String item = ((TextView) view).getText().toString();

               Toast toast = Toast.makeText(getBaseContext(), item, 50);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();*//*

       *//*         Toast toast = new Toast(getBaseContext());

                toast.setDuration(Toast.LENGTH_LONG);
                       toast.setText(item);
                //  toast.setView(layout);
                toast.show();*//*

            }
        });*/


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
  public void onDestroy() {
    finish();
  }

  @Override
  public void onUserLeaveHint() {
    onDestroy();
  }

  @Override
  public void onBackPressed() {
    new AlertDialog.Builder(this)
        .setTitle("Really Exit?")
        .setMessage("Are you sure you want to exit?")
        .setNegativeButton(android.R.string.no, null)
        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface arg0, int arg1) {
            onDestroy();
          }
        }).create().show();
  }
}
