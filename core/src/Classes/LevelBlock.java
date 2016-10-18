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

    private Color color = Color.BLACK;
    private float size = 26;
    private float halfSize = size/2;

    public LevelBlock(Vector2 position, float rotation, Vector2[] hitbox) {
        super (position, rotation);
        this.setHitbox(hitbox);
    }
    @Override
    public void Draw(ShapeRenderer sr)
    {
        sr.setColor(Color.BROWN);

        sr.rect(position.x - halfSize, position.y - halfSize, size, size);
    }

    @Override
    public void Update()
    {
        hitbox.setPosition(position.x, position.y);
        hitbox.setRotation(-rotation);
    }
}
