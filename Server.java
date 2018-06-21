import java.rmi.*;
import java.rmi.registry.*;
import java.util.*;
import java.rmi.server.*;


public class Server extends UnicastRemoteObject implements ServerInterface {

	
	private ArrayList<ClientInterface> clients; 
	public int uid = 1;

	public synchronized void addClient(ClientInterface client)
			throws RemoteException {
		
		boolean cc_a = true;
		for(int i=0;i<clients.size();i++){ 
			if(clients.get(i).getName().equalsIgnoreCase(client.getName())){
				cc_a = false;
				client.showErrorMsg("Server", "Duplicate username found. Please try again with a different username.");
				break;				
			}
		}
		
		if(cc_a == true){
			client.setUniqueID(uid);
			this.clients.add(client);
			System.out.println();
			System.out.println("Id   Name   Sex   Age	Location");

					System.out.println(client.getUniqueID() + "   " + client.getName() + "   " + client.getSex() + "   " + client.getAge()+"   " + client.getLocationX()+ "	" +client.getLocationY());
	
			uid++;
		}
	}
	
	protected Server() throws RemoteException {
		clients = new ArrayList<ClientInterface>();	
		System.out.println();
		System.out.println("Server connection established !!");
	}

	

	
	public synchronized void SendMessageOver(int id, String msg)
			throws RemoteException { //id = msg sender's id

		ArrayList<String> collection = new ArrayList<String>();
		for (String retval : msg.split(" ")) {
			collection.add(retval);
		}

		if (msg.equalsIgnoreCase("quit")
				|| collection.get(0).equalsIgnoreCase("quit")) {
			for (int i = 0; i < clients.size(); i++) {
				if (clients.get(i).getUniqueID() == id) {
					System.out.println(clients.get(i).getName()
							+ " has left.");
					clients.remove(i); //remove client
				}
			}
		} else if (collection.get(0).equalsIgnoreCase("send")) {
			
			String sendMsg = "";
			for (int i = 2; i < collection.size(); i++) {
				sendMsg = sendMsg + collection.get(i) + " ";
			}
			
			String sender_name = "$";
			Client c;
			for(int i=0;i<clients.size();i++){
				if(clients.get(i).getUniqueID() == id){
					sender_name = clients.get(i).getName();					
				}
			}
			boolean check = true;
			for (int i = 0; i < clients.size(); i++) {
				if (clients.get(i).getUniqueID() == Integer.parseInt(collection.get(1)) && (clients.get(i).getUniqueID() != id)) {					
					clients.get(i).showMesg(sender_name,sendMsg); 					
				}else check = false;
			}
			
		} else if(collection.get(0).equalsIgnoreCase("get")){ //getting information
			String location = "";
			for(int i=0;i<clients.size();i++){
				if(clients.get(i).getUniqueID() == id){
					location = location + clients.get(i).getLocationX() + " " + clients.get(i).getLocationY();
					clients.get(i).showMesg("Own Location", location);
				}
			}			
		}else if(collection.get(0).equalsIgnoreCase("go")){ //changing location
			double locationX = 0;
			double locationY = 0;
			for(int i=0;i<clients.size();i++){
				if(clients.get(i).getUniqueID() == id){
					locationX = Double.parseDouble(collection.get(1)) + clients.get(i).getLocationX();
					locationY = Double.parseDouble(collection.get(2)) + clients.get(i).getLocationY();
					clients.get(i).setLocationX(locationX);
					clients.get(i).setLocationY(locationY);
					String location = String.valueOf(locationX) + " " + String.valueOf(locationY);
					clients.get(i).showMesg("New Location", location);
				}	
			}
		}else if(collection.get(0).equalsIgnoreCase("list")){ //find client list with euclid formula
			double range = Double.parseDouble(collection.get(1));
			int index = -1;
			
			double x=0;
			double y=0;
			for(int i=0;i<clients.size();i++){
				if(id == clients.get(i).getUniqueID()){
					index = i;
					x = clients.get(i).getLocationX();
					y = clients.get(i).getLocationY();
				}
			}
			if(index == -1){
				System.err.println("No such Client Found");
				System.exit(1);
			}
			
			clients.get(index).clearList();
			for(int i=0;i<clients.size();i++){
				if(clients.get(i).getUniqueID() == id){
					continue;
				}
				double a = clients.get(i).getLocationX();
				double b = clients.get(i).getLocationY();
				double circumference = (x-a)*(x-a) + (y-b)*(y-b); 
				if(circumference<=range*range){
					clients.get(index).retrieveList(clients.get(i));
				}
			}
			clients.get(index).printList();
		}
	}
	  public static void main(String [] args) throws RemoteException{
	    	Registry registry = LocateRegistry.getRegistry(1099); 
	        registry.rebind("RMIServer",new Server()); 
	    }
}
