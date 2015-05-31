package game.graphics;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.example.alessandro.computergraphicsexample.R;

import java.util.List;

import game.generators.FundamentalGenerator;
import sfogl.integration.ArrayObject;
import sfogl.integration.Node;
import sfogl.integration.ShadingProgram;
import shadow.math.SFMatrix3f;
import shadow.math.SFTransform3f;
import shadow.math.SFVertex3f;

/**
 * @author Torlaschi
 * @date 31/03/2015
 */
public class Sky {

    private static final int SKY_DISTANCE = 100;
    private static final short[] indices = new short[]{3, 2, 0, 0, 1, 3};

    private ArrayObject arrayObjectPosX = new ArrayObject(
            new float[]{
                    +SKY_DISTANCE, -SKY_DISTANCE, -SKY_DISTANCE,
                    +SKY_DISTANCE, -SKY_DISTANCE, +SKY_DISTANCE,
                    +SKY_DISTANCE, +SKY_DISTANCE, -SKY_DISTANCE,
                    +SKY_DISTANCE, +SKY_DISTANCE, +SKY_DISTANCE
            },
            new float[]{
                    -1, 0, 0
            },
            new float[]{
                    0, 0, 0,
                    1, 0, 0,
                    0, 1, 0,
                    1, 1, 0
            },
            indices
    );
    private ArrayObject arrayObjectPosY = new ArrayObject(
            new float[]{
                    +SKY_DISTANCE, +SKY_DISTANCE, +SKY_DISTANCE,
                    -SKY_DISTANCE, +SKY_DISTANCE, +SKY_DISTANCE,
                    +SKY_DISTANCE, +SKY_DISTANCE, -SKY_DISTANCE,
                    -SKY_DISTANCE, +SKY_DISTANCE, -SKY_DISTANCE

            },
            new float[]{
                    0, -1, 0,
            },
            new float[]{
                    1, 1, 0,
                    0, 1, 0,
                    1, 0, 0,
                    0, 0, 0
            },
            indices
    );
    private ArrayObject arrayObjectPosZ = new ArrayObject(
            new float[]{
                    -SKY_DISTANCE, -SKY_DISTANCE, +SKY_DISTANCE,
                    -SKY_DISTANCE, +SKY_DISTANCE, +SKY_DISTANCE,
                    +SKY_DISTANCE, -SKY_DISTANCE, +SKY_DISTANCE,
                    +SKY_DISTANCE, +SKY_DISTANCE, +SKY_DISTANCE
            },
            new float[]{
                    0, 0, -1
            },
            new float[]{
                    1, 0, 0,
                    1, 1, 0,
                    0, 0, 0,
                    0, 1, 0
            },
            indices
    );
    private ArrayObject arrayObjectNegX = new ArrayObject(
            new float[]{
                    -SKY_DISTANCE, +SKY_DISTANCE, +SKY_DISTANCE,
                    -SKY_DISTANCE, -SKY_DISTANCE, +SKY_DISTANCE,
                    -SKY_DISTANCE, +SKY_DISTANCE, -SKY_DISTANCE,
                    -SKY_DISTANCE, -SKY_DISTANCE, -SKY_DISTANCE
            },
            new float[]{
                    +1, 0, 0
            },
            new float[]{
                    0, 1, 0,
                    0, 0, 0,
                    1, 1, 0,
                    1, 0, 0
            },
            indices
    );
    private ArrayObject arrayObjectNegZ = new ArrayObject(
            new float[]{
                    +SKY_DISTANCE, -SKY_DISTANCE, -SKY_DISTANCE,
                    +SKY_DISTANCE, +SKY_DISTANCE, -SKY_DISTANCE,
                    -SKY_DISTANCE, -SKY_DISTANCE, -SKY_DISTANCE,
                    -SKY_DISTANCE, +SKY_DISTANCE, -SKY_DISTANCE
            },
            new float[]{
                    0, 0, +1
            },
            new float[]{
                    1, 0, 0,
                    1, 1, 0,
                    0, 0, 0,
                    0, 1, 0
            },
            indices
    );
    private Context context;
    private ShadingProgram program;
    private SFVertex3f position;
    private Node mainNode = new Node();

    /**
     * Creates a new Sky cube centered in the given position.
     *
     * @param context  Context from which to retrieve the resources.
     * @param program  ShadingProgram to use for drawing the Sky.
     * @param position position of the center of the Sky;
     */
    public Sky(Context context, ShadingProgram program, SFVertex3f position) {
        this.program = program;
        this.context = context;
        this.position = position;

        setup();
    }

    private void setup() {
        List<Node> list = mainNode.getSonNodes();
        list.add(generateNode(arrayObjectPosX, R.drawable.skybox_posx));
        list.add(generateNode(arrayObjectPosY, R.drawable.skybox_posy));
        list.add(generateNode(arrayObjectPosZ, R.drawable.skybox_negz));
        list.add(generateNode(arrayObjectNegX, R.drawable.skybox_negx));
        list.add(generateNode(arrayObjectNegZ, R.drawable.skybox_posz));

        mainNode.getRelativeTransform().setPosition(0, 0, 0);
        mainNode.getRelativeTransform().setMatrix(SFMatrix3f.getIdentity());
    }

    private Node generateNode(ArrayObject arrayObject, int textureId) {
        return FundamentalGenerator.generateNode(arrayObject,
                BitmapFactory.decodeResource(context.getResources(), textureId), ShadersKeeper.getProgram(ShadersKeeper.STANDARD_TEXTURE_SHADER));
    }

    /**
     * Draws the Sky.
     */
    public void draw() {
        mainNode.getRelativeTransform().setPosition(position.getX(), position.getY(), position.getZ());
        mainNode.updateTree(new SFTransform3f());
        mainNode.draw();
    }

}
