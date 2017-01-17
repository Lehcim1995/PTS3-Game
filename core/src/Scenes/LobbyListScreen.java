package Scenes;

import Interfaces.IGameManager;
import Interfaces.IServer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Level;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

/**
 * Created by Nick on 22-11-2016.
 */
public class LobbyListScreen extends AbstractScreen
{

    private Texture txtrLogout;
    private Texture txtrJoin;
    private Texture txtrRefresh;
    private Texture txtrStats;
    private Texture txtrCreate;
    private TextureRegion trLogout;
    private TextureRegion trJoin;
    private TextureRegion trStats;
    private TextureRegion trCreate;
    private TextureRegion trRefresh;
    private TextureRegionDrawable trdLogout;
    private TextureRegionDrawable trdJoin;
    private TextureRegionDrawable trdStats;
    private TextureRegionDrawable trdCreate;
    private TextureRegionDrawable trdRefresh;
    private List listServers;
    private ScrollPane scrollPaneServers;
    private TextField txtLobbyName;
    private Skin skin;
    private IServer pgm;
    private Skin tfSkin;

    /**
     * LobbyListScreen constructor
     */
    public LobbyListScreen()
    {
        super();

        txtrLogout = new Texture(Gdx.files.internal("core\\assets\\btn_logout.png"));
        txtrJoin = new Texture(Gdx.files.internal("core\\assets\\btn_join.png"));
        txtrStats = new Texture(Gdx.files.internal("core\\assets\\btn_stats.png"));
        txtrCreate = new Texture(Gdx.files.internal("core\\assets\\btn_create.png"));
        txtrRefresh = new Texture(Gdx.files.internal("core\\assets\\btn_refresh.png"));

        try
        {
            pgm = (IServer) ScreenManager.getInstance().getRegistry().lookup(ScreenManager.getInstance().GetMeaningOfServer());
            ScreenManager.getInstance().setpgm(pgm);
        }
        catch (RemoteException|NotBoundException e)
        {
            LOGGER.log(Level.WARNING, e.getMessage(), e);
        }


    }

    @Override
    public void buildStage()
    {
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        tfSkin = new Skin(Gdx.files.internal("uiskin.json"));
        //Logout
        trLogout = new TextureRegion(txtrLogout);
        trdLogout = new TextureRegionDrawable(trLogout);
        ImageButton btnLogout = new ImageButton(trdLogout);
        btnLogout.setPosition(260.f, 40.f, Align.center);

        //JoinGame
        trJoin = new TextureRegion(txtrJoin);
        trdJoin = new TextureRegionDrawable(trJoin);
        ImageButton btnJoin = new ImageButton(trdJoin);
        btnJoin.setPosition(260.f, 120.f, Align.center);

        //Stats
        trStats = new TextureRegion(txtrStats);
        trdStats = new TextureRegionDrawable(trStats);
        ImageButton btnStats = new ImageButton(trdStats);
        btnStats.setPosition(260.f, 200.f, Align.center);

        //Create
        trCreate = new TextureRegion(txtrCreate);
        trdCreate = new TextureRegionDrawable(trCreate);
        ImageButton btnCreate = new ImageButton(trdCreate);
        btnCreate.setPosition(375.f, 475.f, Align.center);

        //refresh
        trRefresh = new TextureRegion(txtrRefresh);
        trdRefresh = new TextureRegionDrawable(trRefresh);
        ImageButton btnRefresh = new ImageButton(trdRefresh);
        btnRefresh.setPosition(375.f, 400.f, Align.center);

        //textfieldlobby
        txtLobbyName = new TextField("", tfSkin);
        txtLobbyName.setMessageText("Enter Lobby Name");
        txtLobbyName.setPosition(150.f, 300.f, Align.center);

        //List Lobbies
        listServers = new List(skin);
        try
        {
            listServers.setItems(pgm.getLobbies().toArray());
        }
        catch (RemoteException e)
        {
            LOGGER.log(Level.WARNING, e.getMessage(), e);
        }
        scrollPaneServers = new ScrollPane(listServers, skin);
        scrollPaneServers.setSize(200.f, 150.f);
        scrollPaneServers.setPosition(25.f, 350.f);

        addActor(btnLogout);
        addActor(btnJoin);
        addActor(btnStats);
        addActor(scrollPaneServers);
        addActor(btnCreate);
        addActor(btnRefresh);
        addActor(txtLobbyName);

        scrollPaneServers.addListener(new InputListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {

                String servernaam = (String) listServers.getItems().get(listServers.getSelectedIndex());
                try
                {
                    ScreenManager.getInstance().setLobby(txtLobbyName.getText());
                    IGameManager sgm = pgm.JoinLobby(servernaam, ScreenManager.getInstance().getUser());
                    if (sgm != null)
                    {
                        ScreenManager.getInstance().setGameManager(sgm);
                        sgm.addUser(ScreenManager.getInstance().getUser());
                        ScreenManager.getInstance().setLobbyName(servernaam);
                        ScreenManager.getInstance().showScreen(ScreenEnum.GAMELOBBY);
                    }

                }
                catch (RemoteException e)
                {
                    LOGGER.log(Level.WARNING, e.getMessage(), e);
                }

                return false;
            }
        });

        btnLogout.addListener(new InputListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                ScreenManager.getInstance().showScreen(ScreenEnum.LOGIN);
                return false;
            }
        });
        btnJoin.addListener(new InputListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                try
                {
                    if (pgm.getLobbies().contains(txtLobbyName.getText()))
                    {
                        ScreenManager.getInstance().setLobby(txtLobbyName.getText());
                        IGameManager sgm = pgm.JoinLobby(txtLobbyName.getText(), ScreenManager.getInstance().getUser());
                        if (sgm != null)
                        {

                            ScreenManager.getInstance().setGameManager(sgm);
                            sgm.addUser(ScreenManager.getInstance().getUser());
                            ScreenManager.getInstance().setLobbyName(txtLobbyName.getText());
                            ScreenManager.getInstance().showScreen(ScreenEnum.GAMELOBBY);
                        }
                    }
                    else
                    {
                        LOGGER.log(Level.INFO, "Voer geldige Lobby in!");
                    }

                }
                catch (RemoteException e)
                {
                    LOGGER.log(Level.WARNING, e.getMessage(), e);
                }
                return false;
            }
        });
        btnStats.addListener(new InputListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                ScreenManager.getInstance().showScreen(ScreenEnum.STATS);
                return false;
            }
        });
        btnCreate.addListener(new InputListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                ScreenManager.getInstance().showScreen(ScreenEnum.NEWLOBBY);
                return false;
            }
        });

        btnRefresh.addListener(new InputListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                ScreenManager.getInstance().showScreen(ScreenEnum.LOBBYLIST);
                return false;
            }
        });
    }
}
