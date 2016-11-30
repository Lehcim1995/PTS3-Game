package Classes;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Stefan on 10/11/2016.
 */
public class Chat
{
    private String message;
    private Player fromPlayer;
    private Date time;
    private Calendar cal = Calendar.getInstance();

    public Chat(String message, Player fromPlayer)
    {
        this.message = message;
        this.fromPlayer = fromPlayer;
        this.time = cal.getTime();
    }

    @Override
    public String toString() {
        return time.toString() + " - " + fromPlayer.getName() + " : " + message;
    }
}
