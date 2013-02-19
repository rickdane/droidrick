package com.rickdane.farmersmarkets.dao;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 2/17/13
 * Time: 11:41 AM
 * To change this template use File | Settings | File Templates.
 */
public class FarmersMarketGeoSearchDao {

    private FarmersMarketsDatabaseHandler farmersMarketsDatabaseHandler;
    private ZipcodesDatabaseHandler zipcodesDatabaseHandler;

    private static Map<String, String> states;

    public FarmersMarketGeoSearchDao(Context context) {
        farmersMarketsDatabaseHandler = new FarmersMarketsDatabaseHandler(context);
        zipcodesDatabaseHandler = new ZipcodesDatabaseHandler(context);
    }

    static {
        states = new HashMap<String, String>();
        states.put("Alabama", "AL");
        states.put("Alaska", "AK");
        states.put("Alberta", "AB");
        states.put("American Samoa", "AS");
        states.put("Arizona", "AZ");
        states.put("Arkansas", "AR");
        states.put("Armed Forces (AE)", "AE");
        states.put("Armed Forces Americas", "AA");
        states.put("Armed Forces Pacific", "AP");
        states.put("British Columbia", "BC");
        states.put("California", "CA");
        states.put("Colorado", "CO");
        states.put("Connecticut", "CT");
        states.put("Delaware", "DE");
        states.put("District Of Columbia", "DC");
        states.put("Florida", "FL");
        states.put("Georgia", "GA");
        states.put("Guam", "GU");
        states.put("Hawaii", "HI");
        states.put("Idaho", "ID");
        states.put("Illinois", "IL");
        states.put("Indiana", "IN");
        states.put("Iowa", "IA");
        states.put("Kansas", "KS");
        states.put("Kentucky", "KY");
        states.put("Louisiana", "LA");
        states.put("Maine", "ME");
        states.put("Manitoba", "MB");
        states.put("Maryland", "MD");
        states.put("Massachusetts", "MA");
        states.put("Michigan", "MI");
        states.put("Minnesota", "MN");
        states.put("Mississippi", "MS");
        states.put("Missouri", "MO");
        states.put("Montana", "MT");
        states.put("Nebraska", "NE");
        states.put("Nevada", "NV");
        states.put("New Brunswick", "NB");
        states.put("New Hampshire", "NH");
        states.put("New Jersey", "NJ");
        states.put("New Mexico", "NM");
        states.put("New York", "NY");
        states.put("Newfoundland", "NF");
        states.put("North Carolina", "NC");
        states.put("North Dakota", "ND");
        states.put("Northwest Territories", "NT");
        states.put("Nova Scotia", "NS");
        states.put("Nunavut", "NU");
        states.put("Ohio", "OH");
        states.put("Oklahoma", "OK");
        states.put("Ontario", "ON");
        states.put("Oregon", "OR");
        states.put("Pennsylvania", "PA");
        states.put("Prince Edward Island", "PE");
        states.put("Puerto Rico", "PR");
        states.put("Quebec", "PQ");
        states.put("Rhode Island", "RI");
        states.put("Saskatchewan", "SK");
        states.put("South Carolina", "SC");
        states.put("South Dakota", "SD");
        states.put("Tennessee", "TN");
        states.put("Texas", "TX");
        states.put("Utah", "UT");
        states.put("Vermont", "VT");
        states.put("Virgin Islands", "VI");
        states.put("Virginia", "VA");
        states.put("Washington", "WA");
        states.put("West Virginia", "WV");
        states.put("Wisconsin", "WI");
        states.put("Wyoming", "WY");
        states.put("Yukon Territory", "YT");
    }


    /**
     * Logic to attempt to get location based on  user input such as city and state or zip
     *
     * @param input
     * @return
     */
    public Collection<Map<String, String>> searchByCityStateorZipInput(String input) {

        Collection<Map<String, String>> responses = new ArrayList<Map<String, String>>();

        input = input.toUpperCase().trim();

        Pattern zipPattern = Pattern.compile("(\\d{5})");
        Matcher zipMatcher = zipPattern.matcher(input);
        String zip = null;
        if (zipMatcher.find()) {
            zip = zipMatcher.group(1);
        }

        Double[] latLong = null;

        if (zip == null) {

            String state = null;
            String city = null;

            //remove all non-letter characters from input
            input = input.replaceAll("[^\\p{L}\\p{N}]", "");

            //continue on to try to get city / state
            for (Map.Entry<String, String> entry : states.entrySet()) {

                String stateName = entry.getKey();
                String stateAbbrev = entry.getValue();


                if (input.toUpperCase().contains(stateName) || input.toUpperCase().contains(stateAbbrev)) {
                    state = stateAbbrev;
                    input = input.replace(stateName, "");
                    input = input.replace(stateAbbrev, "");
                    break;
                }

            }

            if (state != null && input.length() > 3) {
                //we should be left with the city
                city = input;
            }

            if (city != null && state != null) {
                //TODO implement this
            }

        } else {
            //get latLong by zip
            Map<String, String> matchZipEntry = zipcodesDatabaseHandler.findOneByField("zipcode", zip);
            if (!matchZipEntry.isEmpty()) {
                latLong = new Double[2];
                latLong[0] = Double.parseDouble(matchZipEntry.get("latitude"));
                latLong[1] = Double.parseDouble(matchZipEntry.get("longitude"));
            }

        }

        if (latLong != null) {
            //we were able to figure out location from user input so can do a search with this
            responses = farmersMarketsDatabaseHandler.geoSearch(latLong[0], latLong[1], .1);
        }

        return responses;
    }


}
