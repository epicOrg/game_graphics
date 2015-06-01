package game.graphics;

import android.content.Context;

import game.generators.FundamentalGenerator;
import game.physics.CollisionBox;
import sfogl.integration.Node;
import shadow.math.SFMatrix3f;
import shadow.math.SFVertex3f;

/**
 * Decoration of Object. Basically a collision-free <code>MazeObject</code> with customizable 3D model.
 *
 * @author De Pace
 * @see sfogl.integration.Model
 */
public class Decoration implements MazeObject {

    private int texture_id;
    private String model;
    private SFVertex3f position;
    private float scaleXZ, scaleY;
    private boolean supportCulling;

    /**
     * Creates a new <code>Decoration</code>.
     *
     * @param position       Position of the <code>Decoration</code>.
     * @param model          String name of the 3D model file to represent this <code>Decoration</code>.
     * @param texture_id     Id of the texture that represents this <code>Decoration</code>.
     * @param scalexz        Scale factor on the x-z plane.
     * @param scaley         Scale factor in the y direction.
     * @param supportCulling
     */
    public Decoration(SFVertex3f position, String model, int texture_id, float scalexz, float scaley, boolean supportCulling) {
        this.texture_id = texture_id;
        this.model = model;
        this.position = position;
        this.scaleXZ = scalexz;
        this.scaleY = scaley;
        this.supportCulling = supportCulling;
    }

    @Override
    public Node getNode(Context context) {
        Node node = new Node();
        node.setModel(FundamentalGenerator.getModel(context, ShadersKeeper.getProgram(ShadersKeeper.STANDARD_TEXTURE_SHADER),
                texture_id, model));
        node.getRelativeTransform().setPosition(position);
        node.getRelativeTransform().setMatrix(SFMatrix3f.getScale(scaleXZ, scaleY, scaleXZ));
        return node;
    }

    @Override
    public CollisionBox getBox() {
        return null;
    }

    @Override
    public boolean supportingCulling() {
        return false;
    }

    @Override
    public MazeObject cloneFromData(String position, String size, int textureId) {
        float posX = Float.parseFloat(position.split(" ")[0]);
        float posY = Float.parseFloat(position.split(" ")[1]);
        float posZ = Float.parseFloat(position.split(" ")[2]);
        float sizeX = Float.parseFloat(size.split(" ")[0]);
        float sizeY = Float.parseFloat(size.split(" ")[1]);
        return new Decoration(new SFVertex3f(posX, posY, posZ), model, textureId, sizeX, sizeY, supportCulling);
    }
}

