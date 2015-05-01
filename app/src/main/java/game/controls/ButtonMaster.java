package game.controls;

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

    private HashMap<Button,Node> map=new HashMap<>();
    public static final String LOG_TAG = "ButtonsMaster";
    private Model model;
    private Node mainNode;

    public ButtonMaster(Model model, float scaling, SFVertex3f position){
        this.model=model;
        mainNode=new Node();
        mainNode.getRelativeTransform().setPosition(position);
        mainNode.getRelativeTransform().setMatrix(SFMatrix3f.getScale(scaling, scaling, scaling));
    }

    public void setModel(Model model){
        this.model=model;
    }

    public void addButton(Button button, SFVertex3f position, float angle){
        Node node=new Node();
        map.put(button,setupNode(node,position,model,angle));
        mainNode.getSonNodes().add(node);
    }

    public Set<Button> getButtons(){
        return map.keySet();
    }

    public Node getButtonNode(Button button){
        return map.get(button);
    }

    public void draw(){
        mainNode.updateTree(new SFTransform3f());
        mainNode.draw();
    }

    private Node setupNode(Node node, SFVertex3f position, Model model, float angleZ){
        SFMatrix3f tmp = SFMatrix3f.getRotationZ(angleZ);
        node.setModel(model);
        node.getRelativeTransform().setPosition(position);
        node.getRelativeTransform().setMatrix(tmp);
        return node;
    }

}
