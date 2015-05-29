package game.listeners;

import shadow.math.SFMatrix3f;
import shadow.math.SFVertex3f;

public class PositionMoveListenerXZ implements PositionMoveListenerInterface {

    public static final String LOG_TAG = "PositionMoveListenerXZ";

    private SFVertex3f position;
    private SFVertex3f direction;

    public PositionMoveListenerXZ(SFVertex3f position, SFVertex3f direction) {
        this.position = position;
        this.direction = direction;
    }

    @Override
    public void move(float angleXZ, float angleYZ, long delta) {
        SFMatrix3f rotationMatrix = SFMatrix3f.getRotationY(angleXZ);
        SFVertex3f tmp1 = new SFVertex3f(direction.getX(), 0, direction.getZ());
        SFVertex3f tmp2 = rotationMatrix.Mult(tmp1);
        tmp2.normalize3f();
        tmp2.mult(0.5f);

        position.add3f(tmp2);
    }

}
