package com.rickdane.farmersmarkets.dao;

import android.content.Context;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 2/16/13
 * Time: 11:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class FarmersMarketsDatabaseHandler extends DatabaseHandler {

  private static FarmersMarketsDatabaseHandler dbHandler = null;

  //TODO may need to revisit better way to provide singleton such as with IOC container
  public static FarmersMarketsDatabaseHandler getDefaultInstance(Context context) {
    if (dbHandler == null) {
      dbHandler = new FarmersMarketsDatabaseHandler(context);
    }
    return dbHandler;
  }

  // All Static variables
  // Database Version
  private static final int DATABASE_VERSION = 1;

  // Database Name
  private static final String DATABASE_NAME = "farmersMarkets.db";

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
    super(schema, TABLE_NAME, context, DATABASE_NAME, null, DATABASE_VERSION);
  }

}