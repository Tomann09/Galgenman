package Server;
import java.rmi.Remote;
import java.rmi.RemoteException;

import Client.ClientInterface;
import Session.SessionInterface;

/**
 * Legt die Methoden des Servers fest, hier nur die Erzeugung einer neuen Sitzung
 */
public interface ServerInterface extends Remote {
	
/**
 * Erzeugt eine neue Session mit dem Namen des Clienten und dem Interface der Clients
 * @param name
 * @param clientInt
 * @return
 * @throws RemoteException
 */
  public SessionInterface makeSession(String name, ClientInterface clientInt) throws RemoteException;
  
}

