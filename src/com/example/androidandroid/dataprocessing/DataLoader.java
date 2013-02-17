package com.example.androidandroid.dataprocessing;

import android.os.Environment;
import android.util.Log;
import com.example.androidandroid.dao.DatabaseHandler;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 2/16/13
 * Time: 10:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class DataLoader {

    public static void test(InputStream inputStream, final DatabaseHandler dbHandler) {

        //reloadDbFromScratchHelper(inputStream, dbHandler);


        Collection<Map<String, String>> responses = dbHandler.geoSearch(-76.1354, 36.8419, 1);

        responses = null;
    }

    /**
     * create and load db table
     *
     * @param inputStream
     * @param dbHandler
     */
    protected static void reloadDbFromScratchHelper(InputStream inputStream, final DatabaseHandler dbHandler) {
        dbHandler.truncate();
        dbHandler.onUpgrade(null, 1, 2);

        loadData(inputStream, new Closure() {
            public void processLine(String line) {
                if (line != null) {
                    Map<String, String> entry = new HashMap<String, String>();
                    String[] columns = line.split("\\|");
                    int i = 0;
                    for (String column : columns) {
                        if (DatabaseHandler.getSchema().length > i) {
                            String key = DatabaseHandler.getSchema()[i];
                            if (key != null) {
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
