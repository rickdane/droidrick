package com.rickdane.farmersmarkets.util;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 2/27/13
 * Time: 4:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class LocationsUtil {

    public static String getZipFromCurUserLocation(Activity activity) {
        //attempt to get user's current location
        LocationManager locationManager = (LocationManager)
                activity.getSystemService(Context.LOCATION_SERVICE);
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

                Geocoder gcd = new Geocoder(activity.getBaseContext(),
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
        return zipCode;
    }

}
