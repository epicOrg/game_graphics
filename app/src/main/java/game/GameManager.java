package game;

/**
 * Created by Andrea on 22/04/2015.
 */
public enum GameManager {
    MANAGER;

    private Room room;

    GameManager() {
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Room getRoom() {
        return room;
    }

}
