package Interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

/**
 * Created by Stefan on 12/6/2016.
 */
public interface IConnection extends Remote
{
    boolean CreateUser(String name, String lastname, String email, String username, String password) throws RemoteException;
    IUser LogIn(String email, String password) throws SQLException, RemoteException;
}
