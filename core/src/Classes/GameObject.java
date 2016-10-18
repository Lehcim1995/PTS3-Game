package Classes;

import Interfaces.IGameObject;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Shape;

/**
 * Created by michel on 27-9-2016.
 */
abstract class GameObject implements IGameObject
{

    protected Texture texture;
    protected Sprite sprite;

    protected Vector2 position;
    protected float rotation;
    protected Shape boundingShape;

    private Polygon hitbox;

    protected GameObject ()
    {

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
    }

    public Shape getBoundingShape()
    {
        return boundingShape;
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

    public Vector2 GetScreenPosition() { return new Vector2(position.x, Gdx.app.getGraphics().getHeight() - position.y ); }

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
}
