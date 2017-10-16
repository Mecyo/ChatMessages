package teste;

	import java.io.*;
	import java.net.*; 
	import java.util.Random;


	public class ServerTeste {

	    
	    private static ServerSocket socketRecepcao;

		public static void main(String argv[]) throws Exception {
	        String fraseCliente;
	        String resultado = ""; 
	        String numero;
	        Random random = new Random();
	        socketRecepcao = new ServerSocket(6789); 
	        while(true) { 
	            Socket socketConexao = socketRecepcao.accept(); 
	            BufferedReader doCliente = new BufferedReader(new InputStreamReader(socketConexao.getInputStream())); 
	            
	            DataOutputStream  paraCliente = new DataOutputStream(socketConexao.getOutputStream());
	            numero = String.valueOf(random.nextInt(101));
	            
	            int cont = 0;
	            fraseCliente= doCliente.readLine(); 
	            String mensagem = "Informe um numero: ";
	            paraCliente.writeBytes(mensagem);
	            while(cont < 5){
	                fraseCliente= doCliente.readLine(); 
	                if(fraseCliente.equalsIgnoreCase(numero)){
	                    resultado = "Parabens! Voce acertou!";
	                    break;                    
	                }else if(fraseCliente.compareToIgnoreCase(numero) > 0){
	                    resultado = fraseCliente + " eh maior que o numero!";
	                }else{
	                    resultado = fraseCliente + " eh menor que o numero!";
	                }

	                paraCliente.writeBytes(resultado + '\n' + mensagem);
	                cont++;
	            }
	            paraCliente.writeBytes("FIM");
	            if(cont >= 5){
	                resultado = "Voce excedeu o limite de tentativas!";
	            }
	            paraCliente.writeBytes(resultado);
	        } 
	    } 
	}