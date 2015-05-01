package game.generators;

import game.controls.ButtonAction;
import game.controls.Button;
import game.controls.ButtonMaster;
import game.listeners.PositionMoveListenerInterface;
import sfogl.integration.Model;
import shadow.math.SFVertex3f;

/**
 * Created by depa on 21/04/15.
 */
public class MoveButtonsGenerator {

    public static void generate(ButtonMaster buttonMaster, Model model, final PositionMoveListenerInterface positionMoveListener) {
        buttonMaster.setModel(model);

        buttonMaster.addButton(new Button(new ButtonAction() {
                    @Override
                    public void action(Object parameter) {
                        positionMoveListener.move((float) +Math.PI / 2, 0, (long) parameter);
                    }
                }),
                new SFVertex3f(-2, 0, 0), (float) -Math.PI / 2);

        buttonMaster.addButton(new Button(new ButtonAction() {
                    @Override
                    public void action(Object parameter) {
                        positionMoveListener.move((float) -Math.PI / 2, 0, (long) parameter);
                    }
                }),
                new SFVertex3f(2, 0, 0), (float) Math.PI / 2);

        buttonMaster.addButton(new Button(new ButtonAction() {
                    @Override
                    public void action(Object parameter) {
                        positionMoveListener.move(0, 0, (long) parameter);
                    }
                }),
                new SFVertex3f(0, 2, 0), (float) 0);

        buttonMaster.addButton(new Button(new ButtonAction() {
                    @Override
                    public void action(Object parameter) {
                        positionMoveListener.move((float) -Math.PI, 0, (long) parameter);
                    }
                }),
                new SFVertex3f(0, -2, 0), (float) -Math.PI);
    }

}
