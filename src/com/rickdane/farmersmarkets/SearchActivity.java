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
public class SearchActivity extends ListActivity {

    private FarmersMarketGeoSearchDao searchDao;
    private ArrayAdapter<String> listAdapter;
    private ItemsAdapter adapter;
    private ListView mainListView;
    private TextView banner;

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

            setContentView(R.layout.main);
            mainListView = getListView();
            mainListView.setVisibility(View.VISIBLE);


            if (!responses.isEmpty()) {

                //hide the intro text
                View introText = (View) findViewById(R.id.intro_text);
                introText.setVisibility(View.INVISIBLE);
                View itemDisplay = (View) findViewById(R.id.item_display);
                itemDisplay.setVisibility(View.INVISIBLE);

                banner = (TextView) findViewById(R.id.banner);
                banner.setText("Showing Farmer's Markets near " + query + ":");
                banner.setVisibility(View.VISIBLE);

                this.adapter = new ItemsAdapter(this, R.layout.item_layout, prepareDisplayFromResults(responses));
                mainListView.setAdapter(adapter);
            } else {

                Toast toast = Toast.makeText(getBaseContext(), "", Toast.LENGTH_LONG);
                toast.setText("Sorry, no results found for that location");
                toast.setGravity(Gravity.TOP | Gravity.LEFT, 20, 65);
                toast.show();
            }
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
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Map<String, String> item = this.adapter.getItem(position); //.click(

        mainListView.setVisibility(View.INVISIBLE);
        banner.setVisibility(View.INVISIBLE);

        TextView itemDisplay = (TextView) findViewById(R.id.item_display);

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

        itemDisplay.setVisibility(View.VISIBLE);


    }

}