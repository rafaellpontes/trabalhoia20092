package algoritmo;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.unifor.ia.heuristica.HeuristicaOlfato;
import br.com.unifor.ia.heuristica.HeuristicaVisao;
import br.com.unifor.ia.util.Movimentacao;
import br.com.unifor.ia.util.Registro;

import controle.Constantes;

public class Ladrao extends ProgramaLadrao {

	/** Posição de poupadores no campo de visão do agente ladrão */
	private List<Integer> posicaoPoupador;

	/**
	 * Posição do rastro de um poupador no campo de visão do agente ladrão
	 * posicaoRastroPoupador = -1 Nenhum Rastro encontrado 
	 * posicaoRastroPoupador >= 0 Rastro encontrado
	 */
	private Integer posicaoRastroPoupador;
	
	private HashMap<Integer, Registro> hashPoupador200 = new HashMap<Integer, Registro>();
	private HashMap<Integer, Registro> hashPoupador210 = new HashMap<Integer, Registro>();
	private HashMap<Integer, Registro> hashPoupador220 = new HashMap<Integer, Registro>();
	private HashMap<Integer, Registro> hashPoupador230 = new HashMap<Integer, Registro>();
		
	
	public int acao() {

		// Decisao que sera tomada pelo ladrao
		Integer decisao = 0;

		// Lista para armazenar as distancias manhattan
		List<Integer> manhattanBuffer = new ArrayList<Integer>();

		// Map que armazena as distancias manhattan e as possiveis decisoes
		Map<Integer, Integer> caminhosMap = new HashMap<Integer, Integer>();

		// Visao do Ladrao
		int[] visao = sensor.getVisaoIdentificacao();
		
		// Olfato do Ladrao
		int[] olfato = sensor.getAmbienteOlfatoPoupador();

		// Populando lista com a posicao dos poupadores
		verificarCampoVisaoLadrao(visao);

		// Se encontou algum poupador no campo de visao
		if (posicaoPoupador.size() > 0) {
			// Carrega o mapa 5 X 5 da visao do Ladrao
			HeuristicaVisao.carregaMapa();

			// Carrega os possiveis passos do Ladrao
			HeuristicaVisao.carregaPassosLadrao();
			List<Point> list = HeuristicaVisao.getPassosLadrao();

			// Percorre os possiveis passos do Ladrao, para escolher a melhor
			// decisao
			for (Point p : list) {
				// Recupera o index do Ponto p no Mapa de visao do Ladrao
				Integer indexPNoMapa = HeuristicaVisao.getIndexOfPointOnVisionMap(p);
				// Boolean para decidir se ele tem a possibilidade de seguir ou
				// nao
				boolean seguir = false;
				// Se os pontos forem P[3,4] ou P[4,3]
				if (p.getX() + p.getY() == 7) {
					seguir = HeuristicaVisao.sondarCaminho(visao[indexPNoMapa - 1]);
				} else {
					// Se forem os outros pontos
					seguir = HeuristicaVisao.sondarCaminho(visao[indexPNoMapa]);
				}
				
				// Se permitir seguir
				if (seguir) {
					// Inicia o Calculo da Heuristica

					Point pPoupador = new Point();

					// Recupera a posicao do poupador
					Integer posicao = posicaoPoupador.get(0);

					// Se for depois da posicao P[3,3] do Ladrao no mapa de
					// Visao do Ladrao
					if (posicao >= 12) {
						pPoupador = HeuristicaVisao
								.getPointFromVisionMap(posicao + 1);
					} else {
						pPoupador = HeuristicaVisao.getPointFromVisionMap(posicao);
					}
					// Calcula a distancia manhatam
					Integer manhattan = HeuristicaVisao.distanciaManhattan(p,
							pPoupador);
					// Armazena numa lista para depois pegar a de menor
					// distancia do objetivo
					manhattanBuffer.add(manhattan);
					// Armazena no hashmap a distancia manhattan e a possivel
					// decisao
					caminhosMap.put(manhattan, Movimentacao.selecionarDirecaoLadraoBaseadoVisao(p));
				}
			}
			// Ordena por menor distancia manhattan
			Collections.sort(manhattanBuffer);
			// Seleciona a decisao pela menor distancia manhatam
			decisao = caminhosMap.get(manhattanBuffer.get(0));

			// retorna a decisao
			return decisao;
			
		} else if ((posicaoRastroPoupador = buscarRastroPoupadorCampoOlfatoAgenteLadrao(olfato)) >= 0) { // Verifica o olfato do agente ladrão

			// Carrega o mapa 3 X 3 do olfato do Ladrao
			HeuristicaOlfato.carregaMapa();

			// Carrega os possiveis passos do Ladrao
			HeuristicaOlfato.carregaPassosLadrao();
			List<Point> list = HeuristicaOlfato.getPassosLadrao();

			// Percorre os possiveis passos do Ladrao, para escolher a melhor
			// decisao
			for (Point p : list) {
				// Recupera o index do Ponto p no Mapa de olfato do Ladrao
				Integer indexPNoMapa = HeuristicaOlfato.getIndexOfPointOnSmellMap(p);
				// Boolean para decidir se ele tem a possibilidade de seguir
				boolean seguir = false;
				// Se os pontos forem P[3,2] ou P[2,3]
				if (p.getX() + p.getY() == 5) {
					seguir = HeuristicaOlfato.sondarCaminho(visao[indexPNoMapa - 1]);
				} else {
					// Se forem os outros pontos
					seguir = HeuristicaOlfato.sondarCaminho(visao[indexPNoMapa]);
				}
				
				// Se permitir seguir
				if (seguir) {
					// Inicia o Calculo da Heuristica

					Point pPoupador = new Point();

					// Recupera a posicao do poupador
					Integer posicao = posicaoRastroPoupador;

					// Se for depois da posicao P[2,2] do Ladrao no mapa de
					// Olfato do Ladrao
					if (posicao >= 4) {
						pPoupador = HeuristicaOlfato
								.getPointFromVisionMap(posicao + 1);
					} else {
						pPoupador = HeuristicaOlfato.getPointFromVisionMap(posicao);
					}
					// Calcula a distancia manhatam
					Integer manhattan = HeuristicaOlfato.distanciaManhattan(p,
							pPoupador);
					// Armazena numa lista para depois pegar a de menor
					// distancia do objetivo
					manhattanBuffer.add(manhattan);
					// Armazena no hashmap a distancia manhattan e a possivel
					// decisao
					caminhosMap.put(manhattan, Movimentacao.selecionarDirecaoLadraoBaseadoOlfato(p));
				}
			}
			// Ordena por menor distancia manhattan
			Collections.sort(manhattanBuffer);
			// Seleciona a decisao pela menor distancia manhatam
			decisao = caminhosMap.get(manhattanBuffer.get(0));

			// retorna a decisao
			return decisao;			
			
		} else { // Anda sem nenhuma informacao sobre uma posicao de um poupador
			
			return (int) (Math.random() * 5);
			
		}
	}

