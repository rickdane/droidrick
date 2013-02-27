package com.rickdane.farmersmarkets;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created with IntelliJ IDEA.
 * User: rdane
 * Date: 2/26/13
 * Time: 4:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestListFragment  extends ListFragment {

  // ListFragment is a very useful class that provides a simple ListView inside of a Fragment.
  // This class is meant to be sub-classed and allows you to quickly build up list interfaces
  // in your app.

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    Activity activity = getActivity();

    if (activity != null) {

      Object[] sArray = {"sdfa", "fs", 3.5, true, 2, "fsf", "agsagsaga"};
      ArrayAdapter adp = new ArrayAdapter(activity, android.R.layout.simple_list_item_1, sArray);
      setListAdapter(adp);
      //  ListAdapter listAdapter = new LocationModelListAdapter(activity, FragmentTabTutorialApplication.sLocations);
      setListAdapter(adp);
    }
  }

  @Override
  public void onListItemClick(ListView l, View v, int position, long id) {
    Activity activity = getActivity();

    if (activity != null) {
      ListAdapter listAdapter = getListAdapter();
      //   LocationModel locationModel = (LocationModel) listAdapter.getItem(position);

      // Display a simple Toast to demonstrate that the click event is working. Notice that Fragments have a
      // getString() method just like an Activity, so that you can quickly access your localized Strings.
      Toast.makeText(activity, "toast", Toast.LENGTH_SHORT).show();
    }
  }

}
