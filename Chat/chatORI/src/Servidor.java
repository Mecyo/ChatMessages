import java.io.*;
import java.net.*;

public class Servidor {
	public static void main(String[] args) {
		try {
	System.out.println("Criando servidor socket...");
			ServerSocket serverSocket = new ServerSocket(4444);

			System.out.println("Aguardando conex�o...");
			Socket socket = serverSocket.accept();
			System.out.println("Conex�o aceita, socket criado!");

			// Fluxo de Entrada e Sa�da do Socket.
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			InputStreamReader isr = new InputStreamReader(socket.getInputStream());
			BufferedReader in = new BufferedReader(isr);

			// Enviando mensagens ao Cliente.
			out.println("Servidor: Sou o servidor, vc est� conectado!");
			// Recebendo mensagens do Cliente.
			System.out.println(in.readLine());
			out.println("Servidor: Envie sua mensagem.");
			System.out.println(in.readLine());

			// Fechando conex�es.
			out.close(); in.close(); socket.close(); serverSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
