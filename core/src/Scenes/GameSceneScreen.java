package Scenes;

import Classes.ASOM;
import Classes.GameManager;
import Interfaces.IGameObject;
import com.asom.mygame.MainClass;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.rmi.RemoteException;
import java.util.Iterator;

/**
 * Created by Nick on 22-11-2016.
 */
public class GameSceneScreen extends AbstractScreen{

    SpriteBatch batch;
    //Texture img;
    private Camera camera;
    private ShapeRenderer shapeRenderer;
    private float zoom = 1;

    public GameSceneScreen()
    {
        super();
    }

    @Override
    public void buildStage() {
        batch = new SpriteBatch();
        //img = new Texture(Gdx.files.internal("core\\assets\\badlogic.jpg"));

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera(w * zoom, h * zoom);
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, zoom);
        camera.update();

        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        try {
            update();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        if (GameManager.getInstance().GetPlayer() != null) {
            camera.position.set(GameManager.getInstance().GetPlayer().GetPosition().x, GameManager.getInstance().GetPlayer().GetPosition().y, 1);
        }
        //camera.rotate(camera. , 0, 0, 1);
        camera.update();

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //batch.setProjectionMatrix(camera.combined);
        //batch.begin();
        //batch.draw(img, position.x, position.y);s
        //batch.end();
        shapeRenderer.setProjectionMatrix(camera.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Iterator<IGameObject> iterator = GameManager.getInstance().getObjects().iterator(); iterator.hasNext(); ) {
            IGameObject go = iterator.next();
            go.Draw(shapeRenderer);
        }

        shapeRenderer.end();

        /*
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);
        for(Classes.GameObject go :  GameManager.getInstance().getObjects())
        {
            shapeRenderer.polygon(go.getHitbox().getTransformedVertices());
        }
        shapeRenderer.end();*/
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
        //img.dispose();
        shapeRenderer.dispose();
    }

    public void update() throws RemoteException {
        GameManager.getInstance().Update();
    }
}
