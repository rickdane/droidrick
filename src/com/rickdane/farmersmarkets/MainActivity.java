package com.rickdane.farmersmarkets;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.*;
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
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        TextView itemDisplay = (TextView) findViewById(R.id.text_display);
        itemDisplay.setBackgroundResource(R.drawable.farmers_market_bg);
        itemDisplay.setHeight(AbsListView.LayoutParams.MATCH_PARENT);

        //set focus to remove focus from search input, as this was causing some issues with back button functionality that interrupted flow
        itemDisplay.requestFocus();

/*    View introText = (View) findViewById(R.id.intro_text);
    introText.setVisibility(View.VISIBLE);*/

        //load databases, if needed, as we are using pre-loaded databases
        try {
            DbUtils.createDatabaseIfNotExists(getApplicationContext(), "farmersMarkets.db");
            DbUtils.createDatabaseIfNotExists(getApplicationContext(), "zipcodecitystate.db");
        } catch (Exception e) {
            Log.w("--Exception trying to create DB", e);
        }

        //attempt to get user's current location
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        Location loc;
        if (locationManager != null) {
            loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (loc == null) {
                //try to get from network (such as wifi)
                loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
            if (loc == null) {
                //try to get from passive provider //TODO figure out what exactly this is and if we want to use this
                loc = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            }
            if (loc != null) {

                String zipCode = null;
                Geocoder gcd = new Geocoder(getBaseContext(),
                        Locale.getDefault());

                List<Address> addresses;
                try {
                    addresses = gcd.getFromLocation(loc.getLatitude(),
                            loc.getLongitude(), 1);
                    if (addresses.size() > 0) {

                        zipCode = addresses.get(0).getPostalCode();
                        if (zipCode != null) {

                            //perform location search
                            Intent intent = new Intent(this, SearchActivity.class);
                            intent.setAction(Intent.ACTION_SEARCH);
                            intent.putExtra(SearchManager.QUERY, zipCode);
                            startActivity(intent);
                        }
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


/*        LocationListener locationListener = new MyLocationListener();
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 5000, 10, locationListener);*/


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

/*    @Override
    public void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    public void onUserLeaveHint() {
        onDestroy();
    }*/

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Close Application?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                    }
                }).create().show();
    }


    //TOD make separate class
    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location loc) {

            String zipCode = null;
            Geocoder gcd = new Geocoder(getBaseContext(),
                    Locale.getDefault());

            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(loc.getLatitude(),
                        loc.getLongitude(), 1);
                if (addresses.size() > 0)

                    zipCode = addresses.get(0).getPostalCode();

                //for testing
                Toast.makeText(getBaseContext(),
                        "Location changed: Lat: " + loc.getLatitude() + " Lng: "
                                + loc.getLongitude() + " zip:" + zipCode, Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }


        @Override
        public void onStatusChanged(String provider, int status,
                                    Bundle extras) {
            // TODO Auto-generated method stub
        }
    }
}
