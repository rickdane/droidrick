package com.rickdane.farmersmarkets;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.*;
import android.widget.*;
import com.rickdane.farmersmarkets.dao.FarmersMarketGeoSearchDao;
import org.stringtemplate.v4.ST;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 2/17/13
 * Time: 3:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class DisplayDetailsActivity extends ListActivity {

    private FarmersMarketGeoSearchDao searchDao;
    private ArrayAdapter<String> listAdapter;
    private ListView mainListView;
    private TextView banner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handleIntent(getIntent());
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
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        Map<String, String> item = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            item = (HashMap<String, String>) extras.get("item");
        }

        if (item != null) {
            setContentView(R.layout.main);
//            banner.setVisibility(View.INVISIBLE);

            TextView itemDisplay = (TextView) findViewById(R.id.text_display);

            //todo pull from text file instead of having template definition within code

            String website = "";
            if (item.get("website") != null) {
                website = "<strong>Website:</strong> <a href='url'>$url$</a><br/><br/>";
            }

            ST st = new ST("<h2>$name$</h2>" +
                    "<strong>Address:</strong> $address$ <br/><br/>" +
                    "<strong>Location:</strong> $location$ <br/><br/>" +
                    "<strong>Schedule:</strong> $schedule$ <br/><br/>" +
                    website, '$', '$');

            st.add("name", item.get("name")).
                    add("address", item.get("street")).
                    add("schedule", item.get("schedule")).
                    add("url", item.get("website")).
                    add("location", item.get("city") + ", " + item.get("state"));

            itemDisplay.setText(Html.fromHtml(st.render()));
            //  itemDisplay.setVisibility(View.VISIBLE);

        } else {
            //TODO figure out else to do, if anything, in case of null item
            finish();
        }

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

}