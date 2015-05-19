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
 * Classe che si occupa di costruire il Button di pausa.
 * @author Andrea
 */
public class SettingsButtonsGenerator {

    public static final String LOG_TAG = "SettingsButtonsGen";

    private Context context;
    private ShadingProgram program;
    private ButtonMaster buttonMaster;
    private SettingsScreen settingsScreen;

    /**
     * Crea un nuovo SettingsButtonsGenerator.
     * @param context Context per recuperare le risorse.
     * @param program ShadingProgram per rappresentare i Button.
     * @param buttonMaster ButtonMaster a cui associare i Button di movimento.
     * @param settingsScreen SettingScreen da mostrare quando si preme il pulsante.
     */
    public SettingsButtonsGenerator(Context context, ShadingProgram program, ButtonMaster buttonMaster, SettingsScreen settingsScreen) {
        this.context = context;
        this.program = program;
        this.buttonMaster = buttonMaster;
        this.settingsScreen = settingsScreen;
    }

    /**
     * Genera i Button di pausa, con alcuni parametri regolabili.
     * @param position Posizione del Button (la componente z non ha importanza).
     * @param scale Fattore di scala omogeneo dei Model del Button.
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
