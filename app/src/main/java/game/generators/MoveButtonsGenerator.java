package game.generators;

import android.content.Context;

import com.example.alessandro.computergraphicsexample.R;

import game.controls.Button;
import game.controls.ButtonAction;
import game.controls.ButtonMaster;
import game.graphics.MaterialKeeper;
import game.listeners.PositionMoveListenerInterface;
import sfogl.integration.Model;
import sfogl.integration.Node;
import sfogl.integration.ShadingProgram;
import shadow.math.SFMatrix3f;
import shadow.math.SFVertex3f;

/**
 * This class creates the 4 {@link Button} of movement.
 *
 * @author De Pace
 * @see Button
 */
public class MoveButtonsGenerator {

    private Context context;
    private ShadingProgram program;
    private ButtonMaster buttonMaster;
    private PositionMoveListenerInterface positionMoveListener;

    /**
     * Creates a new <code>MoveButtonsGenerator</code>.
     *
     * @param context              <code>Context</code> to find resources.
     * @param program              <code>ShadingProgram</code> to represent the <code>Button</code>.
     * @param buttonMaster         <code>ButtonMaster</code> which the movement <code>Button</code> has to be associated with.
     * @param positionMoveListener Interface through which the movement action is associated with <code>Button</code>.
     */
    public MoveButtonsGenerator(Context context, ShadingProgram program, ButtonMaster buttonMaster, PositionMoveListenerInterface positionMoveListener) {
        this.context = context;
        this.program = program;
        this.buttonMaster = buttonMaster;
        this.positionMoveListener = positionMoveListener;
    }

    /**
     * Generates the movement <code>Button</code> in a cross configuration.
     *
     * @param center   Central position of the <code>Button</code>.
     * @param scale    Scale factor.
     * @param distance Distance between movement <code>Button</code>.
     */
    public void generate(SFVertex3f center, float scale, float distance) {
        Node parentNode = new Node();
        SFMatrix3f scalingMatrix = SFMatrix3f.getScale(scale, scale, scale);

        parentNode.getRelativeTransform().setPosition(center);
        parentNode.getRelativeTransform().setMatrix(scalingMatrix);

        int color = context.getResources().getColor(R.color.primary);
        Model model = FundamentalGenerator.getModelFromFileAndMaterial(context, MaterialKeeper.MATERIAL_KEEPER.getColorMaterial(program, color), "Arrow.obj");

        buttonMaster.setModel(model);
        buttonMaster.addButton(new Button("LEFT", new ButtonAction() {
                    @Override
                    public void action(Object parameter) {
                        positionMoveListener.move((float) +Math.PI / 2, 0, (long) parameter);
                    }
                }, true, true),
                new SFVertex3f(-distance, 0, 0), (float) -Math.PI / 2, parentNode);

        buttonMaster.addButton(new Button("RIGHT", new ButtonAction() {
                    @Override
                    public void action(Object parameter) {
                        positionMoveListener.move((float) -Math.PI / 2, 0, (long) parameter);
                    }
                }, true, true),
                new SFVertex3f(distance, 0, 0), (float) Math.PI / 2, parentNode);

        buttonMaster.addButton(new Button("UP", new ButtonAction() {
                    @Override
                    public void action(Object parameter) {
                        positionMoveListener.move(0, 0, (long) parameter);
                    }
                }, true, true),
                new SFVertex3f(0, distance, 0), (float) 0, parentNode);

        buttonMaster.addButton(new Button("DOWN", new ButtonAction() {
                    @Override
                    public void action(Object parameter) {
                        positionMoveListener.move((float) -Math.PI, 0, (long) parameter);
                    }
                }, true, true),
                new SFVertex3f(0, -distance, 0), (float) -Math.PI, parentNode);
    }

}
