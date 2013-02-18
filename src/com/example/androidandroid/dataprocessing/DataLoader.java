package com.example.androidandroid.dataprocessing;

import com.example.androidandroid.dao.DatabaseHandler;
import com.example.androidandroid.dao.FarmersMarketsDatabaseHandler;
import com.example.androidandroid.dao.ZipcodesDatabaseHandler;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 2/16/13
 * Time: 10:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class DataLoader {


    /**
     * create and load db table
     *
     * @param inputStream
     * @param dbHandler
     */
    public void reloadDbFromScratchHelper(InputStream inputStream, final DatabaseHandler dbHandler, final String[] schema) {
        //   dbHandler.truncate();
        dbHandler.onUpgrade(null, 1, 2);

        loadData(inputStream, new Closure() {
            public void processLine(String line) {
                if (line != null) {
                    Map<String, String> entry = new HashMap<String, String>();
                    String[] columns = line.split("\\|");
                    int i = 0;
                    for (String column : columns) {
                        if (schema.length > i) {
                            String key = schema[i];
                            if (key != null) {
                                //hack for zip codes that begin with zero (TODO fix within csv file)
                                if (key.equals("zipcode") && column.length() < 5) {
                                    while (column.length() < 5) {
                                        column = "0" + column;
                                    }
                                }
                                entry.put(key, column);
                            }
                        }
                        i++;
                    }
                    dbHandler.insert(entry);
                }
            }
        }, 1);
    }


    public interface Closure {

        public void processLine(String line);

    }


    public static void loadData(InputStream inputStream, Closure lineProcessor, int skipLines) {

        Reader r = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(r);

        try {

            String line;
            int i = 1;
            while ((line = reader.readLine()) != null) {
                if (i > skipLines) {
                    lineProcessor.processLine(line);
                }
                i++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


}
