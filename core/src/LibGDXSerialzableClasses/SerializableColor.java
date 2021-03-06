package LibGDXSerialzableClasses;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.NumberUtils;

import java.io.Serializable;
import java.util.Random;

/**
 * Created by michel on 19-11-2016.
 */
public class SerializableColor implements Serializable
{
    public static final SerializableColor CLEAR = new SerializableColor(0, 0, 0, 0);
    public static final SerializableColor BLACK = new SerializableColor(0, 0, 0, 1);

    public static final SerializableColor WHITE = new SerializableColor(0xffffffff);
    public static final SerializableColor LIGHT_GRAY = new SerializableColor(0xbfbfbfff);
    public static final SerializableColor GRAY = new SerializableColor(0x7f7f7fff);
    public static final SerializableColor DARK_GRAY = new SerializableColor(0x3f3f3fff);

    public static final SerializableColor BLUE = new SerializableColor(0, 0, 1, 1);
    public static final SerializableColor NAVY = new SerializableColor(0, 0, 0.5f, 1);
    public static final SerializableColor ROYAL = new SerializableColor(0x4169e1ff);
    public static final SerializableColor SLATE = new SerializableColor(0x708090ff);
    public static final SerializableColor SKY = new SerializableColor(0x87ceebff);
    public static final SerializableColor CYAN = new SerializableColor(0, 1, 1, 1);
    public static final SerializableColor TEAL = new SerializableColor(0, 0.5f, 0.5f, 1);

    public static final SerializableColor GREEN = new SerializableColor(0x00ff00ff);
    public static final SerializableColor CHARTREUSE = new SerializableColor(0x7fff00ff);
    public static final SerializableColor LIME = new SerializableColor(0x32cd32ff);
    public static final SerializableColor FOREST = new SerializableColor(0x228b22ff);
    public static final SerializableColor OLIVE = new SerializableColor(0x6b8e23ff);

    public static final SerializableColor YELLOW = new SerializableColor(0xffff00ff);
    public static final SerializableColor GOLD = new SerializableColor(0xffd700ff);
    public static final SerializableColor GOLDENROD = new SerializableColor(0xdaa520ff);
    public static final SerializableColor ORANGE = new SerializableColor(0xffa500ff);

    public static final SerializableColor BROWN = new SerializableColor(0x8b4513ff);
    public static final SerializableColor TAN = new SerializableColor(0xd2b48cff);
    public static final SerializableColor FIREBRICK = new SerializableColor(0xb22222ff);

    public static final SerializableColor RED = new SerializableColor(0xff0000ff);
    public static final SerializableColor SCARLET = new SerializableColor(0xff341cff);
    public static final SerializableColor CORAL = new SerializableColor(0xff7f50ff);
    public static final SerializableColor SALMON = new SerializableColor(0xfa8072ff);
    public static final SerializableColor PINK = new SerializableColor(0xff69b4ff);
    public static final SerializableColor MAGENTA = new SerializableColor(1, 0, 1, 1);

    public static final SerializableColor PURPLE = new SerializableColor(0xa020f0ff);
    public static final SerializableColor VIOLET = new SerializableColor(0xee82eeff);
    public static final SerializableColor MAROON = new SerializableColor(0xb03060ff);

    /**
     * the red, green, blue and alpha components
     **/
    public float r, g, b, a;

    /**
     * Constructs a new SerializableColor with all components set to 0.
     */
    public SerializableColor()
    {
    }

    public SerializableColor(Color color)
    {
        rgb888ToColor(this, color.toIntBits());
    }

    /**
     * @see #rgba8888ToColor(SerializableColor, int)
     */
    public SerializableColor(int rgba8888)
    {
        rgba8888ToColor(this, rgba8888);
    }

    /**
     * Constructor, sets the components of the color
     *
     * @param r the red component
     * @param g the green component
     * @param b the blue component
     * @param a the alpha component
     */
    public SerializableColor(float r, float g, float b, float a)
    {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        clamp();
    }

    /**
     * Constructs a new color using the given color
     *
     * @param color the color
     */
    public SerializableColor(SerializableColor color)
    {
        set(color);
    }

    /**
     * Returns a new color from a hex string with the format RRGGBBAA.
     *
     * @see #toString()
     */
    public static SerializableColor valueOf(String hex)
    {
        hex = hex.charAt(0) == '#' ? hex.substring(1) : hex;
        int r = Integer.valueOf(hex.substring(0, 2), 16);
        int g = Integer.valueOf(hex.substring(2, 4), 16);
        int b = Integer.valueOf(hex.substring(4, 6), 16);
        int a = hex.length() != 8 ? 255 : Integer.valueOf(hex.substring(6, 8), 16);
        return new SerializableColor(r / 255f, g / 255f, b / 255f, a / 255f);
    }

