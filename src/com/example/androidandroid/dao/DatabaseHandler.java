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
public class DatabaseHandler extends SQLiteOpenHelper {

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


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder schemaTablesBldr = new StringBuilder();
        int i = 0;
        for (String column : schema) {
            if (i != 0) {
                schemaTablesBldr.append(",");
            }
            schemaTablesBldr.append(column + " TEXT");
            i++;
        }

        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + schemaTablesBldr.toString() + ")";
        db.execSQL(CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //hack
        db = this.getWritableDatabase();

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public void insert(Map<String, String> map) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        for (String fieldName : schema) {
            values.put(fieldName, map.get(fieldName));
        }

        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    public Map<String, String> findById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, schema, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        return mapCursorRespToMap(cursor);

    }

    /**
     * Meant for testing, finds the first entry in the DB, without any input parameters
     *
     * @return
     */
    public Map<String, String> findFirst() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, schema, null, null, null, null, null, "1");
        if (cursor != null)
            cursor.moveToFirst();

        return mapCursorRespToMap(cursor);

    }

    public Collection<Map<String, String>> findByField(String fieldName, String value) {
        Collection<Map<String, String>> responses = new ArrayList<Map<String, String>>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, schema, fieldName + "=" + value, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                responses.add(mapCursorRespToMap(cursor));
            }
        }

        return responses;

    }


    protected Map<String, String> mapCursorRespToMap(Cursor cursor) {
        Map<String, String> retMap = new HashMap<String, String>();

        int i = 0;
        for (String columnName : schema) {
            String value = cursor.getString(i);
            if (value != null) {
                retMap.put(columnName, cursor.getString(i));
            }
            i++;
        }
        return retMap;
    }

    // Truncate Table
    public void truncate() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }
}