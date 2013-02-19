package com.rickdane.farmersmarkets;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import com.rickdane.farmersmarkets.dao.FarmersMarketGeoSearchDao;

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
public class SearchActivity extends ListActivity {

    private FarmersMarketGeoSearchDao searchDao;
    private ArrayAdapter<String> listAdapter;
    private ItemsAdapter adapter;
    private ListView mainListView;
    private TextView mainTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (searchDao == null) {
            searchDao = new FarmersMarketGeoSearchDao(getApplicationContext());

        }

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

        //   handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);


            Collection<Map<String, String>> responses = searchDao.searchByCityStateorZipInput(query);

            // Create ArrayAdapter using the planet list.
            listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, prepareDisplayFromResults(responses));


            setContentView(R.layout.main);

            mainTextView = (TextView) findViewById(R.id.textView);
            mainTextView.setText("Showing Farmer's Markets near " + query + ":");

            //custom adapter
            Item[] items = new Item[1];
            Item item = new Item();
            item.setLabel("a label");
            item.setUrl("http://");
            items[0] = item;

            mainListView = getListView();
            this.adapter = new ItemsAdapter(this, R.layout.item_layout, items);
            mainListView.setAdapter(adapter);

   /*
            mainListView.setAdapter(listAdapter);*/

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

    private class ItemsAdapter extends ArrayAdapter<Item> {

        private Item[] items;

        public ItemsAdapter(Context context, int textViewResourceId, Item[] items) {
            super(context, textViewResourceId, items);
            this.items = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                //TODO why is it doing this here?
                view = vi.inflate(R.layout.item_layout, null);
            }

            Item it = items[position];
            TextView textView = (TextView) view.findViewById(R.id.data_item);

            if (it != null) {
                textView.setText(it.getLabel());
            }

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
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Item item = this.adapter.getItem(position); //.click(
        //  this.getApplicationContext();
    }

    private class Item {
        private String label;
        private String url;

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }


}