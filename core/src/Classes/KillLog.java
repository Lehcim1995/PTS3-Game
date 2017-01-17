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
 * Created by Sibe on 11-10-2016.
 * In het klassendiagram heet deze klasse Killlog.
 * Maar de drie kleine l'en achter elkaar is lastig lezen,
 * dus heb ik hier de L van log een hoofd letter gemaakt.
 */
public class KillLog extends GameObject
{
    private Projectile projectile;
    private Player player;
    private transient GlyphLayout layout = new GlyphLayout();
    private transient BitmapFont font;
    private float lifeTime = 10000 * 1000; //in milli secondss
    private long born;
    private boolean dead;
    private Vector2 position;

    public KillLog(Projectile projectile, Player player) throws RemoteException
    {
        this.projectile = projectile;
        this.player = player;
        born = nanoTime();
        position = new Vector2(0, 0);
    }

    public void setTextColor(Color c)
    {
        if (font == null)
        {
            font = new BitmapFont();
        }

        font.setColor(c);
    }

    public GlyphLayout getLayout()
    {
        if (layout == null) layout = new GlyphLayout();
        return layout;
    }

    @Override
    public void Draw(ShapeRenderer shapeRenderer, Batch batch)
    {
        super.Draw(shapeRenderer, batch);
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

    public float getBorn()
    {
        return born;
    }

    @Override
    public String toString()
    {
        return projectile.getGun().getOwner().getName() + " " + projectile.getGun().getName() + " " + player.getName();
    }
}
