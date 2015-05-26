package game.graphics;

import com.example.alessandro.computergraphicsexample.R;

import java.util.HashMap;

/**
 * Singleton che si occupa di gestire il database di MazeObjects e relative textures.
 *
 * @author Stefano De Pace
 */
public enum  MapObjects {
    MAP;

    private HashMap<String,MazeObject> map;
    private HashMap<String, Integer> codes;

    /**
     * Restituisce un MazeObject da una serie di dati
     *
     * @param object Nome del MazeObject
     * @param position Posizione del MazeObject
     * @param size Dimensioni del MazeObject
     * @param texture nome della texture del MazeObject
     * @return MazeObject ricostruito dai dati.
     */
    public MazeObject getObjectFromNameAndData(String object, String position, String size, String texture){
        return map.get(object).cloneFromData(position, size, getTextureIdFromString(texture));
    }

    private int getTextureIdFromString(String s){
        return codes.get(s);
    }

    private MapObjects(){
        fillObjects();
        fillTextures();
    }

    private void fillObjects(){
        map=new HashMap<>();
        map.put("Wall",new Wall(null, 0));
        map.put("Obstacle", new Obstacle(null, 0, 0));
        map.put("Vase", new Vase(null, 0, 0));
    }

    private void fillTextures(){
        codes = new HashMap<>();
        codes.put("wall_texture_01", R.drawable.wall_texture_01);
        codes.put("wall_texture_02", R.drawable.wall_texture_02);
        codes.put("wall_texture_03", R.drawable.wall_texture_03);
        codes.put("obstacle_texture_01", R.drawable.obstacle_texture_01);
        codes.put("hedge_texture_01", R.drawable.hedge_texture_01);
        codes.put("hedge_texture_02", R.drawable.hedge_texture_02);
        codes.put("hedge_texture_02_1", R.drawable.hedge_texture_02_1);
        codes.put("hedge_texture_02_2", R.drawable.hedge_texture_02_2);
        codes.put("vase_texture", R.drawable.vase_texture);
    }

}
