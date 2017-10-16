package teste;


	import java.io.*;
	import java.net.*;

	public class ClienteTeste {
	    
	    public static void main(String argv[]) throws Exception {
	        String jogada;
	        String mensagem; 
	        BufferedReader doUsuario = new BufferedReader(new InputStreamReader(System.in)); 
	        //while(true){
	            Socket socketCliente = new Socket("127.0.0.1", 6789);
	            DataOutputStream paraServidor = new DataOutputStream(socketCliente.getOutputStream());

	            BufferedReader doServidor = new BufferedReader(new InputStreamReader(socketCliente.getInputStream())); 

	            jogada = doUsuario.readLine();
                paraServidor.writeBytes(jogada);
                mensagem = doServidor.readLine();
	            System.out.println(mensagem); 
	            while(!mensagem.equalsIgnoreCase("FIM")) { 
	                jogada = doUsuario.readLine();
	                paraServidor.writeBytes(jogada);
	                mensagem = doServidor.readLine();
	                System.out.println(mensagem); 
	            }
	            mensagem = doServidor.readLine();
	            System.out.println(mensagem); 
	            socketCliente.close(); 
	        //} 
	    } 
	}
