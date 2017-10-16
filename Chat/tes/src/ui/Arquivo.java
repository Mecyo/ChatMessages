package ui;

import java.io.Serializable;
import java.util.Date;
 
 
public class Arquivo implements Serializable {
 
         /**
          *
          */
         private static final long serialVersionUID = 1L;
         
         private String nome;
         private byte[] conteudo;
         private Date dataHoraUpload;  
         private String destino;
         private transient long tamanhoKB;
         
         public Date getDataHoraUpload() {
                   return dataHoraUpload;
         }
         public void setDataHoraUpload(Date dataHoraUpload) {
                   this.dataHoraUpload = dataHoraUpload;
         }
         public String getNome() {
                   return nome;
         }
         public void setNome(String nome) {
                   this.nome = nome;
         }
         public byte[] getConteudo() {
                   return conteudo;
         }
         public void setConteudo(byte[] conteudo) {
                   this.conteudo = conteudo;
         }
		/**
		 * @return the destino
		 */
		public String getDestino() {
			return destino;
		}
		/**
		 * @param destino the destino to set
		 */
		public void setDestino(String destino) {
			this.destino = destino;
		}
		/**
		 * @return the tamanhoKB
		 */
		public long getTamanhoKB() {
			return tamanhoKB;
		}
		/**
		 * @param tamanhoKB the tamanhoKB to set
		 */
		public void setTamanhoKB(long tamanhoKB) {
			this.tamanhoKB = tamanhoKB;
		}
}