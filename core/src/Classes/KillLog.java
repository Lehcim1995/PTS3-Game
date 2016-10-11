package Classes;

/**
 * Created by Sibe on 11-10-2016.
 * In het klassendiagram heet deze klasse Killlog.
 * Maar de drie kleine l'en achter elkaar is lastig lezen,
 * dus heb ik hier de L van log een hoofd letter gemaakt.
 */
public class KillLog
{
   private Projectile projectile;
   private Player player;

    public void Killlog(Projectile projectile, Player player){
        this.projectile = projectile;
        this.player = player;
    }

    // GetGun() moet nog gemaakt worden
    @Override
    public String toString()
    {
        return projectile.GetGun().GetOwner().GetName() + " Shot " + player.GetName() ;
    }
}
