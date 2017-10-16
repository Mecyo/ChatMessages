package ui;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Cliente  {

	private ObjectInputStream sInput;
	private ObjectOutputStream sOutput;
	private Socket socket;	
	private TelaClienteTeste tc;
	private String servidor, nomeUsuario;
	private int porta;

	Cliente(String servidor, int porta, String nomeUsuario) {
		this(servidor, porta, nomeUsuario, null);
	}
	Cliente(String servidor, int porta, String nomeUsuario, TelaClienteTeste tc) {
		this.servidor = servidor;
		this.porta = porta;
		this.nomeUsuario = nomeUsuario;
		this.tc = tc;
	}

	public boolean iniciar() {
		try {
			socket = new Socket(servidor, porta);
		} 
		catch(Exception ec) {
			mostrar(new Mensagem(Mensagem.ERRO, "", "Erro conectando ao servidor:" + ec));
			return false;
		}

		String msg = "Conexão aceita " + socket.getInetAddress() + ":" + socket.getPort();
		mostrar(new Mensagem(Mensagem.MENSAGEM, "", msg));

		try
		{
			sInput  = new ObjectInputStream(socket.getInputStream());
			sOutput = new ObjectOutputStream(socket.getOutputStream());
		}
		catch (IOException eIO) {
			mostrar(new Mensagem(Mensagem.ERRO, "", "Erro criando novo stream de Input/output: " + eIO));
			return false;
		} 
		new EscutandoDoServidor().start();
		try
		{
			sOutput.writeObject(new Mensagem(Mensagem.LOGIN, nomeUsuario, ""));
			sOutput.writeObject(new Mensagem(Mensagem.QUEMESTAON, nomeUsuario, ""));
		}
		catch (IOException eIO) {
			mostrar(new Mensagem(Mensagem.ERRO, "", "Erro fazendo login : " + eIO));
			desconectar();
			return false;
		}
		return true;
	}
	
	private void mostrar(Mensagem msg) {
		if(tc == null)
			System.out.println(msg.getMensagem());
		else
			tc.append(msg.getMensagem() + "\n");
	}

	void enviarMensagem(Mensagem msg) {
		try {
			sOutput.writeObject(msg);
		}
		catch(IOException e) {
			mostrar(new Mensagem(Mensagem.ERRO, "", "Erro escrevendo ao servidor: " + e));
		}
	}

	private void desconectar() {
		try { 
			if(sInput != null) sInput.close();
		}
		catch(Exception e) {}
		try {
			if(sOutput != null) sOutput.close();
		}
		catch(Exception e) {}
		try{
			if(socket != null) socket.close();
		}
		catch(Exception e) {}


		if(tc != null)
			tc.connectionFailed();

	}

	public static void main(String[] args) {

		int numeroDaPorta = 6970;
		String enderecoDoServidor = "localhost";
		String nomeUsuario = "Anônimo";

		switch(args.length) {

		case 3:
			enderecoDoServidor = args[2];

		case 2:
			try {
				numeroDaPorta = Integer.parseInt(args[1]);
			}
			catch(Exception e) {
				System.out.println("Número da porta inválido.");
				System.out.println("Uso e: > Cliente java [nomeUsuario] [numeroDaPorta] [serverAddress]");
				return;
			}

		case 1: 
			nomeUsuario = args[0];

		case 0:
			break;

		default:
			System.out.println("Uso e: > Cliente java [nomeUsuario] [numeroDaPorta] [serverAddress]");
			return;
		}

		Cliente client = new Cliente(enderecoDoServidor, numeroDaPorta, nomeUsuario);

		if(!client.iniciar())
			return;

		Scanner scan = new Scanner(System.in);

		while(true) {
			System.out.print("> ");
			
			Mensagem msg = new Mensagem(Integer.getInteger(scan.nextLine()), "", "");

			/*if(msg.getTipo().equals(Mensagem.LOGOUT)) {
				client.enviarMensagem(new Mensagem(Mensagem.LOGOUT, ""));

				break;
			}

			else if(msg.equalsIgnoreCase("QUEMESTAON")) {
				client.enviarMensagem(new Mensagem(Mensagem.QUEMESTAON, ""));				
			}
			else {*/
				client.enviarMensagem(msg);
			//}
				client.desconectar();
				scan.close();
		}

	}

	class EscutandoDoServidor extends Thread {

		public void run() {
			while(true) {
				try {
					Mensagem msg = (Mensagem) sInput.readObject();

					if (tc == null) {
						System.out.println(msg.getMensagem());
						System.out.print("> ");
					} else{
						switch(msg.getTipo()) {

							case Mensagem.MENSAGEM:
								tc.append(msg.getMensagem());
								break;
							case Mensagem.PRIVADO:
								tc.privado(msg.getMensagem());
								break;
							case Mensagem.LOGIN:
								tc.addUser(msg.getNomeUsuario());
								break;
							case Mensagem.LOGOUT:
								tc.removeUser(msg.getNomeUsuario());
								break;
							case Mensagem.QUEMESTAON:
								if(nomeUsuario.equals(msg.getDestino())){
									tc.addUser(msg.getNomeUsuario());
								}
								break;
						}
					}
				}
				catch(IOException e) {
					mostrar(new Mensagem(Mensagem.ERRO, "", "Servidor fechou a conexão: " + e));
					if(tc != null) 
						tc.connectionFailed();
					break;
				}

				catch(ClassNotFoundException e2) {
				}
			}
		}
	}

	public ObjectInputStream getsInput() {
		return sInput;
	}
	public void setsInput(ObjectInputStream sInput) {
		this.sInput = sInput;
	}
	/**
	 * @return the tc
	 */
	public TelaClienteTeste getTc() {
		return tc;
	}
	/**
	 * @param tc the tc to set
	 */
	public void setTc(TelaClienteTeste tc) {
		this.tc = tc;
	}
	/**
	 * @return the socket
	 */
	public Socket getSocket() {
		return socket;
	}
	/**
	 * @param socket the socket to set
	 */
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	/**
	 * @return the sOutput
	 */
	public ObjectOutputStream getsOutput() {
		return sOutput;
	}
	/**
	 * @param sOutput the sOutput to set
	 */
	public void setsOutput(ObjectOutputStream sOutput) {
		this.sOutput = sOutput;
	}
}
