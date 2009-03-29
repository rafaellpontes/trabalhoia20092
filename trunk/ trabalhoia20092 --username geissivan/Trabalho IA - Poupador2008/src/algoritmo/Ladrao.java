package algoritmo;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.unifor.ia.heuristica.HeuristicaOlfato;
import br.com.unifor.ia.heuristica.HeuristicaVisao;
import br.com.unifor.ia.util.Ambiente;
import br.com.unifor.ia.util.Movimentacao;
import br.com.unifor.ia.util.RegistroPoupador;

import controle.Constantes;

public class Ladrao extends ProgramaLadrao {

	/** Heuristica de visao do ladrao */
	HeuristicaVisao heuristicaVisao = new HeuristicaVisao();
	
	/** Heuristica de olfato do ladrao */
	HeuristicaOlfato heuristicaOlfato = new HeuristicaOlfato();
	
	/** Posição de poupadores no campo de visão do agente ladrão */
	private List<Integer> posicaoPoupador;

	/**
	 * Posição do rastro de um poupador no campo de visão do agente ladrão
	 * posicaoRastroPoupador = -1 Nenhum Rastro encontrado 
	 * posicaoRastroPoupador >= 0 Rastro encontrado
	 */
	private Integer posicaoRastroPoupador;
	
	/** Informacoes de poupadores ja encontrados */
	private HashMap<Integer, RegistroPoupador> hashPoupador = new HashMap<Integer, RegistroPoupador>();
			
	/** Posicao do banco, so sabe onde fica o banco quando entra em seu campo de visao */
	private Point posicaoBanco;
	
	/** Quantidade de moedas antes da proxima acao */
	private Integer qtdMoedaRodadaAnterior = 0;
	
	/** Contador de rodadas */
	private Integer contadorRodadas = 0;
	
	private Point posicaoAnteriorLadrao;
	
	/** Posicao anterior a acao do poupador que esta sendo perseguido */
	private Point posicaoAnteriorPoupador;
	
	/** Poupador seguido */
	private Integer poupadorSeguido;
	
	private Ambiente ambiente = new Ambiente();
		
