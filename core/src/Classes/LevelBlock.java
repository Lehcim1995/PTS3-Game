package Classes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Shape;

/**
 * Created by Jasper on 11-10-2016.
 */
public class LevelBlock extends GameObject
{
    Vector2[] vectorHitbox;
    private Color color = Color.BLACK;
    public LevelBlock(Vector2 position, float rotation, Vector2[] hitbox) {
        super (position, rotation);
        this.setHitbox(hitbox);
        this.vectorHitbox = hitbox;
    }
//    @Override
//    public void Draw(ShapeRenderer sr)
//    {
//        sr.setColor(color);
//        sr.
//
//        float rad = MathUtils.degreesToRadians * (rotation - 90);
//        Vector2 rot = new Vector2(width * MathUtils.sin(rad), width * MathUtils.cos(rad));
//        sr.setColor(Color.RED);
//        sr.line(position.x, position.y, position.x + rot.x, position.y + rot.y);
//    }

}
