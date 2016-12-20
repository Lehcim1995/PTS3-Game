package Classes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Comparator;

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
        /*if (font == null || layout == null)
        {
            font = new BitmapFont();
            layout = new GlyphLayout();
        }
        DrawText(batch, font, layout, toString(), position);*/
    }

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

    public boolean isDead()
    {
        return dead;
    }

    public float getBorn()
    {
        return born;
    }

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
