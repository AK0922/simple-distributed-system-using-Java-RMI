import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.Scanner;


public class Client extends UnicastRemoteObject implements ClientInterface, Runnable
		 {
	
	private ServerInterface server;
	public static String name = null;
	public static int age = 0;
	public static String sex = null;
	public static double locationX = 0.0;
	public static double locationY = 0.0;
	public static int uniqueID;
	
	public static LinkedList<ClientInterface> clientList = new LinkedList<ClientInterface>();

	protected Client(String[] args, ServerInterface server)
			throws RemoteException {
		this.server = server;
		this.name = args[3];
		this.locationX = Double.parseDouble(args[5]);
		this.locationY = Double.parseDouble(args[6]);
		this.sex = args[8];
		this.age = Integer.parseInt(args[10]);
		
		server.addClient(this);
		
	}

	
	
	public void retrieveList(ClientInterface clientInfo) throws RemoteException{
		clientList.add(clientInfo);		
	}
	
	public void showErrorMsg(String name, String msg) throws RemoteException{
		System.out.println(name+"(error): " + msg);
		System.exit(1);
	}
	public void clearList() throws RemoteException{
		clientList.clear();
	}
	public void printList() throws RemoteException{
		if(clientList.size() != 0){
		for(int i=0;i<clientList.size();i++){
			
			System.out.println("Unique ID : " + clientList.get(i).getUniqueID());
			System.out.println("Name : " + clientList.get(i).getName());
			System.out.println("Age : " + clientList.get(i).getAge());
			System.out.println("Sex " + clientList.get(i).getSex());
			System.out.println("Location : " + clientList.get(i).getLocationX() + " " + clientList.get(i).getLocationY());
			
		}
		}else System.err.println("No client found in the range given.");
	}

	public String getName() throws RemoteException {
		return this.name;
	}


	public double getLocationX() throws RemoteException {
		return this.locationX;
	}
	public void setLocationX(double x) throws RemoteException{
		this.locationX = x;
	}

	public double getLocationY() throws RemoteException {
		return this.locationY;
	}
	public void setLocationY(double y) throws RemoteException{
		this.locationY = y;
	}
    public void showMesg(String sender_name, String msg)
			throws RemoteException {		
			System.out.println(sender_name + " : " + msg);		
	}
	public String getSex() throws RemoteException {
		return this.sex;
	}
	public int getAge() { 
		return this.age;
	}
	
	public int getUniqueID() throws RemoteException {
		return this.uniqueID;
	}

	public void setUniqueID(int id) throws RemoteException {
		this.uniqueID = id;
	}
	
		
	public void run() {
		Scanner scanner = new Scanner(System.in);
		String msg;		
		boolean value = true;

		while (value) {
			System.out.println("Enter your command");
			msg = scanner.nextLine();
			try {
			    	
				server.SendMessageOver(uniqueID, msg);
				if (msg.equalsIgnoreCase("quit")) {
					System.exit(1);
					value = false;
				}
			} catch (RemoteException ex) {
				ex.printStackTrace();
			}
		}
	}

	
	public static void main(String[] args) throws NotBoundException, MalformedURLException, RemoteException{
        String host = args[1];
    	String ServerURL = "rmi://"+host+"/RMIServer";
        ServerInterface server = (ServerInterface) Naming.lookup(ServerURL); 
		Scanner scanner = new Scanner(System.in);
		
        new Thread(new Client(args, server)).start(); 
    }


}
