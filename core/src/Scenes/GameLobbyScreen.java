package Scenes;

import Classes.Player;
import Classes.User;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;

/**
 * Created by Nick on 22-11-2016.
 */
public class GameLobbyScreen extends AbstractScreen{
    private Texture txtrLeave;
    private Texture txtrReady;
    private TextureRegion TrLeave;
    private TextureRegion TrReady;
    private TextureRegionDrawable TrdLeave;
    private TextureRegionDrawable TrdReady;
    private List listPlayers;
    private List listChat;
    private Skin skin;
    private ScrollPane scrollPanePlayer;
    private ScrollPane scrollPaneChat;
    private TextField txtChatInput;

    public GameLobbyScreen()
    {
        super();
        txtrLeave = new Texture(Gdx.files.internal("core\\assets\\btn_leave.png"));
        txtrReady = new Texture(Gdx.files.internal("core\\assets\\btn_ready.png"));
    }

    @Override
    public void buildStage() {
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        //Button Leave
        TrLeave = new TextureRegion(txtrLeave);
        TrdLeave = new TextureRegionDrawable(TrLeave);
        ImageButton btnLeave = new ImageButton(TrdLeave);
        btnLeave.setPosition(125.f, 40.f, Align.center);

        //Button Ready
        TrReady = new TextureRegion(txtrReady);
        TrdReady = new TextureRegionDrawable(TrReady);
        ImageButton btnReady = new ImageButton(TrdReady);
        btnReady.setPosition(375.f, 40.f, Align.center);

        //List Players
        listPlayers = new List(skin);
        listPlayers.setItems("asd","qwe","zxc","fdg","Sibe","Myron","asdasdasd");
        scrollPanePlayer = new ScrollPane(listPlayers,skin);
        scrollPanePlayer.setSize(200.f, 150.f);
        scrollPanePlayer.setPosition(25.f, 350.f);

        //List Chat
        listChat = new List(skin);
        listChat.setItems("asd","qwe","zxc","fdg","Sibe","Myron","asdasdasd asd sad sa  d as dsa sd sad da s ads das dsa sad dsa");
        scrollPaneChat = new ScrollPane(listChat,skin);
        scrollPaneChat.setSize(200.f, 225.f);
        scrollPaneChat.setPosition(25.f, 110.f);
        scrollPaneChat.isScrollingDisabledX();

        //ChatBoxInput
        txtChatInput = new TextField("",skin);
        txtChatInput.setSize(200f, 25f);
        txtChatInput.setPosition(125.f,85.f, Align.center);

        //Add all items to scene
        addActor(btnLeave);
        addActor(btnReady);
        addActor(scrollPanePlayer);
        addActor(scrollPaneChat);
        addActor(txtChatInput);

        btnLeave.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event,
                                     float x, float y, int pointer, int button) {
                ScreenManager.getInstance().showScreen(ScreenEnum.LOBBYLIST);
                return false;
            }
        });

        btnReady.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event,
                                     float x, float y, int pointer, int button) {
                //todo set player to ready
                System.out.println("Player X is ready");
                //todo only when all players are ready
                ScreenManager.getInstance().showScreen(ScreenEnum.GAMESCENE);
                return false;
            }
        });
        //TODO Check if enter pressed then push text in message to server and add to chatlist
        /*
        txtChatInput.setTextFieldListener(new TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char key) {
                if ((key == '\r' || key == '\n')){
                    textField.next(Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT));
                }
            }
        });
*/
    }
    public void addNewMessage(String message, User user){
        //TODO: add message to the list of messages (may need to update the scrollpane)
    }
    public void addNewUser(User user){
        //TODO: add user to the list of users in the lobby (may need to update the scrollpane)
    }
}
