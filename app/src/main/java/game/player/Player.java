package game.player;

import game.physics.Circle;
import shadow.math.SFVertex3f;

/**
 * This class represent a User: its features are name and status.
 *
 * @author Torlaschi
 * @date 28/03/2015
 */
public class Player {

    private volatile String name;
    private volatile PlayerStatus status;

    public Player(PlayerStatus status, String name) {
        this.status = status;
        this.name = name;
    }

    public Player(String name) {
        SFVertex3f position = new SFVertex3f(5, 0.5f, -7);
        SFVertex3f direction = new SFVertex3f(-1, -0.25f, 0);
        Circle circle = new Circle(position, 0.75);
        this.status = new PlayerStatus(direction, circle);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public PlayerStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return name;
    }

}
