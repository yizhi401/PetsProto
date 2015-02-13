package cn.peterchen.pets.ui.game;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Send Game Command through this controller, and get game status from this controller.
 * Created by peter on 15-1-29.
 */
public class GameController {


    public static final int STATUS_MINI_GAME = 1;
    public static final int STATUS_EATING = 2;
    public static final int STATUS_WORKING = 3;
    public static final int STATUS_NORMAL = 4;

    public static final int COMMAND_PRESSED = 1;
    public static final int COMMAND_NORMAL = 0;
    public static final int COMMAND_JUMPING = 2;

    private static GameController instance;

    private List<GameControlObserver> gameControlObservers;

    private int gameStatus;

    private GameCommand command;

    private GameController() {
        gameControlObservers = new ArrayList<>();
        gameStatus = STATUS_NORMAL;
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


    public void attach(GameControlObserver observer) {
        gameControlObservers.add(observer);
    }

    public void dettach(GameControlObserver observer) {
        if (!gameControlObservers.contains(observer)) {
            Log.i("mInfo", "not contained in the observer tree");
        } else {
            gameControlObservers.remove(observer);
        }
    }

    public void setCommand(GameCommand command) {
        this.command = command;
        switch (command) {
            case START_MINI_GAME:
                gameStatus = GameController.STATUS_MINI_GAME;
                break;
            case STOP_MINI_GAME:
                gameStatus = GameController.STATUS_NORMAL;
                break;
        }
        update(command);
    }

    private void update(GameCommand command) {
        for (GameControlObserver observer : gameControlObservers) {
            observer.update(command);
        }
    }

    public interface GameControlObserver {
        public void update(GameCommand command);
    }
}
