package Scenes;

import Interfaces.IUser;
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
public class StatsScreen extends AbstractScreen
{

    IUser user;
    Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

    public StatsScreen()
    {
        super();

        // TODO Pick up current user (from server)
        this.user = ScreenManager.getInstance().getUser();
    }

    @Override
    public void buildStage()
    {

        Label stats = new Label("Stats", skin);

        Label kills = new Label("Total Kills: " + String.valueOf(user.getKills()), skin);
        Label deaths = new Label("Total Deaths: " + String.valueOf(user.getDeaths()), skin);
        Label KDratio = new Label("K/D ratio: " + String.valueOf(user.getKDRatio()), skin);
        Label shotsFired = new Label("Total shots fired: " + String.valueOf(user.getShots()), skin);
        Label shotsHit = new Label("Total shots hit: " + String.valueOf(user.getShotsHit()), skin);
        Label accuracy = new Label("Accuracy: " + String.valueOf(user.getAccuracyPercentage()) + "%", skin);
        Label matchesPlayed = new Label("Total matches played: " + String.valueOf(user.getMatchesPlayed()), skin);
        Label winRatio = new Label("Win ratio: " + String.valueOf(user.getWinPercentage() + "%"), skin);
        Label matchesWon = new Label("Total matches won: " + String.valueOf(user.getMatchesWon()), skin);
        Label matchesLost = new Label("Total matches lost: " + String.valueOf(user.getMatchesLost()), skin);
        ImageButton btnBack = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("btn_back.png")))));

        stats.setPosition(50.f, 440.f, Align.left);

        kills.setPosition(50.f, 380.f, Align.left);
        deaths.setPosition(50.f, 360.f, Align.left);
        KDratio.setPosition(300.f, 370.f, Align.left);
        shotsFired.setPosition(50.f, 320.f, Align.left);
        shotsHit.setPosition(50.f, 300.f, Align.left);
        accuracy.setPosition(300.f, 310.f, Align.left);
        matchesPlayed.setPosition(50.f, 260.f, Align.left);
        winRatio.setPosition(50.f, 240.f, Align.left);
        matchesWon.setPosition(300.f, 260.f, Align.left);
        matchesLost.setPosition(300.f, 240.f, Align.left);

        addActor(stats);

        addActor(kills);
        addActor(deaths);
        addActor(KDratio);
        addActor(shotsFired);
        addActor(shotsHit);
        addActor(accuracy);
        addActor(matchesPlayed);
        addActor(winRatio);
        addActor(matchesWon);
        addActor(matchesLost);

        btnBack.setPosition(260.f, 40.f, Align.center);
        addActor(btnBack);

        btnBack.addListener(new InputListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                ScreenManager.getInstance().showScreen(ScreenEnum.LOBBYLIST);
                return false;
            }
        });
    }

    @Override
    public void dispose()
    {
        super.dispose();
    }
}
