package Interfaces;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Shape;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by michel on 27-9-2016.
 */
public interface IGameObject
{
    Texture GetTexture();
    void SetTexture(Texture tex);

    Vector2 GetPosition();
    void SetPosition(Vector2 pos);

    float GetRotation();
    void SetRotation(float rot);

    Shape GetBoundingShape();
    void SetBoundingShape(Shape shape);

    void OnCollisionEnter(IGameObject Other);
    void OnCollisionExit(IGameObject Other);
    void OnCollisionStay(IGameObject Other);

    void Update() throws RemoteException;

    void Draw();
}
