package game.controls;

/**
 * Created by depa on 21/04/15.
 */
public class Button {

    private String name;
    private ButtonAction action;
    private boolean continuousPressing;
    private boolean needToBeReady;

    public Button(String name, ButtonAction action, boolean continuousPressing, boolean needToBeReady) {
        this.name = name;
        this.action = action;
        this.continuousPressing = continuousPressing;
        this.needToBeReady = needToBeReady;
    }

    public String getName() {
        return name;
    }

    public void execute(Object parameter) {
        action.action(parameter);
    }

    public boolean isContinuousPressing() {
        return continuousPressing;
    }

    public boolean isNeedToBeReady() {
        return needToBeReady;
    }

}
