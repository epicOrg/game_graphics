package game.codes;

import com.example.alessandro.computergraphicsexample.R;

import java.util.HashMap;

/**
 * Created by Andrea on 22/04/2015.
 */
public class TextureCodes {

    private static HashMap<String, Integer> codes = new HashMap<>();

    static {
        codes.put("wall_texture_01", R.drawable.wall_texture_01);
        codes.put("wall_texture_02", R.drawable.wall_texture_02);
        codes.put("wall_texture_03", R.drawable.wall_texture_03);
        codes.put("obstacle_texture_01", R.drawable.obstacle_texture_01);
        codes.put("hedge_texture_01", R.drawable.hedge_texture_01);
        codes.put("hedge_texture_02", R.drawable.hedge_texture_02);
        codes.put("hedge_texture_02_1", R.drawable.hedge_texture_02_1);
        codes.put("vase_texture", R.drawable.vase_texture);
    }

    public static int getTextureIdFromString(String s){
        return codes.get(s);
    }

}