    /**
     * Packs the color components into a 32-bit integer with the format ABGR and then converts it to a float. Note that no range
     * checking is performed for higher performance.
     *
     * @param r the red component, 0 - 255
     * @param g the green component, 0 - 255
     * @param b the blue component, 0 - 255
     * @param a the alpha component, 0 - 255
     * @return the packed color as a float
     * @see NumberUtils#intToFloatColor(int)
     */
    public static float toFloatBits(int r, int g, int b, int a)
    {
        int color = (a << 24) | (b << 16) | (g << 8) | r;
        float floatColor = NumberUtils.intToFloatColor(color);
        return floatColor;
    }

    /**
     * Packs the color components into a 32-bit integer with the format ABGR and then converts it to a float.
     *
     * @return the packed color as a 32-bit float
     * @see NumberUtils#intToFloatColor(int)
     */
    public static float toFloatBits(float r, float g, float b, float a)
    {
        int color = ((int) (255 * a) << 24) | ((int) (255 * b) << 16) | ((int) (255 * g) << 8) | ((int) (255 * r));
        return NumberUtils.intToFloatColor(color);
    }

    /**
     * Packs the color components into a 32-bit integer with the format ABGR. Note that no range checking is performed for higher
     * performance.
     *
     * @param r the red component, 0 - 255
     * @param g the green component, 0 - 255
     * @param b the blue component, 0 - 255
     * @param a the alpha component, 0 - 255
     * @return the packed color as a 32-bit int
     */
    public static int toIntBits(int r, int g, int b, int a)
    {
        return (a << 24) | (b << 16) | (g << 8) | r;
    }

    public static int alpha(float alpha)
    {
        return (int) (alpha * 255.0f);
    }

    public static int luminanceAlpha(float luminance, float alpha)
    {
        return ((int) (luminance * 255.0f) << 8) | (int) (alpha * 255);
    }

    public static int rgb565(float r, float g, float b)
    {
        return ((int) (r * 31) << 11) | ((int) (g * 63) << 5) | (int) (b * 31);
    }

    public static int rgba4444(float r, float g, float b, float a)
    {
        return ((int) (r * 15) << 12) | ((int) (g * 15) << 8) | ((int) (b * 15) << 4) | (int) (a * 15);
    }

    public static int rgb888(float r, float g, float b)
    {
        return ((int) (r * 255) << 16) | ((int) (g * 255) << 8) | (int) (b * 255);
    }

    public static int rgba8888(float r, float g, float b, float a)
    {
        return ((int) (r * 255) << 24) | ((int) (g * 255) << 16) | ((int) (b * 255) << 8) | (int) (a * 255);
    }

    public static int argb8888(float a, float r, float g, float b)
    {
        return ((int) (a * 255) << 24) | ((int) (r * 255) << 16) | ((int) (g * 255) << 8) | (int) (b * 255);
    }

    public static int rgb565(SerializableColor color)
    {
        return ((int) (color.r * 31) << 11) | ((int) (color.g * 63) << 5) | (int) (color.b * 31);
    }

    public static int rgba4444(SerializableColor color)
    {
        return ((int) (color.r * 15) << 12) | ((int) (color.g * 15) << 8) | ((int) (color.b * 15) << 4) | (int) (color.a * 15);
    }

    public static int rgb888(SerializableColor color)
    {
        return ((int) (color.r * 255) << 16) | ((int) (color.g * 255) << 8) | (int) (color.b * 255);
    }

    public static int rgba8888(SerializableColor color)
    {
        return ((int) (color.r * 255) << 24) | ((int) (color.g * 255) << 16) | ((int) (color.b * 255) << 8) | (int) (color.a * 255);
    }

    public static int argb8888(SerializableColor color)
    {
        return ((int) (color.a * 255) << 24) | ((int) (color.r * 255) << 16) | ((int) (color.g * 255) << 8) | (int) (color.b * 255);
    }

    /**
     * Sets the SerializableColor components using the specified integer value in the format RGB565. This is inverse to the rgb565(r, g, b)
     * method.
     *
     * @param color The SerializableColor to be modified.
     * @param value An integer color value in RGB565 format.
     */
    public static void rgb565ToColor(SerializableColor color, int value)
    {
        color.r = ((value & 0x0000F800) >>> 11) / 31f;
        color.g = ((value & 0x000007E0) >>> 5) / 63f;
        color.b = ((value & 0x0000001F) >>> 0) / 31f;
    }

