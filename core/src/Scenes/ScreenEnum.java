package Scenes;

import com.asom.mygame.MainClass;

/**
 * Created by Nick on 22-11-2016.
 */
public enum ScreenEnum {
    //login, register, lobbyList, stats, newLobby, gameLobby, gameScene
    LOGIN {
        public AbstractScreen getScreen(Object... params) {
            return new LoginScreen();
        }
    },
    REGISTER {
        public AbstractScreen getScreen(Object... params) {
            return new RegisterScreen();
        }
    },
    LOBBYLIST {
        public AbstractScreen getScreen(Object... params) {
            return new LobbyListScreen();
        }
    },
    STATS {
        public AbstractScreen getScreen(Object... params) { return new StatsScreen(); }
    },
    NEWLOBBY {
        public AbstractScreen getScreen(Object... params) {
            return new NewLobbyScreen();
        }
    },
    GAMELOBBY {
        public AbstractScreen getScreen(Object... params) {
            return new GameLobbyScreen();
        }
    },
    GAMESCENE {
        public AbstractScreen getScreen(Object... params) {
            return new GameSceneScreen();
        }
    };

    public abstract AbstractScreen getScreen(Object... params);
}
