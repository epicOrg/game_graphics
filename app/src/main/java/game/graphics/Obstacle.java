package game.graphics;

import android.content.Context;
import game.generators.FundamentalGenerator;
import game.physics.Circle;
import game.physics.CollisionBox;
import sfogl.integration.Node;
import shadow.math.SFMatrix3f;
import shadow.math.SFVertex3f;

/**
 * Ostacolo cilindrico.
 *
 * @author Stefano De Pace
 */
public class Obstacle implements MazeObject {

    private Circle c;
    private int texture_id;
    private float height;

    /**
     * Crea un nuovo Obstacle di dimensioni e texture dati.
     *
     * @param c          Circle che costituisce la CollisionBox e determina le dimensioni nel piano xz.
     * @param height     altezza nella direzione y.
     * @param texture_id indice della texture nelle risorse.
     */
    public Obstacle(Circle c, double height, int texture_id) {
        this.c = c;
        this.texture_id = texture_id;
        this.height = (float) height / 2;
    }

    public Node getNode(Context context) {
        Node node = new Node();
        node.setModel(FundamentalGenerator.getModel(context, ShadersKeeper.getProgram(ShadersKeeper.STANDARD_TEXTURE_SHADER),
                texture_id, "Obstacle01.obj"));
        node.getRelativeTransform().setPosition(c.getPos());
        node.getRelativeTransform().setMatrix(SFMatrix3f.getScale(c.getRadius(), height, c.getRadius()));
        return node;
    }

    public CollisionBox getBox() {
        return c;
    }

    @Override
    public MazeObject cloneFromData(String position, String size, int textureId) {
        float posX = Float.parseFloat(position.split(" ")[0]);
        float posY = Float.parseFloat(position.split(" ")[1]);
        float posZ = Float.parseFloat(position.split(" ")[2]);
        float sizeX = Float.parseFloat(size.split(" ")[0]);
        float sizeY = Float.parseFloat(size.split(" ")[1]);
        return new Obstacle(new Circle(new SFVertex3f(posX, posY, posZ), sizeX), sizeY, textureId);
    }
}

