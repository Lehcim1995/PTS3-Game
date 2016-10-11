package Classes;

/**
 * Created by Stefan on 10/11/2016.
 */
public class User
{
    private String name;
    private int kills;
    private int deaths;
    private int shotsHit;
    private int shots;
    private int matchesPlayed;
    private int matchesWon;
    private int matchesLost;

    public User(String name, int kills, int deaths, int shotsHit, int shots, int matchesPlayed, int matchesWon, int matchesLost)
    {
        this.name = name;
        this.kills = kills;
        this.deaths = deaths;
        this.shotsHit = shotsHit;
        this.shots = shots;
        this.matchesPlayed = matchesPlayed;
        this.matchesWon = matchesWon;
        this.matchesLost = matchesLost;
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
}
