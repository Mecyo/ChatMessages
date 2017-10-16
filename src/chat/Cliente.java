/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aluno
 */
public class Cliente {

    
    private PrintWriter esc;
    private BufferedReader ler; 
    private String mensagem;
    private Socket conexao = null;
    
    
     public Cliente (String server, Integer porta) {
    	 
    	 try {
            
            conexao = new Socket(server,porta);

            esc= new PrintWriter(conexao.getOutputStream(),true);
            ler= new BufferedReader(new InputStreamReader(conexao.getInputStream()));
                
            
        } catch (Exception ex) {
            Logger.getLogger(ServidorMensagens.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void lerMensagem(){
        try {
            mensagem = ler.readLine();
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
    
    public void enviarMensagem(String msg){
        esc.println(msg);
    }
     
     public PrintWriter getEsc() {
        return esc;
    }

    public void setEsc(PrintWriter esc) {
        this.esc = esc;
    }

    public BufferedReader getLer() {
        return ler;
    }

    public void setLer(BufferedReader ler) {
        this.ler = ler;
    }

    /**
     *
     * @return
     */
    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

	/**
	 * @return the conexao
	 */
	public Socket getConexao() {
		return conexao;
	}

	/**
	 * @param conexao the conexao to set
	 */
	public void setConexao(Socket conexao) {
		this.conexao = conexao;
	}
}
