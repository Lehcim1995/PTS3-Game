package Classes;

import Interfaces.IUser;

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
    /**
     * User constructor
     *
     * @param name          - Name of the user
     * @param email         - Email of the user
     * @param kills         - Killes made by the user
     * @param deaths        - Deaths of the user
     * @param shotsHit      - Shots hit by the user
     * @param shots         - Amount of times the user shot
     * @param matchesPlayed - Amount of matches played by the user
     * @param matchesWon    - Amount of matches won by the user
     * @param matchesLost   - Amount of matches lost by the user
     * @param isBanned      - boolean if player banned or not
     */
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
        else
        {
            this.isBanned = true;
        }


    }
    /**
     * Chat constructor
     *
     * @param kills     - Amount of kills that has to be added to total
     * @param deaths    - Amount of deaths that has to be added to total
     * @param shotsHit  - Amount of shotsHit that has to be added to total
     * @param shots     - Amount of shots that has to be added to total
     * @param matchWon  - Amount of matchWon that has to be added to total
     */
    public void UpdateData(int kills, int deaths, int shotsHit, int shots, boolean matchWon)
    {
        this.kills += kills;
        this.deaths += deaths;
        this.shotsHit += shotsHit;
        this.shots += shots;
        if (matchWon)
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
    public String getName()
    {
        return name;
    }

    @Override
    public String getEmail()
    {
        return email;
    }

    @Override
    public int getKills()
    {
        return kills;
    }

    @Override
    public int getDeaths()
    {
        return deaths;
    }

    @Override
    public int getShots()
    {
        return shots;
    }

    @Override
    public int getShotsHit()
    {
        return shotsHit;
    }

    @Override
    public int getMatchesPlayed()
    {
        return matchesPlayed;
    }

    @Override
    public int getMatchesWon()
    {
        return matchesWon;
    }

    @Override
    public int getMatchesLost()
    {
        return matchesLost;
    }

    @Override
    public float getKDRatio()
    {
        return (float) kills / deaths;
    }

    @Override
    public float getAccuracyPercentage()
    {
        return (float) shotsHit / shots * 100;
    }

    @Override
    public float getWinPercentage()
    {
        return (float) matchesWon / matchesPlayed * 100;
    }

    @Override
    public boolean getBanned()
    {
        return isBanned;
    }
}
