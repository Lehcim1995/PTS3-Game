package Interfaces;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by michel on 27-9-2016.
 */
public interface IGameObject extends Remote
{
    Vector2 getPosition() throws RemoteException;

    void setPosition(Vector2 pos) throws RemoteException;

    float getRotation() throws RemoteException;

    void setRotation(float rot) throws RemoteException;

    void onCollisionEnter(IGameObject Other) throws RemoteException;

    void onCollisionExit(IGameObject Other);

    void onCollisionStay(IGameObject Other);

    void update() throws RemoteException;

    void Draw(ShapeRenderer shapeRenderer) throws RemoteException;

    void Draw(ShapeRenderer shapeRenderer, Batch batch) throws RemoteException;

    boolean isHit(IGameObject go2) throws RemoteException;

    ;

    long getID();

    Polygon getHitbox();
}
