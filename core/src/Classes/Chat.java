package Classes;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.print.attribute.standard.DateTimeAtCompleted;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

}
