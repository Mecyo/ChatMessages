

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Chat {

	private ServerSocket serverSocket = null;
	private Socket socket = null;
	private PrintWriter out;
	private BufferedReader in;
	private boolean podeRodar = true;
	private static final String IP = "127.0.0.1";
	
	public static void main(String[] args) throws IOException {
		
		final Chat chat = new Chat();
		
		try {
			chat.socket = new Socket(IP, 4444);
			System.out.println("Conexão aceita, socket criado!");
			
		} catch (Exception e) {
			
			chat.serverSocket = new ServerSocket(4444);
			System.out.println("Aguardando conexão...");
			chat.socket = chat.serverSocket.accept();
			System.out.println("Conexão aceita, socket criado!");
		}
		
		chat.out = new PrintWriter(chat.socket.getOutputStream(), true);
		InputStreamReader isr = new InputStreamReader(chat.socket.getInputStream());
		chat.in = new BufferedReader(isr);
		
		final Scanner scanner = new Scanner(System.in);
		
		System.out.println("Digite seu nome: ");
		String nome = scanner.next();
		
		Thread t1 = new Thread() {
			
			public void run() {
				while (chat.podeRodar) {
					
					String texto = scanner.nextLine();
					chat.out.println(nome + " : " + texto);
					
					if (texto.equals("sair")) {
						chat.podeRodar = false;
						chat.fecharSocket();
					}
				}
			}
		};
		t1.start();
		
		while (chat.podeRodar) {
			String texto = chat.in.readLine();
			
			if (chat.podeRodar) {				
				if (texto.equals("sair")) {
					chat.podeRodar = false;
					chat.fecharSocket();
				} else {
					System.out.println(texto);
				}
			}
		}
		System.exit(1);
		scanner.close();
	}
	
	private void fecharSocket() {
		try {
			this.in.close();
			this.out.close();
			this.socket.close();
			
			if (this.serverSocket != null) {
				this.serverSocket.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
