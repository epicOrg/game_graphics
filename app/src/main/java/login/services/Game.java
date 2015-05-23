package login.services;

import android.os.Handler;
import android.os.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import login.interaction.FieldsNames;
import shadow.math.SFVertex3f;

public class Game implements Service {

    public static final String LOG_TAG = "Game";

    public static final int STATUS = 0;
    public static final int MAP = 1;
    public static final int POSITIONS = 2;

    private JSONObject json;
    private Handler handler;

    public Game(JSONObject json) {
        super();
        this.json = json;
    }

    @Override
    public void start() {
        readFields();
    }

    private void readFields() {
        try {
            Message message = null;
            switch (json.getString(FieldsNames.SERVICE_TYPE)) {
                case FieldsNames.GAME_STATUS:
                    message = getGameStatusMessage();
                    break;
                case FieldsNames.GAME_MAP:
                    message = getGameMapMessage();
                    break;
                case FieldsNames.GAME_POSITIONS:
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
            if (json.has(FieldsNames.GAME_GO))
                go = json.getBoolean(FieldsNames.GAME_GO);
            if (json.has(FieldsNames.GAME_END))
                gameEnd = json.getString(FieldsNames.GAME_END);
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
            JSONArray jItems = json.getJSONArray(FieldsNames.GAME_ITEMS);
            for (int i = 0; i < jItems.length(); i++) {
                JSONObject jObject = jItems.getJSONObject(i);
                String object = jObject.getString(FieldsNames.GAME_OBJECT);
                String texture = jObject.getString(FieldsNames.GAME_TEXTURE);
                String position = jObject.getString(FieldsNames.GAME_POSITION);
                String size = jObject.getString(FieldsNames.GAME_SIZE);
                gameMapObjects.add(new GameMapObject(object, texture, position, size));
            }

            width = json.getInt(FieldsNames.GAME_WIDTH);
            height = json.getInt(FieldsNames.GAME_HEIGHT);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        gameMapResult = new GameMapResult(width, height, gameMapObjects);

        try {
            String jPlayerPosition = json.getString(FieldsNames.GAME_PLAYER_POSITION);
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
            JSONArray jPlayers = json.getJSONArray(FieldsNames.GAME_PLAYERS);

            for (int i = 0; i < jPlayers.length(); i++) {
                JSONObject jPlayer = jPlayers.getJSONObject(i);
                String username = jPlayer.getString(FieldsNames.USERNAME);
                JSONObject pos = jPlayer.getJSONObject(FieldsNames.GAME_POSITION);
                JSONObject dir = jPlayer.getJSONObject(FieldsNames.GAME_DIRECTION);

                float xPos = Float.parseFloat(pos.getString(FieldsNames.GAME_X));
                float yPos = Float.parseFloat(pos.getString(FieldsNames.GAME_Y)) - 1.5f;
                float zPos = Float.parseFloat(pos.getString(FieldsNames.GAME_Z));
                float xDir = Float.parseFloat(dir.getString(FieldsNames.GAME_X));
                float yDir = Float.parseFloat(dir.getString(FieldsNames.GAME_Y)) - 1.5f;
                float zDir = Float.parseFloat(dir.getString(FieldsNames.GAME_Z));

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

        private boolean go;
        private String gameEnd;

        public GameStatusResult(boolean go, String gameEnd) {
            this.go = go;
            this.gameEnd = gameEnd;
        }

        public boolean isGo() {
            return go;
        }

        public String getGameEnd() {
            return gameEnd;
        }

    }

    public class GameMapResult {

        private int width, height;
        private ArrayList<GameMapObject> gameMapObjects;

        public float playerPositionX, playerPositionY, playerPositionZ;

        public GameMapResult(int width, int height, ArrayList<GameMapObject> gameMapObjects) {
            this.width = width;
            this.height = height;
            this.gameMapObjects = gameMapObjects;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public ArrayList<GameMapObject> getGameMapObjects() {
            return gameMapObjects;
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