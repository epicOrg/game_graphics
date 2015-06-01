package game.net.services;

import android.os.Handler;

import org.json.JSONObject;

/**
 * A general service: it needs a <code>JSONObject</code> to start and a <code>Handler</code>.
 * It gets a <code>JSONObject</code> from the server with <code>start</code>, and translates it to a <code>Message</code> for an
 * <code>Handler</code> which is set through <code>setHandler</code>.
 */

public interface Service {

    /**
     * Starts the <code>Service</code> and interpretes the incoming <code>JSONObject</code>, sending the resulting <code>Message</code> to the previously defined
     * <code>Handler</code>; should be called always after <code>setHandler</code>.
     *
     * @param json <code>JSONObject</code> to interpret and translate to <code>Message</code>.
     */
    public void start(JSONObject json);

    /**
     * Sets the <code>Handler</code> to which send the interpreted <code>Message</code>.
     *
     * @param handler <code>Handler</code> to which send the interpreted <code>Message</code>.
     */
    public void setHandler(Handler handler);

}