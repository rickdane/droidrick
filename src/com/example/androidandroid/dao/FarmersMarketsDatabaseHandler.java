package com.example.androidandroid.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 2/16/13
 * Time: 11:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class FarmersMarketsDatabaseHandler extends DatabaseHandler {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "farmersMarkets";

    // Contacts table name
    private static final String TABLE_NAME = "markets";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";

    final static String[] schema = new String[]{
            "market_id",
            "name",
            "website",
            "street",
            "city",
            "county",
            "state",
            "zipcode",
            "schedule",
            "longitude",
            "latitude",
            "location",
            "credit",
            "wic",
            "wic_cash",
            "sfmnp",
            "bakedgoods",
            "cheese",
            "crafts",
            "flowers",
            "eggs",
            "seafood",
            "herbs",
            "vegetables",
            "honey",
            "jams",
            "maple",
            "meat",
            "nursery",
            "nuts",
            "plants",
            "poultry",
            "prepared",
            "soap",
            "trees",
            "wine",
            "update_time"
    };

    public static String[] getSchema() {
        return schema;
    }


    public FarmersMarketsDatabaseHandler(Context context) {
        super(schema, context, DATABASE_NAME, null, DATABASE_VERSION);
    }

}