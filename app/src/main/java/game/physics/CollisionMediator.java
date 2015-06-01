package game.physics;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import shadow.math.SFVertex3f;

/**
 * It's a mediator which manages the collision system, saving all the object that must be checked
 * and implementing SAT system; works with objects which implement the interface {@link CollisionBox}.
 *
 * @author De Pace
 * @see CollisionBox
 */
public class CollisionMediator {

    public static final String LOG_TAG = "CollisionMediator";

    private List<CollisionBox> list=new LinkedList<>();

    /**
     * Adds a <code>CollisionBox</code> to the list of objects in the system of collision checking.
     *
     * @param c <code>CollisionBox</code> to be added to the list of objects.
     */
    public void addObject(CollisionBox c){
        list.add(c);
    }

    /**
     * Checks if a specific <code>CollisionBox</code> collides with another one in the system.
     *
     * @return The first <code>CollisionBox</code> in the system with which a collision has happened.
     */
    public CollisionBox collide(CollisionBox box){
        for (CollisionBox b : list) {
            if (b==box)
                continue;
            if (checkCollision(box, b))
                return b;
        }
        return null;
    }

    /**
     * Check if there is a collision between the two specified <code>CollisionBox</code>.
     * The result is independent from the order.
     *
     * @return 'true' if there is a collision.
     */
    public static boolean checkCollision(CollisionBox box1, CollisionBox box2){
        if (box1==null || box2==null)
            return false;
        SFVertex3f temp=new SFVertex3f(box2.getPos());
        float d=box1.getRadius()+box2.getRadius();
        temp.subtract(box1.getPos());
        return ((new SFVertex3f(temp.getX(),0,temp.getZ())).getSquareModulus()<=d*d
                && (checkOneBoxAxes(box1, box2) && checkOneBoxAxes(box2, box1)));
    }

    /**
     * Empties the list of <code>CollisionBox</code>.
     */
    public void clear(){
        list.clear();
    }

    private static boolean checkOneBoxAxes(CollisionBox box1, CollisionBox box2){
        ArrayList<SFVertex3f> list=box1.getAxes(box2.getPos());
        for (SFVertex3f v : list) {
            if (!checkIntervals(box1.getProjections(v), box2.getProjections(v)))
                return false;
        }
        return true;
    }

    private static boolean checkIntervals(float[] i1, float[] i2){
        return (i1[1]-i2[0])*(i2[1]-i1[0])>0;
    }

}
