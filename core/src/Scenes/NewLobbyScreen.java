package Scenes;

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

import java.rmi.RemoteException;

/**
 * Created by Nick on 22-11-2016.
 */
public class NewLobbyScreen extends AbstractScreen{

    private Texture txrBack;
    private Texture txrCreate;
    private TextureRegion createTr;
    private TextureRegion backTr;
    private TextureRegionDrawable createTrd;
    private TextureRegionDrawable backTrd;
    private TextField txtLobbyName;
    private Skin tfSkin;

    public NewLobbyScreen(){
        super();
        txrBack = new Texture(Gdx.files.internal("core\\assets\\btn_back.png"));
        txrCreate = new Texture(Gdx.files.internal("core\\assets\\btn_create.png"));
    }

    @Override
    public void buildStage() {


        tfSkin = new Skin(Gdx.files.internal("uiskin.json"));
        // maak lobbyname textfield
        txtLobbyName = new TextField("",tfSkin);
        txtLobbyName.setMessageText("Enter Lobby Name");
        txtLobbyName.setPosition(260.f,300.f,Align.center);
        //maak create button
        createTr = new TextureRegion(txrCreate);
        createTrd = new TextureRegionDrawable(createTr);
        ImageButton btnCreate = new ImageButton(createTrd);
        btnCreate.setPosition(400.f,30.f,Align.center);
        // maakt back button
        backTr = new TextureRegion(txrBack);
        backTrd = new TextureRegionDrawable(backTr);
        ImageButton btnBack = new ImageButton(backTrd);
        btnBack.setPosition(100.f,30.f, Align.center);
        // Voeg componenten to aan de scene
        addActor(txtLobbyName);
        addActor(btnCreate);
        addActor(btnBack);

        // Ga terug naar het LobbyList scherm
        btnBack.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event,
                                     float x, float y, int pointer, int button) {
                ScreenManager.getInstance().showScreen(ScreenEnum.LOBBYLIST);
                return false;
            }
        });

        //Ga door naar het GameLobby scherm.
        btnCreate.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event,
                                     float x, float y, int pointer, int button) {
                // TODO maak game lobby
                if(!txtLobbyName.getText().isEmpty()){
                    try
                    {
                        ScreenManager.getInstance().getpgm().CreateLobby(txtLobbyName.getText());
                    }
                    catch (RemoteException e)
                    {
                        e.printStackTrace();
                    }
                    ScreenManager.getInstance().showScreen(ScreenEnum.LOBBYLIST);
                }
                else{
                    System.out.println("Geen lobby naam");
                }
                return false;
            }
        });
    }
}
