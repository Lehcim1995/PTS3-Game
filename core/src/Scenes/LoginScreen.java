package Scenes;

import Classes.ASOM;
import com.asom.mygame.MainClass;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

import java.time.format.TextStyle;


/**
 * Created by Nick on 22-11-2016.
 */
public class LoginScreen extends AbstractScreen {

    private Texture txtrLogin;
    private TextureRegion myTextureRegion;
    private TextureRegionDrawable myTrd;
    private TextField txtUserName;
    private TextField txtPassword;
    private Skin tfSkin;

    public LoginScreen()
    {
        super();
        txtrLogin = new Texture(Gdx.files.internal("core\\assets\\btn_login.png"));
    }

    @Override
    public void buildStage() {
        // todo fix die kk skin shit.
        tfSkin = new Skin(Gdx.files.internal("uiskin.json"));
        //TextField.TextFieldStyle tfs = new TextField.TextFieldStyle();
        //tfs.font = tfSkin.getFont("tf");
        txtUserName = new TextField("",tfSkin);
        txtUserName.setMessageText("Username");
        txtUserName.setPosition(260.f,300.f,Align.center);
        txtPassword = new TextField("",tfSkin);
        txtPassword.setMessageText("Password");
        txtPassword.setPosition(260.f,200.f, Align.center);
        myTextureRegion = new TextureRegion(txtrLogin);
        myTrd = new TextureRegionDrawable(myTextureRegion);
        final ImageButton btnLogin = new ImageButton(myTrd);
        btnLogin.setPosition(260.f, 40.f, Align.center);
        addActor(txtUserName);
        addActor(txtPassword);
        addActor(btnLogin);

        btnLogin.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event,
                                     float x, float y, int pointer, int button) {
<<<<<<< HEAD
                ScreenManager.getInstance().showScreen(ScreenEnum.GAMESCENE);
=======
                System.out.println(txtUserName.getText());
                ScreenManager.getInstance().showScreen(ScreenEnum.LOBBYLIST);
>>>>>>> origin/master
                return false;
            }
        });
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
