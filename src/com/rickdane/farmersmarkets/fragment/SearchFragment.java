package com.rickdane.farmersmarkets.fragment;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.*;
import android.widget.*;
import com.rickdane.farmersmarkets.DisplayDetailsActivity;
import com.rickdane.farmersmarkets.R;
import com.rickdane.farmersmarkets.TabFragment;
import com.rickdane.farmersmarkets.dao.FarmersMarketGeoSearchDao;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: rdane
 * Date: 2/26/13
 * Time: 12:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class SearchFragment extends ListFragment {

    private FarmersMarketGeoSearchDao searchDao;
    private ArrayAdapter<String> listAdapter;
    private ItemsAdapter adapter;
    private TextView banner;
    private TabFragment tabFragment;
    private String query;

    public SearchFragment(TabFragment tabFragment, String query) {
        this.tabFragment = tabFragment;
        this.query = query;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        searchDao = FarmersMarketGeoSearchDao.getDefaultInstance(getActivity().getApplication());

        //TODO, just for testing, if query is null we won't really want to do this
        if (query == null) {
            query = "94523";
        }

        handleIntent(query);
    }

    /**
     * This should possibly be renamed, its not exactly handling intent, more handling "focus" event, such as tab change
     *
     * @param query
     */
    private void handleIntent(String query) {

        Activity activity = getActivity();

        if (activity != null) {

            Collection<Map<String, String>> responses = searchDao.searchByCityStateorZipInput(query);

            if (responses.isEmpty()) {
                //put message as first entry
                Map<String, String> item = new HashMap<String, String>();
                item.put("list_title", "No Results");
                responses.add(item);
            }

            //  banner.setText("Showing Farmer's Markets near " + query + ":");

        /*      View textDisplay = getActivity().findViewById(R.id.text_display);
        textDisplay.setVisibility(View.INVISIBLE);*/

            this.adapter = new ItemsAdapter(getActivity(), R.layout.item_layout, prepareDisplayFromResults(responses));
            //   mainListView.setAdapter(adapter);

            //  ArrayAdapter adp = new ArrayAdapter(activity, android.R.layout.simple_list_item_1, responses);
            //  setListAdapter(adp);
            //  ListAdapter listAdapter = new LocationModelListAdapter(activity, FragmentTabTutorialApplication.sLocations);
            setListAdapter(adapter);

        }

    }

    //TODO figure out where to put this, probably doesn't belong here
    protected List<Map<String, String>> prepareDisplayFromResults(Collection<Map<String, String>> responses) {
        List<Map<String, String>> displayList = new ArrayList<Map<String, String>>();

        for (Map<String, String> map : responses) {
            StringBuilder title = new StringBuilder().
                    append(map.get("name")).
                    append(" - ").
                    append(map.get("city")).
                    append(", ").
                    append(map.get("state"));
            Map<String, String> item = new HashMap<String, String>();
            item.put("list_title", title.toString());
            item.putAll(map);
            displayList.add(item);
        }

        return displayList;
    }

    private class ItemsAdapter extends ArrayAdapter<Map<String, String>> {

        private List<Map<String, String>> items;

        public ItemsAdapter(Context context, int textViewResourceId, List<Map<String, String>> items) {
            super(context, textViewResourceId, items);
            this.items = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                //TODO why is it doing this here?
                view = vi.inflate(R.layout.item_layout, null);
            }

            Map<String, String> it = items.get(position);
            TextView textView = (TextView) view.findViewById(R.id.data_item);

            if (it != null) {
                textView.setText(it.get("list_title"));
            }

            //this
/*            if (it != null) {
                ImageView iv = (ImageView) v.findViewById(R.id.list_item_image);
                if (iv != null) {
                    iv.setImageDrawable(it.getImage());
                }
            }*/

            return view;
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        HashMap<String, String> item = (HashMap<String, String>) this.adapter.getItem(position);

        //change to other fragment
        tabFragment.gotoDetailsView(item);

    }

}
