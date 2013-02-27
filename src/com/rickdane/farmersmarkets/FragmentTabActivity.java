package com.rickdane.farmersmarkets;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.SearchView;
import com.rickdane.farmersmarkets.dao.DbUtils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 2/26/13
 * Time: 9:32 AM
 * To change this template use File | Settings | File Templates.
 */
public class FragmentTabActivity extends FragmentActivity {

  private  TabFragment tabFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


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
        String zipCode = null;
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

                Geocoder gcd = new Geocoder(getBaseContext(),
                        Locale.getDefault());

                List<Address> addresses;
                try {
                    addresses = gcd.getFromLocation(loc.getLatitude(),
                            loc.getLongitude(), 1);
                    if (addresses.size() > 0) {

                        zipCode = addresses.get(0).getPostalCode();
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        // Notice how there is not very much code in the Activity. All the business logic for
        // rendering tab content and even the logic for switching between tabs has been pushed
        // into the Fragments. This is one example of how to organize your Activities with Fragments.
        // This benefit of this approach is that the Activity can be reorganized using layouts
        // for different devices and screen resolutions.
        setContentView(R.layout.activity_fragment_tab);


        // Grab the instance of TabFragment that was included with the layout and have it
        // launch the initial tab.
        FragmentManager fm = getSupportFragmentManager();
        tabFragment = (TabFragment) fm.findFragmentById(R.id.fragment_tab);

        if (Intent.ACTION_SEARCH.equals(getIntent().getAction())) {
            onNewIntent(getIntent());
        } else {
            //this is the tab that is displayed by default
            tabFragment.goToSearchResultsView(zipCode);
        }

    }

    @Override
    public void onNewIntent(Intent intent) {

        String query = intent.getStringExtra(SearchManager.QUERY);

        tabFragment.goToSearchResultsView(query);

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

}