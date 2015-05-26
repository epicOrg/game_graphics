package game.graphics;

import android.content.Context;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import game.physics.CollisionBox;
import game.physics.CollisionMediator;
import sfogl.integration.Node;
import shadow.math.SFTransform3f;

/**
 * Map that contains all the MazeObjects, loads them in the collision system and draws them.
 *
 * @author Stefano De Pace
 */
public class Map {

    private LinkedList<MazeObject> list = new LinkedList<>();
    private Node mainNode = new Node();

    /**
     * Adds some MazeObjects to the Map.
     *
     * @param ms one or more MazeObject to add to the Map.
     */
    public void addObjects(MazeObject... ms) {
        Collections.addAll(list, ms);
    }

    /**
     * Loads all the MazeObject's CollisionBoxes that are not null in the given CollisionMediator, and
     * loads their Nodes for the rendering.
     *
     * @param cm      CollisionMediator where to insert the MazeObject's CollisionBox.
     * @param context Context from which to get the resources to represent the MazeObjects.
     */
    public void loadMap(CollisionMediator cm, Context context) {
        List<Node> ln = mainNode.getSonNodes();
        for (MazeObject m : list) {
            CollisionBox cb = m.getBox();
            if (cb != null)
                cm.addObject(cb);
            ln.add(m.getNode(context));
        }
    }

    /**
     * Draw the Map, namely every MazeObject's Node.
     */
    public void draw() {
        mainNode.updateTree(new SFTransform3f());
        mainNode.draw();
    }


}