    /**
     * Sets the SerializableColor components using the specified integer value in the format RGBA4444. This is inverse to the rgba4444(r, g,
     * b, a) method.
     *
     * @param color The SerializableColor to be modified.
     * @param value An integer color value in RGBA4444 format.
     */
    public static void rgba4444ToColor(SerializableColor color, int value)
    {
        color.r = ((value & 0x0000f000) >>> 12) / 15f;
        color.g = ((value & 0x00000f00) >>> 8) / 15f;
        color.b = ((value & 0x000000f0) >>> 4) / 15f;
        color.a = ((value & 0x0000000f)) / 15f;
    }

    /**
     * Sets the SerializableColor components using the specified integer value in the format RGB888. This is inverse to the rgb888(r, g, b)
     * method.
     *
     * @param color The SerializableColor to be modified.
     * @param value An integer color value in RGB888 format.
     */
    public static void rgb888ToColor(SerializableColor color, int value)
    {
        color.r = ((value & 0x00ff0000) >>> 16) / 255f;
        color.g = ((value & 0x0000ff00) >>> 8) / 255f;
        color.b = ((value & 0x000000ff)) / 255f;
    }

    /**
     * Sets the SerializableColor components using the specified integer value in the format RGBA8888. This is inverse to the rgba8888(r, g,
     * b, a) method.
     *
     * @param color The SerializableColor to be modified.
     * @param value An integer color value in RGBA8888 format.
     */
    public static void rgba8888ToColor(SerializableColor color, int value)
    {
        color.r = ((value & 0xff000000) >>> 24) / 255f;
        color.g = ((value & 0x00ff0000) >>> 16) / 255f;
        color.b = ((value & 0x0000ff00) >>> 8) / 255f;
        color.a = ((value & 0x000000ff)) / 255f;
    }

    /**
     * Sets the SerializableColor components using the specified integer value in the format ARGB8888. This is the inverse to the argb8888(a,
     * r, g, b) method
     *
     * @param color The SerializableColor to be modified.
     * @param value An integer color value in ARGB8888 format.
     */
    public static void argb8888ToColor(SerializableColor color, int value)
    {
        color.a = ((value & 0xff000000) >>> 24) / 255f;
        color.r = ((value & 0x00ff0000) >>> 16) / 255f;
        color.g = ((value & 0x0000ff00) >>> 8) / 255f;
        color.b = ((value & 0x000000ff)) / 255f;
    }

    public static SerializableColor getRandomColor()
    {
        Random r = new Random();
        float rr, rg, rb, ra;
        rr = r.nextFloat();
        rg = r.nextFloat();
        rb = r.nextFloat();
        ra = 1;//r.nextFloat();

        return new SerializableColor(rr, rg, rb, ra);
    }

    /**
     * Sets this color to the given color.
     *
     * @param color the SerializableColor
     */
    public SerializableColor set(SerializableColor color)
    {
        this.r = color.r;
        this.g = color.g;
        this.b = color.b;
        this.a = color.a;
        return this;
    }

    /**
     * Multiplies the this color and the given color
     *
     * @param color the color
     * @return this color.
     */
    public SerializableColor mul(SerializableColor color)
    {
        this.r *= color.r;
        this.g *= color.g;
        this.b *= color.b;
        this.a *= color.a;
        return clamp();
    }

    /**
     * Multiplies all components of this SerializableColor with the given value.
     *
     * @param value the value
     * @return this color
     */
    public SerializableColor mul(float value)
    {
        this.r *= value;
        this.g *= value;
        this.b *= value;
        this.a *= value;
        return clamp();
    }

    /**
     * Adds the given color to this color.
     *
     * @param color the color
     * @return this color
     */
    public SerializableColor add(SerializableColor color)
    {
        this.r += color.r;
        this.g += color.g;
        this.b += color.b;
        this.a += color.a;
        return clamp();
    }

    /**
     * Subtracts the given color from this color
     *
     * @param color the color
     * @return this color
     */
    public SerializableColor sub(SerializableColor color)
    {
        this.r -= color.r;
        this.g -= color.g;
        this.b -= color.b;
        this.a -= color.a;
        return clamp();
    }

    /**
     * Clamps this SerializableColor's components to a valid range [0 - 1]
     *
     * @return this SerializableColor for chaining
     */
    public SerializableColor clamp()
    {
        if (r < 0) r = 0;
        else if (r > 1) r = 1;

        if (g < 0) g = 0;
        else if (g > 1) g = 1;

        if (b < 0) b = 0;
        else if (b > 1) b = 1;

        if (a < 0) a = 0;
        else if (a > 1) a = 1;
        return this;
    }

    /**
     * Sets this SerializableColor's component values.
     *
     * @param r Red component
     * @param g Green component
     * @param b Blue component
     * @param a Alpha component
     * @return this SerializableColor for chaining
     */
    public SerializableColor set(float r, float g, float b, float a)
    {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        return clamp();
    }

