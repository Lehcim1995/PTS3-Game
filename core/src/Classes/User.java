package Classes;

/**
 * Created by Stefan on 10/11/2016.
 */
public class User
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

    public String GetName() { return name; }

    public String GetEmail() { return email; }

    public int GetKills() { return  kills; }

    public int GetDeaths() { return deaths; }

    public int GetShots() { return shots; }

    public int GetShotsHit() { return shotsHit; }

    public int GetMatchesPlayed() { return matchesPlayed; }

    public int GetMatchesWon() { return matchesWon; }

    public int GetMatchesLost() { return matchesLost; }

    public float GetKDRatio() { return (float) kills / deaths; }

    public float GetAccuracyPercentage() {
        return (float) (shotsHit / shots) * 100;
    }

    public float GetWinPercentage() {
        return (float) (matchesWon / matchesLost) * 100;
    }
}
