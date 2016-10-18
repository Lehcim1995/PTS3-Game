package Classes;

import Interfaces.IGameObject;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Shape;

import java.util.ArrayList;

/**
 * Created by michel on 27-9-2016.
 */
public class GameObject implements IGameObject
{

    protected Texture texture;
    protected Sprite sprite;

    protected Vector2 position;
    protected float rotation;
    protected Shape boundingShape;
    protected Polygon hitbox;

    protected GameObject()
    {

    }
    protected GameObject(Vector2 position, float rotation, Polygon hitbox)
    {
        this.position = position;
        this.rotation = rotation;
        this.hitbox = hitbox;
    }
    protected GameObject(Texture texture, Vector2 position, float rotation, Shape boundingShape)
    {
        this.texture = texture;
        this.position = position;
        this.rotation = rotation;
        this.boundingShape = boundingShape;
    }

    protected GameObject(Vector2 position, float rotation, Shape boundingShape)
    {
        this.position = position;
        this.rotation = rotation;
        this.boundingShape = boundingShape;
    }

    public GameObject(Vector2 position, float rotation)
    {
        this.position = position;
        this.rotation = rotation;
        hitbox = new Polygon();
    }

    public Polygon getHitbox()
    {
        return hitbox;
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

        hitbox = new Polygon();

        float[] verticisList = new float[verticis.length * 2];

        for (int i = 0, j = 0; i < verticis.length; i++, j += 2)
        {
            verticisList[j] = verticis[i].x;
            verticisList[j + 1] = verticis[i].y;
        }

        hitbox.setOrigin(0, 0);
        hitbox.setVertices(verticisList);
    }

    public void setHitbox(Polygon hitbox)
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

    public boolean isHit(GameObject go)
    {
        return isOverlap(hitbox, go.getHitbox());
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
        //you can choose either Crossing Number or Winding Numer, you can google implementation
        //return p.contains(x,y);

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
        return new Vector2(position.x, Gdx.app.getGraphics().getHeight() - position.y);
    }

    @Override
    public void SetPosition(Vector2 pos)
    {
        position = pos;
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
    }

    @Override
    public Shape GetBoundingShape()
    {
        return boundingShape;
    }

    @Override
    public void SetBoundingShape(Shape shape)
    {
        boundingShape = shape;
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

    public void Update()
    {

    }

    public void Draw()
    {

    }

    public void Draw(ShapeRenderer sr)
    {

    }
}
