package Server;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.util.List;

import Client.ClientInterface;
import Session.SessionInterface;
import Session.Session;

import java.util.ArrayList;

public class Server extends UnicastRemoteObject implements ServerInterface {
	private List sessionList = new ArrayList(); //Liste die mit den einzelnen Sessions gefüllt ist
	private String solution;
	private String tmpSolution="";
	private int lifes=12;

	public Server() throws RemoteException {
	}

	/**
	 * Startet Server mit Port 2344
	 * @param args
	 */
	public static void main(String args[]) {

		try {
			Registry reg=LocateRegistry.createRegistry(2344);
			reg.rebind("Galgenmann", new Server());
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}


	/**
	 * Verarbeitet den Lösungsvorschlag und prüft, ob das Wort gefunden wurde. Wenn ja
	 * wird ein neues Spiel gestartet.
	 * @param message
	 * @param s
	 */
	public void sendAnswer(String message, Session s) {
		message=message.toUpperCase();
		Session tmpSession;
		String tmpSolutionSplit[];
		String tmpSolutionSave="";
		boolean found=false;

		for(int j=0; j < this.sessionList.size(); j++) {		//Schreibt nachricht des Nutzers über die verschiedenen Sessions an die einzelnen Clients
			tmpSession = (Session)this.sessionList.get(j);
			try {
				tmpSession.getClientInt().getMessage(s.getName(),message);
			} catch(RemoteException e) {						//Wenn der Nuter nicht erreicht wird, wird er vom Server geschmissen, indem die Sessions miteinander verglichen werden
				Session tmp2Session;
				System.out.println(tmpSession.getName()+" ist unerreichbar und wird entfernt...");
				for(int i=0; i<sessionList.size();i++) {
					tmp2Session=(Session)this.sessionList.get(i);
					if(tmp2Session.isEqual(tmpSession)) {
						sessionList.remove(i);
					}
				}
			}
		}


		if(solution!=null){				//Wenn die gespeicherte Lösung ungleich null, also das Spiel begonnen hat
			if(message.length()==1) {	//Unterscheidung in Buchtsabenvorschläge und Lösungswortvorschläge
				char input=message.charAt(0);
				suche:
					for(int i=0; i<solution.length();i++) {		//Geht alle Buchstaben durch und prüft, ob sie im Lösungswort sind
						if(input==solution.charAt(i)) {
							found=true;
							for(int j=0; j < this.sessionList.size(); j++) {
								tmpSession = (Session)this.sessionList.get(j);
								try {
									tmpSession.getClientInt().getMessage("Answer-Bot", "Richtig: "+message.charAt(0));
								} catch(Exception ex) {
									System.out.println(ex.getMessage());
								}
							}
							break suche;
						}
					}

				if(found==true) {		
					tmpSolutionSplit=tmpSolution.split(" ");
					for(int i=0; i<solution.length();i++) {		//Füllt die temporäre Lösung mit dem gefundenen Buchstaben
						
						
						if(input==solution.charAt(i)){
							if(!String.valueOf(input).equals(null)) {
								tmpSolutionSplit[i]=String.valueOf(input);
							}
						}
					}
					for(int k=0; k<tmpSolutionSplit.length;k++) {
						tmpSolutionSave+=tmpSolutionSplit[k]+" ";
					}

					tmpSolution=tmpSolutionSave;

				}else {		//Wenn der Buchstabe nicht gefunden wurde werden die Leben abgezogen
					lifes-=1;
					for(int j=0; j < this.sessionList.size(); j++) {
						tmpSession = (Session)this.sessionList.get(j);
						try {
							tmpSession.getClientInt().getMessage("Answer-Bot", "Falsch: "+ message.charAt(0));
						} catch(Exception ex) {

						}
					}
				}
				this.showInformation();
			}

			if(message.length()!=1) {

				if(message.equals(solution)) {	//Wenn das eingegebene Wort das Lösungswort ist
					found=true;
					for(int j=0; j < this.sessionList.size(); j++) {
						tmpSession = (Session)this.sessionList.get(j);
						try {
							if(s.isEqual(tmpSession)) {
								tmpSession.getClientInt().newGame(true);	//starte neues Spiel als Spielmacher
								tmpSession.getClientInt().getMessage("Answer-Bot", "Du hast das Lösungswort "+solution+" erraten!");
							}
							else {
								tmpSession.getClientInt().newGame(false);	//starte neues Spiel als Mitspieler
								tmpSession.getClientInt().getMessage("Answer-Bot", s.getName()+" hat das Lösungswort "+solution+" erraten!");
							}

						} catch(Exception ex) {
							System.out.println(ex.getMessage());
						}
					}
					this.reset();
				}
				else {
					lifes-=1;
					for(int j=0; j < sessionList.size(); j++) {
						tmpSession = (Session)sessionList.get(j);
						try {
							tmpSession.getClientInt().getMessage("Answer-Bot",message+" ist nicht das Lösungsowrt!");
						} catch(Exception ex) {
							System.out.println(ex.getMessage());
						}
					}
					this.showInformation();	
				}

			}


			if(solution!=null&&this.isTmpSolutionFilled()) {	//Abfrage ungleich null, da nach restart wenn die Lösung gefunden wurde auch null ist
				for(int j=0; j < this.sessionList.size(); j++) {
					tmpSession = (Session)this.sessionList.get(j);
					try {
						if(s.isEqual(tmpSession)) {
							tmpSession.getClientInt().newGame(true);
							tmpSession.getClientInt().getMessage("Answer-Bot", "Du hast das Lösungswort "+solution+" erraten!");
						}
						else {
							tmpSession.getClientInt().newGame(false);
							tmpSession.getClientInt().getMessage("Answer-Bot",s.getName()+ " hat das Lösungswort "+solution+" erraten!");
						}

					} catch(Exception ex) {
						System.out.println(ex.getMessage());
					}
				}
				this.reset();
			}


			if(solution!=null&&lifes<=0) { //Startet neues Spiel, wenn Wort nicht gefunden wurde
				for(int j=0; j < this.sessionList.size(); j++) {
					tmpSession = (Session)this.sessionList.get(j);
					try {
						if(tmpSession.getClientInt().getGamemaker())
							tmpSession.getClientInt().newGame(true);
						else
							tmpSession.getClientInt().newGame(false);
						tmpSession.getClientInt().getMessage("Answer-Bot","Das Lösungswort "+solution+" wurde nicht erraten!");
					} catch(Exception ex) {
						System.out.println(ex.getMessage());
					}
				}
				this.reset();
			}

		}
	}


	/**
	 * wird über die Session vom Spielmacher ausgeführt, um das Lösung festzulegen
	 * @param solution
	 * @param session
	 */
	public void sendSolution(String solution, Session session) {
		this.solution=solution.toUpperCase();
		for (int i=0; i<solution.length();i++) {
			this.tmpSolution+="_ ";
		}

		Session tmpSession;	
		for(int j=0; j < this.sessionList.size(); j++) {
			tmpSession = (Session)this.sessionList.get(j);
			try {
				tmpSession.getClientInt().getMessage("Answer-Bot", "Das Lösungswort steht fest, Los Gehts!");

			} catch(RemoteException e) {
				Session tmp2Session;
				System.out.println(tmpSession.getName()+" ist unerreichbar und wird entfernt...");
				for(int i=0; i<sessionList.size();i++) {
					tmp2Session=(Session)this.sessionList.get(i);
					if(tmp2Session.isEqual(tmpSession)) {
						sessionList.remove(i);
					}
				}
			}
		}
		this.showInformation();
	}


	/**
	 * Erstellt die neue Session mit Nutzernamen
	 */
	public SessionInterface makeSession(String name, ClientInterface clientInt) throws RemoteException {
		System.out.println(name +" ist der Sitzung beigetreten.");
		SessionInterface sessionInt = new Session(this, name, this.sessionList.size()+1,clientInt);
		this.sessionList.add(sessionInt);
		return sessionInt;
	}


	/**
	 * Setzt nach einem Spiel alles zurück
	 */
	private void reset() {
		solution=null;
		tmpSolution="";
		lifes=12;
	}

	/**
	 * Prüft, ob die temporäre Lösung gefüllt ist
	 * @return
	 */
	public boolean isTmpSolutionFilled() {
		boolean filled=true;
		for(int o=0; o<tmpSolution.length();o++) {
			if(tmpSolution.charAt(o)=='_') //Da "_" durch Buchstaben ersetzt werden, dürfen keine "_" vorhanden sein
				filled=false;
		}

		return filled;
	}

	/**
	 * Gibt Leben und temporäre Lösung weiter zur Anzeige auf der RunClient Oberfläche
	 */
	public void showInformation() {
		for(int j=0; j < this.sessionList.size(); j++) {
			Session tmpSession = (Session)this.sessionList.get(j);
			try {
				tmpSession.getClientInt().showTmpSolution("  "+tmpSolution+"  ;  "+ "Leben: "+lifes);
			} catch(Exception ex) {
				System.out.println(ex.getMessage());
			}
		}
	}

}