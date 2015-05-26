package game.controls;

/**
 * Interface for objects which encapsulates an action.
 * @author De Pace
 */
public interface ButtonAction {

    /**
     * An action which can be invoked.
     * @param parameter Generic parameter for an action.
     */
    void action(Object parameter);

}
