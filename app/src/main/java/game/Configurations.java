package game;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Singleton for the operations of configuration.
 *
 * @author De Pace
 */
public enum Configurations {
    CONF;

    public static final String LOG_TAG = "Configurations";
    private HashMap<String, HashMap<String, String>> configurations = new HashMap<>();

    private String address;
    private Context context;

    public void init(String address, Context context) {
        this.address = address;
        this.context = context;
        Log.d(LOG_TAG, "Initialized Configurations with address: " + address + " and context: " + context);
    }

    public String getString(String filename, String name) {
        return getValue(filename, name);
    }

    public int getInt(String filename, String name) {
        return Integer.parseInt(getValue(filename, name));
    }

    public float getFloat(String filename, String name) {
        return Float.parseFloat(getValue(filename, name));
    }

    private String getValue(String filename, String name) {
        if (this.context == null || this.address == null)
            throw new RuntimeException("Must first call the init method with valid parameters!!");
        if (configurations.containsKey(filename)) {
            Log.d(LOG_TAG, "read: " + filename + "->" + name);
            return configurations.get(filename).get(name);
        } else {
            Log.d(LOG_TAG, "loaded: " + filename + "->" + name);
            HashMap<String, String> map = loadFile(filename);
            if (map != null)
                return map.get(name);
            else
                throw new RuntimeException("File '" + filename + "' not found!");
        }
    }

    private HashMap<String, String> loadFile(String filename) {
        InputStream stream;
        try {
            stream = this.context.getAssets().open(address + "/" + filename);
            HashMap<String, String> map = new HashMap<>();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String line = reader.readLine();
            while (line != null && line.length() > 0) {
                mapValues(map, line);
                line = reader.readLine();
            }
            loadMap(filename, map);
            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void mapValues(HashMap<String, String> map, String line) {
        String[] stringA = line.split(" ");
        if (stringA.length > 1) {
            String result = line.substring(stringA[0].length(), line.length()).trim();
            map.put(stringA[0], result);
        }
    }

    private void loadMap(String filename, HashMap<String, String> map) {
        configurations.put(filename, map);
    }
}
