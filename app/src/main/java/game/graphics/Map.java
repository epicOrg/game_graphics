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
 * Map that contains all the <code>MazeObjects</code>, loads them in the collision system and draws them.
 *
 * @author Stefano De Pace
 */
public class Map {

    private LinkedList<MazeObject> list = new LinkedList<>();
    private Node mainNode = new Node();

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
        List<Node> ln = mainNode.getSonNodes();
        for (MazeObject m : list) {
            CollisionBox cb = m.getBox();
            if (cb != null)
                cm.addObject(cb);
            ln.add(m.getNode(context));
        }
    }

    /**
     * Draw the <code>Map</code>, namely every <code>MazeObject</code>'s <code>Node</code>.
     */
    public void draw() {
        mainNode.updateTree(new SFTransform3f());
        mainNode.draw();
    }


}
