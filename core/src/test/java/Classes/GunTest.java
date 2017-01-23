package Classes;

import com.badlogic.gdx.math.Vector2;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by myron on 23-01-17.
 */
public class GunTest
{
    Gun CZ75;
    Player owner;
    @Before
    public void setUp() throws Exception
    {
        //owner = new Player(new Vector2(1,1),10f);
        CZ75 = new Gun("cz-75", 2000, 5, 0, 10, 7, Gun.gunType.BOLT_ACTION, true, 670, 10, owner);
    }

    @After
    public void tearDown() throws Exception
    {
        CZ75 = null;
        owner = null;
    }
    @Test
    public void getName() throws Exception
    {
        Assert.assertEquals("cz-75", CZ75.getName());
    }

    @Test
    public void getReloadTime() throws Exception
    {
        Assert.assertEquals(2000, CZ75.getReloadTime(), 1000000);
    }

    @Test
    public void getBulletsPerSecond() throws Exception
    {
        Assert.assertEquals(5, CZ75.getBulletsPerSecond(), 1000000);
    }

    @Test
    public void getSpread() throws Exception
    {
        Assert.assertEquals(0f, CZ75.getSpread(), 1000000);
    }

    @Test
    public void getCurrentBullets() throws Exception
    {
        Assert.assertEquals(CZ75.getMaxBullets(), CZ75.getCurrentBullets());
    }

    @Test
    public void getMaxBullets() throws Exception
    {
        Assert.assertEquals(7, CZ75.getMaxBullets());
    }

    @Test
    public void getShootType() throws Exception
    {
        Assert.assertEquals(null, CZ75.getShootType());
    }

    @Test
    public void isHasInfinit() throws Exception
    {
        Assert.assertEquals(true, CZ75.isHasInfinit());
    }

    @Test
    public void getOwner() throws Exception
    {
        Assert.assertEquals(owner, CZ75.getOwner());
    }

    @Test
    public void setOwner() throws Exception
    {
        //Player newOwner = new Player(new Vector2(10,10),15f);
        Player newOwner = null;
        CZ75.setOwner(newOwner);
        Assert.assertEquals(newOwner, CZ75.getOwner());
    }

    @Test
    public void getBulletSpeed() throws Exception
    {
        Assert.assertEquals(670, CZ75.getBulletSpeed(), 1000000);
    }

    @Test
    public void getProjectileDamage() throws Exception
    {
        Assert.assertEquals(10, CZ75.getProjectileDamage());
    }

    @Test
    public void isHasShot() throws Exception
    {
        Assert.assertEquals(false, CZ75.isHasShot());
    }

    @Test
    public void setHasShot() throws Exception
    {
        CZ75.setHasShot(true);
        Assert.assertEquals(true, CZ75.isHasShot());
    }

    @Test
    public void shoot() throws Exception
    {
        int before = CZ75.getCurrentBullets();
        CZ75.shoot();
        Assert.assertNotEquals(before, CZ75.getCurrentBullets());
    }

    @Test
    public void reload() throws Exception
    {
        CZ75.shoot();
        CZ75.shoot();
        CZ75.shoot();
        CZ75.reload();
        Assert.assertEquals(CZ75.getMaxBullets(), CZ75.getCurrentBullets());
    }
}