package Scenes;

import Classes.ServerGameManger;
import Interfaces.IGameManager;
import Interfaces.IServer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by Nick on 22-11-2016.
 */
public class LobbyListScreen extends AbstractScreen{

    private Texture txtrLogout;
    private Texture txtrJoin;
    private Texture txtrStats;
    private Texture txtrCreate;
    private TextureRegion TrLogout;
    private TextureRegion TrJoin;
    private TextureRegion TrStats;
    private TextureRegion TrCreate;
    private TextureRegionDrawable TrdLogout;
    private TextureRegionDrawable TrdJoin;
    private TextureRegionDrawable TrdStats;
    private TextureRegionDrawable TrdCreate;
    private List listServers;
    private ScrollPane scrollPaneServers;
    private TextField txtLobbyName;
    private Skin skin;
    private IServer pgm;
    private Skin tfSkin;

    public LobbyListScreen()
    {
        super();

        txtrLogout = new Texture(Gdx.files.internal("core\\assets\\btn_logout.png"));
        txtrJoin = new Texture(Gdx.files.internal("core\\assets\\btn_join.png"));
        txtrStats = new Texture(Gdx.files.internal("core\\assets\\btn_stats.png"));
        txtrCreate = new Texture(Gdx.files.internal("core\\assets\\btn_create.png"));
        try
        {
            pgm =  (IServer) ScreenManager.getInstance().getRegistry().lookup(ScreenManager.getInstance().GetMeaningOfServer());
            ScreenManager.getInstance().setpgm(pgm);
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        catch (NotBoundException e)
        {
            e.printStackTrace();
        }


    }

    @Override
    public void buildStage() {

        skin = new Skin(Gdx.files.internal("uiskin.json"));
        tfSkin = new Skin(Gdx.files.internal("uiskin.json"));
        //Logout
        TrLogout = new TextureRegion(txtrLogout);
        TrdLogout = new TextureRegionDrawable(TrLogout);
        ImageButton btnLogout = new ImageButton(TrdLogout);
        btnLogout.setPosition(260.f, 40.f, Align.center);

        //JoinGame
        TrJoin = new TextureRegion(txtrJoin);
        TrdJoin = new TextureRegionDrawable(TrJoin);
        ImageButton btnJoin = new ImageButton(TrdJoin);
        btnJoin.setPosition(260.f, 120.f, Align.center);

        //Stats
        TrStats = new TextureRegion(txtrStats);
        TrdStats = new TextureRegionDrawable(TrStats);
        ImageButton btnStats = new ImageButton(TrdStats);
        btnStats.setPosition(260.f, 200.f, Align.center);

        //Create
        TrCreate = new TextureRegion(txtrCreate);
        TrdCreate = new TextureRegionDrawable(TrCreate);
        ImageButton btnCreate = new ImageButton(TrdCreate);
        btnCreate.setPosition(375.f, 475.f, Align.center);

        //textfieldlobby
        txtLobbyName = new TextField("",tfSkin);
        txtLobbyName.setMessageText("Enter Lobby Name");
        txtLobbyName.setPosition(150.f,300.f,Align.center);

        //List Lobbies
        listServers = new List(skin);
        try
        {
            listServers.setItems(pgm.getLobbies().toArray());
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        scrollPaneServers = new ScrollPane(listServers,skin);
        scrollPaneServers.setSize(200.f, 150.f);
        scrollPaneServers.setPosition(25.f, 350.f);

        addActor(btnLogout);
        addActor(btnJoin);
        addActor(btnStats);
        addActor(scrollPaneServers);
        addActor(btnCreate);
        addActor(txtLobbyName);


        btnLogout.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event,
                                     float x, float y, int pointer, int button) {
                ScreenManager.getInstance().showScreen(ScreenEnum.LOGIN);
                return false;
            }
        });
        btnJoin.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event,
                                     float x, float y, int pointer, int button) {


                try
                {
                    if(pgm.getLobbies().contains(txtLobbyName.getText()))
                    {
                        ScreenManager.getInstance().setLobby(txtLobbyName.getText());
                        IGameManager sgm = pgm.JoinLobby(txtLobbyName.getText(), ScreenManager.getInstance().getUser());
                        if(sgm != null) {

                            ScreenManager.getInstance().setGameManager(sgm);
                            sgm.addUser(ScreenManager.getInstance().getUser());
                            ScreenManager.getInstance().showScreen(ScreenEnum.GAMELOBBY);
                        }
                    }
                    else{
                        System.out.println("voer geldige lobby in!");
                    }

                }
                catch (RemoteException e)
                {
                    e.printStackTrace();
                }
                return false;
            }
        });
        btnStats.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event,
                                     float x, float y, int pointer, int button) {
                ScreenManager.getInstance().showScreen(ScreenEnum.STATS);
                return false;
            }
        });
        btnCreate.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event,
                                     float x, float y, int pointer, int button) {
                ScreenManager.getInstance().showScreen(ScreenEnum.NEWLOBBY);
                return false;
            }
        });
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
