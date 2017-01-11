package Scenes;


import Interfaces.IConnection;
import Interfaces.IUser;
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
import java.util.logging.Level;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;


/**
 * Created by Nick on 22-11-2016.
 */
public class LoginScreen extends AbstractScreen
{

    private Texture txtrLogin;
    private Texture txtrRegister;
    private TextureRegion myTextureRegion;
    private TextureRegion registerTextureregion;
    private TextureRegionDrawable myTrd;
    private TextureRegionDrawable registerTrd;
    private TextField txtUserName;
    private TextField txtPassword;
    private TextField txtIP;
    private Skin tfSkin;
    private IUser user;

    /**
     * LoginScreen Constructor
     */
    public LoginScreen()
    {
        super();
        txtrLogin = new Texture(Gdx.files.internal("core\\assets\\btn_login.png"));
        txtrRegister = new Texture(Gdx.files.internal("core\\assets\\btn_register.png"));
    }

    @Override
    public void buildStage()
    {

        // set textfield skin
        tfSkin = new Skin(Gdx.files.internal("uiskin.json"));
        // maak username textfield
        txtUserName = new TextField("", tfSkin);
        txtUserName.setMessageText("EMAIL");
        txtUserName.setPosition(260.f, 300.f, Align.center);
        // maak password textfield
        txtPassword = new TextField("", tfSkin);
        txtPassword.setPasswordMode(true);
        txtPassword.setMessageText("Password");
        txtPassword.setPosition(260.f, 250.f, Align.center);
        //maak ip textfield
        txtIP = new TextField("", tfSkin);
        txtIP.setMessageText("Server IP");
        txtIP.setPosition(260.f, 200.f, Align.center);
        // maak login button
        myTextureRegion = new TextureRegion(txtrLogin);
        myTrd = new TextureRegionDrawable(myTextureRegion);
        ImageButton btnLogin = new ImageButton(myTrd);
        btnLogin.setPosition(260.f, 150.f, Align.center);
        // maak registrate button
        registerTextureregion = new TextureRegion(txtrRegister);
        registerTrd = new TextureRegionDrawable(registerTextureregion);
        ImageButton btnRegister = new ImageButton(registerTrd);
        btnRegister.setPosition(260.f, 90.f, Align.center);
        // voeg componenten toe aan de scene
        addActor(txtUserName);
        addActor(txtPassword);
        addActor(btnLogin);
        addActor(btnRegister);
        addActor(txtIP);

        btnLogin.addListener(new InputListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                String email = txtUserName.getText();
                String ww = txtPassword.getText();
                String ip = txtIP.getText();
                if (!txtUserName.getText().isEmpty() && !txtPassword.getText().isEmpty() && !txtIP.getText().isEmpty())
                {
                    try
                    {
                        try
                        {
                            ScreenManager.getInstance().setIp(ip);
                            registry = LocateRegistry.getRegistry(ScreenManager.getInstance().getIp(), ScreenManager.getInstance().getPortNumber());
                            ScreenManager.getInstance().setRegistry(registry);
                            conn = (IConnection) ScreenManager.getInstance().getRegistry().lookup(ScreenManager.getInstance().Getmeaningofconnection());
                            ScreenManager.getInstance().setConn(conn);
                        }
                        catch (RemoteException e)
                        {
                            LOGGER.log(Level.WARNING, "RemoteException: " + e.getMessage(), e);
                        }
                        catch (NotBoundException e)
                        {
                            LOGGER.log(Level.WARNING, "NotBoundException: " + e.getMessage(), e);
                        }
                        if (conn != null)
                        {
                            user = ScreenManager.getInstance().getConn().LogIn(email, ww);
                        }
                        else
                        {
                            LOGGER.log(Level.INFO, "kon registry niet juist ophalen");
                        }
                        if (user != null)
                        {
                            ScreenManager.getInstance().setUser(user);
                            ScreenManager.getInstance().showScreen(ScreenEnum.LOBBYLIST);
                        }
                        else
                        {
                            LOGGER.log(Level.INFO, "Niet geregistreerd!");
                        }
                    }
                    catch (Exception e)
                    {
                        LOGGER.log(Level.WARNING, "Error: lege strings" + e.getMessage(), e);
                    }
                }
                return false;
            }
        });

        btnRegister.addListener(new InputListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                LOGGER.log(Level.INFO, "Registerbutton klicked");
                ScreenManager.getInstance().showScreen(ScreenEnum.REGISTER);
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
