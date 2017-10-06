package cs3524.solutions.mud;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.server.UnicastRemoteObject;

public class MUDServerMainline {
	static BufferedReader in = new BufferedReader( new InputStreamReader (System.in));

	public static void main (String args[]) {
		if (args.length < 2) {
			//If the user does not enter two arguements. Being the host and port nummber
			//The following message will appear.
			System.err.println("Missing arguments. Please specify both <host> <port>");
			return;
		}
		//Port
		int registryPort = Integer.parseInt(args[0]);
		//Host
		int serverPort = Integer.parseInt(args[1]);

		System.out.println("Starting server on port " + Integer.toString(registryPort));

		try {
			//Make hostName = to the local host 
			String hostName = (InetAddress.getLocalHost()).getCanonicalHostName();

			//RMI Security policy
			System.setProperty("java.security.policy", "mud.policy");
			System.setSecurityManager(new RMISecurityManager());

			//Make instance of MUDServiceImplements and bind it to mudService
			MUDServiceImplements mudService = new MUDServiceImplements();
			MUDService mudstub = (MUDService)UnicastRemoteObject.exportObject(mudService, serverPort);
			System.out.println("Host name: " + hostName);
			System.out.println("Server Port: " + serverPort);
			System.out.println("Registry Port: " + registryPort);
			String regUrl = "rmi://" + hostName + ":" + registryPort + "/MudService";

			try {
				Naming.rebind(regUrl, mudstub);
			}
			catch (Exception e) {
				System.out.println("Error rebinding: " + e.getMessage());
			}
			System.out.println("Server running at: " + regUrl);
			System.out.println("Creating default MUD");

			mudService.createMUDs();
		}
		catch(Exception b){

		}
	}
}