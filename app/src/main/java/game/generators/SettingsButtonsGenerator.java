package game.generators;

import android.content.Context;

import com.example.alessandro.computergraphicsexample.R;

import game.controls.Button;
import game.controls.ButtonAction;
import game.controls.ButtonMaster;
import game.views.SettingsScreen;
import sfogl.integration.Model;
import sfogl.integration.Node;
import sfogl.integration.ShadingProgram;
import shadow.math.SFMatrix3f;
import shadow.math.SFVertex3f;

/**
 * This class builds the pause Button.
 *
 * @author Torlaschi
 */
public class SettingsButtonsGenerator {

    public static final String LOG_TAG = "SettingsButtonsGen";

    private Context context;
    private ShadingProgram program;
    private ButtonMaster buttonMaster;
    private SettingsScreen settingsScreen;

    /**
     * It creates a new SettingsButtonGenerator.
     *
     * @param context Context to find resources.
     * @param program ShadingProgram to represent the Button.
     * @param buttonMaster ButtonMaster which the movement Button has to be associated with.
     * @param settingsScreen SettingScreen to be shown when the Button is pressed.
     */
    public SettingsButtonsGenerator(Context context, ShadingProgram program, ButtonMaster buttonMaster, SettingsScreen settingsScreen) {
        this.context = context;
        this.program = program;
        this.buttonMaster = buttonMaster;
        this.settingsScreen = settingsScreen;
    }

    /**
     * It creates pause Button.
     *
     * @param position Button position.
     * @param scale Scale factor.
     */
    public void generate(SFVertex3f position, float scale) {
        Node parentNode = new Node();
        parentNode.getRelativeTransform().setPosition(position);
        parentNode.getRelativeTransform().setMatrix(SFMatrix3f.getRotationY((float) (Math.PI / 2)).MultMatrix(SFMatrix3f.getScale(scale,scale,scale)));

        Model pauseModel = FundamentalGenerator.getModel(context, program, R.drawable.button_circle_pause_texture_01, "ButtonCirclePause01.obj");
        buttonMaster.setModel(pauseModel);
        buttonMaster.addButton(new Button("PAUSE", new ButtonAction() {
                    @Override
                    public void action(Object parameter) {
                        settingsScreen.show();
                    }
                }, false, false),
                new SFVertex3f(0, 0, 0), 0, parentNode);
    }

}
