package Scenes;


import Classes.Connection;

import Interfaces.IConnection;
import Interfaces.IUser;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.sun.media.jfxmedia.logging.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.logging.Level;


/**
 * Created by Nick on 22-11-2016.
 */
public class LoginScreen extends AbstractScreen {

    private Texture txtrLogin;
    private Texture txtrRegister;
    private TextureRegion myTextureRegion;
    private TextureRegion registerTextureregion;
    private TextureRegionDrawable myTrd;
    private TextureRegionDrawable registerTrd;
    private TextField txtUserName;
    private TextField txtPassword;
    private Skin tfSkin;

    public LoginScreen()
    {
        super();
        txtrLogin = new Texture(Gdx.files.internal("core\\assets\\btn_login.png"));
        txtrRegister = new Texture(Gdx.files.internal("core\\assets\\btn_register.png"));
    }

    @Override
    public void buildStage() {

        // set textfield skin
        tfSkin = new Skin(Gdx.files.internal("uiskin.json"));
        // maak username textfield
        txtUserName = new TextField("",tfSkin);
        txtUserName.setMessageText("EMAIL");
        txtUserName.setPosition(260.f,300.f,Align.center);
        // maak password textfield
        txtPassword = new TextField("",tfSkin);
        txtPassword.setPasswordMode(true);
        txtPassword.setMessageText("Password");
        txtPassword.setPosition(260.f,200.f, Align.center);
        // maak login button
        myTextureRegion = new TextureRegion(txtrLogin);
        myTrd = new TextureRegionDrawable(myTextureRegion);
        ImageButton btnLogin = new ImageButton(myTrd);
        btnLogin.setPosition(260.f, 150.f, Align.center);
        // maak registrate button
        registerTextureregion = new TextureRegion(txtrRegister);
        registerTrd = new TextureRegionDrawable(registerTextureregion);
        ImageButton btnRegister = new ImageButton(registerTrd);
        btnRegister.setPosition(260.f,40.f,Align.center);
        // voeg componenten toe aan de scene
        addActor(txtUserName);
        addActor(txtPassword);
        addActor(btnLogin);
        addActor(btnRegister);

        btnLogin.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event,
                                     float x, float y, int pointer, int button) {
                System.out.println(txtUserName.getText());
                System.out.println(txtPassword.getText());
                String email = txtUserName.getText();
                String ww = txtPassword.getText();
                if(txtUserName.getText().isEmpty() != true && txtPassword.getText().isEmpty() != true){
                    try
                    {
                        IUser user = conn.LogIn(email, ww);
                        if(user != null)
                        {
                            ScreenManager.getInstance().SetUser(user);
                            ScreenManager.getInstance().showScreen(ScreenEnum.LOBBYLIST);
                        }
                        else
                        {
                            System.out.println("Niet geregistreerd");
                            ///TODO: popup fout bij inloggen
                        }

                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        System.out.println("Error: lege strings");
                    }
                }
                return false;
            }
        });

        btnRegister.addListener(new InputListener(){
            @Override
                    public boolean touchDown(InputEvent event,
            float x, float y, int pointer, int button){
                System.out.println("Registerbutton klicked");
                ScreenManager.getInstance().showScreen(ScreenEnum.REGISTER);
                return false;
            }
        });
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
