package Scenes;

import Classes.Chat;
import Classes.GameManager;
import Classes.KillLog;
import Interfaces.IGameObject;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.logging.Level;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

/**
 * Created by Nick on 22-11-2016.
 */
public class GameSceneScreen extends AbstractScreen
{

    SpriteBatch batch;
    SpriteBatch killBatch;

    private Camera camera;
    private ShapeRenderer shapeRenderer;
    private float zoom = 1;
    private BitmapFont font;
//    private BitmapFont klFont;
    private GlyphLayout layout;
    private GlyphLayout killLog;

    private Skin skin;
    private TextField txtChatInput;

    public GameSceneScreen()
    {
        super();
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void buildStage()
    {
        batch = new SpriteBatch();
        killBatch = new SpriteBatch();
        font = new BitmapFont();
//        klFont = new BitmapFont();
        font.setColor(Color.BLACK);
//        klFont.setColor(Color.BLACK);

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera(w * zoom, h * zoom);
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, zoom);
        camera.update();

        shapeRenderer = new ShapeRenderer();
        layout = new GlyphLayout();
        killLog = new GlyphLayout();

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        txtChatInput = new TextField("", skin);
        txtChatInput.setSize(200f, 25f);
        txtChatInput.setPosition(125.f, 85.f, Align.center);

        addActor(txtChatInput);
    }

    @Override
    public void render(float delta)
    {
        super.render(delta);
        try
        {
            if (GameManager.getInstance().IsStopped())
            {
                //ScreenManager.getInstance().showScreen(ScreenEnum.LOBBYLIST);
            }
            update();
        }
        catch (RemoteException e)
        {
            LOGGER.log(Level.WARNING, e.getMessage(), e);
        }

        if (GameManager.getInstance().getScene() == null)
        {
            GameManager.getInstance().setScene(this);
        }

        if (GameManager.getInstance().getPlayer() != null)
        {
            camera.position.set(GameManager.getInstance().getPlayer().getPosition().x, GameManager.getInstance().getPlayer().getPosition().y, 1);
        }
        if (GameManager.getInstance().getSpectator() != null)
        {
            camera.position.set(GameManager.getInstance().getSpectator().getSpectatedPlayer().getPosition().x, GameManager.getInstance().getSpectator().getSpectatedPlayer().getPosition().y, 1);
        }

        camera.update();

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.setProjectionMatrix(camera.combined);

        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.begin();
        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        for (Iterator<IGameObject> iterator = GameManager.getInstance().getAllObjects().iterator(); iterator.hasNext(); )
        {
            IGameObject go = iterator.next();
            try
            {
                go.Draw(shapeRenderer, batch);
                go.Draw(shapeRenderer, killBatch);
            }
            catch (RemoteException e)
            {
                LOGGER.log(Level.WARNING, e.getMessage(), e);
                ScreenManager.getInstance().showScreen(ScreenEnum.LOBBYLIST);
            }
        }
        shapeRenderer.set(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        for (IGameObject go : GameManager.getInstance().getAllObjects())
        {
            shapeRenderer.polygon(go.getHitbox().getTransformedVertices());
        }
        shapeRenderer.end();

        batch.begin();

        font.setColor(Color.RED);
        layout.setText(font, GameManager.getInstance().chat);
        float height = layout.height;
        float padding = 4;
        font.draw(batch, layout, padding, height);

        int i = (int) (height + height);
        float start = (int) (height + height);
        float maxitmes = height * 15;

        for (Iterator<Chat> iterator = GameManager.getInstance().getChats().iterator(); iterator.hasNext(); )
        {
            Chat c = iterator.next();
            i += c.getLayout().height + 3;
            try
            {
                //start is max-items = 100% en start = 0;
                float alpha = 1f - (i / (start + maxitmes));
                c.setTextColor(new Color(0, 0, 0, alpha));
                c.setPosition(new Vector2(padding, i));
                c.DrawChat(batch);
            }
            catch (RemoteException e)
            {
                LOGGER.log(Level.WARNING, e.getMessage(), e);
            }
        }

//        batch.end();

//        killBatch.begin();

//        klFont.setColor(Color.RED);
        killLog.setText(font, GameManager.getInstance().killLog);
        float klHeight = killLog.height;
        float klPadding = 4;
//        klFont.draw(killBatch, killLog, klPadding, klHeight);
        font.draw(batch, killLog, klPadding, klHeight);

        int iKill = (int) (height + height);
        float startKill = (int) (height + height);
        float maxitemsKill = height * 15;

        for (Iterator<KillLog> logs = GameManager.getInstance().getKillLogs().iterator(); logs.hasNext(); )
        {
            KillLog kl = logs.next();
            iKill += kl.getLayout().height + 3;
            try
            {
                //start is max-items = 100% en start = 0;
                float alpha = 1f - (iKill / (startKill + maxitemsKill));
                kl.setTextColor(new Color(0, 0, 0, alpha));
                kl.setPosition(new Vector2(klPadding, iKill));
//                kl.DrawKL(killBatch);
                kl.DrawKL(batch);
            }
            catch (RemoteException e)
            {
                LOGGER.log(Level.WARNING, e.getMessage(), e);
            }
        }
//        killBatch.end();
        batch.end();
    }

    @Override
    public void resize(int width, int height)
    {
        super.resize(width, height);
    }

    @Override
    public void dispose()
    {
        super.dispose();
        batch.dispose();
        killBatch.dispose();
        shapeRenderer.dispose();
        try
        {
            GameManager.getInstance().stop();
        }
        catch (RemoteException e)
        {
            LOGGER.log(Level.WARNING, e.getMessage(), e);
        }
    }

    /**
     * update the Game scene.
     */
    public void update() throws RemoteException
    {
        if (!GameManager.getInstance().IsStopped())
        {
            GameManager.getInstance().Update();
        }
    }

    @Override
    public Camera getCamera()
    {
        return camera;
    }
}
