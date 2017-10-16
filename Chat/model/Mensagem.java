package model;

import java.io.*;

public class Mensagem implements Serializable {

    protected static final long serialVersionUID = 1112122200L;

    public static final int QUEMESTAON = 0, MENSAGEM = 1, LOGOUT = 2;
    private Integer tipo;
    private String mensagem;

    public Mensagem(Integer tipo, String mensagem) {
            this.tipo = tipo;
            this.mensagem = mensagem;
    }

    public Integer getTipo() {
            return tipo;
    }
    String getMensagem() {
            return mensagem;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }  
}