	public int acao() {
	
		// Assalto realizado com sucesso
		if(sensor.getNumeroDeMoedas() > qtdMoedaRodadaAnterior){
			
			RegistroPoupador registroPoupador = new RegistroPoupador();
			hashPoupador.put(poupadorSeguido, registroPoupador);
			
		}
		
		contadorRodadas++;

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
		
		// Atualiza a quantidade de moedas do ladrao
		qtdMoedaRodadaAnterior = sensor.getNumeroDeMoedas();		

		// Se encontou algum poupador no campo de visao
		if (posicaoPoupador.size() > 0) {
			// Carrega o mapa 5 X 5 da visao do Ladrao
			heuristicaVisao.carregaMapa(sensor.getPosicao());

			// Atualiza a informacao do ambiente
			ambiente.inserirInformacaoPonto(sensor.getPosicao(), visao);
			
			// Carrega os possiveis passos do Ladrao
			heuristicaVisao.carregaPassosLadrao();
			List<Point> list = heuristicaVisao.getPassosLadrao();

			// Percorre os possiveis passos do Ladrao, para escolher a melhor
			// decisao
			for (Point p : list) {
				// Recupera o index do Ponto p no Mapa de visao do Ladrao
				Integer indexPNoMapa = heuristicaVisao.getIndexOfPointOnVisionMap(p);
				// Boolean para decidir se ele tem a possibilidade de seguir ou
				// nao
				boolean seguir = false;
				// Se os pontos forem DIREITA ou SOB o ponto ladrao.
				if (p.getX() + p.getY() > sensor.getPosicao().x + sensor.getPosicao().y) {
					seguir = heuristicaVisao.sondarCaminho(visao[indexPNoMapa - 1]);
				} else {
					// Se forem os outros pontos
					seguir = heuristicaVisao.sondarCaminho(visao[indexPNoMapa]);
				}
				
				// Se permitir seguir
				if (seguir) {
					// Inicia o Calculo da Heuristica

					Point pPoupador = new Point();

					// Recupera a posicao do poupador
					Integer posicao = posicaoPoupador.get(0);
										
					// Se for depois da posicao do Ladrao no mapa de
					// Visao do Ladrao
					if (posicao >= 12) {
						pPoupador = heuristicaVisao
								.getPointFromVisionMap(posicao + 1);
					} else {
						pPoupador = heuristicaVisao.getPointFromVisionMap(posicao);
					}
					
					// Apos selecionar qual poupador perseguir, salva a posicao atual do poupador
					// na variavel da ultima posicao do poupador.
					posicaoAnteriorPoupador = pPoupador;
					
					// Seta o ultimo poupador perseguido na variavel do ultimo poupador perseguido
					poupadorSeguido = visao[posicao];
					
					// Calcula a distancia manhatam
					Integer manhattan = HeuristicaVisao.distanciaManhattan(p,
							pPoupador);
					
					// Armazena numa lista para depois pegar a de menor
					// distancia do objetivo
					manhattanBuffer.add(manhattan);
					
					// Armazena no hashmap a distancia manhattan e a possivel
					// decisao
					caminhosMap.put(manhattan, Movimentacao.selecionarDirecaoLadraoBaseadoVisao(sensor.getPosicao(), p));
										
				}
			}
			// Ordena por menor distancia manhattan
			Collections.sort(manhattanBuffer);
			// Seleciona a decisao pela menor distancia manhatam
			decisao = caminhosMap.get(manhattanBuffer.get(0));
			
		} else if ((posicaoRastroPoupador = buscarRastroPoupadorCampoOlfatoAgenteLadrao(olfato)) >= 0) { // Verifica o olfato do agente ladrão

			// Carrega o mapa 3 X 3 do olfato do Ladrao
			heuristicaOlfato.carregaMapa(sensor.getPosicao());

			// Carrega os possiveis passos do Ladrao
			heuristicaOlfato.carregaPassosLadrao();
			List<Point> list = heuristicaOlfato.getPassosLadrao();

			// Percorre os possiveis passos do Ladrao, para escolher a melhor
			// decisao
			for (Point p : list) {
				// Recupera o index do Ponto p no Mapa de olfato do Ladrao
				Integer indexPNoMapa = heuristicaOlfato.getIndexOfPointOnSmellMap(p);
				// Boolean para decidir se ele tem a possibilidade de seguir
				boolean seguir = false;
				// Se os pontos forem DIREITA ou SOB o ponto ladrao.
				if (p.getX() + p.getY() > sensor.getPosicao().x + sensor.getPosicao().y) {
					seguir = heuristicaOlfato.sondarCaminho(visao[indexPNoMapa - 1]);
				} else {
					// Se forem os outros pontos
					seguir = heuristicaOlfato.sondarCaminho(visao[indexPNoMapa]);
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
						pPoupador = heuristicaOlfato
								.getPointFromVisionMap(posicao + 1);
					} else {
						pPoupador = heuristicaOlfato.getPointFromVisionMap(posicao);
					}
					// Calcula a distancia manhatam
					Integer manhattan = heuristicaOlfato.distanciaManhattan(p,
							pPoupador);
					// Armazena numa lista para depois pegar a de menor
					// distancia do objetivo
					manhattanBuffer.add(manhattan);
					// Armazena no hashmap a distancia manhattan e a possivel
					// decisao
					caminhosMap.put(manhattan, Movimentacao.selecionarDirecaoLadraoBaseadoOlfato(sensor.getPosicao(), p));
				}
			}
			// Ordena por menor distancia manhattan
			Collections.sort(manhattanBuffer);
			// Seleciona a decisao pela menor distancia manhatam
			decisao = caminhosMap.get(manhattanBuffer.get(0));

		} else { // Anda sem nenhuma informacao sobre uma posicao de um poupador
			
			decisao = (int) (Math.random() * 5);
			
		}
		
		// Seta a posicao atual do ladrao na variavel ultimaPosicao para que na proxima acao,
		// saber qual foi sua ultima posicao.
		posicaoAnteriorLadrao = sensor.getPosicao();
				
		return decisao;
		
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
		
		Point posBanco;
		
		if( (posBanco = heuristicaVisao.buscarPosicaoBanco(sensor.getPosicao(), visao)) != null){
			posicaoBanco = posBanco;
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