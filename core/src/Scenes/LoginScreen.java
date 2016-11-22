package Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;


/**
 * Created by Nick on 22-11-2016.
 */
public class LoginScreen extends AbstractScreen {

    private Texture txtrLogin;
    private TextureRegion myTextureRegion;
    private TextureRegionDrawable myTrd;

    public LoginScreen()
    {
        super();
        txtrLogin = new Texture(Gdx.files.internal("core\\assets\\btn_login.png"));
    }

    @Override
    public void buildStage() {
        myTextureRegion = new TextureRegion(txtrLogin);
        myTrd = new TextureRegionDrawable(myTextureRegion);
        ImageButton btnLogin = new ImageButton(myTrd);
        btnLogin.setPosition(260.f, 40.f, Align.center);
        addActor(btnLogin);

        btnLogin.addListener(new InputListener() {
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
