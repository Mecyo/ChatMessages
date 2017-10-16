package teste;

public class Main {

	public static void main(String[] args) throws Exception {
		Conversor<ObjetoExemplo> conversor = new Conversor<ObjetoExemplo>();
		ObjetoExemplo objeto = conversor.convertToObject(ObjetoExemplo.class, "1,joao,10d".split(","));
		System.out.println(objeto.getCodigo());
		System.out.println(objeto.getNome());
		System.out.println(objeto.getValor());
	}

}
