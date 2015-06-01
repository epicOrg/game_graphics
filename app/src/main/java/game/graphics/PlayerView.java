package game.graphics;

import android.content.Context;
import android.util.Log;

import game.generators.FundamentalGenerator;
import game.player.Player;
import sfogl.integration.Node;
import shadow.math.SFMatrix3f;
import shadow.math.SFTransform3f;
import shadow.math.SFVertex3f;

/**
 * Represents a graphical view of a <code>Player</code>.
 *
 * @author De Pace
 * @date 21/04/15
 */
public class PlayerView {

    public static final String LOG_TAG = "GameActivity";

    private Player player;
    private Node node;

    /**
     * Creates a new <code>PlayerView</code> for a given <code>Player</code>.
     *
     * @param player    <code>Player</code> to be represented.
     * @param context   <code>Context</code> to retrieve resources.
     * @param textureId id of the texture used for drawing the <code>PlayerView</code>.
     */
    public PlayerView(Player player, Context context, int textureId) {
        this.player = player;
        this.node = new Node();
        node.setModel(FundamentalGenerator.getModel(context, ShadersKeeper.getProgram(ShadersKeeper.STANDARD_TEXTURE_SHADER), textureId, "Rabbit.obj"));
    }

    /**
     * Draws the <code>PlayerView</code>.
     */
    public void draw() {
        node.getRelativeTransform().setPosition(player.getStatus().getPosition());

        SFVertex3f zAxis = new SFVertex3f(0, 0, 1);
        SFVertex3f normalizedDir = new SFVertex3f(player.getStatus().getDirection().getX(), 0, player.getStatus().getDirection().getZ());
        normalizedDir.normalize3f();

        float angle = (float) Math.acos(zAxis.dot3f(normalizedDir));
        node.getRelativeTransform().setMatrix(SFMatrix3f.getRotationY(normalizedDir.getX() < 0 ? -angle : angle));

        node.updateTree(new SFTransform3f());
        node.draw();
    }

}
