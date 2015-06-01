package game.graphics;

import com.example.alessandro.computergraphicsexample.R;

/**
 * It's a singleton that manages the {@link MazeObject} database and the relative textures.
 *
 * @author De Pace
 */
public enum MapObjects {
    MAP;

    public static final String LOG_TAG = "MapObjects";
    public static final int DEFAULT_TEXTURE = R.drawable.wall_texture_01;
    private MapResources<MazeObject> map;
    private MapResources<Integer> codes;

    /**
     * @param object   <code>MazeObject</code> name.
     * @param position <code>MazeObject</code> position.
     * @param size     <code>MazeObject</code> dimension.
     * @param texture  <code>MazeObject</code> texture's name.
     * @return <code>MazeObject</code> which is built from the specified parameters.
     */
    public MazeObject getObjectFromNameAndData(String object, String position, String size, String texture) {
        return map.getResourceFromName(object).cloneFromData(position, size, codes.getResourceFromName(texture));
    }

    MapObjects() {
        fillObjects();
        fillTextures();
    }

    private void fillObjects() {
        map = new MapResources<>(null, "Object", LOG_TAG);
        map.mapResource("Wall", new Wall(null, 0));
        map.mapResource("Column", new Obstacle(null, 0, 0, "Obstacle01.obj"));
        map.mapResource("Vase", new Obstacle(null, 0, 0, "vase.obj"));
        map.mapResource("Meat", new Decoration(null, "Meat.obj", 0, 0, 0));
        map.mapResource("Carrot", new Decoration(null, "Carrot.obj", 0, 0, 0));
    }

    private void fillTextures() {
        codes = new MapResources<>(DEFAULT_TEXTURE, "Texture", LOG_TAG);
        codes.mapResource("wall_texture_01", R.drawable.wall_texture_01);
        codes.mapResource("wall_texture_02", R.drawable.wall_texture_02);
        codes.mapResource("obstacle_texture_01", R.drawable.obstacle_texture_01);
        codes.mapResource("vase_texture", R.drawable.vase_texture);
        codes.mapResource("meat_texture_01", R.drawable.meat_texture_01);
        codes.mapResource("carrot_texture_01", R.drawable.carrot_texture_01);
    }


}
