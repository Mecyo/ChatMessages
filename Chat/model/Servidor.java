package model;

import UI.TelaServidor;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Servidor {
	private static Integer uniqueId;
	private ArrayList<ClienteThread> al;
	private TelaServidor ts;
	private SimpleDateFormat sdf;
	private Integer porta;
	private boolean continuar;
        private Socket socket;
	
	public Servidor(Integer porta) {
		this(porta, null);
	}
	
	public Servidor(Integer porta, TelaServidor ts) {
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
				socket = serverSocket.accept();
				if(!continuar)
					break;
				ClienteThread t = new ClienteThread(this);
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

	public void parar() {
		continuar = false;
		try {
			new Socket("localhost", porta);
		}
		catch(Exception e) {
			
		}
	}

	public void mostrar(String msg) {
		String tempo = sdf.format(new Date()) + " " + msg;
		if(ts == null)
			System.out.println(tempo);
		else
			ts.anexarEvento(tempo + "\n");
	}

	public synchronized void broadcast(String mensagem) {
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

	synchronized void remover(Integer id) {

		for(int i = 0; i < al.size(); ++i) {
			ClienteThread ct = al.get(i);

			if(ct.id == id) {
				al.remove(i);
				return;
			}
		}
	}

	public static void main(String[] args) {

		Integer numeroDaPorta = 6970;
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

    public static Integer getUniqueId() {
        return uniqueId;
    }

    public static void setUniqueId(Integer uniqueId) {
        Servidor.uniqueId = uniqueId;
    }

    public ArrayList<ClienteThread> getAl() {
        return al;
    }

    public void setAl(ArrayList<ClienteThread> al) {
        this.al = al;
    }

    public SimpleDateFormat getSdf() {
        return sdf;
    }

    public void setSdf(SimpleDateFormat sdf) {
        this.sdf = sdf;
    }

    public Integer getPorta() {
        return porta;
    }

    public void setPorta(Integer porta) {
        this.porta = porta;
    }

    public boolean isContinuar() {
        return continuar;
    }

    public void setContinuar(boolean continuar) {
        this.continuar = continuar;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }    
}