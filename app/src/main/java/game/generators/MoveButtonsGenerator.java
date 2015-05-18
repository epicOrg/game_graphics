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
 * Classe che si occupa di generare i 4 Button di controllo del movimento.
 */
public class MoveButtonsGenerator {

    private Context context;
    private ShadingProgram program;
    private ButtonMaster buttonMaster;
    private PositionMoveListenerInterface positionMoveListener;

    /**
     * Crea un nuovo MoveButtonsGenerator.
     * @param context Context per recuperare le risorse.
     * @param program ShadingProgram per rappresentare i Button.
     * @param buttonMaster ButtonMaster a cui associare i Button di movimento.
     * @param positionMoveListener Interfaccia grazie al quale si associa l'azione di movimento ai Button.
     */
    public MoveButtonsGenerator(Context context, ShadingProgram program, ButtonMaster buttonMaster, PositionMoveListenerInterface positionMoveListener) {
        this.context = context;
        this.program = program;
        this.buttonMaster = buttonMaster;
        this.positionMoveListener = positionMoveListener;
    }

    /**
     * Genera i Button di movimento in una configurazione a croce, con alcuni parametri regolabili.
     * @param center Posizione centrale dei Button.
     * @param scale Fattore di scala omogeneo dei Model dei Button.
     * @param distance Distanza tra i Button di movimento.
     */
    public void generate(SFVertex3f center, float scale, float distance) {
        Node parentNode = new Node();
        SFMatrix3f scalingMatrix = SFMatrix3f.getScale(scale,scale,scale);

        parentNode.getRelativeTransform().setPosition(center);
        parentNode.getRelativeTransform().setMatrix(scalingMatrix);

//        Model model = FundamentalGenerator.getColorModel(context, program, context.getResources().getColor(R.color.primary), "Arrow.obj");
        int color=context.getResources().getColor(R.color.primary);
        Model model = FundamentalGenerator.getModelFromFileAndMaterial(context, MaterialKeeper.getInstance().getColorMaterial(context, program, color), "Arrow.obj");

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
