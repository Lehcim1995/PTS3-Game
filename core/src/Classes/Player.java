package Classes;

import Interfaces.IGameObject;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.input.*;

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

    //
    private InputClass ic;

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
        position = new Vector2(0,0);
        speed = 1;
        ic = new InputClass(this);
        Gdx.input.setInputProcessor(ic);

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

    public void Draw(ShapeRenderer sr)
    {

        sr.setColor(1, 0, 0, 1);
        //shapeRenderer.line(position.x, position.y, mous
        // e.x, mouse.y);
        sr.circle(position.x, position.y, 15);
        //sr.dispose();
    }

    @Override
    public void Update()
    {
        Vector2 pos = new Vector2();
        //System.out.println("Update");
        if (ic.GetKey(Input.Keys.W)){ pos.y += 1; System.out.println("Up"); }
        if (ic.GetKey(Input.Keys.S)){ pos.y -= 1; System.out.println("Down");}
        if (ic.GetKey(Input.Keys.A)){ pos.x -= 1; System.out.println("Left");}
        if (ic.GetKey(Input.Keys.D)){ pos.x += 1; System.out.println("Right");}

        position.add(pos.scl(speed));
    }


}
