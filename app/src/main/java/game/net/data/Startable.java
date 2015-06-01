package game.net.data;

/**
 * Interface for a class that has some operation that should remotely be started.
 *
 * @author De Pace
 */
public interface Startable {

    /**
     * Starts an opearation.
     *
     * @param parameter Argument for the operation.
     */
    public void start(Object parameter);

}
