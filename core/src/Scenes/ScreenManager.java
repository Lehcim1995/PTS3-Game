package Scenes;

import Interfaces.IConnection;
import Interfaces.IGameManager;
import Interfaces.IServer;
import Interfaces.IUser;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

import java.net.InetAddress;
import java.rmi.registry.Registry;

/**
 * Created by Nick on 22-11-2016.
 */
public class ScreenManager
{
    // Singleton: unique instance
    private static ScreenManager instance;
    private String lobby;

    // Reference to game
    private Game game;

    private IUser user;

    private InetAddress localhost = null;
    private String ip;
    private int portNumber = 1099;
    private String connection = "connection";
    private Registry registry;
    private IConnection conn;
    private String Server = "serermanager";
    private IServer pgm;
    private IGameManager gameManager;
    private String lobbyname;
    //Player is spectator
    private boolean isSpectator = false;

    // Singleton: private constructor
    private ScreenManager()
    {
        super();
    }

    // Singleton: retrieve instance
    public static ScreenManager getInstance()
    {
        if (instance == null)
        {
            instance = new ScreenManager();
        }
        return instance;
    }

    public String getLobby()
    {
        return lobby;
    }

    public void setLobby(String lobby)
    {
        this.lobby = lobby;
    }

    // Initialization with the game class
    public void initialize(Game game)
    {
        this.game = game;
    }

    // Show in the game the screen which enum type is received
    public void showScreen(ScreenEnum screenEnum, Object... params)
    {

        // Get current screen to dispose it
        Screen currentScreen = game.getScreen();

        // Show new screen
        AbstractScreen newScreen = screenEnum.getScreen(params);
        newScreen.buildStage();
        game.setScreen(newScreen);

        // Dispose previous screen
        if (currentScreen != null)
        {
            currentScreen.dispose();
        }
    }

    public Registry GetRegistry()
    {
        return registry;
    }

    public IConnection GetConnection()
    {
        return conn;
    }

    public String Getmeaningofconnection()
    {
        return connection;
    }

    public String GetMeaningOfServer()
    {
        return Server;
    }

    public String getIp()
    {
        return ip;
    }

    public void setIp(String ip)
    {
        this.ip = ip;
    }

    public int getPortNumber()
    {
        return portNumber;
    }

    public void setPortNumber(int portNumber)
    {
        this.portNumber = portNumber;
    }

    public String getConnection()
    {
        return connection;
    }

    public void setConnection(String connection)
    {
        this.connection = connection;
    }

    public Registry getRegistry()
    {
        return registry;
    }

    public void setRegistry(Registry registry)
    {
        this.registry = registry;
    }

    public IConnection getConn()
    {
        return conn;
    }

    public void setConn(IConnection conn)
    {
        this.conn = conn;
    }

    public IUser getUser()
    {
        return user;
    }

    public void setUser(IUser user)
    {
        this.user = user;
    }

    public void setpgm(IServer pgm)
    {
        this.pgm = pgm;
    }

    public IServer getpgm()
    {
        return pgm;
    }

    public IGameManager getGameManager()
    {
        return gameManager;
    }

    public void setGameManager(IGameManager gameManager)
    {
        this.gameManager = gameManager;
    }

    public boolean getIsSpectator()
    {
        return isSpectator;
    }

    public void setIsSpectator(boolean spectator)
    {
        isSpectator = spectator;
    }

    public void setLobbyName(String name)
    {
        this.lobbyname = name;
    }

    public String getLobbyname()
    {
        return lobbyname;
    }
}
