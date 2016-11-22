package Scenes;

import Classes.User;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

/**
 * Created by Nick on 22-11-2016.
 */
public class StatsScreen extends AbstractScreen{

    User user;
    Skin skin = new Skin();

    public StatsScreen()
    {
        super();

        // TODO Pich up current user (from server)
        user = null;
    }
    @Override
    public void buildStage() {
        Label kills = new Label("Total Kills: " + String.valueOf(user.GetKills()), skin);
        Label deaths = new Label("Total Deaths" + String.valueOf(user.GetDeaths()), skin);
        Label KDratio = new Label("K/D ratio: " + String.valueOf(user.GetKDRatio()), skin);
        Label shotsFired = new Label("Total shots fired: " + String.valueOf(user.GetShots()), skin);
        Label shotsHit = new Label("Total shots hit: " + String.valueOf(user.GetShotsHit()), skin);
        Label accuracy = new Label("Accuracy: " + String.valueOf(user.GetAccuracyPercentage()) + "%", skin);
        Label matchesPlayed = new Label("Total matches played: " + String.valueOf(user.GetMatchesPlayed()), skin);
        Label winRatio = new Label("Win ratio: " + String.valueOf(user.GetWinPercentage() + "%"), skin);
        Label matchesWon = new Label("Total matches won: " + String.valueOf(user.GetMatchesWon()), skin);
        Label matchesLost = new Label("Total matches lost: " + String.valueOf(user.GetMatchesLost()), skin);
        ImageButton btnBack = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("core\\assets\\btn_back.png")))));



        btnBack.setPosition(260.f, 40.f, Align.center);
        addActor(btnBack);

        btnBack.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event,
                                     float x, float y, int pointer, int button) {
                ScreenManager.getInstance().showScreen(ScreenEnum.LOBBYLIST);
                return false;
            }
        });
}

    @Override
    public void dispose() {
        super.dispose();
    }
}
