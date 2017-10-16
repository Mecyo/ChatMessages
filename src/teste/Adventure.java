package teste;

import java.util.Scanner;

public class Adventure {
	private Scanner keyboard = new Scanner(System.in);
	private Boolean fim = Boolean.TRUE;
	private String acao, andar, olhar, fugir, pegar;
	private String direcao, frente, esquerda, direita, tras;
	private String verificaAcao = "andar" + "olhar" + "fugir" + "pegar" + "verificaAcao";
	private String verificaDirecao = "frente" + "esquerda" + "direita" + "tras" + "verificaDirecao";
	

	public void escolheAcao(){
		do{
			System.out.print('\u000C');
			System.out.println("O que você gostaria de fazer: 'olhar', 'andar', 'pegar' ou 'fugir' ?");
			acao = keyboard.next();
			
			if(!verificaAcao.contains(acao)){
				System.out.println("Ação inválida!");
			}
			
		}while(!verificaAcao.contains(acao));
	}
	
	public void escolheDirecao(String acao){
		do{
			System.out.print('\u000C');
			System.out.println("Para qual direção deseja " + acao + ": 'frente', 'esquerda', 'direita', 'tras' ?");
			direcao = keyboard.next();
			
			if(!verificaDirecao.contains(direcao)){
				System.out.println("Direção inválida!");
			}
			
		}while(!verificaDirecao.contains(direcao));
	}
	
	
	public static void main( String[] args ){ 
		Adventure jogo = new Adventure();
		System.out.println( " SEJA BEM VINDO AO NEW ADVENTURE GAME!");
		System.out.println(" ");
		try { Thread.sleep (1000); } catch (InterruptedException ex) {}
		System.out.print('\u000C');
		System.out.println("Você está em uma casa assustadora.");
		try { Thread.sleep (800); } catch (InterruptedException ex) {}
		System.out.print('\u000C');
		System.out.println("Dentro de um quarto.");
		try { Thread.sleep (800); } catch (InterruptedException ex) {}
		System.out.print('\u000C');
		System.out.println(" ");
		
		
		while(jogo.fim){
			jogo.escolheAcao();
			
			switch (jogo.acao) {
				case "andar":
					jogo.escolheDirecao(jogo.acao);
					break;
					
				case "olhar":
					jogo.escolheDirecao(jogo.acao);
					break;
					
				case "fugir":
					System.out.println("Você não tem como fugir!");
					jogo.acao = "";
					jogo.direcao = "";				
					break;
					
				case "pegar":
					System.out.println("Não há nada próximo para pegar!");
					jogo.acao = "";
					jogo.direcao = "";
					break;
	
				default:
					break;
			}
			
			if(jogo.acao.equalsIgnoreCase("andar")){
				switch (jogo.direcao) {
					case "frente":
						System.out.println("Existe uma mesa à sua frente.");
						System.out.println("Em cima dela estão 4 objetos:");
						break;
						
					case "esquerda":
						System.out.println("Você está de frente com a porta do quarto.");
						break;
						
					case "direita":
						System.out.println("Você não tem como andar! Há uma parede aqui!");
						break;
						
					case "atras":
						System.out.println("Você não tem como andar! Há uma cama aqui!");
						break;
		
					default:
						break;
				}
				jogo.escolheAcao();
				
			}else if(jogo.acao.equalsIgnoreCase("olhar")){
				switch (jogo.direcao) {
					case "frente":
						System.out.println("Existe uma mesa à sua frente.");
						break;
						
					case "esquerda":
						System.out.println("Aqui está a porta do quarto.");
						break;
						
					case "direita":
						System.out.println("Há uma parede aqui.");
						break;
						
					case "atras":
						System.out.println("Há uma cama aqui!");
						break;
		
					default:
						break;
				}
			}
			jogo.escolheAcao();
			jogo.fim = Boolean.FALSE;
			
		}
		/*System.out.print( "> ");
		Go = keyboard.next();
		if (Go.equalsIgnoreCase("kitchen")) {
			System.out.println("There is a long countertop with dirty dishes everywhere. Off to one side there is, as you'd expect, a refrigerator. You may open the 'refrigerator' or look in the 'pantry'. ");
		}
		System.out.print("> ");
		Look = keyboard.next();
		if (Look.equalsIgnoreCase( "refrigerator" )) {
			System.out.println("Inside the refrigerator you see food and stuff. It looks pretty nasty. Would you like to eat some of the food, 'Yes' or 'No'?");
		}
		System.out.print("> ");
		Eat = keyboard.next();
		if (Eat.equalsIgnoreCase("Yes")) {
			System.out.println(" ");
			System.out.println("You live!");
		} else if (Eat.equalsIgnoreCase("No")) {
			System.out.println(" ");
			System.out.println("You die of starvation!");
		} else if (Look.equalsIgnoreCase( "pantry" )) {
			System.out.println("There is a killer inside. Do you want to 'fight' them, or 'run away'?");
		}
		System.out.print("> ");
		Pantry = keyboard.next();
		if (Pantry.equalsIgnoreCase("fight")) {
			System.out.println(" ");
			System.out.println("You're weak and die");
		} else if(Pantry.equalsIgnoreCase("run away")) {
			System.out.println(" ");
			System.out.println("You died because your too slow & can't run");
		}
	}*/
} 

	
	
	

}
