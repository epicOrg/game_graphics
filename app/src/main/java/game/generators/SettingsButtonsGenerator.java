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
 * This class builds the pause {@link Button}.
 *
 * @author Torlaschi
 * @see Button
 */
public class SettingsButtonsGenerator {

    public static final String LOG_TAG = "SettingsButtonsGen";

    private Context context;
    private ShadingProgram program;
    private ButtonMaster buttonMaster;
    private SettingsScreen settingsScreen;

    /**
     * Creates a new <code><SettingsButtonGenerator</code>.
     *
     * @param context        <code>Context to find resources.
     * @param program        <code>ShadingProgram</code> to represent the <code>Button</code>.
     * @param buttonMaster   <code>ButtonMaster</code> which the movement <code>Button</code> has to be associated with.
     * @param settingsScreen <code>SettingScreen</code> to be shown when the <code>Button</code> is pressed.
     */
    public SettingsButtonsGenerator(Context context, ShadingProgram program, ButtonMaster buttonMaster, SettingsScreen settingsScreen) {
        this.context = context;
        this.program = program;
        this.buttonMaster = buttonMaster;
        this.settingsScreen = settingsScreen;
    }

    /**
     * Creates pause <code>Button</code>.
     *
     * @param position <code>Button</code> position.
     * @param scale    Scale factor.
     */
    public void generate(SFVertex3f position, float scale) {
        Node parentNode = new Node();
        parentNode.getRelativeTransform().setPosition(position);
        parentNode.getRelativeTransform().setMatrix(SFMatrix3f.getRotationY((float) (Math.PI / 2)).MultMatrix(SFMatrix3f.getScale(scale, scale, scale)));

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
