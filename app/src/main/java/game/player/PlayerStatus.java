package game.player;

import game.physics.Circle;
import game.physics.CollisionBox;
import game.physics.CollisionMediator;
import shadow.math.SFMatrix3f;
import shadow.math.SFVertex3f;

/**
 * Created by Andrea on 28/03/2015.
 */
public class PlayerStatus {

    private static final int NUMBER_OF_ANGLE_DIVISIONS = 50;
    private SFVertex3f position;
    private SFVertex3f direction;
    private Circle circle;

    public PlayerStatus(SFVertex3f direction, Circle circle) {
        this.position = new SFVertex3f(circle.getPos());
        this.direction = direction;
        this.circle = circle;
    }

    public void move(SFVertex3f motion, CollisionMediator cm) {
        SFVertex3f circlePosition=circle.getPos(), originalPosition = new SFVertex3f(circlePosition);
        circlePosition.add3f(motion);

        CollisionBox box=cm.collide(circle);
        // Correct for obstacles
        if (box!=null)
            correctMotion(circlePosition, originalPosition, motion, box);
        box=cm.collide(circle);
        // Correct for junctions
        if (box!=null)
            correctMotion(circlePosition, originalPosition, motion, box);
        // Reset if blocked, else update effective position
        if (cm.collide(circle)!=null)
            circlePosition.set(originalPosition);
        else
            position.set(circlePosition);
    }

    private void correctMotion(SFVertex3f v, SFVertex3f v0, SFVertex3f tempVertex, CollisionBox box){
        SFVertex3f vertex;
        for (int i = 0; i < NUMBER_OF_ANGLE_DIVISIONS; i++) {
            for (int j = -1; j < 2; j+=2) {
                SFMatrix3f rotationMatrix = SFMatrix3f.getRotationY(i*j*(float)Math.PI/(2*NUMBER_OF_ANGLE_DIVISIONS));
                vertex=new SFVertex3f(tempVertex);
                vertex = rotationMatrix.Mult(vertex);
                v.set(v0);
                v.add(vertex);
                if (!CollisionMediator.checkCollision(circle, box)){
                    return;
                }
            }
        }
    }

    public SFVertex3f getPosition() {
        return position;
    }

    public SFVertex3f getDirection() {
        return direction;
    }

    public void setPosition(SFVertex3f position) {
        this.position = position;
    }

    public void setDirection(SFVertex3f direction) {
        this.direction = direction;
    }

}
