package Scenes;

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
            return new LoginScreen();
        }
    },
    LOBBYLIST {
        public AbstractScreen getScreen(Object... params) {
            return new LoginScreen();
        }
    },
    STATS {
        public AbstractScreen getScreen(Object... params) {
            return new LoginScreen();
        }
    },
    NEWLOBBY {
        public AbstractScreen getScreen(Object... params) {
            return new LoginScreen();
        }
    },
    GAMELOBBY {
        public AbstractScreen getScreen(Object... params) {
            return new LoginScreen();
        }
    },
    GAMESCENE {
        public AbstractScreen getScreen(Object... params) {
            return new LoginScreen();
        }
    };

    public abstract AbstractScreen getScreen(Object... params);
}
