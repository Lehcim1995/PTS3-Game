package Classes;

import Interfaces.IGameObject;
import LibGDXSerialzableClasses.SerializablePolygon;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Shape;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Random;


/**
 * Created by michel on 27-9-2016.
 */
public class GameObject implements IGameObject, Serializable
{

    protected Texture texture;
    protected Sprite sprite;

    protected Vector2 position;
    protected float rotation;
    protected Shape boundingShape;
    protected SerializablePolygon hitbox;
    protected long id;

    protected GameObject() throws RemoteException
    {
        id = this.hashCode();
    }

    protected GameObject(Vector2 position, float rotation, SerializablePolygon hitbox) throws RemoteException
    {
        super();
        this.position = position;
        this.rotation = rotation;
        this.hitbox = hitbox;
    }

    protected GameObject(Texture texture, Vector2 position, float rotation) throws RemoteException
    {
        super();
        this.texture = texture;
        this.position = position;
        this.rotation = rotation;
    }

    protected GameObject(Vector2 position, float rotation) throws RemoteException
    {
        super();
        this.position = position;
        this.rotation = rotation;
        hitbox = new SerializablePolygon();
    }

    public Polygon getHitbox()
    {
        return hitbox.getLIBGDXPolygon();
    }

    public final static Vector2[] DEFAULTHITBOX(float size)
    {
        Vector2 v1 = new Vector2(size,size);
        Vector2 v2 = new Vector2(-size,size);
        Vector2 v3 = new Vector2(size,-size);
        Vector2 v4 = new Vector2(-size,-size);
        Vector2[] v5 = {v1,v2,v4,v3};

        return v5;
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
        float y = height/2;
        float x = width/2;

        Vector2 v1 = new Vector2(x,y);
        Vector2 v2 = new Vector2(-x,y);
        Vector2 v3 = new Vector2(x,-y);
        Vector2 v4 = new Vector2(-x,-y);

        Vector2[] v5 = {v1,v2,v4,v3};

        return v5;
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

    public void setHitbox(SerializablePolygon hitbox)
    {
        this.hitbox = hitbox;
    }

    public void setOrigin(Vector2 origin)
    {
        hitbox.setOrigin(origin.x, origin.y);
    }

    public Shape getBoundingShape()
    {
        return boundingShape;
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
            if ((((verList.get(i).y <= y) && (y < verList.get(j).y))
                    || ((verList.get(j).y <= y) && (y < verList.get(i).y)))
                    && (x < (verList.get(j).x - verList.get(i).x) * (y - verList.get(i).y) / (verList.get(j).y - verList.get(i).y) + verList.get(i).x))

                c = !c;
        }
        return c;
    }

    @Override
    public Texture GetTexture()
    {
        return texture;
    }

    @Override
    public void SetTexture(Texture tex)
    {
        texture = tex;
    }

    @Override
    public Vector2 GetPosition()
    {
        return position;
    }

    public Vector2 GetScreenPosition()
    {
        return new Vector2(/*position.x*/ Gdx.app.getGraphics().getWidth()/2, Gdx.app.getGraphics().getHeight()/2/* - position.y*/);
    }

    @Override
    public void SetPosition(Vector2 pos)
    {
        position = pos;
        hitbox.setPosition(pos.x, pos.y);
    }

    @Override
    public float GetRotation()
    {
        return rotation;
    }

    @Override
    public void SetRotation(float rot)
    {
        rotation = rot;
        hitbox.setRotation(rot);
    }

    @Override
    public void OnCollisionEnter(IGameObject other)
    {

    }

    @Override
    public void OnCollisionExit(IGameObject other)
    {

    }

    @Override
    public void OnCollisionStay(IGameObject other)
    {

    }

    @Override
    public void Update() throws RemoteException
    {

    }

    @Override
    public void Draw(ShapeRenderer shapeRenderer)
    {

    }
}
