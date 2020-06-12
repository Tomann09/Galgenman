package Client;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface f�r die Implementierung der Client Klasse. Legt die Methoden fest, auf die der Server 
 * zugreifen k�nnen muss, um z.B. bei der Benutzerfl�che individuelle Anzeigen zu erm�glichen.
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
	 * Zeigt die aktuelle L�sung die auf dem Server gespeichert ist
	 * @param solution
	 * @throws RemoteException
	 */
	public void showTmpSolution(String solution) throws RemoteException; //Anzeige der temor�r gel�sten L�sung

}