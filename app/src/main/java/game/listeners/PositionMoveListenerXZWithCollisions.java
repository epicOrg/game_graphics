package game.listeners;

import android.util.Log;

import game.physics.CollisionMediator;
import game.player.PlayerStatus;
import shadow.math.SFMatrix3f;
import shadow.math.SFVertex3f;

/**
 * Performs action of movement taking collisions into account.
 *
 * @author Torlaschi
 * @see PlayerStatus
 * @see CollisionMediator
 */
public class PositionMoveListenerXZWithCollisions implements PositionMoveListenerInterface {

    public static final String LOG_TAG = "PositionMoveListenerXZ";
    private float moveSpeed;

    private PlayerStatus playerStatus;
    private CollisionMediator cm;

    /**
     * Creates a new <code>PositionMoveListenerXZWithCollisionss</code>.
     *
     * @param playerStatus <code>PlayerStatus</code> to move.
     * @param cm           <code>CollisionMediator</code> to perform collision checking.
     * @param moveSpeed    speed of motion.
     */
    public PositionMoveListenerXZWithCollisions(PlayerStatus playerStatus, CollisionMediator cm, float moveSpeed) {
        this.playerStatus = playerStatus;
        this.cm = cm;
        this.moveSpeed = moveSpeed;
    }

    @Override
    public void move(float angleXZ, float angleYZ, long delta) {

        SFMatrix3f rotationMatrix = SFMatrix3f.getRotationY(angleXZ);
        SFVertex3f tempVertex = rotationMatrix.Mult(new SFVertex3f(playerStatus.getDirection().getX(), 0, playerStatus.getDirection().getZ()));
        tempVertex.normalize3f();
        tempVertex.mult(moveSpeed * delta / 1000);

        playerStatus.move(tempVertex, cm);
    }

}
