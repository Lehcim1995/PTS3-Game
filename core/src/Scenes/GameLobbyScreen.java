package Scenes;

import Classes.Chat;
import Classes.GameManager;
import Classes.Player;
import Classes.User;
import Interfaces.IGameManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

import java.rmi.RemoteException;
import java.util.ArrayList;

import static com.badlogic.gdx.graphics.g3d.particles.ParticleChannels.Color;

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
    private ArrayList<Chat> chats = new ArrayList<>();
    private BitmapFont font = new BitmapFont();
    private GlyphLayout layout = new GlyphLayout();
    private Batch bc = new SpriteBatch();

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
        try
        {
            listPlayers.setItems(ScreenManager.getInstance().getGameManager().getUsers().toArray());
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        scrollPanePlayer = new ScrollPane(listPlayers,skin);
        scrollPanePlayer.setSize(200.f, 150.f);
        scrollPanePlayer.setPosition(25.f, 350.f);

        //List Chat
        listChat = new List(skin);
        listChat.setItems("asd","qwe","zxc","fdg","Sibe","Myron");
        Table textAreaHolder = new Table();
        textAreaHolder.debug();
        scrollPaneChat = new ScrollPane(textAreaHolder);
        scrollPaneChat.setForceScroll(false, true);
        scrollPaneChat.setFlickScroll(true);
        scrollPaneChat.setOverscroll(false, false);
        scrollPaneChat.setSize(200.f, 150.f);
        scrollPaneChat.setPosition(25.f, 110.f);
        //final Table table = new Table();
       // table.setFillParent(true);
        //table.add(scrollPaneChat).fill();
        //scrollPaneChat.setForceScroll(false, true);
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

        txtChatInput.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char key) {
                if ((key == '\r' || key == '\n')){
                    textField.next(Gdx.input.isKeyPressed(Input.Keys.ENTER));
                    listChat.setItems(listChat.getItems().toString(System.lineSeparator()) + System.lineSeparator() + textField.getText());
//                    if (font != null && layout != null)
//                    {
//                        bc.begin();
//                        font.setColor(com.badlogic.gdx.graphics.Color.BLACK);
//                        String text = txtChatInput.getText();
//                        layout.setText(font, text);
//                        float width = layout.width;// contains the width of the current set text
//                        float height = layout.height; // contains the height of the current set text
//                        font.draw(bc, layout, 0f, 0f);
//                        bc.end();
//                    }
                    textField.setText("");
                }
            }
        });

    }
    public void addNewMessage(String message, User user){
        //TODO: add message to the list of messages (may need to update the scrollpane)
    }
    public void addNewUser(User user){
        //TODO: add user to the list of users in the lobby (may need to update the scrollpane)
    }
}
