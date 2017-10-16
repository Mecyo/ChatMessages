/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aluno
 */
public class ServidorMensagens {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // TODO code application logic
            ServerSocket server = new ServerSocket(6970);
            while(true){
                Socket conexao=server.accept();
             
                MessageRequest  message = new MessageRequest(conexao);
                Thread t = new Thread(message);
                t.start();
              
            
            }
        } catch (Exception ex) {
            Logger.getLogger(ServidorMensagens.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
