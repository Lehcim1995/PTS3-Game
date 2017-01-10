package Scenes;

import Classes.Connection;
import Interfaces.IConnection;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.sql.SQLException;

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
    private TextField tfName;
    private TextField tfLastName;
    private TextField tfEmail;
    private TextField tfUsername;
    private TextField tfPassword;
    private TextField tfIP;
    private Skin tfSkin;

    public RegisterScreen()
    {
        super();
        txtrRegister = new Texture(Gdx.files.internal("core\\assets\\btn_register.png"));
        txtrCancel = new Texture(Gdx.files.internal("core\\assets\\btn_back.png"));
    }

    @Override
    public void buildStage() {
        tfSkin = new Skin(Gdx.files.internal("uiskin.json"));
        tfName = new TextField("",tfSkin);
        tfName.setMessageText("First name");
        tfLastName = new TextField("",tfSkin);
        tfLastName.setMessageText("Last name");
        tfEmail = new TextField("",tfSkin);
        tfEmail.setMessageText("Email");
        tfUsername = new TextField("",tfSkin);
        tfUsername.setMessageText("Username");
        tfPassword = new TextField("",tfSkin);
        tfPassword.setMessageText("Password");
        tfIP = new TextField("",tfSkin);
        tfIP.setMessageText("Server IP");

        myTrRegister = new TextureRegion(txtrRegister);
        myTrCancel = new TextureRegion(txtrCancel);
        myTrdRegister = new TextureRegionDrawable(myTrRegister);
        myTrdCancel = new TextureRegionDrawable(myTrCancel);

        ImageButton btnRegister = new ImageButton(myTrdRegister);
        ImageButton btnBack = new ImageButton(myTrdCancel);

        tfName.setPosition(250.f, 450.f, Align.center);
        tfLastName.setPosition(250.f, 400.f, Align.center);
        tfEmail.setPosition(250.f, 350.f, Align.center);
        tfUsername.setPosition(250.f, 300.f, Align.center);
        tfPassword.setPosition(250.f, 250.f, Align.center);
        tfIP.setPosition(250.f, 200.f, Align.center);

        btnRegister.setPosition(375.f, 40.f, Align.center);
        btnBack.setPosition(125.f, 40.f, Align.center);

        addActor(tfName);
        addActor(tfLastName);
        addActor(tfEmail);
        addActor(tfUsername);
        addActor(tfPassword);
        addActor(btnBack);
        addActor(btnRegister);
        addActor(tfIP);


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
                //TODO:
                String name = tfName.getText();
                String lastName = tfLastName.getText();
                String email = tfEmail.getText();
                String username = tfUsername.getText();
                String password = tfPassword.getText();
                String IP = tfIP.getText();

                System.out.println("Empty string: " + name.isEmpty());
                try {
                        ScreenManager.getInstance().setIp(IP);
                        registry =  LocateRegistry.getRegistry(ScreenManager.getInstance().getIp(), ScreenManager.getInstance().getPortNumber());
                        ScreenManager.getInstance().setRegistry(registry);
                        conn = (IConnection) ScreenManager.getInstance().getRegistry().lookup(ScreenManager.getInstance().Getmeaningofconnection());
                        ScreenManager.getInstance().setConn(conn);

                        if (ScreenManager.getInstance().getConn().CreateUser(name, lastName, email, username, password)) {
                            ScreenManager.getInstance().setUser(ScreenManager.getInstance().getConn().LogIn(email, password));
                            ScreenManager.getInstance().showScreen(ScreenEnum.LOBBYLIST);
                        }
                } catch (RemoteException | SQLException e) {
                    e.printStackTrace();
                }catch(NotBoundException ex)
                {
                    System.out.println("NotBoundException: " + ex.getMessage());
                }catch (Exception err)
                {
                    System.out.println("error");
                }
                return false;
            }
        });
    }
}
