package game.net;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import game.GameManager;
import game.Room;
import game.graphics.Map;
import game.graphics.MapObjects;
import game.player.Player;
import game.views.MessageScreen;
import login.interaction.FieldsNames;
import login.services.Game;

/**
 * Created by Andrea on 22/04/2015.
 */
public class GameHandler extends Handler {

    public static final String LOG_TAG = "GameHandler";

    private ArrayList<GameHandlerListener> gameHandlerListeners = new ArrayList<>();
    private GameManager gameManager = GameManager.MANAGER;

    private Map map;

    private int groundWidth = 0;
    private int groundHeight = 0;

    private Player player;
    private GamePositionSender gamePositionSender;
    private MessageScreen messageScreen;

    public GameHandler(Player player, GamePositionSender gamePositionSender, MessageScreen messageScreen) {
        this.player = player;
        this.gamePositionSender = gamePositionSender;
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
        //Log.d(LOG_TAG, "handleMessage");
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
        //Log.d(LOG_TAG, "processStatusMessage");
        Game.GameStatusResult results = (Game.GameStatusResult) msg.obj;

        if (results.isGo())
            Log.d(LOG_TAG, "results.isGo");
        callOnGameGo();
        if (results.getGameEnd() != null) {
            Log.d(LOG_TAG, "results.getGameEnd");
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
                case FieldsNames.GAME_INTERRUPTED:
                    messageScreen.setText("INTERRUPTED!", Color.BLACK);
                    break;
            }

            gamePositionSender.setSending(false);
            messageScreen.show();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    callOnGameFinish();
                }
            }).start();
        }
    }

    private void processMapMessage(Message msg) {
        //Log.d(LOG_TAG, "processMapMessage");
        Game.GameMapResult results = (Game.GameMapResult) msg.obj;
        map = new Map();

        for (Game.GameMapObject o : results.getGameMapObjects()) {
            map.addObjects(MapObjects.MAP.getObjectFromNameAndData(o.object, o.position, o.size, o.texture));
        }

        groundWidth = results.getWidth();
        groundHeight = results.getHeight();

        // In attesa che Fabio termini il generatore
        player.getStatus().getPosition().set3f(results.playerPositionX, results.playerPositionY, results.playerPositionZ);

        callOnMapReceived();
    }

    private void processPositionsMessage(Message msg) {
        //Log.d(LOG_TAG, "processPositionsMessage");
        Game.GamePositionsResult results = (Game.GamePositionsResult) msg.obj;

        Room room = gameManager.getRoom();
        HashMap<String, Game.GamePositionsObject> gamePositionsObjectHashMap = results.getGamePositionsObjectHashMap();

        for (String s : gamePositionsObjectHashMap.keySet()) {
            Player p = room.getPlayerByUsername(s);
            p.getStatus().getPosition().set(gamePositionsObjectHashMap.get(s).pos);
            p.getStatus().getDirection().set(gamePositionsObjectHashMap.get(s).dir);
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


//public static final String LOG_TAG = "GameHandler";
//
//    private ArrayList<GameHandlerListener> gameHandlerListeners = new ArrayList<>();
//    private GameManager gameManager = GameManager.MANAGER;
//
//    private Map map;
//
//    private int groundWidth = 0;
//    private int groundHeight = 0;
//
//    private Player player;
//    private GamePositionSender gamePositionSender;
//    private MessageScreen messageScreen;
//
//    public GameHandler(Player player, GamePositionSender gamePositionSender, MessageScreen messageScreen) {
//        this.player = player;
//        this.gamePositionSender = gamePositionSender;
//        this.messageScreen = messageScreen;
//    }
//
//    public void addGameHandlerListeners(GameHandlerListener l) {
//        gameHandlerListeners.add(l);
//    }
//
//    public void removeGameHandlerListeners(GameHandlerListener l) {
//        gameHandlerListeners.remove(l);
//    }
//
//    @Override
//    public void handleMessage(Message msg) {
//       if (msg.what== Game.MESSAGE_TAG){
//           JSONObject json=(JSONObject)msg.obj;
//           try{
//               String service=json.getString(FieldsNames.SERVICE_TYPE);
//               switch (service) {
//                   case FieldsNames.GAME_STATUS:
//                       processStatusMessage(json);
//                       break;
//                   case FieldsNames.GAME_MAP:
//                       processMapMessage(json);
//                       break;
//                   case FieldsNames.GAME_POSITIONS:
//                       processPositionsMessage(json);
//                       break;
//               }
//           }catch (JSONException e){
//               e.printStackTrace();
//           }
//       }
//    }
//
//    private void callOnGameGo() {
//        for (GameHandlerListener l : gameHandlerListeners)
//            l.onGameGo();
//    }
//
//    private void callOnMapReceived() {
//        for (GameHandlerListener l : gameHandlerListeners)
//            l.onMapReceived();
//    }
//
//    private void callOnGameFinish() {
//        for (GameHandlerListener l : gameHandlerListeners)
//            l.onGameFinish();
//    }
//
//    private void processStatusMessage(JSONObject json) {
//        Log.d(LOG_TAG, "processStatusMessage");
//
//        try{
//            if (json.has(FieldsNames.GAME_GO) && json.getBoolean(FieldsNames.GAME_GO))
//                Log.d(LOG_TAG, "results.isGo");
//            callOnGameGo();
//            if (json.has(FieldsNames.GAME_END) && (json.getString(FieldsNames.GAME_END) != null)) {
//                Log.d(LOG_TAG, "results.getGameEnd");
//                String gameEnd = json.getString(FieldsNames.GAME_END);
//                switch (gameEnd) {
//                    case FieldsNames.GAME_WIN:
//                        messageScreen.setText("YOU WIN!", Color.GREEN);
//                        break;
//                    case FieldsNames.GAME_DRAW:
//                        messageScreen.setText("YOU DIDN'T WIN and YOU DIDN'T LOSE!", Color.BLUE);
//                        break;
//                    case FieldsNames.GAME_LOSE:
//                        messageScreen.setText("YOU LOSE!", Color.RED);
//                        break;
//                    case FieldsNames.GAME_INTERRUPTED:
//                        messageScreen.setText("INTERRUPTED!", Color.BLACK);
//                        break;
//                }
//
//                gamePositionSender.setSending(false);
//                messageScreen.show();
//
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            Thread.sleep(5000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        callOnGameFinish();
//                    }
//                }).start();
//            }
//        }catch (JSONException e){
//            e.printStackTrace();
//        }
//    }
//
//    private void processMapMessage(JSONObject json) {
//
//        map = new Map();
//
//        try {
//            JSONArray jItems = json.getJSONArray(FieldsNames.GAME_ITEMS);
//            for (int i = 0; i < jItems.length(); i++) {
//                JSONObject jObject = jItems.getJSONObject(i);
//                String object = jObject.getString(FieldsNames.GAME_OBJECT);
//                String texture = jObject.getString(FieldsNames.GAME_TEXTURE);
//                String position = jObject.getString(FieldsNames.GAME_POSITION);
//                String size = jObject.getString(FieldsNames.GAME_SIZE);
//                map.addObjects(MapObjects.MAP.getObjectFromNameAndData(object, position, size, texture));
//            }
//
//            groundWidth = json.getInt(FieldsNames.GAME_WIDTH);
//            groundHeight = json.getInt(FieldsNames.GAME_HEIGHT);
//
//            String jPlayerPosition = json.getString(FieldsNames.GAME_PLAYER_POSITION);
//            float playerPositionX = Float.parseFloat(jPlayerPosition.split(" ")[0]);
//            float playerPositionY = Float.parseFloat(jPlayerPosition.split(" ")[1]);
//            float playerPositionZ = Float.parseFloat(jPlayerPosition.split(" ")[2]);
//
//            player.getStatus().getPosition().set3f(playerPositionX, playerPositionY, playerPositionZ);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        callOnMapReceived();
//    }
//
//    private void processPositionsMessage(JSONObject json) {
//        Log.d(LOG_TAG, "processPositionsMessage");
//
//        Room room = gameManager.getRoom();
//
//        try {
//            JSONArray jPlayers = json.getJSONArray(FieldsNames.GAME_PLAYERS);
//
//            for (int i = 0; i < jPlayers.length(); i++) {
//                JSONObject jPlayer = jPlayers.getJSONObject(i);
//                String username = jPlayer.getString(FieldsNames.USERNAME);
//                JSONObject pos = jPlayer.getJSONObject(FieldsNames.GAME_POSITION);
//                JSONObject dir = jPlayer.getJSONObject(FieldsNames.GAME_DIRECTION);
//
//                float xPos = Float.parseFloat(pos.getString(FieldsNames.GAME_X));
//                float yPos = Float.parseFloat(pos.getString(FieldsNames.GAME_Y)) - 1.5f;
//                float zPos = Float.parseFloat(pos.getString(FieldsNames.GAME_Z));
//                float xDir = Float.parseFloat(dir.getString(FieldsNames.GAME_X));
//                float yDir = Float.parseFloat(dir.getString(FieldsNames.GAME_Y)) - 1.5f;
//                float zDir = Float.parseFloat(dir.getString(FieldsNames.GAME_Z));
//
//                Player p = room.getPlayerByUsername(username);
//                p.getStatus().getPosition().set(new SFVertex3f(xPos,yPos,zPos));
//                p.getStatus().getDirection().set(new SFVertex3f(xDir,yDir,zDir));
//
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    public Map getMap() {
//        return map;
//    }
//
//    public int getGroundWidth() {
//        return groundWidth;
//    }
//
//    public int getGroundHeight() {
//        return groundHeight;
//    }
//
//}



}