package game.graphics;

import android.content.Context;

import java.util.HashMap;

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

    public final String LOG_TAG = "ModelKeeper";

    private HashMap<ModelData, Model> map = new HashMap<>();

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
        ModelData modelData = new ModelData(name, material);
        if (map.containsKey(modelData))
            return map.get(modelData);
        else {
            Model model = getModelFromObjectAndMaterial(context, name, material);
            map.put(modelData, model);
            return model;
        }
    }

    private Model getModelFromObjectAndMaterial(Context context, String name, Material material) {
        Mesh meshPos = MeshKeeper.MESH_KEEPER.getMesh(context, name);
        Model modelPos = new Model();
        modelPos.setRootGeometry(meshPos);
        modelPos.setMaterialComponent(material);
        return modelPos;
    }

    public void clear() {
        map.clear();
    }

    private class ModelData {

        private String obj;
        private Material material;

        public ModelData(String obj, Material material) {
            this.obj = obj;
            this.material = material;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ModelData modelData = (ModelData) o;

            if (obj != null ? !obj.equals(modelData.obj) : modelData.obj != null) return false;
            return !(material != null ? !material.equals(modelData.material) : modelData.material != null);

        }

        @Override
        public int hashCode() {
            int result = obj != null ? obj.hashCode() : 0;
            result = 31 * result + (material != null ? material.hashCode() : 0);
            return result;
        }

    }

}
