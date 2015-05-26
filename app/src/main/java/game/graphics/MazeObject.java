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
     * @param context Context to find the resouces to represent the object
     * @return Node representing the object.
     */
    public Node getNode(Context context);

    /**
     * @return Object CollisionBox; 'null' if the Object doesn't have to be in the collision system.
     */
    public CollisionBox getBox();

    /**
     * @param position MazeObject position.
     * @param size MazeObject dimension.
     * @param textureId MazeObject texture ID.
     * @return MazeObject which is built from the specified parameters..
     */
    public MazeObject cloneFromData(String position, String size, int textureId);

}
