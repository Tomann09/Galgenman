package Client;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.Naming;

/**
 * Erbt ebenfalls von UnicastRemoteObject um Anfragen entgegennehmen zu k�nnen. Beschreibt, 
 * was genau die Methoden aus dem Interface k�nnen soll und arbeitet mit einem RunClient
 * Objekt, der u.A. die Oberfl�che des Nutzers darstellt.
 *
 */
public class Client extends UnicastRemoteObject implements ClientInterface {
	RunClient runClient;		//

	public Client() throws RemoteException {
	}

	/**
	 * Konstruktor dem ein runClient Objekt �bergeben wird
	 * @param runClient
	 * @throws RemoteException
	 */
	public Client(RunClient runClient) throws RemoteException {
		this.runClient = runClient;
	}

	@Override
	public void getMessage(String name, String message) {
		runClient.getMessage(name,message);
	}

	@Override
	public void newGame(boolean gamemaker) {
		runClient.newGame(gamemaker);
	}
	
	@Override
	public boolean getGamemaker() {
		return runClient.getGamemaker();
	}

	@Override
	public void showTmpSolution(String solution) throws RemoteException {
		runClient.showTmpSolution(solution);
	}

	

}