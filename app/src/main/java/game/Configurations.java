package game;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Created by depa on 27/05/15.
 */
public enum Configurations {
    CONF;

    public static final String ADDRESS ="configurations";
    public static final String LOG_TAG="Configurations";
    private HashMap<String, HashMap<String,String>> configurations=new HashMap<>();

    public String getString(Context context, String filename, String name){
        return getValue(context, filename, name);
    }

    public int getInt(Context context, String filename, String name){
        return Integer.parseInt(getValue(context, filename, name));
    }

    public float getFloat(Context context, String filename, String name){
        return Float.parseFloat(getValue(context, filename, name));
    }

    private String getValue(Context context, String filename, String name){
        if (configurations.containsKey(filename)) {
            Log.d(LOG_TAG,"read: "+filename+"->"+name);
            return configurations.get(filename).get(name);
        }
        else{
            Log.d(LOG_TAG,"loaded: "+filename+"->"+name);
            return loadFile(context, filename).get(name);
        }
    }

    private HashMap<String,String> loadFile(Context context, String filename){
        InputStream stream;
        try {
            stream = context.getAssets().open(ADDRESS +"/" + filename);
            HashMap<String,String> map=new HashMap<>();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String line=reader.readLine();
            while(line!=null && line.length()>0){
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

    private void mapValues(HashMap<String, String> map, String line){
        String[] stringA = line.split(" ");
        map.put(stringA[0], stringA[1]);
    }

    private void loadMap(String filename, HashMap<String,String> map){
        configurations.put(filename,map);
    }

}
