package Classes;

import Interfaces.IGameObject;
import LibGDXSerialzableClasses.SerializablePolygon;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

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
        position = new Vector2();
        rotation = 0;
        hitbox = VerticisToPolygon(CircleHitbox(5));
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
        setHitbox(DefaultHitbox(10));
    }

    public static final Vector2[] DefaultHitbox(float size)
    {
        return DefaultHitbox(size, size);
    }

    public static final Vector2[] CircleHitbox(float size)
    {
        return CircleHitbox(size, 18);
    }

    public static final Vector2[] CircleHitbox(float size, int vertices)
    {
        Vector2[] vList = new Vector2[vertices];
        double rad = Math.toRadians((double)360 / vertices);

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

    public static final Vector2[] DefaultHitbox(float height, float width)
    {
        float y = height / 2;
        float x = width / 2;

        Vector2 v1 = new Vector2(x, y);
        Vector2 v2 = new Vector2(-x, y);
        Vector2 v3 = new Vector2(x, -y);
        Vector2 v4 = new Vector2(-x, -y);

        return new Vector2[] {v1, v2, v4, v3};
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
        hitbox = VerticisToPolygon(verticis);
        hitbox.setOrigin(0, 0);
    }
    /**
     * Converts Vector2 Array to SerializablePolygon
     *
     * @param vertices Vector2 vertices array
     * @return Polygon
     */
    public SerializablePolygon VerticisToPolygon(Vector2[] vertices)
    {
        if (vertices.length < 3)
        {
            throw new IllegalArgumentException("Need atleast 3 or more verticies");
        }

        float[] verticisList = new float[vertices.length * 2];

        for (int i = 0, j = 0; i < vertices.length; i++, j += 2)
        {
            verticisList[j] = vertices[i].x;
            verticisList[j + 1] = vertices[i].y;
        }

        return new SerializablePolygon(verticisList);
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

        if (dis > 100)
            return false;
        return isOverlap(hitbox.getLIBGDXPolygon(), go.getHitbox());
    }

    @Override
    public long getID()
    {
        return id;
    }


    /**
     * Static Methode to check if 2 polygons are intersecting
     *
     * @param A Polygon
     * @param B Polygon
     * @return Is overlapping or not.
     */
    public boolean isOverlap(Polygon A, Polygon B)
    {
        float[] verticesA = A.getTransformedVertices();
        float[] verticesB = B.getTransformedVertices();
        //Check vertices B
        for (int i = 0; i < verticesB.length; i += 2)
        {
            float x = verticesB[i];
            float y = verticesB[i + 1];

            if (isInside(x, y, A))
                return true;
        }
        //Check vertices A
        for (int i = 0; i < verticesA.length; i += 2)
        {
            float x = verticesA[i];
            float y = verticesA[i + 1];

            if (isInside(x, y, B))
                return true;
        }
        return false;
    }

    private boolean isInside(float x, float y, Polygon p)
    {
        int i;
        int j;
        boolean c = false;

        ArrayList<Vector2> verList = new ArrayList<>();

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
    public void onCollisionEnter(IGameObject other) throws RemoteException
    {
        // Has to have a Override.
    }

    @Override
    public void onCollisionExit(IGameObject other)
    {
        // Has to have a Override.
    }

    @Override
    public void onCollisionStay(IGameObject other)
    {
        // Has to have a Override.
    }

    @Override
    public void update() throws RemoteException
    {
        // Has to have a Override.
    }

    @Override
    public void Draw(ShapeRenderer shapeRenderer)
    {
        // Has to have a Override.
    }
    /**
     * Draw objects
     *
     * @param shapeRenderer Shape
     * @param batch Batch
     */
    public void Draw(ShapeRenderer shapeRenderer, Batch batch)
    {
        Draw(shapeRenderer);
    }
    /**
     * DrawText on screen
     *
     * @param batch
     * @param font Font used
     * @param layout Layout used
     * @param text Text to draw
     * @param position Position to draw
     */
    public void DrawText(Batch batch, BitmapFont font, GlyphLayout layout, String text, Vector2 position)
    {
        //font.setColor(Color.BLACK);
        layout.setText(font, text);
        float width = layout.width;// contains the width of the current set text
        float height = layout.height; // contains the height of the current set text
        font.draw(batch, layout, position.x, position.y);
    }
}
