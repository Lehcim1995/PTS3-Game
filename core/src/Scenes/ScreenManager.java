package Scenes;

import Interfaces.IConnection;
import Interfaces.IUser;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by Nick on 22-11-2016.
 */
public class ScreenManager {
    // Singleton: unique instance
    private static ScreenManager instance;

    // Reference to game
    private Game game;

    private IUser user;

    private InetAddress localhost = null;
    private String ip;
    private int portNumber = 1099;
    private String connection = "connection";
    private Registry registry;
    private IConnection conn;

    // Singleton: private constructor
    private ScreenManager() {
        super();
    }

    // Singleton: retrieve instance
    public static ScreenManager getInstance() {
        if (instance == null) {
            instance = new ScreenManager();
        }
        return instance;
    }

    public void setUser(IUser user)
    {
        this.user = user;
    }

    // Initialization with the game class
    public void initialize(Game game) {
        this.game = game;
    }

    // Show in the game the screen which enum type is received
    public void showScreen(ScreenEnum screenEnum, Object... params) {

        // Get current screen to dispose it
        Screen currentScreen = game.getScreen();

        // Show new screen
        AbstractScreen newScreen = screenEnum.getScreen(params);
        newScreen.buildStage();
        game.setScreen(newScreen);

        // Dispose previous screen
        if (currentScreen != null) {
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

    public String getIp()
    {
        return ip;
    }

    public int getPortNumber()
    {
        return portNumber;
    }

    public String getConnection()
    {
        return connection;
    }

    public Registry getRegistry()
    {
        return registry;
    }

    public IConnection getConn()
    {
        return conn;
    }

    public void setIp(String ip)
    {
        this.ip = ip;
    }

    public void setPortNumber(int portNumber)
    {
        this.portNumber = portNumber;
    }

    public void setConnection(String connection)
    {
        this.connection = connection;
    }

    public void setRegistry(Registry registry)
    {
        this.registry = registry;
    }

    public void setConn(IConnection conn)
    {
        this.conn = conn;
    }

    public IUser getUser()
    {
        return user;
    }
}
