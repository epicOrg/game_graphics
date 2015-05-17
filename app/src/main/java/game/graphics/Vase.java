package game.graphics;

import android.content.Context;

import game.generators.FundamentalGenerator;
import game.physics.Circle;
import game.physics.CollisionBox;
import sfogl.integration.Node;
import shadow.math.SFMatrix3f;

/**
 * Ostacolo cilindrico: vaso.
 *
 * @author Irene
 */
public class Vase implements MazeObject {

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
    public Vase(Circle c, double height, int texture_id) {
        this.c = c;
        this.texture_id = texture_id;
        this.height = (float) height / 2;
    }

    public Node getNode(Context context) {
        Node node = new Node();
        node.setModel(FundamentalGenerator.getModel(context, ShadersKeeper.getProgram(ShadersKeeper.STANDARD_TEXTURE_SHADER),
                texture_id, "vase.obj"));
        node.getRelativeTransform().setPosition(c.getPos());
        node.getRelativeTransform().setMatrix(SFMatrix3f.getScale(c.getRadius(), height, c.getRadius()));
        return node;
    }


    public CollisionBox getBox() {
        return c;
    }

}

