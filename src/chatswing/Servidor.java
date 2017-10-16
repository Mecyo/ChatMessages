package chatswing;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Servidor {
	private static int uniqueId;
	private ArrayList<ClienteThread> al;
	private TelaServidor ts;
	private SimpleDateFormat sdf;
	private int porta;
	private boolean continuar;
	
	public Servidor(int porta) {
		this(porta, null);
	}
	
	public Servidor(int porta, TelaServidor ts) {
		this.ts = ts;
		this.porta = porta;
		sdf = new SimpleDateFormat("HH:mm:ss");
		al = new ArrayList<ClienteThread>();
	}
	
	public void start() {
		continuar = true;
		try 
		{
			ServerSocket serverSocket = new ServerSocket(porta);
			while(continuar) 
			{
				mostrar("Servidor esperando por clientes na porta " + porta + ".");				
				Socket socket = serverSocket.accept();
				if(!continuar)
					break;
				ClienteThread t = new ClienteThread(socket);
				al.add(t);
				t.start();
			}

			try {
				serverSocket.close();
				for(int i = 0; i < al.size(); ++i) {
					ClienteThread tc = al.get(i);
					try {
					tc.sInput.close();
					tc.sOutput.close();
					tc.socket.close();
					}
					catch(IOException ioE) {
						
					}
				}
			}
			catch(Exception e) {
				mostrar("Houve um erro! O servidor e os clientes: " + e +" serão encerrados.");
			}
		}

		catch (IOException e) {
            String msg = sdf.format(new Date()) + " Erro no novo ServerSocket: " + e + "\n";
            mostrar(msg);
		}
	}

	protected void parar() {
		continuar = false;
		try {
			new Socket("localhost", porta);
		}
		catch(Exception e) {
			
		}
	}

	private void mostrar(String msg) {
		String tempo = sdf.format(new Date()) + " " + msg;
		if(ts == null)
			System.out.println(tempo);
		else
			ts.anexarEvento(tempo + "\n");
	}

	private synchronized void broadcast(String mensagem) {
		String tempo = sdf.format(new Date());
		String mensagemLf = tempo + " " + mensagem + "\n";
		if(ts == null)
			System.out.print(mensagemLf);
		else
			ts.anexarSala(mensagemLf);

		for(int i = al.size(); --i >= 0;) {
			ClienteThread ct = al.get(i);

			if(!ct.escreverMensagem(mensagemLf)) {
				al.remove(i);
				mostrar("Cliente desconectado " + ct.nomeUsuario + " removido da lista.");
			}
		}
	}

	synchronized void remover(int id) {

		for(int i = 0; i < al.size(); ++i) {
			ClienteThread ct = al.get(i);

			if(ct.id == id) {
				al.remove(i);
				return;
			}
		}
	}

	public static void main(String[] args) {

		int numeroDaPorta = 6970;
		switch(args.length) {
			case 1:
				try {
					numeroDaPorta = Integer.parseInt(args[0]);
				}
				catch(Exception e) {
					System.out.println("Número da porta inválida.");
					System.out.println("Uso e: > Servidor java [numeroDaPorta]");
					return;
				}
			case 0:
				break;
			default:
				System.out.println("Uso e: > Servidor java [numeroDaPorta]");
				return;
				
		}

		Servidor servidor = new Servidor(numeroDaPorta);
		servidor.start();
	}

	
	class ClienteThread extends Thread {

		Socket socket;
		ObjectInputStream sInput;
		ObjectOutputStream sOutput;
		int id;
		String nomeUsuario;
		Mensagem cm;
		Date data;
		SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
		
		
		  

		ClienteThread(Socket socket) {
			id = ++uniqueId;
			this.socket = socket;
			System.out.println("Thread tentando criar Objeto stream Input/Output");
			try
			{
				sOutput = new ObjectOutputStream(socket.getOutputStream());
				sInput  = new ObjectInputStream(socket.getInputStream());
				nomeUsuario = (String) sInput.readObject();
				mostrar(nomeUsuario + " acabou de conectar.");
			}
			catch (IOException e) {
				mostrar("Erro criando novo stream Input/output: " + e);
				return;
			}

			catch (ClassNotFoundException e) {
			}
			data = new Date();
		}

		public void run() {
			boolean continuar = true;
			while(continuar) {
				try {
					cm = (Mensagem) sInput.readObject();
				}
				catch (IOException e) {
					mostrar(nomeUsuario + " Erro lendo Streams: " + e);
					break;				
				}
				catch(ClassNotFoundException e2) {
					break;
				}

				String mensagem = cm.getMensagem();

				switch(cm.getTipo()) {

				case Mensagem.MENSAGEM:
					broadcast(nomeUsuario + ": " + mensagem);
					break;
				case Mensagem.LOGOUT:
					mostrar(nomeUsuario + " desconectado com uma mensagem LOGOUT.");
					continuar = false;
					break;
				case Mensagem.QUEMESTAON:
					escreverMensagem("Lista de usuários conectados em " + sdf.format(new Date()) + "\n");
					for(int i = 0; i < al.size(); ++i) {
						ClienteThread ct = al.get(i);
						escreverMensagem((i+1) + ") " + ct.nomeUsuario + " desde " + out.format(ct.data) + "\n");
					}
					break;
				}
			}
			remover(id);
			fechar();
		}

		private void fechar() {
			try {
				if(sOutput != null) sOutput.close();
			}
			catch(Exception e) {}
			try {
				if(sInput != null) sInput.close();
			}
			catch(Exception e) {};
			try {
				if(socket != null) socket.close();
			}
			catch (Exception e) {}
		}

		private boolean escreverMensagem(String msg) {
			if(!socket.isConnected()) {
				fechar();
				return false;
			}
			try {
				sOutput.writeObject(msg);
			}
			catch(IOException e) {
				mostrar("Erro enviando mensagem para " + nomeUsuario);
				mostrar(e.toString());
			}
			return true;
		}
	}
}