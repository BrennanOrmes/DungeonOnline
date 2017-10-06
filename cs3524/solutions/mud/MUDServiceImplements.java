package cs3524.solutions.mud;

import java.rmi.*; 
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;


public class MUDServiceImplements implements MUDService {
	private MUD mudInstance;
	public int maxusers = 10;

    //public Map<MUD> Server = new ArrayList<MUD>();
    public Map<String, MUD> servers = new HashMap<String, MUD>();

	public void createMUDs() throws RemoteException {
		try {
            servers.put("Mud One", new MUD("mymud.edg","mymud.msg","mymud.thg"));
            servers.put("Mud Two", new MUD("mymud.edg","mymud.msg","mymud.thg"));
		}
		catch (Exception ex) {
			System.err.println("Error creating MUD. Error details: " + ex.getMessage()); 
		}
	}

	public String introduction() throws RemoteException
    {
    	String output = "";
        output = ("----- List of MUD Servers -----\n");
        
        for(Map.Entry<String, MUD> entry : servers.entrySet()) {
            String key = entry.getKey();
            output += (key + "\n");
        }
        output += ("--------------------------------\n");
        output += ("Please type your MUD Server name to connect to: ");
        
        return output;
	}

    public String connect(String inputMudServer) throws RemoteException {
        String output = "";
        if(inputMudServer != ""){
            mudInstance = servers.get(inputMudServer);
            output = ( "Welcome to the MUD Server!\n" );
            output += ( "Please enter a username: " );
        }        
        return output;    
    }

	public String getStartLocation() throws RemoteException {
        return mudInstance.startLocation();
    }
    
     public void updateUserLocation(String username, String location) throws RemoteException {
     	    mudInstance.users.remove(username);
     	    mudInstance.users.put(username, location);
     	    }
    
  public String location(String location) throws RemoteException{
        
       return mudInstance.getVertex(location).toString();
        
 }
    
    public String moveDirection(String current, String direction) throws RemoteException{
        Vertex currentVertex = mudInstance.getVertex(current);
        if(currentVertex._routes.containsKey(direction)){
            Edge newLocation = currentVertex._routes.get(direction);
            Vertex newVert = (newLocation._dest);
            
        return newVert._name;
        } else {
            return current;
        }
}  

public String getPlayersAtLocation(String location) throws RemoteException {
        
        ArrayList<String> Players = new ArrayList<String>();
        String username;
        StringBuilder stringb = new StringBuilder(); 
        Iterator iter = mudInstance.users.keySet().iterator();
        while (iter.hasNext()) {
	    username = iter.next().toString();
            if(mudInstance.users.get(username).equalsIgnoreCase(location)){
                Players.add(username);
                stringb.append(username);
                stringb.append(", ");
            }
	}
        stringb.setLength(stringb.length() - 2);   
        return "You can see: " + stringb.toString(); //String of all players at current location
    }

    public String getInfo(String location) throws RemoteException {
        String info = mudInstance.locationInfo(location);
        return info;
    }
    public boolean takeItem(String item, String location) throws RemoteException {
        Vertex currentVertex = mudInstance.getVertex(location);
        List<String> things = currentVertex._things;        
        if(things.contains(item)){
            mudInstance.delThing(location, item);       
            return true;
        }
        return false;
}
	public boolean addUser(String username) throws RemoteException {
		if(mudInstance.users.size() < maxusers) {
			mudInstance.users.put(username, mudInstance.startLocation());
			return true;
		} else {
			return false;
		}
	}
}