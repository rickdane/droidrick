package com.rickdane.farmersmarkets.dao;

import android.content.ContentValues;
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
 * Date: 2/17/13
 * Time: 10:32 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class DatabaseHandler extends SQLiteOpenHelper {


    // Contacts Table Columns names
    private static final String KEY_ID = "id";

    private String DATABASE_NAME;
    private String TABLE_NAME;

    protected String[] schema;


    public DatabaseHandler(String[] schema, String tableName, android.content.Context context, java.lang.String dbName, android.database.sqlite.SQLiteDatabase.CursorFactory factory, int version) {
        super(context, dbName, null, version);
        DATABASE_NAME = dbName;
        TABLE_NAME = tableName;
        this.schema = schema;
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
            //hack, should use map that defines column types eventually
            if (column.equals("latitude") || column.equals("longitude")) {
                schemaTablesBldr.append(column + " FLOAT");
            } else {
                schemaTablesBldr.append(column + " TEXT");
            }
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

    public Collection<Map<String, String>> geoSearch(double currentLatitude, double currentLongitude, double radius) {

        double minLat = currentLatitude - radius;
        double maxLat = currentLatitude + radius;
        double minLong = currentLongitude - radius;
        double maxLong = currentLongitude + radius;

        Collection<Map<String, String>> responses = new ArrayList<Map<String, String>>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, schema, "latitude > " + minLat + " AND " + " latitude < " + maxLat
                + " AND longitude > " + minLong + " AND longitude < " + maxLong, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                responses.add(mapCursorRespToMap(cursor));
            }
        }

        return responses;
    }

    public Map<String, String> findOneByField(String fieldName, String value) {
        Map<String, String> map = new HashMap<String, String>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, schema, fieldName + " = '" + value + "'", null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            map = mapCursorRespToMap(cursor);
        }

        return map;
    }

    public Map<String, String> findOneByMultipleFields(String[] keys, String[] values) {
        Map<String, String> map = new HashMap<String, String>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, schema, prepareConditionalQuery(keys), values, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            map = mapCursorRespToMap(cursor);
        }

        return map;
    }

    public Collection<Map<String, String>> findByMultipleFields(String[] keys, String[] values) {
        Collection<Map<String, String>> responses = new ArrayList<Map<String, String>>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, schema, prepareConditionalQuery(keys), values, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                responses.add(mapCursorRespToMap(cursor));
            }
        }

        return responses;
    }

    /**
     * Prepares conditional query from array of keys
     *
     * @param keys
     * @return
     */
    public static String prepareConditionalQuery(String[] keys) {
        //prepare the conditional query
        StringBuilder strBld = new StringBuilder();
        int i = 0;
        for (String key : keys) {
            if (i != 0) {
                strBld.append(" AND ");
            }
            strBld.append(key).append(" = ? ");
            i++;
        }
        return strBld.toString();
    }


    public Collection<Map<String, String>> findByField(String fieldName, String value) {
        Collection<Map<String, String>> responses = new ArrayList<Map<String, String>>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, schema, fieldName + " = '" + value + "'", null, null, null, null);
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