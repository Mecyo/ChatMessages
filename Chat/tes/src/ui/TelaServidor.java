package ui;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import java.awt.*;
import java.awt.event.*;

public class TelaServidor extends JFrame implements ActionListener, WindowListener {
	
	private static final long serialVersionUID = 1L;
	private JButton stopStart;
	private JTextPane chat, event;
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
		chat = new JTextPane();
		chat.setEditable(false);
		anexarSala("Sala de Chat.\n");
		Centro.add(new JScrollPane(chat));
		event = new JTextPane();
		event.setEditable(false);
		anexarEvento("Log de eventos.\n");
		Centro.add(new JScrollPane(event));	
		getContentPane().add(Centro);

		addWindowListener(this);
		setSize(400, 600);
		setVisible(true);
	}

	public void anexarSala(String stringComSeuTexto) {
		
        StyledDocument doc = chat.getStyledDocument();
        Style style = chat.addStyle("mudaCor", null);
        StyleConstants.setForeground(style, Color.RED);

        try { 
            doc.insertString(doc.getLength(), "Você disse: " + stringComSeuTexto + "\n",style); }
        catch(BadLocationException e){}

		/*chat.append(str);
		chat.setCaretPosition(chat.getText().length() - 1);*/
	}
	public void anexarEvento(String str) {
		StyledDocument doc = event.getStyledDocument();
        Style style = event.addStyle("mudaCor", null);
        StyleConstants.setForeground(style, Color.BLUE);

        try { 
            doc.insertString(doc.getLength(), "Você disse: " + str + "\n",style); }
        catch(BadLocationException e){}
		/*event.append(str);
		event.setCaretPosition(chat.getText().length() - 1);*/
		
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
			anexarEvento("Número da porta inválido");
			return;
		}

		servidor = new Servidor(porta, this);
		ServerRunning sr = new ServerRunning(servidor, this);
                        sr.start();
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


    public JButton getStopStart() {
        return stopStart;
    }

    public void setStopStart(JButton stopStart) {
        this.stopStart = stopStart;
    }

    public JTextField gettPortNumber() {
        return tPortNumber;
    }

    public void settPortNumber(JTextField tPortNumber) {
        this.tPortNumber = tPortNumber;
    }

	/**
	 * @return the chat
	 */
	public JTextPane getChat() {
		return chat;
	}

	/**
	 * @param chat the chat to set
	 */
	public void setChat(JTextPane chat) {
		this.chat = chat;
	}

	/**
	 * @return the event
	 */
	public JTextPane getEvent() {
		return event;
	}

	/**
	 * @param event the event to set
	 */
	public void setEvent(JTextPane event) {
		this.event = event;
	}      
}