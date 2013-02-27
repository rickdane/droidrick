package com.rickdane.farmersmarkets;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.SearchView;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 2/26/13
 * Time: 9:32 AM
 * To change this template use File | Settings | File Templates.
 */
public class FragmentTabActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Notice how there is not very much code in the Activity. All the business logic for
        // rendering tab content and even the logic for switching between tabs has been pushed
        // into the Fragments. This is one example of how to organize your Activities with Fragments.
        // This benefit of this approach is that the Activity can be reorganized using layouts
        // for different devices and screen resolutions.
        setContentView(R.layout.activity_fragment_tab);

        // Grab the instance of TabFragment that was included with the layout and have it
        // launch the initial tab.
        FragmentManager fm = getSupportFragmentManager();
        TabFragment tabFragment = (TabFragment) fm.findFragmentById(R.id.fragment_tab);

        //this is the tab that is displayed by default
        tabFragment.gotoDetailsView(null);
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