package Classes;

import com.badlogic.gdx.graphics.Color;

import java.io.Serializable;

/**
 * Created by michel on 19-11-2016.
 */
public class SerializableColor extends Color implements Serializable
{

    public SerializableColor(float r, float g, float b, float a)
    {
        super(r, g, b, a);
    }
}
