package game.graphics;

import android.content.Context;

import game.physics.CollisionBox;
import game.physics.Square;
import sfogl.integration.Node;
import shadow.math.SFVertex3f;

/**
 * It represents a wall with the form of a parallelepiped.
 *
 * @author De Pace
 * @see Square
 */
public class Wall implements MazeObject {

    private Square box;
    private int textureID;

    /**
     * Creates a new wall. Its dimension is proportional to those of the specific <code>Square</code>.
     *
     * @param box       <code>Square</code> which represent the <code>Wall</code> collision box.
     * @param textureID Index of the texture which represent the <code>Wall</code> face.
     */
    public Wall(Square box, int textureID) {
        this.box = box;
        this.textureID = textureID;
    }

    @Override
    public Node getNode(Context context) {
        Node node = new Node();
        node.setModel(new WallGenerator(context, ShadersKeeper.getProgram(ShadersKeeper.STANDARD_TEXTURE_SHADER),
                textureID, box.getxSize(), box.getySize(), box.getzSize()).getModel());
        SFVertex3f position = box.getPos();
        node.getRelativeTransform().setPosition(position.getX(), position.getY(), position.getZ());
        return node;
    }

    @Override
    public CollisionBox getBox() {
        return box;
    }

    @Override
    public boolean supportingCulling() {
        return true;
    }

    @Override
    public MazeObject cloneFromData(String position, String size, int textureId) {
        float posX = Float.parseFloat(position.split(" ")[0]);
        float posY = Float.parseFloat(position.split(" ")[1]);
        float posZ = Float.parseFloat(position.split(" ")[2]);
        float sizeX = Float.parseFloat(size.split(" ")[0]);
        float sizeY = Float.parseFloat(size.split(" ")[1]);
        float sizeZ = Float.parseFloat(size.split(" ")[2]);
        return new Wall(new Square(new SFVertex3f(posX, posY, posZ), sizeX, sizeY, sizeZ), textureId);
    }
}
