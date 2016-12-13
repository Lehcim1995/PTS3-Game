package Interfaces;

import java.io.Serializable;

/**
 * Created by Stefan on 12/6/2016.
 */
public interface IUser extends Serializable
{
    String getName();
    String getEmail();
    int getKills();
    int getDeaths();
    int getShots();
    int getShotsHit();
    int getMatchesPlayed();
    int getMatchesWon();
    int getMatchesLost();
    float getKDRatio();
    float getAccuracyPercentage();
    float getWinPercentage();
    boolean getBanned();
}
