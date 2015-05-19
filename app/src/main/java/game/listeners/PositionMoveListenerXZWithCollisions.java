package game.listeners;

import android.util.Log;

import game.physics.CollisionMediator;
import game.player.PlayerStatus;
import shadow.math.SFMatrix3f;
import shadow.math.SFVertex3f;

/**
 * Created by Andrea on 28/03/2015.
 */
public class PositionMoveListenerXZWithCollisions implements PositionMoveListenerInterface {

    public static final String LOG_TAG = "PositionMoveListenerXZ";
    private static float MOVE_SPEED = 3.00f; // Distance/s

    private PlayerStatus playerStatus;
    private CollisionMediator cm;

    public PositionMoveListenerXZWithCollisions(PlayerStatus playerStatus, CollisionMediator cm) {
        this.playerStatus = playerStatus;
        this.cm = cm;
    }

    @Override
    public void move(float angleXZ, float angleYZ, long delta) {

        SFMatrix3f rotationMatrix = SFMatrix3f.getRotationY(angleXZ);
        SFVertex3f tempVertex = rotationMatrix.Mult(new SFVertex3f(playerStatus.getDirection().getX(), 0, playerStatus.getDirection().getZ()));
        tempVertex.normalize3f();
        tempVertex.mult(MOVE_SPEED * delta / 1000);

        playerStatus.move(tempVertex, cm);
    }

}
