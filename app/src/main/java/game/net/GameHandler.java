package game.net;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import game.GameManager;
import game.Room;
import game.codes.TextureCodes;
import game.graphics.Map;
import game.graphics.Obstacle;
import game.graphics.Wall;
import game.physics.Circle;
import game.physics.Square;
import game.player.Player;
import game.views.MessageScreen;
import login.interaction.FieldsNames;
import login.services.Game;
import shadow.math.SFVertex3f;

/**
 * Created by Andrea on 22/04/2015.
 */
public class GameHandler extends Handler {

    public static final String LOG_TAG = "GameHandler";

    private ArrayList<GameHandlerListener> gameHandlerListeners = new ArrayList<>();
    private GameManager gameManager = GameManager.getInstance();

    private Map map;

    private int groundWidth = 0;
    private int groundHeight = 0;

    private MessageScreen messageScreen;

    public GameHandler(MessageScreen messageScreen) {
        this.messageScreen = messageScreen;
    }

    public void addGameHandlerListeners(GameHandlerListener l) {
        gameHandlerListeners.add(l);
    }

    public void removeGameHandlerListeners(GameHandlerListener l) {
        gameHandlerListeners.remove(l);
    }

    @Override
    public void handleMessage(Message msg) {
        Log.d(LOG_TAG, "handleMessage");
        switch (msg.what) {
            case Game.STATUS:
                processStatusMessage(msg);
                break;
            case Game.MAP:
                processMapMessage(msg);
                break;
            case Game.POSITIONS:
                processPositionsMessage(msg);
                break;
        }
    }

    private void callOnGameGo() {
        for (GameHandlerListener l : gameHandlerListeners)
            l.onGameGo();
    }

    private void callOnMapReceived() {
        for (GameHandlerListener l : gameHandlerListeners)
            l.onMapReceived();
    }

    private void callOnGameFinish() {
        for (GameHandlerListener l : gameHandlerListeners)
            l.onGameFinish();
    }

    private void processStatusMessage(Message msg) {
        Log.d(LOG_TAG, "processStatusMessage");
        Game.GameStatusResult results = (Game.GameStatusResult) msg.obj;

        if (results.isGo())
            callOnGameGo();
        if (results.getGameEnd() != null) {
            String gameEnd = results.getGameEnd();
            switch (gameEnd) {
                case FieldsNames.GAME_WIN:
                    messageScreen.setText("YOU WIN!", Color.GREEN);
                    break;
                case FieldsNames.GAME_DRAW:
                    messageScreen.setText("YOU DIDN'T WIN and YOU DIDN'T LOSE!", Color.BLUE);
                    break;
                case FieldsNames.GAME_LOSE:
                    messageScreen.setText("YOU LOSE!", Color.RED);
                    break;
            }
            messageScreen.show();

            try {
                Thread.sleep(5*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            callOnGameFinish();
        }
    }

    private void processMapMessage(Message msg) {
        Log.d(LOG_TAG, "processMapMessage");
        Game.GameMapResult results = (Game.GameMapResult) msg.obj;
        map = new Map();

        for (Game.GameMapObject o : results.getGameMapObjects()) {
            if (o.object.equals("Wall")) {
                float posX = Float.parseFloat(o.position.split(" ")[0]);
                float posY = Float.parseFloat(o.position.split(" ")[1]);
                float posZ = Float.parseFloat(o.position.split(" ")[2]);
                float sizeX = Float.parseFloat(o.size.split(" ")[0]);
                float sizeY = Float.parseFloat(o.size.split(" ")[1]);
                float sizeZ = Float.parseFloat(o.size.split(" ")[2]);
                int texture = TextureCodes.getTextureIdFromString(o.texture);
                map.addObjects(new Wall(new Square(new SFVertex3f(posX, posY, posZ), sizeX, sizeY, sizeZ), texture));
            } else if (o.object.equals("Obstacle")) {
                float posX = Float.parseFloat(o.position.split(" ")[0]);
                float posY = Float.parseFloat(o.position.split(" ")[1]);
                float posZ = Float.parseFloat(o.position.split(" ")[2]);
                float sizeX = Float.parseFloat(o.size.split(" ")[0]);
                float sizeY = Float.parseFloat(o.size.split(" ")[1]);
                int texture = TextureCodes.getTextureIdFromString(o.texture);
                map.addObjects(new Obstacle(new Circle(new SFVertex3f(posX, posY, posZ), sizeX), sizeY, texture));
            }
        }

        groundWidth = results.getWidth();
        groundHeight = results.getHeight();

        callOnMapReceived();
    }

    private void processPositionsMessage(Message msg) {
        Log.d(LOG_TAG, "processPositionsMessage");
        Game.GamePositionsResult results = (Game.GamePositionsResult) msg.obj;

        Room room = gameManager.getRoom();
        HashMap<String, Game.GamePositionsObject> gamePositionsObjectHashMap = results.getGamePositionsObjectHashMap();

        for (String s : gamePositionsObjectHashMap.keySet()) {
            Player p = room.getPlayerByUsername(s);
            p.getStatus().getPosition().set(gamePositionsObjectHashMap.get(s).pos);
//            p.getStatus().setPosition(gamePositionsObjectHashMap.get(s).pos);
            p.getStatus().setDirection(gamePositionsObjectHashMap.get(s).dir);
        }
    }

    public Map getMap() {
        return map;
    }

    public int getGroundWidth() {
        return groundWidth;
    }

    public int getGroundHeight() {
        return groundHeight;
    }

}