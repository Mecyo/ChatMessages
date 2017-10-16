package model;

import java.io.Serializable;
import java.util.Date;
 
public class Arquivo implements Serializable {
 
         /**
          *
          */
         private static final long serialVersionUID = 1L;
                   
         private String nome;
         private byte[] conteudo;
         private transient long tamanhoKB;
         private transient Date dataHoraUpload;
         private String destino;
         private transient String portaDestino;
         
         
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
         public long getTamanhoKB() {
                   return tamanhoKB;
         }
         public void setTamanhoKB(long tamanhoKB) {
                   this.tamanhoKB = tamanhoKB;
         }
         public Date getDataHoraUpload() {
                   return dataHoraUpload;
         }
         public void setDataHoraUpload(Date dataHoraUpload) {
                   this.dataHoraUpload = dataHoraUpload;
         }
         public String getDestino() {
                   return destino;
         }
         public void setDestino(String ipDestino) {
                   this.destino = ipDestino;
         }
         public String getPortaDestino() {
                   return portaDestino;
         }
         public void setPortaDestino(String portaDestino) {
                   this.portaDestino = portaDestino;
         }
}