    /**
     * Sets this color's component values through an integer representation.
     *
     * @return this SerializableColor for chaining
     * @see #rgba8888ToColor(SerializableColor, int)
     */
    public SerializableColor set(int rgba)
    {
        rgba8888ToColor(this, rgba);
        return this;
    }

    /**
     * Adds the given color component values to this SerializableColor's values.
     *
     * @param r Red component
     * @param g Green component
     * @param b Blue component
     * @param a Alpha component
     * @return this SerializableColor for chaining
     */
    public SerializableColor add(float r, float g, float b, float a)
    {
        this.r += r;
        this.g += g;
        this.b += b;
        this.a += a;
        return clamp();
    }

    /**
     * Subtracts the given values from this SerializableColor's component values.
     *
     * @param r Red component
     * @param g Green component
     * @param b Blue component
     * @param a Alpha component
     * @return this SerializableColor for chaining
     */
    public SerializableColor sub(float r, float g, float b, float a)
    {
        this.r -= r;
        this.g -= g;
        this.b -= b;
        this.a -= a;
        return clamp();
    }

    /**
     * Multiplies this SerializableColor's color components by the given ones.
     *
     * @param r Red component
     * @param g Green component
     * @param b Blue component
     * @param a Alpha component
     * @return this SerializableColor for chaining
     */
    public SerializableColor mul(float r, float g, float b, float a)
    {
        this.r *= r;
        this.g *= g;
        this.b *= b;
        this.a *= a;
        return clamp();
    }

    /**
     * Linearly interpolates between this color and the target color by t which is in the range [0,1]. The result is stored in
     * this color.
     *
     * @param target The target color
     * @param t      The interpolation coefficient
     * @return This color for chaining.
     */
    public SerializableColor lerp(final SerializableColor target, final float t)
    {
        this.r += t * (target.r - this.r);
        this.g += t * (target.g - this.g);
        this.b += t * (target.b - this.b);
        this.a += t * (target.a - this.a);
        return clamp();
    }

    /**
     * Linearly interpolates between this color and the target color by t which is in the range [0,1]. The result is stored in
     * this color.
     *
     * @param r The red component of the target color
     * @param g The green component of the target color
     * @param b The blue component of the target color
     * @param a The alpha component of the target color
     * @param t The interpolation coefficient
     * @return This color for chaining.
     */
    public SerializableColor lerp(final float r, final float g, final float b, final float a, final float t)
    {
        this.r += t * (r - this.r);
        this.g += t * (g - this.g);
        this.b += t * (b - this.b);
        this.a += t * (a - this.a);
        return clamp();
    }

    /**
     * Multiplies the RGB values by the alpha.
     */
    public SerializableColor premultiplyAlpha()
    {
        r *= a;
        g *= a;
        b *= a;
        return this;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SerializableColor color = (SerializableColor) o;
        return toIntBits() == color.toIntBits();
    }

    @Override
    public int hashCode()
    {
        int result = (r != +0.0f ? NumberUtils.floatToIntBits(r) : 0);
        result = 31 * result + (g != +0.0f ? NumberUtils.floatToIntBits(g) : 0);
        result = 31 * result + (b != +0.0f ? NumberUtils.floatToIntBits(b) : 0);
        result = 31 * result + (a != +0.0f ? NumberUtils.floatToIntBits(a) : 0);
        return result;
    }

    /**
     * Packs the color components into a 32-bit integer with the format ABGR and then converts it to a float.
     *
     * @return the packed color as a 32-bit float
     * @see NumberUtils#intToFloatColor(int)
     */
    public float toFloatBits()
    {
        int color = ((int) (255 * a) << 24) | ((int) (255 * b) << 16) | ((int) (255 * g) << 8) | ((int) (255 * r));
        return NumberUtils.intToFloatColor(color);
    }

    /**
     * Packs the color components into a 32-bit integer with the format ABGR.
     *
     * @return the packed color as a 32-bit int.
     */
    public int toIntBits()
    {
        int color = ((int) (255 * a) << 24) | ((int) (255 * b) << 16) | ((int) (255 * g) << 8) | ((int) (255 * r));
        return color;
    }

    /**
     * Returns the color encoded as hex string with the format RRGGBBAA.
     */
    public String toString()
    {
        String value = Integer.toHexString(((int) (255 * r) << 24) | ((int) (255 * g) << 16) | ((int) (255 * b) << 8) | ((int) (255 * a)));
        while (value.length() < 8) value = "0" + value;
        return value;
    }

    /**
     * @return a copy of this color
     */
    public SerializableColor cpy()
    {
        return new SerializableColor(this);
    }

    public Color getLibGDXColor()
    {
        return new Color(rgba8888(r, g, b, a));
    }
}
