package Classes;

/**
 * Created by michel on 11-10-2016.
 */
public class ClientConnect
{
    private static ClientConnect instance;
    private GameManager gameManager;

    private ClientConnect()
    {

    }

    public static ClientConnect getInstance()
    {
        return instance == null ? instance = new ClientConnect() : instance;
    }

    public boolean Login(String username, String password)
    {
        return false;
    }

    public GameManager EnterLobby(String lobby)
    {
        return null;
    }

    public void Update()
    {

    }
}
