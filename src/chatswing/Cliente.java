package chatswing;

import java.net.*;
import java.io.*;
import java.util.*;

public class Cliente  {

	private ObjectInputStream sInput;
	private ObjectOutputStream sOutput;
	private Socket socket;	
	private TelaCliente tc;
	private String servidor, nomeUsuario;
	private int porta;

	Cliente(String servidor, int porta, String nomeUsuario) {
		this(servidor, porta, nomeUsuario, null);
	}
	Cliente(String servidor, int porta, String nomeUsuario, TelaCliente tc) {
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
			mostrar("Erro conectando ao servidor:" + ec);
			return false;
		}

		String msg = "Conexão aceita " + socket.getInetAddress() + ":" + socket.getPort();
		mostrar(msg);

		try
		{
			sInput  = new ObjectInputStream(socket.getInputStream());
			sOutput = new ObjectOutputStream(socket.getOutputStream());
		}
		catch (IOException eIO) {
			mostrar("Erro criando novo stream de Input/output: " + eIO);
			return false;
		} 
		new EscutandoDoServidor().start();
		try
		{
			sOutput.writeObject(nomeUsuario);
		}
		catch (IOException eIO) {
			mostrar("Erro fazendo login : " + eIO);
			desconectar();
			return false;
		}
		return true;
	}
	private void mostrar(String msg) {
		if(tc == null)
			System.out.println(msg);
		else
			tc.append(msg + "\n");
	}

	void enviarMensagem(Mensagem msg) {
		try {
			sOutput.writeObject(msg);
		}
		catch(IOException e) {
			mostrar("Erro escrevendo ao servidor: " + e);
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
		String nomeUsuario = "Anonimo";

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

			String msg = scan.nextLine();

			if(msg.equalsIgnoreCase("LOGOUT")) {
				client.enviarMensagem(new Mensagem(Mensagem.LOGOUT, ""));

				break;
			}

			else if(msg.equalsIgnoreCase("QUEMESTAON")) {
				client.enviarMensagem(new Mensagem(Mensagem.QUEMESTAON, ""));				
			}
			else {
				client.enviarMensagem(new Mensagem(Mensagem.MENSAGEM, msg));
			}
		}

		client.desconectar();	
	}

	class EscutandoDoServidor extends Thread {

		public void run() {
			while(true) {
				try {
					String msg = (String) sInput.readObject();

					if(tc == null) {
						System.out.println(msg);
						System.out.print("> ");
					}
					else {
						tc.append(msg);
					}
				}
				catch(IOException e) {
					mostrar("Servidor fechou a conexão: " + e);
					if(tc != null) 
						tc.connectionFailed();
					break;
				}

				catch(ClassNotFoundException e2) {
				}
			}
		}
	}
}
