package Interfaces;

import java.io.Serializable;

/**
 * Created by Stefan on 12/6/2016.
 */
public interface IUser extends Serializable
{
    String GetName();
    String GetEmail();
    int GetKills();
    int GetDeaths();
    int GetShots();
    int GetShotsHit();
    int GetMatchesPlayed();
    int GetMatchesWon();
    int GetMatchesLost();
    float GetKDRatio();
    float GetAccuracyPercentage();
    float GetWinPercentage();
    boolean GetIsBanned();
}
