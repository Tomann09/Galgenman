package Client;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface für die Implementierung der Client Klasse. Legt die Methoden fest, auf die der Server 
 * zugreifen können muss, um z.B. bei der Benutzerfläche individuelle Anzeigen zu ermöglichen.
 * @author torbe
 *
 */
public interface ClientInterface extends Remote {
	
	
	/**
	 * Entfangen einer "Nachricht"
	 * @param name
	 * @param message
	 * @throws RemoteException
	 */
	public void getMessage(String name, String message) throws RemoteException;	
	
	/**
	 * Erzeugt ein neues Spiel mit den Spielrechten
	 * @param gamemaker
	 * @throws RemoteException
	 */
	public void newGame(boolean gamemaker) throws RemoteException; //Starten eines neuen Spiels
	
	/**
	 * Abfrage der Spielrechte eines Nutzers
	 * @return
	 * @throws RemoteException
	 */
	public boolean getGamemaker() throws RemoteException; //Abfrage des Spielerstatus
	
	
	/**
	 * Zeigt die aktuelle Lösung die auf dem Server gespeichert ist
	 * @param solution
	 * @throws RemoteException
	 */
	public void showTmpSolution(String solution) throws RemoteException; //Anzeige der temorär gelösten Lösung

}