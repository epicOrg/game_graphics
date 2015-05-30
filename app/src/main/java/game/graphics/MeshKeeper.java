package game.graphics;

import android.content.Context;

import java.util.HashMap;

import objLoader.ObjLoader;
import sfogl.integration.ArrayObject;
import sfogl.integration.Mesh;

/**
 * @author Torlaschi
 * @date 30/05/2015
 */

public enum MeshKeeper {
    MESH_KEEPER;

    public final String LOG_TAG = "MeshKeeper";

    private HashMap<String, Mesh> map = new HashMap<>();

    public Mesh getMesh(Context context, String obj) {
        if (map.containsKey(obj))
            return map.get(obj);
        else {
            ArrayObject arrayObject = ObjLoader.arrayObjectFromFile(context, obj)[0];
            Mesh mesh = new Mesh(arrayObject);
            mesh.init();
            map.put(obj, mesh);
            return mesh;
        }
    }

    public void clear() {
        map.clear();
    }

}
