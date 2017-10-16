package chatswing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TelaServidor extends JFrame implements ActionListener, WindowListener {
	
	private static final long serialVersionUID = 1L;
	private JButton stopStart;
	private JTextArea chat, event;
	private JTextField tPortNumber;
	private Servidor servidor;

	TelaServidor(int port) {
		super("Chat Server");
		servidor = null;
		JPanel Norte = new JPanel();
		Norte.add(new JLabel("Numero da porta: "));
		tPortNumber = new JTextField("  " + port);
		Norte.add(tPortNumber);
		stopStart = new JButton("Start");
		stopStart.addActionListener(this);
		Norte.add(stopStart);
		getContentPane().add(Norte, BorderLayout.NORTH);

		JPanel Centro = new JPanel(new GridLayout(2,1));
		chat = new JTextArea(80,80);
		chat.setEditable(false);
		anexarSala("Sala de Chat.\n");
		Centro.add(new JScrollPane(chat));
		event = new JTextArea(80,80);
		event.setEditable(false);
		anexarEvento("Log de eventos.\n");
		Centro.add(new JScrollPane(event));	
		getContentPane().add(Centro);

		addWindowListener(this);
		setSize(400, 600);
		setVisible(true);
	}

	void anexarSala(String str) {
		chat.append(str);
		chat.setCaretPosition(chat.getText().length() - 1);
	}
	void anexarEvento(String str) {
		event.append(str);
		event.setCaretPosition(chat.getText().length() - 1);
		
	}

	public void actionPerformed(ActionEvent e) {
		if(servidor != null) {
			servidor.parar();
			servidor = null;
			tPortNumber.setEditable(true);
			stopStart.setText("Start");
			return;
		}

		int porta;
		try {
			porta = Integer.parseInt(tPortNumber.getText().trim());
		}
		catch(Exception er) {
			anexarEvento("Numero da porta invalida");
			return;
		}

		servidor = new Servidor(porta, this);
		new ServerRunning().start();
		stopStart.setText("Stop");
		tPortNumber.setEditable(false);
	}

	public static void main(String[] arg) {
		new TelaServidor(6970);
	}

	public void windowClosing(WindowEvent e) {
		if(servidor != null) {
			try {
				servidor.parar();
			}
			catch(Exception eClose) {
			}
			servidor = null;
		}
		dispose();
		System.exit(0);
	}

	public void windowClosed(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}


	class ServerRunning extends Thread {
		public void run() {
			servidor.start();
			stopStart.setText("Start");
			tPortNumber.setEditable(true);
			anexarEvento("Servidor caiu\n");
			servidor = null;
		}
	}

}