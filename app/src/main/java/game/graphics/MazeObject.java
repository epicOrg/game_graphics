package game.graphics;

import android.content.Context;
import game.physics.CollisionBox;
import sfogl.integration.Node;

/**
 * It represents a generic object in the labyrinth.
 *
 * @author De Pace
 */
public interface MazeObject {

    /**
     * Returns a <code>Node</code> representing the <code>MazeObject</code>.
     *
     * @param context <code>Context</code> to find the resouces to represent the object
     * @return <code>Node</code> representing the object.
     */
    public Node getNode(Context context);

    /**
     * Returns the <code>MazeObject</code>'s <code>CollisionBox</code> for collision checking.
     *
     * @return Object <code>CollisionBox</code>; 'null' if the Object doesn't have to be in the collision system.
     */
    public CollisionBox getBox();

    /**
     * Allows to create different <code>MazeObject</code> from a single prototype, with different parameters.
     *
     * @param position <code>CollisionBox</code> position.
     * @param size <code>CollisionBox</code> dimension.
     * @param textureId <code>CollisionBox</code> texture ID.
     * @return <code>CollisionBox</code> which is built from the specified parameters..
     */
    public MazeObject cloneFromData(String position, String size, int textureId);

}
