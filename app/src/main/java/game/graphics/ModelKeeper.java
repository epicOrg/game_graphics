package game.graphics;

import android.content.Context;
import java.util.HashMap;
import objLoader.ObjLoader;
import sfogl.integration.ArrayObject;
import sfogl.integration.Material;
import sfogl.integration.Mesh;
import sfogl.integration.Model;

/**
 * It manages the Models loaded from files.
 * It allows to access the Models without load them several times or
 * automatically load them when needed.
 */
public enum ModelKeeper {
    MODEL_KEEPER;

    private HashMap<String, Model> map = new HashMap<>();

    /**
     * Builds a Model from file, or returns it immediately in case it was already loaded.
     *
     * @param context  Context to load the files.
     * @param name     Name of the file that contains the model geometry.
     * @param material Material to use to build the Model.
     * @return The required Model; if a Model from the same file name was previously requested and loaded, that one is
     * returned, regardless of the other parameters.
     */
    public Model getModel(Context context, String name, Material material) {
        if (map.containsKey(name))
            return map.get(name);
        else {
            Model model = getModelFromArrayObjectAndMaterial(ObjLoader.arrayObjectFromFile(context, name)[0], material);
            map.put(name, model);
            return model;
        }
    }

    /**
     * Builds a Model from an ArrayObject and a specified Material.
     *
     * @param arrayObject The ArrayObject which contains the entire Model geometry.
     * @param material    Material to use to build the Model.
     * @return The generated Model.
     */
    public Model getModelFromArrayObjectAndMaterial(ArrayObject arrayObject, Material material) {
        Mesh meshPos = new Mesh(arrayObject);
        meshPos.init();
        Model modelPos = new Model();
        modelPos.setRootGeometry(meshPos);
        modelPos.setMaterialComponent(material);
        return modelPos;
    }


}
