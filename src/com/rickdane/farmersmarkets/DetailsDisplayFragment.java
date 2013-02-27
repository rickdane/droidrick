package com.rickdane.farmersmarkets;

import android.app.Activity;
import android.content.ClipData;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import org.stringtemplate.v4.ST;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 2/26/13
 * Time: 6:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class DetailsDisplayFragment extends Fragment {

    private Map<String, String> item;

    public DetailsDisplayFragment(Map<String, String> item) {
        this.item = item;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        View view = inflater.inflate(R.layout.shared_textview, container, false);

        if (item != null) {

            TextView itemDisplay = (TextView) view.findViewById(R.id.text_display);
            //set focus to remove focus from search input, as this was causing some issues with back button functionality that interrupted flow
            itemDisplay.requestFocus();

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

        }
        return view;

    }


}
