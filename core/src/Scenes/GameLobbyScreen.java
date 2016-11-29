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
    private Skin skin;

    public GameLobbyScreen()
    {
        super();
        txtrLeave = new Texture(Gdx.files.internal("core\\assets\\btn_leave.png"));
        txtrReady = new Texture(Gdx.files.internal("core\\assets\\btn_ready.png"));
    }

    @Override
    public void buildStage() {
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        myTrLeave = new TextureRegion(txtrLeave);
        myTrReady = new TextureRegion(txtrReady);
        myTrdLeave = new TextureRegionDrawable(myTrLeave);
        myTrdReady = new TextureRegionDrawable(myTrReady);

        ImageButton btnLeave = new ImageButton(myTrdLeave);
        ImageButton btnReady = new ImageButton(myTrdReady);

        listPlayers = new List(skin);

        listPlayers.setPosition(375.f, 400.f);
        btnLeave.setPosition(125.f, 40.f, Align.center);
        btnReady.setPosition(375.f, 40.f, Align.center);

        addActor(btnLeave);
        addActor(btnReady);
        addActor(listPlayers);

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
