package game.generators;

import java.util.ArrayList;

import sfogl.integration.Model;
import sfogl.integration.Node;
import shadow.math.SFTransform3f;

/**
 * Gestisce la costruzione del terreno.
 * @author Andrea
 */
public class GroundGenerator {

    private Model model;

    /**
     * Crea un nuovo GroundGenerator dato un Model.
     * @param model Model con cui costruire il terreno.
     */
    public GroundGenerator(Model model) {
        this.model = model;
    }

    /**
     * Costruisce il terreno con una griglia quadrata di copie del Model dato, il tutto centrato in un punto.
     * @param xCenter x del centro
     * @param zCenter z del centro
     * @param xSize dimensione x del terreno
     * @param zSize dimensione z del terreno
     * @param yCenter dimensione y del terreno
     * @return restituisce il Node che contiene il terreno, con sottonodi disposti a griglia che rappresentano la varie parti.
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
