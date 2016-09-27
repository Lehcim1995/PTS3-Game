package Classes;

import Interfaces.IGameObject;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Shape;

/**
 * Created by michel on 27-9-2016.
 */
abstract class GameObject implements IGameObject
{

    protected Texture texture;
    protected Vector2 position;
    protected float rotation;
    protected Shape boundingShape;

    public GameObject(Texture texture, Vector2 position, float rotation, Shape boundingShape)
    {
        this.texture = texture;
        this.position = position;
        this.rotation = rotation;
        this.boundingShape = boundingShape;
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
    public void OnCollisionEnter()
    {

    }

    @Override
    public void OnCollisionExit()
    {

    }

    @Override
    public void OnCollisionStay()
    {

    }

    @Override
    public void Update()
    {

    }
}
