

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote{
    void addClient(ClientInterface client) throws RemoteException;
    void SendMessageOver(int id, String msg) throws RemoteException;    
}
