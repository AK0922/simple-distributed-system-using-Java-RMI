

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote{	
    
    void showErrorMsg(String name, String msg) throws RemoteException;
    void retrieveList(ClientInterface clientInfo) throws RemoteException;
	
    void printList() throws RemoteException;
    void clearList() throws RemoteException;
	
    double getLocationX() throws RemoteException;
	
    void setLocationX(double x) throws RemoteException;
    
    double getLocationY() throws RemoteException;
	void setLocationY(double y) throws RemoteException;
	
    int getAge() throws RemoteException;
	
    int getUniqueID() throws RemoteException;
    void setUniqueID(int id) throws RemoteException;
	
	void showMesg(String name, String msg) throws RemoteException;
	
	String getName() throws RemoteException;
    String getSex() throws RemoteException;
}
