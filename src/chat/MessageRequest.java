/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aluno
 */
class MessageRequest implements Runnable{

    private Socket conexao;
    
    public MessageRequest(Socket conexao) {
        this.conexao = conexao;
    }
    
    public void doProcess()throws Exception{
        PrintWriter esc= new PrintWriter(conexao.getOutputStream(),true);
        BufferedReader ler= new BufferedReader(new InputStreamReader(conexao.getInputStream()));
        
        String nome = ler.readLine();
        esc.println("Bem vindo " + nome + "!");
        
        while(true){
            String msg = ler.readLine();
            esc.println(nome + "=> " + msg);
        }
    }

    @Override
    public void run() {
        try {
            this.doProcess();
        } catch (Exception ex) {
            Logger.getLogger(MessageRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    
}
