package Classes;

import Interfaces.IGameObject;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Shape;

/**
 * Created by michel on 27-9-2016.
 */
public class Player extends GameObject
{
    //Normal vars
    private String name;
    private float health;
    private int maxHealth;
    private float speed;
    private float maxSpeed;
    private float acceleration;
    private float deAcceleration;
    private Gun gunEquipped;

    //Game vars
    private int kills;
    private int deaths;
    private int shots;
    private int shotsHit;

    //implementation vars
    private boolean walkingUp;
    private boolean walkingDown;
    private boolean walkingLeft;
    private boolean walkingRight;

    enum walkDir
    {
        Up,
        Down,
        Left,
        Right
    }

    public Player(Texture texture, Vector2 position, float rotation, Shape boundingShape)
    {
        super(texture, position, rotation, boundingShape);
    }

    public Player()
    {
        super();
    }

    public Player(Texture texture, Vector2 position, float rotation, Shape boundingShape, String name, Gun gunEquipped)
    {
        super(texture, position, rotation, boundingShape);
        this.name = name;
        this.gunEquipped = gunEquipped;
    }

    public void Walk(walkDir dir, boolean setWalking)
    {
        switch (dir)
        {
            case Up:
                walkingUp = setWalking;
                break;
            case Down:
                walkingDown = setWalking;
                break;
            case Left:
                walkingLeft = setWalking;
                break;
            case Right:
                walkingRight = setWalking;
                break;
            default:
                break;
        }
    }

    public void Shoot()
    {
        this.gunEquipped.Shoot();

    }

    public void Spawn()
    {

    }

    public void Die()
    {

    }

    public void Hit()
    {

    }

    @Override
    public void Update()
    {
        Vector2 pos = new Vector2();
        if (walkingUp){ pos.y += 1; }
        if (walkingDown){ pos.y -= 1; }
        if (walkingLeft){ pos.x -= 1; }
        if (walkingRight){ pos.x += 1; }

        position.add(pos.scl(speed));
    }


}
