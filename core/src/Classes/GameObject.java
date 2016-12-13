package Classes;

import Interfaces.IGameObject;
import LibGDXSerialzableClasses.SerializablePolygon;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Shape;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;


/**
 * Created by michel on 27-9-2016.
 */
public class GameObject implements IGameObject, Serializable
{
    protected Vector2 position;
    protected float rotation;
    protected SerializablePolygon hitbox;
    protected long id;

    protected GameObject() throws RemoteException
    {
        id = this.hashCode();
    }

    protected GameObject(Vector2 position, float rotation, SerializablePolygon hitbox) throws RemoteException
    {
        id = this.hashCode();
        this.position = position;
        this.rotation = rotation;
        this.hitbox = hitbox;
    }

    protected GameObject(Vector2 position, float rotation) throws RemoteException
    {
        id = this.hashCode();
        this.position = position;
        this.rotation = rotation;
        setHitbox(DEFAULTHITBOX(10));
    }

    public final static Vector2[] DEFAULTHITBOX(float size)
    {
        return DEFAULTHITBOX(size, size);
    }

    public final static Vector2[] CIRCLEHITBOX(float size)
    {
        return CIRCLEHITBOX(size, 18);
    }

    public final static Vector2[] CIRCLEHITBOX(float size, int vertices)
    {
        Vector2[] vList = new Vector2[vertices];
        double rad = Math.toRadians(360 / vertices);

        for (int i = 0; i < vertices; i++)
        {
            float x = MathUtils.sin((float) (rad * i));
            float y = MathUtils.cos((float) (rad * i));
            Vector2 v = new Vector2(x, y);
            v = v.scl(size);
            vList[i] = v;
        }

        return vList;
    }

    public final static Vector2[] DEFAULTHITBOX(float height, float width)
    {
        float y = height / 2;
        float x = width / 2;

        Vector2 v1 = new Vector2(x, y);
        Vector2 v2 = new Vector2(-x, y);
        Vector2 v3 = new Vector2(x, -y);
        Vector2 v4 = new Vector2(-x, -y);

        Vector2[] v5 = {v1, v2, v4, v3};

        return v5;
    }

    public Polygon getHitbox()
    {
        return hitbox.getLIBGDXPolygon();
    }

    public void setHitbox(SerializablePolygon hitbox)
    {
        this.hitbox = hitbox;
    }

    public void setHitbox(Vector2[] verticis)
    {
        if (verticis.length < 3)
        {
            throw new IllegalArgumentException("Need atleast 3 or more verticies");
        }

        hitbox = new SerializablePolygon();

        float[] verticisList = new float[verticis.length * 2];

        for (int i = 0, j = 0; i < verticis.length; i++, j += 2)
        {
            verticisList[j] = verticis[i].x;
            verticisList[j + 1] = verticis[i].y;
        }

        hitbox.setOrigin(0, 0);
        hitbox.setVertices(verticisList);
    }

    public void setOrigin(Vector2 origin)
    {
        hitbox.setOrigin(origin.x, origin.y);
    }

    @Override
    public boolean isHit(IGameObject go) throws RemoteException
    {
        Vector2 other = new Vector2();
        other.x = go.getHitbox().getX();
        other.y = go.getHitbox().getY();

        float dis = position.dst(other);

        if (dis > 100) return false;
        return isOverlap(hitbox.getLIBGDXPolygon(), go.getHitbox());
    }

    @Override
    public long getID()
    {
        return id;
    }

    //Static Methode to check if 2 polygons are intersecting
    public boolean isOverlap(Polygon A, Polygon B)
    {
        float[] VerticesA = A.getTransformedVertices();
        float[] VerticesB = B.getTransformedVertices();

        for (int i = 0; i < VerticesB.length; i += 2)
        {
            float x = VerticesB[i];
            float y = VerticesB[i + 1];

            if (isInside(x, y, A)) return true;
        }

        for (int i = 0; i < VerticesA.length; i += 2)
        {
            float x = VerticesA[i];
            float y = VerticesA[i + 1];

            if (isInside(x, y, B)) return true;
        }
        return false;
    }

    private boolean isInside(float x, float y, Polygon p)
    {
        int i, j;
        boolean c = false;

        ArrayList<Vector2> verList = new ArrayList<Vector2>();

        for (int ver = 0; ver < p.getTransformedVertices().length; ver += 2)
        {
            Vector2 v = new Vector2();
            v.x = p.getTransformedVertices()[ver];
            v.y = p.getTransformedVertices()[ver + 1];

            verList.add(v);
        }

        int verCount = verList.size();

        for (i = 0, j = verCount - 1; i < verCount; j = i++)
        {
            if ((((verList.get(i).y <= y) && (y < verList.get(j).y)) || ((verList.get(j).y <= y) && (y < verList.get(i).y))) && (x < (verList.get(j).x - verList.get(i).x) * (y - verList.get(i).y) / (verList.get(j).y - verList.get(i).y) + verList.get(i).x))
                c = !c;
        }
        return c;
    }

    @Override
    public Vector2 getPosition()
    {
        return position;
    }

    @Override
    public void setPosition(Vector2 pos)
    {
        position = pos;
        hitbox.setPosition(pos.x, pos.y);
    }

    public Vector2 getScreenPosition()
    {

        if (GameManager.getInstance().getScene().getCamera() == null)
        {
            return new Vector2(Gdx.app.getGraphics().getWidth() / 2, Gdx.app.getGraphics().getHeight() / 2);
        }
        Vector3 v3 = GameManager.getInstance().getScene().getCamera().project(new Vector3(position.x, position.y, 0));

        return new Vector2(v3.x, v3.y);
    }

    @Override
    public float getRotation()
    {
        return rotation;
    }

    @Override
    public void setRotation(float rot)
    {
        rotation = rot;
        hitbox.setRotation(rot);
    }

    @Override
    public void onCollisionEnter(IGameObject other)
    {

    }

    @Override
    public void onCollisionExit(IGameObject other)
    {

    }

    @Override
    public void onCollisionStay(IGameObject other)
    {

    }

    @Override
    public void update() throws RemoteException
    {

    }

    @Override
    public void Draw(ShapeRenderer shapeRenderer)
    {

    }

    public void Draw(ShapeRenderer shapeRenderer, Batch batch)
    {
        Draw(shapeRenderer);
    }
}
