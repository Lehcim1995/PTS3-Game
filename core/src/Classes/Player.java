package Classes;

import Interfaces.IGameObject;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
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

    //Apperence
    private float width = 17;
    private Color color = Color.BLACK;

    //
    private InputClass ic;
    public boolean reloadThread;

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

    public Player(Texture texture, Vector2 position, float rotation, Shape boundingShape, String name, Gun gunEquipped)
    {
        super(texture, position, rotation, boundingShape);
        this.name = name;
        this.gunEquipped = gunEquipped;
    }

    public Player()
    {
        super();
        position = new Vector2(50, 50);
        speed = 125.1248f;
        ic = new InputClass(this);
        this.gunEquipped = new Gun("kanbkergun",0,10,0,10,10,"kut",true,100,10,this);
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
        this.gunEquipped.Shoot();
    }

    public void Reload()
    {
        gunEquipped.Reload();

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

        sr.setColor(color);
        //shapeRenderer.line(position.x, position.y, mous
        // e.x, mouse.y);
        sr.circle(position.x, position.y, width);

        float rad = MathUtils.degreesToRadians * (rotation - 90);
        Vector2 rot = new Vector2(width * MathUtils.sin(rad),width * MathUtils.cos(rad));
        sr.setColor(Color.RED);
        sr.line(position.x, position.y, position.x + rot.x, position.y + rot.y);
        //sr.dispose();
    }

    @Override
    public void Update()
    {
        Vector2 pos = new Vector2();
        //System.out.println("Update");
        if (ic.GetKey(Input.Keys.W)){ pos.y += 1; }
        if (ic.GetKey(Input.Keys.S)){ pos.y -= 1;}
        if (ic.GetKey(Input.Keys.A)){ pos.x -= 1;}
        if (ic.GetKey(Input.Keys.D)){ pos.x += 1;}
        if (ic.GetKey(Input.Keys.R)){ Reload(); }

        position.add(pos.scl(speed * Gdx.graphics.getDeltaTime()));
    }

    public String GetName()
    {
        return this.name;
    }
}
