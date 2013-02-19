package com.rickdane.farmersmarkets.dao;

import android.content.Context;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 2/16/13
 * Time: 11:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class ZipcodesDatabaseHandler extends DatabaseHandler {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "zipcodecitystate.db";

    // Contacts table name
    private static final String TABLE_NAME = "zipcodes";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";


    final static String[] schema = new String[]{
            "zipcode",
            "latitude",
            "longitude",
            "city",
            "state",
            "county",
            "type"
    };

    public static String[] getSchema() {
        return schema;
    }


    public ZipcodesDatabaseHandler(Context context) {
        super(schema, TABLE_NAME, context, DATABASE_NAME, null, DATABASE_VERSION);
    }

}