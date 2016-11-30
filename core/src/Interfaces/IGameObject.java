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
    Texture getTexture();
    void setTexture(Texture tex);

    Vector2 getPosition() throws RemoteException;
    void setPosition(Vector2 pos) throws RemoteException;

    float getRotation() throws RemoteException;
    void setRotation(float rot) throws RemoteException;

    void onCollisionEnter(IGameObject Other);
    void onCollisionExit(IGameObject Other);
    void onCollisionStay(IGameObject Other);

    void update() throws RemoteException;

    void Draw(ShapeRenderer shapeRenderer)  throws RemoteException;;

    boolean isHit(IGameObject go2)  throws RemoteException;;

    long getID();

    Polygon getHitbox();
}
