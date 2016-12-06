package Scenes;

import Classes.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;

/**
 * Created by Nick on 22-11-2016.
 */
public class GameLobbyScreen extends AbstractScreen{
    private Texture txtrLeave;
    private Texture txtrReady;
    private TextureRegion myTrLeave;
    private TextureRegion myTrReady;
    private TextureRegionDrawable myTrdLeave;
    private TextureRegionDrawable myTrdReady;
    private List listPlayers;
    private List listChat;
    private Skin skin;
    private ScrollPane scrollPanePlayer;
    private ScrollPane scrollPaneChat;

    public GameLobbyScreen()
    {
        super();
        txtrLeave = new Texture(Gdx.files.internal("core\\assets\\btn_leave.png"));
        txtrReady = new Texture(Gdx.files.internal("core\\assets\\btn_ready.png"));
    }

    @Override
    public void buildStage() {
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        //Button Leave
        myTrLeave = new TextureRegion(txtrLeave);
        myTrdLeave = new TextureRegionDrawable(myTrLeave);
        ImageButton btnLeave = new ImageButton(myTrdLeave);
        btnLeave.setPosition(125.f, 40.f, Align.center);

        //Button Ready
        myTrReady = new TextureRegion(txtrReady);
        myTrdReady = new TextureRegionDrawable(myTrReady);
        ImageButton btnReady = new ImageButton(myTrdReady);
        btnReady.setPosition(375.f, 40.f, Align.center);

        //List Players
        listPlayers = new List(skin);
        listPlayers.setItems("asd","qwe","zxc","fdg","Sibe","Myron","asdasdasd");
        scrollPanePlayer = new ScrollPane(listPlayers,skin);
        scrollPanePlayer.setSize(200.f, 150.f);
        scrollPanePlayer.setPosition(25.f, 350.f);

        //List Chat
        listChat = new List(skin);
        listChat.setItems("asd","qwe","zxc","fdg","Sibe","Myron","asdasdasd asd sad sa  d as dsa sd sad da s ads das dsa sad dsa");
        scrollPaneChat = new ScrollPane(listChat,skin);
        scrollPaneChat.setSize(200.f, 225.f);
        scrollPaneChat.setPosition(25.f, 100.f);
        scrollPaneChat.isScrollingDisabledX();
        scrollPaneChat.

        //Add all items to scene
        addActor(btnLeave);
        addActor(btnReady);
        addActor(scrollPanePlayer);
        addActor(scrollPaneChat);

        btnLeave.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event,
                                     float x, float y, int pointer, int button) {
                ScreenManager.getInstance().showScreen(ScreenEnum.LOBBYLIST);
                return false;
            }
        });

        btnReady.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event,
                                     float x, float y, int pointer, int button) {
                //todo set player to ready
                System.out.println("Player X is ready");
                return false;
            }
        });
    }
}
