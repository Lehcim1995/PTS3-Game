package Classes;

import LibGDXSerialzableClasses.SerializableColor;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.rmi.RemoteException;

/**
 * Created by Jasper on 11-10-2016.
 */
public class LevelBlock extends GameObject
{

    private SerializableColor color = SerializableColor.LIGHT_GRAY;
    private float size = 26;
    private float halfSize = size / 2;

    public LevelBlock(Vector2 position, float rotation, Vector2[] hitbox) throws RemoteException
    {
        super(position, rotation);
        this.setHitbox(hitbox);
    }

    public LevelBlock(Vector2 position, float rotation) throws RemoteException
    {
        super(position, rotation);
        this.setHitbox(VerticisToPolygon(defaultHitbox(size)));
    }

    @Override
    public void Draw(ShapeRenderer sr)
    {
        sr.setColor(color.getLibGDXColor());

        sr.rect(position.x - halfSize, position.y - halfSize, size, size);
    }

    @Override
    public void update()
    {
        hitbox.setPosition(position.x, position.y);
        hitbox.setRotation(-rotation);
    }
}
