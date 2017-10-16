/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Mecyo
 */
public class ClienteThread extends Thread {

		Socket socket;
                Servidor servidor;
		ObjectInputStream sInput;
		ObjectOutputStream sOutput;
		int id;
		String nomeUsuario;
		Mensagem cm;
		Date data;
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
		
		
		  

		ClienteThread(Servidor server) {
                        servidor = server;
                        Integer idServer = server.getUniqueId();
			id = ++idServer;
			this.socket = server.getSocket();
			System.out.println("Thread tentando criar Objeto stream Input/Output");
			try
			{
				sOutput = new ObjectOutputStream(socket.getOutputStream());
				sInput  = new ObjectInputStream(socket.getInputStream());
				nomeUsuario = (String) sInput.readObject();
				servidor.mostrar(nomeUsuario + " acabou de conectar.");
			}
			catch (IOException e) {
				servidor.mostrar("Erro criando novo stream Input/output: " + e);
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
					servidor.mostrar(nomeUsuario + " Erro lendo Streams: " + e);
					break;				
				}
				catch(ClassNotFoundException e2) {
					break;
				}

				String mensagem = cm.getMensagem();

				switch(cm.getTipo()) {

				case Mensagem.MENSAGEM:
					servidor.broadcast(nomeUsuario + ": " + mensagem);
					break;
				case Mensagem.LOGOUT:
					servidor.mostrar(nomeUsuario + " desconectado com uma mensagem LOGOUT.");
					continuar = false;
					break;
				case Mensagem.QUEMESTAON:
					escreverMensagem("Lista de usuários conectados em " + sdf.format(new Date()) + "\n");
					for(int i = 0; i < servidor.getAl().size(); ++i) {
						ClienteThread ct = servidor.getAl().get(i);
						escreverMensagem((i+1) + ") " + ct.nomeUsuario + " desde " + out.format(ct.data) + "\n");
					}
					break;
				}
			}
			servidor.remover(id);
			fechar();
		}

		public void fechar() {
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

		public boolean escreverMensagem(String msg) {
			if(!socket.isConnected()) {
				fechar();
				return false;
			}
			try {
				sOutput.writeObject(msg);
			}
			catch(IOException e) {
				servidor.mostrar("Erro enviando mensagem para " + nomeUsuario);
				servidor.mostrar(e.toString());
			}
			return true;
		}
	}
