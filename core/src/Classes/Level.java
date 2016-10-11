package Classes;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Shape;

/**
 * Created by myron on 11-10-16.
 */
public class Level
{
    private long seed;
    private ArrayList<LevelBlock> levelBlocks;
    private ArrayList<LevelBlock> blockModule;
    private int levelSize = 4;
    //<editor-fold desc="Getters & Setters">

    public ArrayList<LevelBlock> getLevelBlocks()
    {
        return levelBlocks;
    }

    public void setLevelBlocks(ArrayList<LevelBlock> levelBlocks)
    {
        this.levelBlocks = levelBlocks;
    }

    public long getSeed()
    {
        return seed;
    }
    //</editor-fold>
    public Level()
    {
        this.seed = GenerateSeed();
        this.blockModule = CreateModules();
        this.levelBlocks = GenerateLevelBlocks();
    }
    public Level(long seed)
    {
        this.seed = seed;
        this.blockModule = CreateModules();
        this.levelBlocks = GenerateLevelBlocks();
    }
    private long GenerateSeed()
    {
        Random random = new SecureRandom();
        return random.nextLong();
    }
    private ArrayList<LevelBlock> GenerateLevelBlocks()
    {
        ArrayList<LevelBlock> newLevelBlockList = new ArrayList<LevelBlock>();
        Random random = new Random(seed);
        for(int i = 0; i < (levelSize * levelSize); i++)
        {
            newLevelBlockList.add(blockModule.get(random.nextInt(blockModule.size())));
        }
        return newLevelBlockList;
    }
    private ArrayList<LevelBlock> CreateModules()
    {
        ArrayList<LevelBlock> newLevelBlockList = new ArrayList<LevelBlock>();
        //newLevelBlockList.add(new LevelBlock(null, new Vector2(),0,new Rectangle(0f,0f,50f,100f)));
        return newLevelBlockList;
    }
}
