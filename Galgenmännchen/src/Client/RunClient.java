package Client;
import java.rmi.*;
import javax.swing.*;

import Server.ServerInterface;
import Session.SessionInterface;

import java.awt.*;
import java.awt.event.*;

/**
 * RunClient ist die Klasse die der Spieler ausführt. Mit einem Objekt aus dieser Klasse
 * arbeiter die Client Klasse. Hier wird die Oberfläche des Nutzers dargestellt und die wichtigen
 * Methoden implementiert
 * */
 
public class RunClient {

	boolean gamemaker;		//Rolle des Spielers
	ClientInterface clientInt;
	SessionInterface sessionInt;
	ServerInterface serverInt;

	JTextArea textArea=new JTextArea();
	JTextField textField=new JTextField();
	JFrame mainframe=new JFrame();
	JLabel label=new JLabel();
	
	/**
	 * Startet die Anwendung und ruf den Konstruktor dieser Klasse mit dem eingegebenen Namen
	 * auf.
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			String name = JOptionPane.showInputDialog(null, "Name eingeben:");
			if(name != null) {
				RunClient runClient = new RunClient(name);
			} else {
				JOptionPane.showMessageDialog(null, "Weil du versuchst ohne Namen zu spielen wird die Anwendung geschlossen :P.");     
				System.exit(0);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			System.exit(0);
		}
	}

	/**
	 * Konstruktor, der die Oberfläche lädt und das Spiel startet
	 */
	public RunClient(String name) throws Exception {
		serverInt = (ServerInterface)Naming.lookup("rmi://localhost:2344/Galgenmann");
		clientInt = new Client(this);
		sessionInt = serverInt.makeSession(name, clientInt); //Übergibt Server den Namen zur Erzeugung einer neuen Session
		
		
		mainframe.setTitle(name);
		mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainframe.getContentPane().setLayout(new BorderLayout());
		textArea.setEditable(false);
		JScrollPane scroller = new JScrollPane();
		scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroller.getViewport().setView(textArea);

		JButton btnClose = new JButton("schließen");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});

		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					sendMessage(textField.getText());
					textField.setText("");     
				} catch(RemoteException ex) {
					ex.printStackTrace();
				}
			}
		});

		
		mainframe.getContentPane().add(scroller, BorderLayout.CENTER);
		mainframe.getContentPane().add(textField, BorderLayout.NORTH);
		mainframe.getContentPane().add(label, BorderLayout.SOUTH);
		

		mainframe.setSize(400, 600);
		mainframe.setVisible(true);


		if(sessionInt.getId()==1) {		//Der erste der das Spiel startet fängt an mit Wort aussuchen
			gamemaker=true;
		}else {
			gamemaker=false;
		}

		newGame(gamemaker);
	}

	/**
	 * Zeigt die empfangene Nachricht und den Sender an
	 * @param name
	 * @param message
	 */
	public void getMessage(String name, String message) {
		textArea.append(name+": "+message+"\n");
		textArea.setCaretPosition(textArea.getText().length()-1);
	}
	
	/**
	 * Setzt das Label auf den Text den der Server übergibt
	 */
	public void showTmpSolution(String solution) {
		label.setText(solution);
	}

	public void close() {
		System.exit(0);
	}

	/**
	 * Übergibt der Session die Nachricht. Wenn der Client der "Spielmacher" ist,
	 * wird sein Textfeld zur Eingabe unsichtbar, da er dann warten muss.
	 * @param message
	 * @throws RemoteException
	 */
	public void sendMessage(String message) throws RemoteException {
		if(gamemaker) {
			sessionInt.sendMessage(message,gamemaker);
			textField.setVisible(false);
		}else {
			sessionInt.sendMessage(message,gamemaker);
		}
	}


	/**
	 * Startet ein neues Spiel
	 * @param gamemaker
	 */
	public void newGame(boolean gamemaker) {
		this.gamemaker=gamemaker;
		textField.setVisible(true);
		textArea.setText("");
		label.setText("");
		if(gamemaker==true) {
			textArea.setText("Gebe den Begriff ein, der erraten werden soll! \n");
		}else {
			textArea.setText("Warte auf Spielbeginn... \n");
		}
	}

	
	public boolean getGamemaker() {
		return gamemaker;
	}

	
}