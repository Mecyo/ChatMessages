package chatswing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TelaCliente extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JLabel lblDigiteSeuNome;
	private JTextField tf;
	private JTextField tfServidor, tfPorta;
	private JButton login, logout, quemEstaOnline;
	private JTextArea ta;
	private boolean conectado;
	private Cliente cliente;
	private int portaDefault;
	private String hostDefault;

	TelaCliente(String host, int porta) {
		super("Chat Client");
		portaDefault = porta;
		hostDefault = host;

		JPanel PainelNorte = new JPanel(new GridLayout(3,1));
		JPanel serverAndPort = new JPanel(new GridLayout(1,5, 1, 3));
		tfServidor = new JTextField(host);
		tfPorta = new JTextField("" + porta);
		tfPorta.setHorizontalAlignment(SwingConstants.RIGHT);

		serverAndPort.add(new JLabel("Endereço do Servidor:  "));
		serverAndPort.add(tfServidor);
		serverAndPort.add(new JLabel(" Número da porta:  "));
		serverAndPort.add(tfPorta);
		serverAndPort.add(new JLabel(""));
		PainelNorte.add(serverAndPort);

		lblDigiteSeuNome = new JLabel("Digite seu nome de usuário abaixo", SwingConstants.CENTER);
		PainelNorte.add(lblDigiteSeuNome);
		tf = new JTextField("Anônimo");
		tf.setBackground(Color.WHITE);
		PainelNorte.add(tf);
		getContentPane().add(PainelNorte, BorderLayout.NORTH);

		ta = new JTextArea("Bem-vindo a sala de Chat!\n", 80, 80);
		JPanel PainelCentro = new JPanel(new GridLayout(1,1));
		PainelCentro.add(new JScrollPane(ta));
		ta.setEditable(false);
		getContentPane().add(PainelCentro, BorderLayout.CENTER);

		login = new JButton("Login");
		login.addActionListener(this);
		logout = new JButton("Logout");
		logout.addActionListener(this);
		logout.setEnabled(false);
		quemEstaOnline = new JButton("Quem está online");
		quemEstaOnline.addActionListener(this);
		quemEstaOnline.setEnabled(false);

		JPanel PainelSul = new JPanel();
		PainelSul.add(login);
		PainelSul.add(logout);
		PainelSul.add(quemEstaOnline);
		getContentPane().add(PainelSul, BorderLayout.SOUTH);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(600, 600);
		setVisible(true);
		tf.requestFocus();
	}

	void append(String str) {
		ta.append(str);
		ta.setCaretPosition(ta.getText().length() - 1);
	}
	void connectionFailed() {
		login.setEnabled(true);
		logout.setEnabled(false);
		quemEstaOnline.setEnabled(false);
		lblDigiteSeuNome.setText("Digite seu nome de usuário abaixo");
		tf.setText("Anônimo");
		tfPorta.setText("" + portaDefault);
		tfServidor.setText(hostDefault);
		tfServidor.setEditable(false);
		tfPorta.setEditable(false);
		tf.removeActionListener(this);
		conectado = false;
	}

	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if(o == logout) {
			cliente.enviarMensagem(new Mensagem(Mensagem.LOGOUT, ""));
			return;
		}
		if(o == quemEstaOnline) {
			cliente.enviarMensagem(new Mensagem(Mensagem.QUEMESTAON, ""));				
			return;
		}
		if(conectado) {
			cliente.enviarMensagem(new Mensagem(Mensagem.MENSAGEM, tf.getText()));				
			tf.setText("");
			return;
		}
		if(o == login) {
			String nomeUsuario = tf.getText().trim();
			if(nomeUsuario.length() == 0)
				return;
			String servidor = tfServidor.getText().trim();
			if(servidor.length() == 0)
				return;
			String numeroDaPorta = tfPorta.getText().trim();
			if(numeroDaPorta.length() == 0)
				return;
			int porta = 0;
			try {
				porta = Integer.parseInt(numeroDaPorta);
			}
			catch(Exception en) {
				return;
			}

			cliente = new Cliente(servidor, porta, nomeUsuario, this);

			if(!cliente.iniciar()) 
				return;
			tf.setText("");
			lblDigiteSeuNome.setText("Digite sua mensagem abaixo");
			conectado = true;

			login.setEnabled(false);
			logout.setEnabled(true);
			quemEstaOnline.setEnabled(true);
			tfServidor.setEditable(false);
			tfPorta.setEditable(false);
			tf.addActionListener(this);
		}

	}

	public static void main(String[] args) {
		new TelaCliente("10.130.163.52", 6970);
	}

}