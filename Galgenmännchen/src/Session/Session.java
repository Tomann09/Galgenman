package Session;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import Client.ClientInterface;
import Server.Server;

import java.rmi.Naming;

/**
 * Implementiert die Methoden des Session Interfaces und stellt die Verbidnung zwischen Nutzer 
 * und Server da. Das Erzeugen einer neuen Session geschieht durch den Server.
 *
 */
public class Session extends UnicastRemoteObject implements SessionInterface {
	Server server;
	String name;
	int id;
	ClientInterface clientInt;

	public Session() throws RemoteException {
	}

	public Session(Server server, String name, int id, ClientInterface clientInt) throws RemoteException {
		this.server = server;
		this.name = name;
		this.id=id;
		this.clientInt = clientInt;
	}

	/**
	 * Verarbeitet die auf dem RunClient ausgeführte sendMessage Methode und entscheidet,
	 * was der Server daraufhin ausführen soll
	 */
	@Override
	public void sendMessage(String message, boolean gamemaker) {
		if(gamemaker) {
			server.sendSolution(message, this);
		}else {
			server.sendAnswer(message, this);
		}

	}


	public ClientInterface getClientInt() {
		return clientInt;
	}

	public String getName() {
		return name;
	}
	
	
	/**
	 *Vergleicht zwei Sessions miteinander
	 */
	public boolean isEqual(Session s) {
		try {
			if(this.getId()==s.getId()) {
				return true;
			}else {
				return false;
			}
		} catch (RemoteException e) {
			return false;
		}
	}

	/**
	 * Wir bei RunClient genutzt, um das erste Spiel zu starten
	 */
	@Override
	public int getId() throws RemoteException{
		return id;
	}
	



}