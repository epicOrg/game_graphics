package game.graphics;

import android.content.Context;
import android.util.Log;

import game.generators.FundamentalGenerator;
import game.player.Player;
import sfogl.integration.Node;
import shadow.math.SFMatrix3f;
import shadow.math.SFTransform3f;

/**
 * Created by depa on 21/04/15.
 */
public class PlayerView {

    public static final String LOG_TAG = "GameActivity";

    private Player player;
    private Node node;

    public PlayerView(Player player, Context context, int textureId) {
        this.player = player;
        this.node = new Node();
        node.setModel(FundamentalGenerator.getModel(context, ShadersKeeper.getProgram(ShadersKeeper.STANDARD_TEXTURE_SHADER), textureId, "Rabbit.obj"));
        node.getRelativeTransform().setPosition(player.getStatus().getPosition());
    }

    public void draw() {
        node.getRelativeTransform().setPosition(player.getStatus().getPosition());
        node.updateTree(new SFTransform3f());
        node.draw();
    }

}