	/*
	 * Mapeia posicao do poupador na visao do ladrao
	 */
	private void verificarCampoVisaoLadrao(int[] visao) {
		
		posicaoPoupador = new ArrayList<Integer>();
		for (int i = 0; i < visao.length; i++) {
			if (visao[i] == Constantes.numeroPoupador01
					|| visao[i] == Constantes.numeroPoupador02) {
				
				//Validar situacoes onde o poupador nao tem moedas e quando ja
				//tem um ladrao persiguindo o poupador.
				posicaoPoupador.add(i);
				
			}
		}
	}

	/**
	 * busca o rastro de agentes poupadores dentro do olfato do ladrao e retorna
	 * um valor determinando se encontrou o rastro:
	 * 
	 * return = -1 Nenhum Rastro encontrado 
	 * return >= 0 Rastro encontrado
	 * 
	 * @param olfato
	 *            List de inteiros do campo de olfato do agente ladrão
	 * @return
	 */
	private Integer buscarRastroPoupadorCampoOlfatoAgenteLadrao(int[] olfato) {

		Integer maiorProximidade = 0;
		Integer indice = -1;

		for (int i = 0; i < olfato.length; i++) {
			if (olfato[i] >= 1 && olfato[i] <= 5
					&& (olfato[i] > 0 && olfato[i] < maiorProximidade)) {
				maiorProximidade = olfato[i];
				indice = i;
			}
		}

		return indice;

	}
	
}