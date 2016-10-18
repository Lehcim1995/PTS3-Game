package Classes;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Polygon;
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
    private int row = 1;
    private int block = 1;
    private int blockSize = 1;
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
        Random random = new Random();
        return random.nextLong();
    }

    private ArrayList<LevelBlock> GenerateLevelBlocks()
    {
        ArrayList<LevelBlock> newLevelBlockList = new ArrayList<LevelBlock>();
        Random random = new Random(seed);
        for(int i = 0; i < (levelSize * levelSize); i++)
        {
            Vector2[] hithoxVectorList = GameObject.DEFAULTHITBOX(13);
            LevelBlock levelBlock = new LevelBlock(new Vector2(0,0),0,hithoxVectorList); //blockModule.get(/*random.nextInt(blockModule.size())*/ 0);
            //GetNewBlockLocation(i)
            levelBlock.SetPosition(new Vector2(random.nextInt(Gdx.graphics.getWidth()), random.nextInt(Gdx.graphics.getHeight())));
            newLevelBlockList.add(levelBlock);

            GameManager.getInstance().addGameObject(levelBlock);
        }
        return newLevelBlockList;
    }
    private ArrayList<LevelBlock> CreateModules()
    {
        ArrayList<LevelBlock> newLevelBlockList = new ArrayList<LevelBlock>();
        Vector2[] hithoxVectorList = GameObject.DEFAULTHITBOX(13); //{new Vector2(50,50),new Vector2(0,50),new Vector2(50,0),new Vector2(0,0)};
        newLevelBlockList.add(new LevelBlock(new Vector2(0,0),0,hithoxVectorList));
        return newLevelBlockList;
    }
    private Vector2 GetNewBlockLocation(int blockNumber){
        if(blockNumber == 0)
        {
            block++;
            return new Vector2(100,100);
        }
        else
        {
            if(block > levelSize)
            {
                block = 0;
                row++;
            }
            Vector2 newLocation = new Vector2(block*blockSize,row*blockSize);
            block++;
            return newLocation;
        }
    }
}
