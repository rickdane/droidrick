package com.rickdane.farmersmarkets;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.rickdane.farmersmarkets.fragment.SearchFragment;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 2/26/13
 * Time: 9:34 AM
 * To change this template use File | Settings | File Templates.
 */
public class TabFragment extends Fragment {

    private static final int LISTINGS_STATE = 0x1;
    private static final int DETAILS_STATE = 0x2;

    private int curTabState;
    private View view;
    private int[] viewIds;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab, container, false);

        // Grab the tab buttons from the layout and attach event handlers. The code just uses standard
        // buttons for the tab widgets. These are bad tab widgets, design something better, this is just
        // to keep the code simple.
        Button listViewTab = (Button) view.findViewById(R.id.list_view_tab);
        Button displayViewTab = (Button) view.findViewById(R.id.details_view_tab);

         viewIds = new int[]{R.id.list_view_tab, R.id.details_view_tab};

        listViewTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO need to maintain results from before, don't want to perform new search each time this tab is clicked

                goToSearchResultsView(null);
            }
        });

        displayViewTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Switch the tab content to display the grid view.
                gotoDetailsView(null);
            }
        });

        return view;
    }

    public void changeTab(int[] viewIds, View curView) {
        for (int viewId : viewIds) {
            View iterView = view.findViewById(viewId);
            if (iterView.getId() != curView.getId()) {

                iterView.setBackgroundResource(R.drawable.tab);
            }
        }
        //set the current tab to the desired background color
        curView.setBackgroundResource(R.drawable.tab_clicked);
    }

    public void goToSearchResultsView(String query) {

        if (curTabState != LISTINGS_STATE) {
            curTabState = LISTINGS_STATE;

            changeTab(viewIds,  view.findViewById(R.id.list_view_tab));

            // Fragments have access to their parent Activity's FragmentManager. You can
            // obtain the FragmentManager like this.
            FragmentManager fm = getFragmentManager();

            if (fm != null) {
                // Perform the FragmentTransaction to load in the list tab content.
                // Using FragmentTransaction#replace will destroy any Fragments
                // currently inside R.id.fragment_content and add the new Fragment
                // in its place.
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment_content, new SearchFragment(this, query));          //()     TestListFragment()
                ft.commit();
            }
        }

    }


    public void gotoDetailsView(HashMap<String, String> item) {
        // curTabState keeps track of which tab is currently displaying its contents.
        // Perform a check to make sure the list tab content isn't already displaying.

        if (curTabState != DETAILS_STATE) {
            curTabState = DETAILS_STATE;

            changeTab(viewIds, view.findViewById(R.id.details_view_tab));

            // Fragments have access to their parent Activity's FragmentManager. You can
            // obtain the FragmentManager like this.
            FragmentManager fm = getFragmentManager();

            if (fm != null) {
                // Perform the FragmentTransaction to load in the list tab content.
                // Using FragmentTransaction#replace will destroy any Fragments
                // currently inside R.id.fragment_content and add the new Fragment
                // in its place.
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment_content, new DetailsDisplayFragment(item));
                ft.commit();
            }
        }
    }


}