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
    public static float LevelSizeX = 500;
    public static float LevelSizeY = 500;
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
    public Level() throws RemoteException
    {
        this.seed = GenerateSeed();
        this.blockModule = CreateModules();
        this.levelBlocks = GenerateLevelBlocks(3);
    }

    public Level(long seed) throws RemoteException
    {
        this.seed = seed;
        this.blockModule = CreateModules();
        this.levelBlocks = GenerateLevelBlocks();
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

    private long GenerateSeed()
    {
        Random random = new Random();
        return random.nextLong();
    }

    private ArrayList<LevelBlock> GenerateLevelBlocks(int mode) throws RemoteException
    {
        ArrayList<LevelBlock> blockList = new ArrayList<>();
        Random r = new Random();

        switch (mode)
        {
            case 0:
                for (int i = 0; i < 5; i++)
                {
                    Vector2 renvec = new Vector2(r.nextFloat() * LevelSizeX, r.nextFloat() * LevelSizeY);
                    blockList.addAll(GenerateCircle(false, r.nextInt(5) + 5, renvec));
                }
                break;
            case 1:
                for (int i = 0; i < 15; i++)
                {
                    blockList.addAll(GenerateLine(r.nextInt(levelBlockSizeX), r.nextInt(levelBlockSizeY), r.nextInt(levelBlockSizeX), r.nextInt(levelBlockSizeY), 0));
                }
                break;
            case 2:
                for (int i = 0; i < 5; i++)
                {
                    Vector2 renvec = new Vector2(r.nextFloat() * LevelSizeX, r.nextFloat() * LevelSizeY);
                    blockList.addAll(GenerateSquare(true, r.nextInt(4) + 2, renvec));
                }
                break;
            case 3:
                for (int i = 0; i < 5; i++)
                {
                    Vector2 renvec = new Vector2(r.nextFloat() * LevelSizeX, r.nextFloat() * LevelSizeY);
                    blockList.addAll(GenerateSquare(false, r.nextInt(4) + 3, renvec));
                }
                break;
            default:
                break;
        }

        System.out.println("Level Size = " + blockList.size());
        return blockList;
    }

    private ArrayList<LevelBlock> GenerateLine(int x1, int y1, int x2, int y2, int thinkness) throws RemoteException
    {
        ArrayList<LevelBlock> blockList = new ArrayList<>();

        float deltax = x2 - x1;
        if (deltax == 0) // verticale lijn
        {
            for (float y = y1; y < y2; y++)
            {
                Vector2 addpos = new Vector2(deltax, y).scl(blockSize);
                LevelBlock bl = new LevelBlock(addpos, 0);
                blockList.add(bl);
            }

            return blockList;
        }
        float deltay = y2 - y1;
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

    private ArrayList<LevelBlock> GenerateCircle(boolean filled, int size, Vector2 position) throws RemoteException
    {
        ArrayList<LevelBlock> blockList = new ArrayList<>();
        int halfsize = size / 2;
        for (int x = -halfsize; x < halfsize + 1; x++)
        {
            for (int y = -halfsize; y < halfsize + 1; y++)
            {
                //TODO add non - filled to code
                if (filled)
                {
                    if (Math.sqrt(x * x + y * y) < halfsize)
                    {
                        Vector2 addpos = new Vector2(x, y).scl(blockSize);
                        LevelBlock bl = new LevelBlock(addpos.add(position), 0);
                        blockList.add(bl);
                    }
                }
                else
                {
                    if (Math.sqrt(x * x + y * y) > halfsize - 0.5f && Math.sqrt(x * x + y * y) < halfsize + 0.5f)
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

    private ArrayList<LevelBlock> GenerateSquare(boolean filled, int size, Vector2 offset) throws RemoteException
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
                else
                {
                    if (x == 0 || x == size - 1 || y == 0 || y == size - 1)
                    {
                        Vector2 addpos = new Vector2(x, y).scl(blockSize);
                        LevelBlock bl = new LevelBlock(addpos.add(offset), 0);
                        blockList.add(bl);
                    }
                }
            }
        }

        System.out.println("Size = " + blockList.size());
        return blockList;
    }


    private ArrayList<LevelBlock> GenerateLevelBlocks() throws RemoteException
    {
        ArrayList<LevelBlock> newLevelBlockList = new ArrayList<LevelBlock>();
        Random random = new Random(seed);
        for (int i = 0; i < (levelSize * levelSize); i++)
        {
            LevelBlock levelBlock = new LevelBlock(new Vector2(0, 0), 0); //blockModule.get(/*random.nextInt(blockModule.size())*/ 0);
            //GetNewBlockLocation(i)
            levelBlock.setPosition(new Vector2(random.nextInt((int) LevelSizeX), random.nextInt((int) LevelSizeY)));
            newLevelBlockList.add(levelBlock);

            //GameManager.getInstance().addGameObject(levelBlock);
        }
        return newLevelBlockList;
    }


    private ArrayList<LevelBlock> CreateModules() throws RemoteException
    {
        ArrayList<LevelBlock> newLevelBlockList = new ArrayList<LevelBlock>();
        //{new Vector2(50,50),new Vector2(0,50),new Vector2(50,0),new Vector2(0,0)};
        newLevelBlockList.add(new LevelBlock(new Vector2(0, 0), 0));
        return newLevelBlockList;
    }

    private Vector2 GetNewBlockLocation(int blockNumber)
    {
        if (blockNumber == 0)
        {
            block++;
            return new Vector2(100, 100);
        }
        else
        {
            if (block > levelSize)
            {
                block = 0;
                row++;
            }
            Vector2 newLocation = new Vector2(block * blockSize, row * blockSize);
            block++;
            return newLocation;
        }


    }

    class Module
    {
        public boolean[][] Module1 = new boolean[][]{{true, true, true, true, true, true, true, true}, {true, true, true, true, true, true, true, true}, {true, true, true, true, true, true, true, true}, {true, true, true, true, true, true, true, true}, {true, true, true, true, true, true, true, true}, {true, true, true, true, true, true, true, true}, {true, true, true, true, true, true, true, true}, {true, true, true, true, true, true, true, true}, {true, true, true, true, true, true, true, true}, {true, true, true, true, true, true, true, true}, {true, true, true, true, true, true, true, true}};
    }
}
