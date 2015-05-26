package game.controls;

/**
 * A button that performs an action which is encapsulated in a ButtonAction.
 * @author De Pace
 */
public class Button {

    private String name;
    private ButtonAction action;
    private boolean continuousPressing;
    private boolean needToBeReady;

    /**
     * It creates a new Button.
     * @param name Button's name.
     * @param action ButtonAction which represents the action to perform when pressed
     * @param continuousPressing Parameter which indicates if the button operates with continuous pressure
     * @param needToBeReady Parameter which marks a Button that can be disabled when needed
     */
    public Button(String name, ButtonAction action, boolean continuousPressing, boolean needToBeReady) {
        this.name = name;
        this.action = action;
        this.continuousPressing = continuousPressing;
        this.needToBeReady = needToBeReady;
    }

    /**
     * @return nome del Button dato alla creazione.
     */
    public String getName() {
        return name;
    }

    /**
     * It performs the action which is encapsulated in ButtonAction.
     * @param parameter Contingent parameter for the action.
     */
    public void execute(Object parameter) {
        action.action(parameter);
    }

    /**
     * @return It returns 'true' if the Button se il Button operates with continuous pressure.
     */
    public boolean isContinuousPressing() {
        return continuousPressing;
    }

    /**
     * @return It returns 'true' if the Button can be disabled.
     */
    public boolean isNeedToBeReady() {
        return needToBeReady;
    }

}
