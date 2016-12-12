package Scenes;

import Classes.GameManager;
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
import com.badlogic.gdx.math.Vector3;

import java.rmi.RemoteException;
import java.util.Iterator;

/**
 * Created by Nick on 22-11-2016.
 */
public class GameSceneScreen extends AbstractScreen{

    SpriteBatch batch;
    private Camera camera;
    private ShapeRenderer shapeRenderer;
    private float zoom = 1;
    private BitmapFont font;
    private GlyphLayout layout;

    public GameSceneScreen()
    {
        super();
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void buildStage() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.BLACK);
        //img = new Texture(Gdx.files.internal("core\\assets\\badlogic.jpg"));

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera(w * zoom, h * zoom);
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, zoom);
        camera.update();

        shapeRenderer = new ShapeRenderer();
        layout = new GlyphLayout();

        //GameManager.getInstance();
        //GameManager.getInstance().setScene(this);
    }



    @Override
    public void render(float delta) {
        super.render(delta);
        try {
            update();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        if (GameManager.getInstance().getScene() == null)
        {
            GameManager.getInstance().setScene(this);
        }

        if (GameManager.getInstance().getPlayer() != null) {
            camera.position.set(GameManager.getInstance().getPlayer().getPosition().x, GameManager.getInstance().getPlayer().getPosition().y, 1);
        }
        //camera.rotate(camera. , 0, 0, 1);
        camera.update();

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //batch.setProjectionMatrix(camera.combined);

        shapeRenderer.setProjectionMatrix(camera.combined);
        
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Iterator<IGameObject> iterator = GameManager.getInstance().getAllObjects().iterator(); iterator.hasNext(); ) {
            IGameObject go = iterator.next();
            try
            {
                go.Draw(shapeRenderer, batch);
            }
            catch (RemoteException e)
            {
                e.printStackTrace();
            }
        }
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        for(IGameObject go :  GameManager.getInstance().getAllObjects())
        {
            shapeRenderer.polygon(go.getHitbox().getTransformedVertices());
        }
        shapeRenderer.end();

        batch.begin();
        if (GameManager.getInstance().getPlayer() != null)
        {
            String text = GameManager.getInstance().getPlayer().getGunEquipped().toString();
            layout.setText(font, text);
            float width = layout.width;// contains the width of the current set text
            float height = layout.height; // contains the height of the current set text
            font.draw(batch, layout, camera.viewportWidth -width, height);
        }
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        camera.viewportHeight = height * zoom;
        camera.viewportWidth = width * zoom;
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        shapeRenderer.dispose();
        GameManager.getInstance().stop();
    }

    public void update() throws RemoteException {
        GameManager.getInstance().Update();
    }

    @Override
    public Camera getCamera()
    {
        return camera;
    }
}
