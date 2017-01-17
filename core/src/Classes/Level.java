package Classes;

import com.badlogic.gdx.math.Vector2;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by myron on 11-10-16.
 */
public class Level implements Serializable
{
    //The Width of the Level
    public static final float LevelSizeX = 500;
    //The Height of the Level
    public static final float LevelSizeY = 500;
    private long seed;
    private ArrayList<LevelBlock> levelBlocks;
    private ArrayList<LevelBlock> blockModule;
    private int levelSize = 4;
    private int row = 1;
    private int block = 1;
    private int blockSize = 26;
    private int levelBlockSizeX = (int) (LevelSizeX / blockSize);
    private int levelBlockSizeY = (int) (LevelSizeY / blockSize);


    //<editor-fold desc="Getters & Setters">

    //</editor-fold>
    /**
     * Level Constructor
     *
     */
    public Level() throws RemoteException
    {
        this.seed = generateSeed();
        this.blockModule = createModules();
        this.levelBlocks = generateLevelBlocks(3);
    }
    /**
     * Level Constructor
     *
     * @param seed - The seed that will generate the same level for all players
     */
    public Level(long seed) throws RemoteException
    {
        this.seed = seed;
        this.blockModule = createModules();
        this.levelBlocks = generateLevelBlocks();
    }

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

    private long generateSeed()
    {
        Random random = new Random();
        return random.nextLong();
    }

    private ArrayList<LevelBlock> generateLevelBlocks(int mode) throws RemoteException
    {
        ArrayList<LevelBlock> blockList = new ArrayList<>();
        Random r = new Random();

        switch (mode)
        {
            case 0:
                for (int i = 0; i < 5; i++)
                {
                    Vector2 renvec = new Vector2(r.nextFloat() * LevelSizeX, r.nextFloat() * LevelSizeY);
                    blockList.addAll(generateCircle(false, r.nextInt(5) + 5, renvec));
                }
                break;
            case 1:
                for (int i = 0; i < 15; i++)
                {
                    blockList.addAll(generateLine(r.nextInt(levelBlockSizeX), r.nextInt(levelBlockSizeY), r.nextInt(levelBlockSizeX), r.nextInt(levelBlockSizeY)));
                }
                break;
            case 2:
                for (int i = 0; i < 5; i++)
                {
                    Vector2 renvec = new Vector2(r.nextFloat() * LevelSizeX, r.nextFloat() * LevelSizeY);
                    blockList.addAll(generateSquare(true, r.nextInt(4) + 2, renvec));
                }
                break;
            case 3:
                for (int i = 0; i < 5; i++)
                {
                    Vector2 renvec = new Vector2(r.nextFloat() * LevelSizeX, r.nextFloat() * LevelSizeY);
                    blockList.addAll(generateSquare(false, r.nextInt(4) + 3, renvec));
                }
                break;
            default:
                break;
        }
        return blockList;
    }

    private ArrayList<LevelBlock> generateLine(int x1, int y1, int x2, int y2) throws RemoteException
    {
        ArrayList<LevelBlock> blockList = new ArrayList<>();

        float deltax = (float)x2 - x1;
        if (deltax == 0.0f) // verticale lijn
        {
            for (float y = y1; y < y2; y++)
            {
                Vector2 addpos = new Vector2(deltax, y).scl(blockSize);
                LevelBlock bl = new LevelBlock(addpos, 0);
                blockList.add(bl);
            }

            return blockList;
        }
        float deltay = (float)y2 - y1;
        float deltaerr = Math.abs(deltay / deltax);
        float error = deltaerr - 0.5f;

        int y = y1;

        for (float x = x1; x < x2; x++)
        {
            Vector2 addpos = new Vector2(x, y).scl(blockSize);
            LevelBlock bl = new LevelBlock(addpos, 0);
            blockList.add(bl);

            error += deltaerr;
            if (error >= 0.5f)
            {
                y++;
                error--;
            }
        }

        return blockList;
    }

    private ArrayList<LevelBlock> generateCircle(boolean filled, int size, Vector2 position) throws RemoteException
    {
        ArrayList<LevelBlock> blockList = new ArrayList<>();
        int halfsize = size / 2;
        for (int x = -halfsize; x < halfsize + 1; x++)
        {
            for (int y = -halfsize; y < halfsize + 1; y++)
            {
                //TODO add non - filled to code
                if (filled && Math.sqrt((double)x * x + y * y) < halfsize)
                {
                    Vector2 addpos = new Vector2(x, y).scl(blockSize);
                    LevelBlock bl = new LevelBlock(addpos.add(position), 0);
                    blockList.add(bl);
                }
                else
                {
                    if (Math.sqrt((double)x * x + y * y) > halfsize - 0.5f && Math.sqrt(x * x + y * y) < halfsize + 0.5f)
                    {
                        Vector2 addpos = new Vector2(x, y).scl(blockSize);
                        LevelBlock bl = new LevelBlock(addpos.add(position), 0);
                        blockList.add(bl);
                    }
                }
            }
        }
        return blockList;
    }

    private ArrayList<LevelBlock> generateSquare(boolean filled, int size, Vector2 offset) throws RemoteException
    {
        ArrayList<LevelBlock> blockList = new ArrayList<>();

        for (int x = 0; x < size; x++)
        {
            for (int y = 0; y < size; y++)
            {
                if (filled)
                {
                    Vector2 addpos = new Vector2(x, y).scl(blockSize);
                    LevelBlock bl = new LevelBlock(addpos.add(offset), 0);
                    blockList.add(bl);
                }
                else if(x == 0 || x == size - 1 || y == 0 || y == size - 1)
                {
                    Vector2 addpos = new Vector2(x, y).scl(blockSize);
                    LevelBlock bl = new LevelBlock(addpos.add(offset), 0);
                    blockList.add(bl);
                }
            }
        }
        return blockList;
    }


    private ArrayList<LevelBlock> generateLevelBlocks() throws RemoteException
    {
        ArrayList<LevelBlock> newLevelBlockList = new ArrayList<>();
        Random random = new Random(seed);
        for (int i = 0; i < (levelSize * levelSize); i++)
        {
            LevelBlock levelBlock = new LevelBlock(new Vector2(0, 0), 0); //blockModule.get(/*random.nextInt(blockModule.size())*/ 0);
            levelBlock.setPosition(new Vector2(random.nextInt((int) LevelSizeX), random.nextInt((int) LevelSizeY)));
            newLevelBlockList.add(levelBlock);
        }
        return newLevelBlockList;
    }


    private ArrayList<LevelBlock> createModules() throws RemoteException
    {
        ArrayList<LevelBlock> newLevelBlockList = new ArrayList<>();
        newLevelBlockList.add(new LevelBlock(new Vector2(0, 0), 0));
        return newLevelBlockList;
    }
}
