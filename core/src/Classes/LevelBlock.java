package Classes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Shape;

/**
 * Created by Jasper on 11-10-2016.
 */
public class LevelBlock extends GameObject
{
    public LevelBlock(Texture texture, Vector2 position, float rotation, Shape boundingShape) {
        super (texture, position, rotation, boundingShape);
    }
}
