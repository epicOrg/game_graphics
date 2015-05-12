package game.generators;

import android.content.Context;

import com.example.alessandro.computergraphicsexample.R;

import game.controls.Button;
import game.controls.ButtonAction;
import game.controls.ButtonMaster;
import game.listeners.PositionMoveListenerInterface;
import sfogl.integration.Model;
import sfogl.integration.Node;
import sfogl.integration.ShadingProgram;
import shadow.math.SFMatrix3f;
import shadow.math.SFVertex3f;

/**
 * Created by depa on 21/04/15.
 */
public class MoveButtonsGenerator {

    private Context context;
    private ShadingProgram program;
    private ButtonMaster buttonMaster;
    private PositionMoveListenerInterface positionMoveListener;

    public MoveButtonsGenerator(Context context, ShadingProgram program, ButtonMaster buttonMaster, PositionMoveListenerInterface positionMoveListener) {
        this.context = context;
        this.program = program;
        this.buttonMaster = buttonMaster;
        this.positionMoveListener = positionMoveListener;
    }

    public void generate() {
        Node parentNode = new Node();
        SFVertex3f parentPosition = new SFVertex3f(-1f, -0.50f, 1);
        SFMatrix3f scalingMatrix = SFMatrix3f.getScale(0.15f, 0.15f, 0.15f);

        parentNode.getRelativeTransform().setPosition(parentPosition);
        parentNode.getRelativeTransform().setMatrix(scalingMatrix);

        Model model = FundamentalGenerator.getModel(context, program, R.drawable.arrow_texture_02, "Arrow.obj");

        buttonMaster.setModel(model);
        buttonMaster.addButton(new Button("LEFT", new ButtonAction() {
                    @Override
                    public void action(Object parameter) {
                        positionMoveListener.move((float) +Math.PI / 2, 0, (long) parameter);
                    }
                }, true, true),
                new SFVertex3f(-2, 0, 0), (float) -Math.PI / 2, parentNode);

        buttonMaster.addButton(new Button("RIGHT", new ButtonAction() {
                    @Override
                    public void action(Object parameter) {
                        positionMoveListener.move((float) -Math.PI / 2, 0, (long) parameter);
                    }
                }, true, true),
                new SFVertex3f(2, 0, 0), (float) Math.PI / 2, parentNode);

        buttonMaster.addButton(new Button("UP", new ButtonAction() {
                    @Override
                    public void action(Object parameter) {
                        positionMoveListener.move(0, 0, (long) parameter);
                    }
                }, true, true),
                new SFVertex3f(0, 2, 0), (float) 0, parentNode);

        buttonMaster.addButton(new Button("DOWN", new ButtonAction() {
                    @Override
                    public void action(Object parameter) {
                        positionMoveListener.move((float) -Math.PI, 0, (long) parameter);
                    }
                }, true, true),
                new SFVertex3f(0, -2, 0), (float) -Math.PI, parentNode);
    }

}
