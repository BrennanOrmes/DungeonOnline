package cs3524.solutions.mud;

/*

DONE:

All CAS D Parts
Users can move in world
Users can see other users in world
Users can obtain items within world
Generate more MUD's
Display list of MUD's that are running
Restrict Number of user's

TO DO:

Update error handling, if a user just types "move" or "take", client crashes.
Let user add new MUD's
Restrict number of MUD's

*/

import java.rmi.Naming; 
import java.rmi.RMISecurityManager;
import java.net.InetAddress;
import java.util.Iterator;
import java.rmi.server.UnicastRemoteObject;
import java.io.InputStreamReader;
import java.util.List;
import java.io.BufferedReader;

public class MUDClient {
	static MUDService service;

	static BufferedReader in = new BufferedReader( new InputStreamReader( System.in ));
    private static String username;
    private static String location; 
    private static String servername;
	//private List<String> inv;

	public static void main(String args[]) throws Exception {
		// Check for missing arguments
		if(args.length < 2) {
			System.err.println("Missing arguments. Please specify both <host> <port>");
			return;
		}

		String hostName = args[0];
		int port = Integer.parseInt(args[1]);

		try{
			//Create registration URL from hostname, port
			String regUrl = "rmi://" + hostName + ":" + port + "/MudService";
			service = (MUDService)Naming.lookup(regUrl);

			setup();
		}
		catch (java.io.IOException e) {
			System.err.println("There has been an input error!");
			System.err.println(e.getMessage());
		}
	}

	static void setup() throws Exception {

		//Print out intro from Implements
		System.out.println(service.introduction());
		servername = in.readLine();
		System.out.println(service.connect(servername));
		//Get username
		username = in.readLine();
		//Set start location
		location = service.getStartLocation();
		
		// Calls addUser from Implements
		if(service.addUser(username)) {
			playGame();

		} else {
			System.out.println("The server is currently full or cannot hanndle your request right now. Please try again later...");
		}
		

	}

	static void playGame() throws Exception {
		//Set start location
		location = service.getStartLocation();
		System.out.println("You are in " + location);
		service.updateUserLocation(username, location);
		//setup variable for user action
		String action = "";
		//setup validplay variable to show if the user is still playing.
		boolean validplay = true;
		try{
			while (validplay) { // Start While
				//Get user input
				System.out.println("What will you do? Type help for help");
				action = in.readLine();
				action = action.toLowerCase();
				//Using if statements decide what to do based on action variable.
				//Split the action so we can know direction the player wants to move after the space, without forcing the player to use two inputs.
				
				if (action.toLowerCase().contains("move")){
					String actionSplit[] = action.split(" "); // Split the action where the space is. Makes user input clean.
					String direction = actionSplit[1];
					//Reminder || = OR
			    	if(direction.equalsIgnoreCase("north") || direction.equalsIgnoreCase("east") || direction.equalsIgnoreCase("south") || direction.equalsIgnoreCase("west")){
                		//Pass newLocation to service.
                		String newLocation = service.moveDirection(location, direction);
                    	if(newLocation.equals(location)) {
                        	System.out.println("Can not move " + direction);
                        } else {
                        	//Set new location for the player where he moved to
                       		location = newLocation;
                       		//Update location
                        	service.updateUserLocation(username, location);
                        }
                    } else {
                    	System.out.println("Unknown Direction " + direction);
                    }
				} else if (action.equalsIgnoreCase("help")) {
					//Print list of commands:
					System.out.println("command: move <direction>");
					System.out.println("<direction> being	north \neast \nsouth \nwest"); 
					System.out.println("command: 		take <item name>");
					System.out.println("command: 		where am I?");
					System.out.println("command: 		who goes there?");
					System.out.println("command: 		quit");
				} else if (action.contains ("take")) {
					String splt[] = action.split (" ");
					String item = splt[1];
					//if takeItem returns true let them pick up the item
					if (service.takeItem(item, location)) {
						System.out.println("You have obtained the " + item);
					} else {
						System.out.println("Could not take the " + item);
					} 
				} else if (action.equalsIgnoreCase("quit")) {
					validplay = false; // End While when the validplay = false
				} else if (action.equalsIgnoreCase("where am i") || action.equalsIgnoreCase("where am i?")) {
					System.out.println("You are in " + location);
				} else if (action.equalsIgnoreCase ("look")) {
					System.out.println(service.getInfo(location));
					//Print other user's names within the same location
				} else if (action.equalsIgnoreCase ("who goes there?") || action.equalsIgnoreCase("who is there?")) {
					System.out.println(service.getPlayersAtLocation(location));
					System.out.println("\n");
			}
		}
	}
		catch(Exception e){
			return;
		}
	}
}