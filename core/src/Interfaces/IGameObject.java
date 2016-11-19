package Interfaces;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Shape;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by michel on 27-9-2016.
 */
public interface IGameObject extends Remote
{
    Texture GetTexture();
    void SetTexture(Texture tex);

    Vector2 GetPosition();
    void SetPosition(Vector2 pos);

    float GetRotation();
    void SetRotation(float rot);

    void OnCollisionEnter(IGameObject Other);
    void OnCollisionExit(IGameObject Other);
    void OnCollisionStay(IGameObject Other);

    void Update() throws RemoteException;

    void Draw(ShapeRenderer shapeRenderer);

    boolean isHit(IGameObject go2);

    Polygon getHitbox();
}
