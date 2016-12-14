package Classes;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by myron on 11-10-16.
 */
public class Level implements Serializable
{
    private long seed;
    private ArrayList<LevelBlock> levelBlocks;
    private ArrayList<LevelBlock> blockModule;
    private int levelSize = 4;
    private int row = 1;
    private int block = 1;
    private int blockSize = 1;

    public static float LevelSizeX = 500;
    public static float LevelSizeY = 500;

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
    public Level() throws RemoteException
    {
        this.seed = GenerateSeed();
        this.blockModule = CreateModules();
        this.levelBlocks = GenerateLevelBlocks();
    }
    public Level(long seed) throws RemoteException
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

    private ArrayList<LevelBlock> GenerateLevelBlocks() throws RemoteException
    {
        ArrayList<LevelBlock> newLevelBlockList = new ArrayList<LevelBlock>();
        Random random = new Random(seed);
        for(int i = 0; i < (levelSize * levelSize); i++)
        {
            LevelBlock levelBlock = new LevelBlock(new Vector2(0,0),0); //blockModule.get(/*random.nextInt(blockModule.size())*/ 0);
            //GetNewBlockLocation(i)
            levelBlock.setPosition(new Vector2(random.nextInt((int)LevelSizeX), random.nextInt((int)LevelSizeY)));
            newLevelBlockList.add(levelBlock);

            //GameManager.getInstance().addGameObject(levelBlock);
        }
        return newLevelBlockList;
    }
    private ArrayList<LevelBlock> CreateModules() throws RemoteException
    {
        ArrayList<LevelBlock> newLevelBlockList = new ArrayList<LevelBlock>();
        //{new Vector2(50,50),new Vector2(0,50),new Vector2(50,0),new Vector2(0,0)};
        newLevelBlockList.add(new LevelBlock(new Vector2(0,0),0));
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

    class Module
    {
        public boolean[][] Module1 = new boolean[][]
        {
                {true,true,true,true,true,true,true,true},
                {true,true,true,true,true,true,true,true},
                {true,true,true,true,true,true,true,true},
                {true,true,true,true,true,true,true,true},
                {true,true,true,true,true,true,true,true},
                {true,true,true,true,true,true,true,true},
                {true,true,true,true,true,true,true,true},
                {true,true,true,true,true,true,true,true},
                {true,true,true,true,true,true,true,true},
                {true,true,true,true,true,true,true,true},
                {true,true,true,true,true,true,true,true}
        };
    }
}
