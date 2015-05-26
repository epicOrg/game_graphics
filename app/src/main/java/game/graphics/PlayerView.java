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
 * @author De Pace
 * @date 21/04/15
 */
public class PlayerView {

    public static final String LOG_TAG = "GameActivity";

    private Player player;
    private Node node;

    public PlayerView(Player player, Context context, int textureId) {
        this.player = player;
        this.node = new Node();
        node.setModel(FundamentalGenerator.getModel(context, ShadersKeeper.getProgram(ShadersKeeper.STANDARD_TEXTURE_SHADER), textureId, "Rabbit.obj"));
    }

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
