/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

/**
 *
 * @author Mecyo
 */
public class ServerRunning extends Thread {
    
    private Servidor servidor;
    private final TelaServidor telaServer;
    
    public ServerRunning(Servidor server, TelaServidor tc){
        servidor = server;
        telaServer = tc;
    }
    
    @Override
    public void run() {
            servidor.start();
            telaServer.getStopStart().setText("Start");
            telaServer.gettPortNumber().setEditable(true);
            telaServer.anexarEvento("Servidor caiu\n");
            servidor = null;
    }

    public Servidor getServidor() {
        return servidor;
    }

    public void setServidor(Servidor servidor) {
        this.servidor = servidor;
    }
}
