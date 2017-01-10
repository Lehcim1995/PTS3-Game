package Classes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.rmi.RemoteException;

import static com.badlogic.gdx.utils.TimeUtils.millis;
import static com.badlogic.gdx.utils.TimeUtils.nanoTime;

/**
 * Created by Stefan on 10/11/2016.
 */
public class Chat extends GameObject
{
    private String message;
    private Player fromPlayer;
    private float lifeTime = 10000 * 1000; //in milli secondss
    private long born;
    private boolean dead;

    private transient BitmapFont font = new BitmapFont();
    private transient GlyphLayout layout = new GlyphLayout();
    /**
     * Chat constructor
     * @param String Message - text message from a player
     * @param Player fromPlayer - player that send the message
     */
    public Chat(String message, Player fromPlayer) throws RemoteException
    {
        super();
        this.message = message;
        this.fromPlayer = fromPlayer;
        born = nanoTime();
        position = new Vector2(0, 0);
    }

    @Override
    public void Draw(ShapeRenderer shapeRenderer, Batch batch)
    {
        super.Draw(shapeRenderer, batch);
    }
    /**
     * DrawChat
     * @param Batch batch - Idk
     */
    public void DrawChat(Batch batch) throws RemoteException
    {
        if (font == null || layout == null)
        {
            font = new BitmapFont();
            layout = new GlyphLayout();
        }

        DrawText(batch, font, layout, toString(), position);

        dead = millis() - born > lifeTime;
    }
    /**
     * isDead
     * @return Boolean true or false
     */
    public boolean isDead()
    {
        return dead;
    }
    /**
     * getBorn
     * @return long
     */
    public float getBorn()
    {
        return born;
    }
    /**
     * setTextColor
     * @parm Color c - The color of the text
     */
    public void setTextColor(Color c)
    {
        if (font == null)
        {
            font = new BitmapFont();
        }

        font.setColor(c);
    }

    @Override
    public String toString()
    {
        return fromPlayer.getName() + " : " + message;
    }

    public GlyphLayout getLayout()
    {
        if (layout == null)
            layout = new GlyphLayout();
        return layout;
    }
}
