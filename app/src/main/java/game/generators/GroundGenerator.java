package game.generators;

import java.util.ArrayList;

import sfogl.integration.Model;
import sfogl.integration.Node;
import shadow.math.SFTransform3f;

/**
 * It anages the ground building.
 *
 * @author Torlaschi
 */
public class GroundGenerator {

    private Model model;

    /**
     * It creates a new GroundGenerator from a specified Model.
     */
    public GroundGenerator(Model model) {
        this.model = model;
    }

    /**
     * It builds the ground with a squared grid of Model's copies.
     * It is centered in a specified position.
     *
     * @param xCenter x of the center.
     * @param zCenter z df the center.
     * @param xSize Ground x dimension.
     * @param zSize Ground z dimension.
     * @param yCenter Ground y dimension.
     */
    public Node getGroundNode(int xCenter, int zCenter, int xSize, int zSize, int yCenter) {
        Node node = new Node();
        node.setup();
        ArrayList<Node> tempList = node.getSonNodes();

        int leftToTheCenter = xSize / 2;
        int nearToTheCenter = zSize / 2;
        int tempX = xCenter - 2 * leftToTheCenter;
        int tempZ = zCenter - 2 * nearToTheCenter;

        for (int i = 0; i < xSize; i++) {
            for (int j = 0; j < zSize; j++) {
                Node tmpNode = new Node();
                tmpNode.setModel(model);
                int xC = tempX + 2 * i;
                int zC = tempZ + 2 * j;
                tmpNode.getRelativeTransform().setPosition(xC, yCenter, zC);
                tempList.add(tmpNode);
            }
        }

        node.updateTree(new SFTransform3f());

        return node;
    }

}
