package game.controls;

/**
 * A button that performs an action which is encapsulated in a <code>ButtonAction</code>.
 *
 * @author De Pace
 */
public class Button {

    private String name;
    private ButtonAction action;
    private boolean continuousPressing;
    private boolean needToBeReady;

    /**
     * Creates a new <code>Button</code>.
     *
     * @param name               <code>Button</code>'s name.
     * @param action             <code>ButtonAction</code> which represents the action to perform when pressed.
     * @param continuousPressing Parameter which indicates if the <code>Button</code> operates with continuous pressure.
     * @param needToBeReady      Parameter which marks a <code>Button</code> that can be disabled when needed.
     */
    public Button(String name, ButtonAction action, boolean continuousPressing, boolean needToBeReady) {
        this.name = name;
        this.action = action;
        this.continuousPressing = continuousPressing;
        this.needToBeReady = needToBeReady;
    }

    /**
     * @return the <code>Button</code>'s name.
     */
    public String getName() {
        return name;
    }

    /**
     * Performs the action which is encapsulated in <code>ButtonAction</code>.
     *
     * @param parameter Contingent parameter for the action.
     */
    public void execute(Object parameter) {
        action.action(parameter);
    }

    /**
     * @return 'true' if the <code>Button</code> operates with continuous pressure.
     */
    public boolean isContinuousPressing() {
        return continuousPressing;
    }

    /**
     * @return 'true' if the <code>Button</code> can be disabled.
     */
    public boolean isNeedToBeReady() {
        return needToBeReady;
    }

}
