package game.controls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import sfogl.integration.Model;
import sfogl.integration.Node;
import shadow.math.SFMatrix3f;
import shadow.math.SFTransform3f;
import shadow.math.SFVertex3f;

/**
 * It keeps a list of Button and draws them.
 *
 * @author De Pace
 */
public class ButtonMaster {

    private HashMap<Button, Node> map = new HashMap<>();
    public static final String LOG_TAG = "ButtonsMaster";
    private Model model;
    private ArrayList<Node> parentNodes;

    /**
     * Creates a new ButtonMaster.
     */
    public ButtonMaster() {
        parentNodes = new ArrayList<>();
    }

    public void setModel(Model model) {
        this.model = model;
    }

    /**
     * Adds a Button, representing it with the last set Model.
     * The Button is represented with a Node placed and rotated compared with the parentNode.
     * Is useful to give a common pre-transformation to more Button.
     *
     * @param button Button to be added.
     * @param position Button position relative to the parentNode.
     * @param angle Button rotation angle relative to the parentNode.
     * @param parentNode Node which can contain transformations for more Button.
     */
    public void addButton(Button button, SFVertex3f position, float angle, Node parentNode) {
        Node node = new Node();
        map.put(button, setupNode(node, position, model, angle));

        Node realParent;
        if (parentNodes.contains(parentNode)) {
            realParent = parentNodes.get(parentNodes.indexOf(parentNode));
        } else {
            realParent = parentNode;
            parentNodes.add(realParent);
        }
        realParent.getSonNodes().add(node);
    }

    /**
     * @return Associated Button set.
     */
    public Set<Button> getButtons() {
        return map.keySet();
    }

    /**
     * @return Node which represents the specified Button.
     */
    public Node getButtonNode(Button button) {
        return map.get(button);
    }

    /**
     * Draws every Button.
     */
    public void draw() {
        for (Node n : parentNodes) {
            n.updateTree(new SFTransform3f());
            n.draw();
        }
    }

    private Node setupNode(Node node, SFVertex3f position, Model model, float angleZ) {
        SFMatrix3f tmp = SFMatrix3f.getRotationZ(angleZ);
        node.setModel(model);
        node.getRelativeTransform().setPosition(position);
        node.getRelativeTransform().setMatrix(tmp);
        return node;
    }

}
