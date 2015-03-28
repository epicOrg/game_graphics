package game.listeners;

import shadow.math.SFMatrix3f;
import shadow.math.SFVertex3f;

/**
 * Created by Andrea on 28/03/2015.
 */
public class DirectionDirectionMoveListenerX implements DirectionMoveListenerInterface {

    private static final float MOVE_FACTOR = 1 / 1080f;

    private SFVertex3f direction;

    public DirectionDirectionMoveListenerX(SFVertex3f direction) {
        this.direction = direction;
    }

    @Override
    public void move(float dx, float dy) {
        //Ignore dy for now;

        SFMatrix3f rotationMatrix = SFMatrix3f.getRotationY((float) (dx * MOVE_FACTOR * Math.PI));
        SFVertex3f tmp = rotationMatrix.Mult(direction);
        direction.set3f(tmp.getX(), tmp.getY(), tmp.getZ());
    }
}
