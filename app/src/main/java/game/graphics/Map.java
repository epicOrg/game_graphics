package game.graphics;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import game.physics.CollisionBox;
import game.physics.CollisionMediator;
import sfogl.integration.Node;
import sfogl2.SFOGLState;
import sfogl2.SFOGLStateEngine;
import shadow.math.SFTransform3f;

import static android.opengl.GLES20.GL_CULL_FACE;

/**
 * Map that contains all the {@link MazeObject}, loads them in the collision system and draws them.
 *
 * @author Stefano De Pace
 * @see MazeObject
 */
public class Map {

    private LinkedList<MazeObject> list = new LinkedList<>();
    private ArrayList<MapNodeData> nodes = new ArrayList<>();

    private boolean currentCullingState;
    private SFOGLState sfsWithCulling = SFOGLStateEngine.glEnable(GL_CULL_FACE);
    private SFOGLState sfsWithoutCulling = SFOGLStateEngine.glDisable(GL_CULL_FACE);

    /**
     * Adds one or more <code>MazeObjects</code> to the Map.
     *
     * @param ms one or more <code>MazeObjects</code> to add to the Map.
     */
    public void addObjects(MazeObject... ms) {
        Collections.addAll(list, ms);
    }

    /**
     * Loads all the <code>MazeObjects</code>'s <code>CollisionBox</code> that are not null in the given <code>CollisionMediator</code>, and
     * loads their <code>Node</code> for the rendering.
     *
     * @param cm      <code>CollisionMediator</code> where to insert the <code>MazeObjects</code>'s <code>CollisionBox</code>.
     * @param context <code>Context</code> from which to get the resources to represent the <code>MazeObjects</code>.
     */
    public void loadMap(CollisionMediator cm, Context context) {
        for (MazeObject m : list) {
            CollisionBox cb = m.getBox();
            if (cb != null)
                cm.addObject(cb);
            Node node = m.getNode(context);
            node.updateTree(new SFTransform3f());
            nodes.add(new MapNodeData(node, m.supportingCulling()));
        }
    }

    /**
     * Draw the <code>Map</code>, namely every <code>MazeObject</code>'s <code>Node</code>.
     */
    public void draw() {
        currentCullingState = true;
        sfsWithCulling.applyState();

        for (MapNodeData n : nodes) {
            if (n.supportCulling != currentCullingState) {
                currentCullingState = n.supportCulling;
                if (currentCullingState)
                    sfsWithCulling.applyState();
                else {
                    sfsWithoutCulling.applyState();
                }
            }
            n.node.draw();
        }
    }

    private class MapNodeData {
        private Node node;
        private boolean supportCulling;

        public MapNodeData(Node node, boolean supportCulling) {
            this.node = node;
            this.supportCulling = supportCulling;
        }
    }

}
