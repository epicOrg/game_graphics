package game.graphics;

import com.example.alessandro.computergraphicsexample.R;

import java.util.HashMap;

/**
 * It's a singleton that manages the MazeObject database and the relative textures.
 *
 * @author De Pace
 */
public enum MapObjects {
    MAP;

    private HashMap<String, MazeObject> map;
    private HashMap<String, Integer> codes;

    /**
     * @param object   MazeObject name.
     * @param position MazeObject position.
     * @param size     MazeObject dimension.
     * @param texture  MazeObject texture's name.
     * @return MazeObject which is built from the specified parameters.
     */
    public MazeObject getObjectFromNameAndData(String object, String position, String size, String texture) {
        return map.get(object).cloneFromData(position, size, getTextureIdFromString(texture));
    }

    private int getTextureIdFromString(String s) {
        return codes.get(s);
    }

    MapObjects() {
        fillObjects();
        fillTextures();
    }

    private void fillObjects() {
        map = new HashMap<>();
        map.put("Wall", new Wall(null, 0));
        map.put("Column", new Obstacle(null, 0, 0, "Obstacle01.obj"));
        map.put("Vase", new Obstacle(null, 0, 0, "vase.obj"));
        map.put("Meat", new Decoration(null, "Meat.obj", 0, 0, 0));
        map.put("Carrot", new Decoration(null, "Carrot.obj", 0, 0, 0));
    }

    private void fillTextures() {
        codes = new HashMap<>();
        codes.put("wall_texture_01", R.drawable.wall_texture_01);
        codes.put("wall_texture_02", R.drawable.wall_texture_02);
        codes.put("obstacle_texture_01", R.drawable.obstacle_texture_01);
        codes.put("hedge_texture_01", R.drawable.hedge_texture_01);
        codes.put("vase_texture", R.drawable.vase_texture);
        codes.put("meat_texture_01", R.drawable.meat_texture_01);
        codes.put("carrot_texture_01", R.drawable.carrot_texture_01);
    }

}
