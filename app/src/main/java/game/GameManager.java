package game;

import game.graphics.Map;

/**
 * Created by Andrea on 22/04/2015.
 */
//public class GameManager {
//
//    private static GameManager instance;
//
//    private Room room;
//    private Map map;
//
//    private GameManager() {
//
//    }
//
//    public static GameManager getInstance() {
//        if (instance == null) {
//            instance = new GameManager();
//        }
//        return instance;
//    }
//
//    public void setRoom(Room room) {
//        this.room = room;
//    }
//
//    public Room getRoom() {
//        return room;
//    }
//
//    public void setMap(Map map) {
//        this.map = map;
//    }
//
//    public Map getMap() {
//        return map;
//    }
//
//}

public enum GameManager {
    MANAGER;

    private Room room;
    private Map map;

    GameManager() {
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Room getRoom() {
        return room;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public Map getMap() {
        return map;
    }

}
