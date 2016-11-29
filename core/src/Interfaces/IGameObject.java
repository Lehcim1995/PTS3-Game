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

    Vector2 GetPosition() throws RemoteException;
    void SetPosition(Vector2 pos) throws RemoteException;

    float GetRotation() throws RemoteException;
    void SetRotation(float rot) throws RemoteException;

    void OnCollisionEnter(IGameObject Other);
    void OnCollisionExit(IGameObject Other);
    void OnCollisionStay(IGameObject Other);

    void Update() throws RemoteException;

    void Draw(ShapeRenderer shapeRenderer)  throws RemoteException;;

    boolean isHit(IGameObject go2)  throws RemoteException;;

    long getID();

    Polygon getHitbox();
}
