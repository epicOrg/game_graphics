package game.graphics;

import android.content.Context;

import game.generators.FundamentalGenerator;
import game.physics.Circle;
import game.physics.CollisionBox;
import sfogl.integration.Node;
import shadow.math.SFMatrix3f;
import shadow.math.SFVertex3f;

/**
 * Ostacolo senza collisione.
 *
 * @author Stefano De Pace
 */
public class Decoration implements MazeObject {

    private int texture_id;
    private String model;
    private SFVertex3f position;
    private float scaleX, scaleY;

    public Decoration(SFVertex3f position, String model, int texture_id, float scalex, float scaley) {
        this.texture_id = texture_id;
        this.model=model;
        this.position=position;
        this.scaleX=scalex;
        this.scaleY=scaley;
    }

    public Node getNode(Context context) {
        Node node = new Node();
        node.setModel(FundamentalGenerator.getModel(context, ShadersKeeper.getProgram(ShadersKeeper.STANDARD_TEXTURE_SHADER),
                texture_id, model));
        node.getRelativeTransform().setPosition(position);
        node.getRelativeTransform().setMatrix(SFMatrix3f.getScale(scaleX, scaleY, scaleX));
        return node;
    }

    public CollisionBox getBox() {
        return null;
    }

    @Override
    public MazeObject cloneFromData(String position, String size, int textureId) {
        float posX = Float.parseFloat(position.split(" ")[0]);
        float posY = Float.parseFloat(position.split(" ")[1]);
        float posZ = Float.parseFloat(position.split(" ")[2]);
        float sizeX = Float.parseFloat(size.split(" ")[0]);
        float sizeY = Float.parseFloat(size.split(" ")[1]);
        return new Decoration(new SFVertex3f(posX, posY, posZ), model, textureId, sizeX, sizeY);
    }
}

