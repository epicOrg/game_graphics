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
 * Mantiene un elenco di Button e li disegna.
 */
public class ButtonMaster {

    private HashMap<Button, Node> map = new HashMap<>();
    public static final String LOG_TAG = "ButtonsMaster";
    private Model model;
    private ArrayList<Node> parentNodes;

    /**
     * Crea un nuovo ButtonMaster.
     */
    public ButtonMaster() {
        parentNodes = new ArrayList<>();
    }

    /**
     * Setta il Model con cui rappresentare i Button aggiunti con addButton. Si può rappresentare diversi
     * Button con Model diversi chiamando setModel prima di ogni addButton.
     * @param model Model con cui rappresentare i Button aggiunti con addButton.
     */
    public void setModel(Model model) {
        this.model = model;
    }

    /**
     * Aggiunge un Button, rappresentandolo con l'ultimo Model settato con setModel. Lo rappresenta con un Node,
     * posizionato e ruotato rispetto ad un paretNode dato: utile per dare una pretrasformazione comune a più Button.
     * @param button Button da aggiungere.
     * @param position Posizione del Button relativa al parentNode dato.
     * @param angle Angolo di rotazione del Button rispetto al parentNode dato-
     * @param parentNode Node che può contenere trasformazioni da concatenare a più Button.
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
     * Restituisce un Set dei Button associati.
     * @return Set dei Button associati.
     */
    public Set<Button> getButtons() {
        return map.keySet();
    }

    /**
     * Restituisce il Node che rappresenta un certo Button.
     * @param button Button di cui si vuole il Node.
     * @return Node che rappresenta il Button dato.
     */
    public Node getButtonNode(Button button) {
        return map.get(button);
    }

    /**
     * Disegna tutti i Button.
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
