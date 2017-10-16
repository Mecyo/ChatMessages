package chatswing;

import java.io.*;

public class Mensagem implements Serializable {

	protected static final long serialVersionUID = 1112122200L;

	static final int QUEMESTAON = 0, MENSAGEM = 1, LOGOUT = 2;
	private int tipo;
	private String mensagem;

	Mensagem(int tipo, String mensagem) {
		this.tipo = tipo;
		this.mensagem = mensagem;
	}

	int getTipo() {
		return tipo;
	}
	String getMensagem() {
		return mensagem;
	}
}