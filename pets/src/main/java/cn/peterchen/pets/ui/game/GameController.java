package cn.peterchen.pets.ui.game;

/**
 * Created by peter on 15-1-29.
 */
public class GameController {


    public static final int STATUS_MINI_GAME = 1;
    public static final int STATUS_EATING = 2;
    public static final int STATUS_WORKING = 3;


    public static final int COMMAND_PRESSED = 1;
    public static final int COMMAND_NORMAL = 0;
    public static final int COMMAND_JUMPING = 2;

    private static GameController instance;

    public int gameStatus;

    public int command;

    private GameController() {
    }

    public static GameController getInstance() {
        if (instance == null) {
            synchronized (GameController.class) {
                if (instance == null) {
                    instance = new GameController();
                }
            }
        }
        return instance;
    }

    public int getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(int gameStatus) {
        this.gameStatus = gameStatus;
    }

    public int getCommand() {
        return command;
    }

    public void setCommand(int command) {
        this.command = command;
    }
}
