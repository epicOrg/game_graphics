package game.graphics;

import android.content.Context;
import game.generators.FundamentalGenerator;
import game.player.Player;
import sfogl.integration.Node;
import shadow.math.SFMatrix3f;
import shadow.math.SFTransform3f;

/**
 * Created by depa on 21/04/15.
 */
public class PlayerView {

    private Player player;
    private Node node;
    private int textureId;

    public PlayerView(Player player, Context context, int textureId){
        this.player=player;
        this.node=new Node();
        this.textureId=textureId;
        node.setModel(FundamentalGenerator.getModel(context,ShadersKeeper.getProgram(ShadersKeeper.STANDARD_TEXTURE_SHADER),textureId,"Obstacle01.obj"));
        node.getRelativeTransform().setPosition(player.getStatus().getPosition());
        node.updateTree(new SFTransform3f());
    }

    public void draw(){
        node.getRelativeTransform().setPosition(player.getStatus().getPosition());
        node.draw();
    }

}
