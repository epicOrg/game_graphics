package game.player;

import game.physics.Circle;
import shadow.math.SFVertex3f;

/**
 * This class represent a User: its features are name and status.
 *
 * @author Torlaschi
 * @date 28/03/2015
 * @see PlayerStatus
 */
public class Player {

    public static final SFVertex3f DEFAULT_POSITION = new SFVertex3f(5, 0.5f, -7), DEFAULT_DIRECTION = new SFVertex3f(-1, -0.25f, 0);
    public static final float DEFAULT_RADIUS = 0.75f;
    private volatile String name;
    private volatile PlayerStatus status;

    /**
     * Creates a new <code>Player</code> with given name and status.
     *
     * @param status status of the <code>Player</code>.
     * @param name   username of the <code>Player</code>.
     */
    public Player(PlayerStatus status, String name) {
        this.status = status;
        this.name = name;
    }

    /**
     * Creates a new <code>Player</code> with default status and given name.
     *
     * @param name username of the <code>Player</code>.
     */
    public Player(String name) {
        SFVertex3f position = new SFVertex3f(DEFAULT_POSITION);
        SFVertex3f direction = new SFVertex3f(DEFAULT_DIRECTION);
        Circle circle = new Circle(position, DEFAULT_RADIUS);
        this.status = new PlayerStatus(direction, circle);
        this.name = name;
    }

    /**
     *
     * @return <code>Player</code> username.
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return <code>Player</code> status.
     */
    public PlayerStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return name;
    }

}
