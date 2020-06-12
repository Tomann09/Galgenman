package Session;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Das Interface legt fest, auf welche Methoden der Client zugreifen muss, die 
 * auf Serverseite ausgeführt werden
 */
public interface SessionInterface extends Remote {

  public void sendMessage(String message, boolean gamemaker) throws RemoteException;
  
  public int getId() throws RemoteException;
  
 
  

}