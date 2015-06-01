package game.net.handling;

/**
 * Listener for Login Handler.
 *
 * @author Torlaschi
 * @date 22/04/2015
 */
public interface LoginHandlerListener {

    /**
     * Called on finishing login operation.
     *
     * @param result indicates wether the login operation were successful.
     */
    public void onLoginComplete(boolean result);

}
