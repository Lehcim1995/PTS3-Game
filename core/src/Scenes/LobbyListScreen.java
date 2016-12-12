package Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

/**
 * Created by Nick on 22-11-2016.
 */
public class LobbyListScreen extends AbstractScreen{

    private Texture txtrLogout;
    private Texture txtrJoin;
    private Texture txtrStats;
    private TextureRegion TrLogout;
    private TextureRegion TrJoin;
    private TextureRegion TrStats;
    private TextureRegionDrawable TrdLogout;
    private TextureRegionDrawable TrdJoin;
    private TextureRegionDrawable TrdStats;
    private List listServers;
    private ScrollPane scrollPaneServers;
    private Skin skin;

    public LobbyListScreen()
    {
        super();
        txtrLogout = new Texture(Gdx.files.internal("core\\assets\\btn_logout.png"));
        txtrJoin = new Texture(Gdx.files.internal("core\\assets\\btn_join.png"));
        txtrStats = new Texture(Gdx.files.internal("core\\assets\\btn_stats.png"));
    }

    @Override
    public void buildStage() {
        skin = new Skin(Gdx.files.internal("uiskin.json"));

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

        //List Players
        listServers = new List(skin);
        listServers.setItems("asd","qwe","zxc","fdg","Sibe","Myron","asdasdasd");
        scrollPaneServers = new ScrollPane(listServers,skin);
        scrollPaneServers.setSize(200.f, 150.f);
        scrollPaneServers.setPosition(25.f, 350.f);

        addActor(btnLogout);
        addActor(btnJoin);
        addActor(btnStats);
        addActor(scrollPaneServers);


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
                ScreenManager.getInstance().showScreen(ScreenEnum.GAMELOBBY);
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
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
