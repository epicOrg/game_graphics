package game.listeners;

import android.util.Log;

import game.physics.Collidable;
import game.physics.CollisionBox;
import game.physics.CollisionMediator;
import shadow.math.SFMatrix3f;
import shadow.math.SFTransform3f;
import shadow.math.SFVertex3f;

/**
 * Created by Andrea on 28/03/2015.
 */
public class PositionMoveListenerXZ implements PositionMoveListenerInterface {

    public static final String LOG_TAG = "PositionMoveListenerXZ";

    private SFVertex3f position;
    private SFVertex3f direction;
    private CollisionMediator cm;
    private CollisionBox box;

    public PositionMoveListenerXZ(SFVertex3f position, SFVertex3f direction, CollisionMediator cm, CollisionBox box) {
        this.position = position;
        this.direction = direction;
        this.cm = cm;
        this.box = box;
    }

    @Override
    public void move(float angleXZ, float angleYZ) {
        SFVertex3f v0 = new SFVertex3f(position.getX(), position.getY(), position.getZ());
        SFMatrix3f rotationMatrix = SFMatrix3f.getRotationY(angleXZ);
        SFVertex3f tmp1 = new SFVertex3f(direction.getX(), 0, direction.getZ());
        SFVertex3f tmp2 = rotationMatrix.Mult(tmp1);
        tmp2.normalize3f();
        tmp2.mult(0.5f);

        position.add3f(tmp2);
        box.setPos(position);
        Collidable c = cm.collide(box);
        if (c != null) {
            Log.d("Collision", "Coll. with: " + c);
            float s = tmp2.getLength();
            position.set3f(v0.getX(), v0.getY(), v0.getZ());
            correctMotion(c, s, 20);
            box.setPos(position);
            if (cm.collide(box) != null) {
                Log.d("Collision", "Other coll. with: " + cm.collide(box));
                position.set3f(v0.getX(), v0.getY(), v0.getZ());
            }
        }
    }

    private void correctMotion(Collidable c, float s, int n) {
        if (n < 2)
            n = 2;
        SFVertex3f motion = new SFVertex3f(direction.getX(), 0, direction.getZ());
        motion.normalize3f();
        motion.mult3f(s);
        SFVertex3f vbackup=new SFVertex3f(position);
        SFVertex3f position0 = new SFVertex3f(position.getX(), position.getY(), position.getZ());
        SFTransform3f rot = new SFTransform3f();
        mainloop:
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < 2; j++) {
                rot.setMatrix(SFMatrix3f.getRotationY((float) ((1 - 2 * j) * Math.PI * 0.5 * i / n)));
                SFVertex3f kv = new SFVertex3f(motion.getX(), motion.getY(), motion.getZ());
                rot.transform(kv);

                SFVertex3f motion2=new SFVertex3f(motion);
                kv.mult3f(motion2.dot3f(kv)/(kv.getSquareModulus()));
                position.add(kv);

                box.setPos(position);
                if (!box.checkCollision(c.getBox())) {
                    break mainloop;
                } else {
                    position.set(vbackup);
                }
            }
        }
    }
}
