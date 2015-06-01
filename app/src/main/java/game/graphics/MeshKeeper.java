package game.graphics;

import android.content.Context;

import java.util.HashMap;

import objLoader.ObjLoader;
import sfogl.integration.ArrayObject;
import sfogl.integration.Mesh;

/**
 * Singleton that manages the {@link Mesh} loaded from files.
 * It allows to access the {@link Mesh} without load them several times or
 * automatically load them when needed.
 *
 * @author Torlaschi
 * @date 30/05/2015
 */

public enum MeshKeeper {
    MESH_KEEPER;

    public final String LOG_TAG = "MeshKeeper";

    private HashMap<String, Mesh> map = new HashMap<>();

    /**
     * Returns a <code>Mesh</code> object from geometry file.
     * @param context <code>Context</code> from which to retrieve the file.
     * @param obj <code>String</code> name of the file containing the <code>Mesh</code> geometry.
     * @return a <code>Mesh</code> object from the given geometry file, or the previusly mapped one if
     * the same obj was was requested before.
     */
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

    /**
     * Clears all the mapped <code>Mesh</code>.
     */
    public void clear() {
        map.clear();
    }

}
