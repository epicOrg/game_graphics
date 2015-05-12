package game.controls;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import sfogl.integration.Model;
import sfogl.integration.Node;
import shadow.math.SFMatrix3f;
import shadow.math.SFTransform3f;
import shadow.math.SFVertex3f;

/**
 * Created by depa on 21/04/15.
 */
public class ButtonMaster {

    private HashMap<Button, Node> map = new HashMap<>();
    public static final String LOG_TAG = "ButtonsMaster";
    private Model model;
    private ArrayList<Node> parentNodes;

    public ButtonMaster() {
        parentNodes = new ArrayList<>();
    }

    public void setModel(Model model) {
        this.model = model;
    }

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

    public Set<Button> getButtons() {
        return map.keySet();
    }

    public Node getButtonNode(Button button) {
        return map.get(button);
    }

    public void draw() {
        Log.d(LOG_TAG,"Draw Buttons: "+parentNodes);
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
