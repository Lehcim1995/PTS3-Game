package Scenes;

import Classes.Chat;
import Classes.GameManager;
import Classes.User;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Nick on 22-11-2016.
 */
public class GameLobbyScreen extends AbstractScreen
{
    private Texture txtrLeave;
    private Texture txtrReady;
    private Texture txtrSpec;
    private Texture txtrRefresh;
    private TextureRegion trLeave;
    private TextureRegion trReady;
    private TextureRegion trSpec;
    private TextureRegion trRefresh;
    private TextureRegionDrawable trdLeave;
    private TextureRegionDrawable trdReady;
    private TextureRegionDrawable trdSpec;
    private TextureRegionDrawable trdRefresh;
    private List listPlayers;
    private List listChat;
    private Skin skin;
    private ScrollPane scrollPanePlayer;
    private ScrollPane scrollPaneChat;
    private TextField txtChatInput;
    private ArrayList<Chat> chats = new ArrayList<>();
    private BitmapFont font = new BitmapFont();
    private GlyphLayout layout = new GlyphLayout();
    private Batch bc = new SpriteBatch();
    /**
     * Screen Constructor
     *
     */
    public GameLobbyScreen()
    {
        super();
        txtrLeave = new Texture(Gdx.files.internal("btn_leave.png"));
        txtrReady = new Texture(Gdx.files.internal("btn_ready.png"));
        txtrSpec = new Texture(Gdx.files.internal("btn_spectate.png"));
        txtrRefresh = new Texture(Gdx.files.internal("btn_refresh.png"));
    }

    @Override
    public void buildStage()
    {
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        //Button Leave
        trLeave = new TextureRegion(txtrLeave);
        trdLeave = new TextureRegionDrawable(trLeave);
        ImageButton btnLeave = new ImageButton(trdLeave);
        btnLeave.setPosition(125.f, 40.f, Align.center);

        //Button Ready
        trReady = new TextureRegion(txtrReady);
        trdReady = new TextureRegionDrawable(trReady);
        ImageButton btnReady = new ImageButton(trdReady);
        btnReady.setPosition(375.f, 40.f, Align.center);

        //Spectate button
        trSpec = new TextureRegion(txtrSpec);
        trdSpec = new TextureRegionDrawable(trSpec);
        ImageButton btnSpectate = new ImageButton(trdSpec);
        btnSpectate.setPosition(375.f, 475.f, Align.center);

        //refresh
        trRefresh = new TextureRegion(txtrRefresh);
        trdRefresh = new TextureRegionDrawable(trRefresh);
        ImageButton btnRefresh = new ImageButton(trdRefresh);
        btnRefresh.setPosition(375.f, 400.f, Align.center);

        //List Players
        listPlayers = new List(skin);
        try
        {
            ///TODO: FIX Nullpointerexception
            listPlayers.setItems(ScreenManager.getInstance().getGameManager().getUsers().toArray());
        }
        catch (RemoteException e)
        {
            Logger.getAnonymousLogger().log(Level.SEVERE, "SetPlayers error: " + e);
        }
        scrollPanePlayer = new ScrollPane(listPlayers, skin);
        scrollPanePlayer.setSize(200.f, 150.f);
        scrollPanePlayer.setPosition(25.f, 350.f);

        //List Chat
        listChat = new List(skin);
        listChat.setItems("asd", "qwe", "zxc", "fdg", "Sibe", "Myron");
        Table textAreaHolder = new Table();
        textAreaHolder.debug();
        //ScrollPanel
        scrollPaneChat = new ScrollPane(textAreaHolder);
        scrollPaneChat.setForceScroll(false, true);
        scrollPaneChat.setFlickScroll(true);
        scrollPaneChat.setOverscroll(false, false);
        scrollPaneChat.setSize(200.f, 150.f);
        scrollPaneChat.setPosition(25.f, 110.f);
        //Chat
        txtChatInput = new TextField("", skin);
        txtChatInput.setSize(200f, 25f);
        txtChatInput.setPosition(125.f, 85.f, Align.center);

        //Add all items to scene
        addActor(btnLeave);
        addActor(btnReady);
        addActor(btnSpectate);
        addActor(btnRefresh);
        addActor(scrollPanePlayer);
        addActor(scrollPaneChat);
        addActor(txtChatInput);

        btnLeave.addListener(new InputListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                try {
                    GameManager.getInstance().stop();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                ScreenManager.getInstance().showScreen(ScreenEnum.LOBBYLIST);
                return false;
            }
        });

        btnReady.addListener(new InputListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                //todo set player to ready
                ScreenManager.getInstance().setIsSpectator(false);
                Logger.getAnonymousLogger().log(Level.INFO, "A player is ready.");
                //todo only when all players are ready
                ScreenManager.getInstance().showScreen(ScreenEnum.GAMESCENE);
                try
                {
                    GameManager.getInstance().StartMatch();
                }
                catch (RemoteException e)
                {
                    e.printStackTrace();
                }
                return false;
            }
        });


        btnSpectate.addListener(new InputListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {

                if (!ScreenManager.getInstance().getIsSpectator())
                {
                    Logger.getAnonymousLogger().log(Level.INFO, "A player is spectating.");
                    ScreenManager.getInstance().setIsSpectator(true);
                    ScreenManager.getInstance().showScreen(ScreenEnum.GAMESCENE);
                }
                else
                {
                    Logger.getAnonymousLogger().log(Level.INFO, "A player is no longer spectating.");
                    ScreenManager.getInstance().setIsSpectator(false);
                }

                return false;
            }

        });

        btnRefresh.addListener(new InputListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                ScreenManager.getInstance().showScreen(ScreenEnum.GAMELOBBY);
                return false;
            }
        });

        //TODO Check if enter pressed then push text in message to server and add to chatlist

        txtChatInput.setTextFieldListener(new TextField.TextFieldListener()
        {
            @Override
            public void keyTyped(TextField textField, char key)
            {
                if (key == '\r' || key == '\n')
                {
                    textField.next(Gdx.input.isKeyPressed(Input.Keys.ENTER));
                    listChat.setItems(listChat.getItems().toString(System.lineSeparator()) + System.lineSeparator() + textField.getText());
                    textField.setText("");
                }
            }
        });

    }
    /**
     * New message by a player
     *
     * @param message    - Text message send by player
     * @param user    - User that send the message
     */
    public void addNewMessage(String message, User user)
    {
        //TODO: add message to the list of messages (may need to update the scrollpane)
    }
    /**
     * New user in lobby
     *
     * @param user    - The user that is new
     */
    public void addNewUser(User user)
    {
        //TODO: add user to the list of users in the lobby (may need to update the scrollpane)
    }
}
