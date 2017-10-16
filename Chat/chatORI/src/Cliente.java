import java.io.*;
import java.net.*;

public class Cliente {
	public static void main(String[] args) {
		try {
	System.out.println("Solicitando conexão...");
		Socket socket = new Socket("127.0.0.1", 4444);
		System.out.println("Conexão aceita, socket criado!");

			// Fluxo de Entrada e Saída do Socket.
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			InputStreamReader isr = new InputStreamReader(socket.getInputStream());
			BufferedReader in = new BufferedReader(isr);

			out.println("Cliente: Ok");
			String s = in.readLine();
			System.out.println(s);

			// Fechando conexões.
			out.close(); in.close(); socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
