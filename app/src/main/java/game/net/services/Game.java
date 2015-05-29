package game.net.services;

import android.os.Handler;
import android.os.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import game.net.fieldsnames.CommonFields;
import game.net.fieldsnames.GameFields;
import game.net.fieldsnames.ServicesFields;
import game.net.interaction.FieldsNames;
import shadow.math.SFVertex3f;

public class Game implements Service {

    public static final String LOG_TAG = "Game";

    public static final int STATUS = 0;
    public static final int MAP = 1;
    public static final int POSITIONS = 2;

    private JSONObject json;
    private Handler handler;

    @Override
    public void start(JSONObject json) {
        this.json = json;
        readFields();
    }

    private void readFields() {
        try {
            Message message = null;
            switch (GameFields.valueOf(json.getString(ServicesFields.SERVICE_TYPE.toString()))) {
                case GAME_STATUS:
                    message = getGameStatusMessage();
                    break;
                case GAME_MAP:
                    message = getGameMapMessage();
                    break;
                case GAME_POSITIONS:
                    message = getGamePositionsMessage();
                    break;
            }
            message.sendToTarget();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private Message getGameStatusMessage() {
        GameStatusResult gameStatusResult;
        boolean go = false;
        String gameEnd = null;
        try {
            if (json.has(GameFields.GAME_GO.toString()))
                go = json.getBoolean(GameFields.GAME_GO.toString());
            if (json.has(GameFields.GAME_END.toString()))
                gameEnd = json.getString(GameFields.GAME_END.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        gameStatusResult = new GameStatusResult(go, gameEnd);
        return handler.obtainMessage(STATUS, gameStatusResult);
    }

    private Message getGameMapMessage() {
        GameMapResult gameMapResult;
        int width = 0;
        int height = 0;

        ArrayList<GameMapObject> gameMapObjects = new ArrayList<>();
        try {
            JSONArray jItems = json.getJSONArray(GameFields.GAME_ITEMS.toString());
            for (int i = 0; i < jItems.length(); i++) {
                JSONObject jObject = jItems.getJSONObject(i);
                String object = jObject.getString(GameFields.GAME_OBJECT.toString());
                String texture = jObject.getString(GameFields.GAME_TEXTURE.toString());
                String position = jObject.getString(GameFields.GAME_POSITION.toString());
                String size = jObject.getString(GameFields.GAME_SIZE.toString());
                gameMapObjects.add(new GameMapObject(object, texture, position, size));
            }

            width = json.getInt(GameFields.GAME_WIDTH.toString());
            height = json.getInt(GameFields.GAME_HEIGHT.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        gameMapResult = new GameMapResult(width, height, gameMapObjects);

        try {
            String jPlayerPosition = json.getString(GameFields.GAME_PLAYER_POSITION.toString());
            gameMapResult.playerPositionX = Float.parseFloat(jPlayerPosition.split(" ")[0]);
            gameMapResult.playerPositionY = Float.parseFloat(jPlayerPosition.split(" ")[1]);
            gameMapResult.playerPositionZ = Float.parseFloat(jPlayerPosition.split(" ")[2]);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return handler.obtainMessage(MAP, gameMapResult);
    }

    private Message getGamePositionsMessage() {
        GamePositionsResult gamePositionsResult = null;
        HashMap<String, GamePositionsObject> gamePositionsObjectHashMap = new HashMap<>();

        try {
            JSONArray jPlayers = json.getJSONArray(GameFields.GAME_PLAYERS.toString());

            for (int i = 0; i < jPlayers.length(); i++) {
                JSONObject jPlayer = jPlayers.getJSONObject(i);
                String username = jPlayer.getString(CommonFields.USERNAME.toString());
                JSONObject pos = jPlayer.getJSONObject(GameFields.GAME_POSITION.toString());
                JSONObject dir = jPlayer.getJSONObject(GameFields.GAME_DIRECTION.toString());

                float xPos = Float.parseFloat(pos.getString(GameFields.GAME_X.toString()));
                float yPos = Float.parseFloat(pos.getString(GameFields.GAME_Y.toString())) - 1.5f;
                float zPos = Float.parseFloat(pos.getString(GameFields.GAME_Z.toString()));
                float xDir = Float.parseFloat(dir.getString(GameFields.GAME_X.toString()));
                float yDir = Float.parseFloat(dir.getString(GameFields.GAME_Y.toString())) - 1.5f;
                float zDir = Float.parseFloat(dir.getString(GameFields.GAME_Z.toString()));

                gamePositionsObjectHashMap.put(username, new GamePositionsObject(new SFVertex3f(xPos, yPos, zPos), new SFVertex3f(xDir, yDir, zDir)));
            }

            gamePositionsResult = new GamePositionsResult(gamePositionsObjectHashMap);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return handler.obtainMessage(POSITIONS, gamePositionsResult);
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public class GameStatusResult {

        public boolean go;
        public String gameEnd;

        public GameStatusResult(boolean go, String gameEnd) {
            this.go = go;
            this.gameEnd = gameEnd;
        }

    }

    public class GameMapResult {

        public int width, height;
        public ArrayList<GameMapObject> gameMapObjects;

        public float playerPositionX, playerPositionY, playerPositionZ;

        public GameMapResult(int width, int height, ArrayList<GameMapObject> gameMapObjects) {
            this.width = width;
            this.height = height;
            this.gameMapObjects = gameMapObjects;
        }

    }

    public class GameMapObject {
        public String object, texture, position, size;

        public GameMapObject(String object, String texture, String position, String size) {
            this.object = object;
            this.texture = texture;
            this.position = position;
            this.size = size;
        }
    }

    public class GamePositionsResult {

        private HashMap<String, GamePositionsObject> gamePositionsObjectHashMap;

        public GamePositionsResult(HashMap<String, GamePositionsObject> gamePositionsObjectHashMap) {
            this.gamePositionsObjectHashMap = gamePositionsObjectHashMap;
        }

        public HashMap<String, GamePositionsObject> getGamePositionsObjectHashMap() {
            return gamePositionsObjectHashMap;
        }

    }

    public class GamePositionsObject {

        public SFVertex3f pos, dir;

        public GamePositionsObject(SFVertex3f pos, SFVertex3f dir) {
            this.pos = pos;
            this.dir = dir;
        }

    }

}