package game.generators;

import android.content.Context;
import android.util.Log;

import com.example.alessandro.computergraphicsexample.R;

import game.controls.Button;
import game.controls.ButtonAction;
import game.controls.ButtonMaster;
import sfogl.integration.Model;
import sfogl.integration.Node;
import sfogl.integration.ShadingProgram;
import shadow.math.SFMatrix3f;
import shadow.math.SFVertex3f;

/**
 * Created by depa on 21/04/15.
 */
public class SettingsButtonsGenerator {

    public static final String LOG_TAG = "SettingsButtonsGen";

    private Context context;
    private ShadingProgram program;
    private ButtonMaster buttonMaster;

    public SettingsButtonsGenerator(Context context, ShadingProgram program, ButtonMaster buttonMaster) {
        this.context = context;
        this.program = program;
        this.buttonMaster = buttonMaster;
    }

    public void generate() {
        Node parentNode = new Node();

        SFVertex3f parentPosition = new SFVertex3f(+1, +0.5f, 0);
        SFMatrix3f scalingMatrix = SFMatrix3f.getScale(0.15f, 0.15f, 0.15f);

        parentNode.getRelativeTransform().setPosition(parentPosition);
        parentNode.getRelativeTransform().setMatrix(SFMatrix3f.getRotationY((float) (Math.PI / 2)).MultMatrix(scalingMatrix));

        Model pauseModel = FundamentalGenerator.getModel(context, program, R.drawable.button_circle_pause_texture_01, "ButtonCirclePause01.obj");
        buttonMaster.setModel(pauseModel);
        buttonMaster.addButton(new Button(new ButtonAction() {
                    @Override
                    public void action(Object parameter) {
                        //TODO
                        Log.d(LOG_TAG, "Pressed pause button");
                    }
                }),
                new SFVertex3f(0, 0, 0), 0, parentNode);
    }

}
