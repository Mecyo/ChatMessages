/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import UI.TelaServidor;

/**
 *
 * @author aluno
 */
public class ServerRunning extends Thread {
    
    private Servidor servidor;
    private TelaServidor telaServer;
    
    
    public ServerRunning(Servidor host, TelaServidor ts){
        servidor = host;
        telaServer = ts;
    }
    
    @Override
    public void run() {
            servidor.start();
            telaServer.getButtonStartStop().setText("Iniciar");
            telaServer.getTextNumPorta().setEditable(true);
            telaServer.anexarEvento("Servidor caiu\n");
            servidor = null;
    }
}
