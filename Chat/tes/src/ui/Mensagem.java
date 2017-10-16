package ui;

import java.io.*;

public class Mensagem implements Serializable {

	protected static final long serialVersionUID = 1112122200L;

	static final int QUEMESTAON = 0, MENSAGEM = 1, LOGOUT = 2, PRIVADO = 3, ERRO = 4, LOGIN = 5, ARQUIVO = 6;
	private Integer tipo;
	private String mensagem;
	private String destino;
	private String nomeUsuario;
	private byte[] bytea;
	
	Mensagem(Integer tipo, String nomeUsuario, String mensagem) {
		this(tipo, nomeUsuario, null, mensagem);
	}
	
	Mensagem(Integer tipo, String nomeUsuario, String destino, String mensagem) {
		this.tipo = tipo;
		this.nomeUsuario = nomeUsuario;
		this.destino = destino;
		this.mensagem = mensagem;
	}

	Integer getTipo() {
		return tipo;
	}
	String getMensagem() {
		return mensagem;
	}

	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	/**
	 * @return the bytea
	 */
	public byte[] getBytea() {
		return bytea;
	}

	/**
	 * @param bytea the bytea to set
	 */
	public void setBytea(byte[] bytea) {
		this.bytea = bytea;
	}
}