package Scenes;

import Classes.ASOM;
import com.asom.mygame.MainClass;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

import java.time.format.TextStyle;

/**
 * Created by Nick on 22-11-2016.
 */
public class RegisterScreen extends AbstractScreen{

    private Texture txtrRegister;
    private Texture txtrCancel;
    private TextureRegion myTrRegister;
    private TextureRegion myTrCancel;
    private TextureRegionDrawable myTrdRegister;
    private TextureRegionDrawable myTrdCancel;

    public RegisterScreen()
    {
        super();
        txtrRegister = new Texture(Gdx.files.internal("core\\assets\\btn_register.png"));
        txtrCancel = new Texture(Gdx.files.internal("core\\assets\\btn_back.png"));
    }

    @Override
    public void buildStage() {
        myTrRegister = new TextureRegion(txtrRegister);
        myTrCancel = new TextureRegion(txtrCancel);
        myTrdRegister = new TextureRegionDrawable(myTrRegister);
        myTrdCancel = new TextureRegionDrawable(myTrCancel);
        TextField.TextFieldStyle tfs = new TextField.TextFieldStyle();

        ImageButton btnRegister = new ImageButton(myTrdRegister);
        ImageButton btnBack = new ImageButton(myTrdCancel);

        btnRegister.setPosition(375.f, 40.f, Align.center);
        btnBack.setPosition(125.f, 40.f, Align.center);

        addActor(btnBack);
        addActor(btnRegister);

        btnBack.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event,
                                     float x, float y, int pointer, int button) {
                ScreenManager.getInstance().showScreen(ScreenEnum.LOGIN);
                return false;
            }
        });

        btnRegister.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event,
                                     float x, float y, int pointer, int button) {
                ScreenManager.getInstance().showScreen(ScreenEnum.GAMESCENE);
                return false;
            }
        });


    }
}
