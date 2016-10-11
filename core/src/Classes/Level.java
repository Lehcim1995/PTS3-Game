package Classes;

/**
 * Created by myron on 11-10-16.
 */
public class Level
{
    private String seed;
    //<editor-fold desc="Getters & Setters">
    public String getSeed()
    {
        return seed;
    }

    private void setSeed(String seed)
    {
        this.seed = seed;
    }
    //</editor-fold>
    public Level()
    {
        this.seed = "GenerateRandomString";
    }
    public Level(String seed)
    {
        this.seed = seed;
    }
}
