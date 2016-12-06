package Classes;

import Interfaces.IUser;

import java.io.Serializable;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Stefan on 10/11/2016.
 */
public class User implements IUser
{
    private String name;
    private String email;
    private int kills;
    private int deaths;
    private int shotsHit;
    private int shots;
    private int matchesPlayed;
    private int matchesWon;
    private int matchesLost;
    private boolean isBanned;

    public User(String name, String email, int kills, int deaths, int shotsHit, int shots, int matchesPlayed, int matchesWon, int matchesLost, int isBanned)
    {
        this.name = name;
        this.email = email;
        this.kills = kills;
        this.deaths = deaths;
        this.shotsHit = shotsHit;
        this.shots = shots;
        this.matchesPlayed = matchesPlayed;
        this.matchesWon = matchesWon;
        this.matchesLost = matchesLost;

        if (isBanned != 1)
        {
            this.isBanned = false;
        }
        else {
            this.isBanned = true;
        }


    }

    public void UpdateData(int kills, int deaths, int shotsHit, int shots, boolean matchWon)
    {
        this.kills += kills;
        this.deaths += deaths;
        this.shotsHit += shotsHit;
        this.shots += shots;
        if(matchWon)
        {
            this.matchesWon++;
        }
        else
        {
            this.matchesLost++;
        }
        this.matchesPlayed++;
    }
    @Override
    public String GetName() { return name; }
    @Override
    public String GetEmail() { return email; }
    @Override
    public int GetKills() { return  kills; }
    @Override
    public int GetDeaths() { return deaths; }
    @Override
    public int GetShots() { return shots; }
    @Override
    public int GetShotsHit() { return shotsHit; }
    @Override
    public int GetMatchesPlayed() { return matchesPlayed; }
    @Override
    public int GetMatchesWon() { return matchesWon; }
    @Override
    public int GetMatchesLost() { return matchesLost; }
    @Override
    public float GetKDRatio() { return (float) kills / deaths; }
    @Override
    public float GetAccuracyPercentage() { return (float) shotsHit / shots * 100; }
    @Override
    public float GetWinPercentage() { return (float) matchesWon / matchesPlayed * 100; }
    @Override
    public boolean GetIsBanned() { return isBanned; }

}
