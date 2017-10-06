package cs3524.solutions.mud;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MUDService extends Remote
{
    public String introduction() throws RemoteException;
    public String connect(String inputMudServer) throws RemoteException;
    public String getStartLocation() throws RemoteException;
    public String location(String location) throws RemoteException;
    public String moveDirection(String current, String direction) throws RemoteException;
    public boolean addUser(String username) throws RemoteException;
    public void updateUserLocation(String username, String location) throws RemoteException ;
    public String getPlayersAtLocation(String location) throws RemoteException;
    public boolean takeItem(String item, String location) throws RemoteException;
    public void createMUDs() throws RemoteException;
    public String getInfo(String location) throws RemoteException;
    
}