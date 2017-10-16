package ui;

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
	private ClienteThread thread;
	
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
				thread = t;
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
				mostrar("Houve um erro! O servidor e os clientes: " + e +" serï¿½o encerrados.");
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

	private synchronized void broadcast(Mensagem mensagem) {
		String tempo = sdf.format(new Date());
		mensagem.setMensagem(tempo + " " + mensagem.getMensagem() + "\n");
		if(ts == null)
			System.out.print(mensagem.getMensagem());
		else
			ts.anexarSala(mensagem.getMensagem());

		for(int i = al.size(); --i >= 0;) {
			ClienteThread ct = al.get(i);

			if(!ct.escreverMensagem(mensagem)) {
				al.remove(i);
				mostrar("Cliente desconectado " + ct.nomeUsuario + " removido da lista.");
			}
		}
	}
	
	private synchronized void privado(Mensagem mensagem) {
		String tempo = sdf.format(new Date());
		String msg = tempo +" "+ mensagem.getNomeUsuario() + " - PRIVADO => " + mensagem.getDestino() + ": " + mensagem.getMensagem() + "\n";
		mensagem.setMensagem(msg);
		if(ts == null)
			System.out.print(mensagem.getMensagem());
		else{
			ts.anexarSala(mensagem.getMensagem());
		}
		for(int i = al.size(); --i >= 0;) {
			ClienteThread ct = al.get(i);

			if(ct.nomeUsuario.equals(mensagem.getNomeUsuario()) || ct.nomeUsuario.equals(mensagem.getDestino())) {
				ct.escreverMensagem(mensagem);
			}
		}
	}
	
	private synchronized void logados(Mensagem mensagem) {
		String tempo = sdf.format(new Date());
		mensagem.setMensagem(tempo + " " + mensagem.getNomeUsuario() + " fez login.\n");
		if(ts == null)
			System.out.print(mensagem.getMensagem());
		else
			ts.anexarSala(mensagem.getMensagem());

		for(int i = al.size(); --i >= 0;) {
			ClienteThread ct = al.get(i);
			ct.escreverMensagem(mensagem);
		}
	}
	
	private synchronized void listaLogados(Mensagem mensagem, ObjectOutputStream sOutput) {
		for(int i = al.size(); --i >= 0;) {
			ClienteThread ct = al.get(i);
			if(!ct.nomeUsuario.equals(mensagem.getNomeUsuario()))
				try {
					sOutput.writeObject(new Mensagem(Mensagem.QUEMESTAON, ct.nomeUsuario, mensagem.getNomeUsuario(), ""));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	private synchronized void removeUser(Mensagem mensagem, ObjectOutputStream sOutput) {
		for(int i = al.size(); --i >= 0;) {
			ClienteThread ct = al.get(i);
			try {
				sOutput.writeObject(new Mensagem(Mensagem.LOGOUT, ct.nomeUsuario, ""));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
	
	private static Object getObjectFromByte(byte[] objectAsByte) {
        Object obj = null;
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        try {
                 bis = new ByteArrayInputStream(objectAsByte);
                 ois = new ObjectInputStream(bis);
                 obj = ois.readObject();

                 bis.close();
                 ois.close();

        } catch (IOException e) {
                 // TODO Auto-generated catch block
                 e.printStackTrace();
        } catch (ClassNotFoundException e) {
                 // TODO Auto-generated catch block
                 e.printStackTrace();           
        }                 
        
        return obj;
	}

	public static void main(String[] args) {

		int numeroDaPorta = 6970;
		switch(args.length) {
			case 1:
				try {
					numeroDaPorta = Integer.parseInt(args[0]);
				}
				catch(Exception e) {
					System.out.println("Número da porta inválido.");
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
		Cliente cliente;
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
			}
			catch (IOException e) {
				mostrar("Erro criando novo stream Input/output: " + e);
				return;
			}
			data = new Date();
		}

		public void run() {
			boolean continuar = true;
			while(continuar) {
				try {
					cm = (Mensagem) sInput.readObject();
					if(cm.getTipo().equals(Mensagem.ARQUIVO)){
						 /*byte[] objectAsByte = new byte[socket.getReceiveBufferSize()];
                         BufferedInputStream bf = new BufferedInputStream(
                                            socket.getInputStream());
                         bf.read(objectAsByte);*/
                         
                         //3
                         //Arquivo arquivo = (Arquivo) getObjectFromByte(objectAsByte);
                         Arquivo arquivo = (Arquivo) getObjectFromByte(cm.getBytea());
                         
                         //4
                         String dir = arquivo.getDestino().endsWith("/") ? arquivo
                                            .getDestino() + arquivo.getNome() : arquivo
                                            .getDestino() + "/" + arquivo.getNome();
                         System.out.println("Escrevendo arquivo " + dir);

                         //5
                         FileOutputStream fos = new FileOutputStream(dir);
                         fos.write(arquivo.getConteudo());
                         fos.close();

					}
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
					cm.setMensagem(nomeUsuario + ": " + mensagem);
					broadcast(cm);
					break;
				case Mensagem.PRIVADO:
					privado(cm);
					break;
				case Mensagem.LOGOUT:
					mostrar(nomeUsuario + " desconectado com uma mensagem LOGOUT.");
					continuar = false;
					removeUser(cm, sOutput);
					break;
				case Mensagem.LOGIN:
					nomeUsuario = cm.getNomeUsuario(); 
					mostrar(nomeUsuario + " acabou de conectar.");
					logados(cm);
					break;
				case Mensagem.QUEMESTAON:
					listaLogados(cm, sOutput);
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

        private boolean escreverMensagem(Mensagem msg) {
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


	/**
	 * @return the thread
	 */
	public ClienteThread getThread() {
		return thread;
	}

	/**
	 * @param thread the thread to set
	 */
	public void setThread(ClienteThread thread) {
		this.thread = thread;
	}
}