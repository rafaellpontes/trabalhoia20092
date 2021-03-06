package br.com.unifor.ia.heuristica;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.unifor.ia.util.Movimentacao;

import controle.Constantes;

public class HeuristicaVisao {

	private List<Point> passosLadrao;
	private List<Point> visionMap;
	private Point posicaoAtual;

	/*
	 * Carregar mapa de visao do ladrao
	 */
	public void carregaMapa(Point posicaoAtual) {

		this.visionMap = new ArrayList<Point>();
		this.posicaoAtual = posicaoAtual;

		/*
		 * for (int i = 0; i < 5; i++) { for (int j = 0; j < 5; j++) { Point p =
		 * new Point(); p.setLocation(i + 1, j + 1); this.visionMap.add(p); } }
		 */

		for (int y = posicaoAtual.y - 2; y <= posicaoAtual.y + 2; y++) {
			for (int x = posicaoAtual.x - 2; x <= posicaoAtual.x + 2; x++) {
				Point p = new Point(x, y);
				this.visionMap.add(p);
			}
		}
	}

	/*
	 * Carrega os possiveis movimentos do ladrao
	 */
	public void carregaPassosLadrao() {

		Point p1 = new Point(posicaoAtual.x - 1, posicaoAtual.y); // (ESQUERDA)
		Point p2 = new Point(posicaoAtual.x, posicaoAtual.y - 1); // (SUBIR)
		Point p3 = new Point(posicaoAtual.x, posicaoAtual.y + 1); // (DESCER)
		Point p4 = new Point(posicaoAtual.x + 1, posicaoAtual.y); // (DIREITA)

		this.passosLadrao = new ArrayList<Point>();
		this.passosLadrao.add(p1);
		this.passosLadrao.add(p2);
		this.passosLadrao.add(p3);
		this.passosLadrao.add(p4);
	}

	public Point buscarPosicaoBanco(Point point, int[] vetorVisao){
		Point p = new Point();

		int xMax = point.x + 2;
		int xMin = point.x - 2;
		int yMin = point.y - 2;

		int x = xMin;
		int y = yMin;

		for (int i = 0; i < 25; i++) {

			if (i == 12) {

			}
			if (i > 12) {
				if (vetorVisao[i - 1] == Constantes.numeroBanco) {
					p.setLocation(x, y);
					return p;
				}
			} else {
				if (vetorVisao[i] == Constantes.numeroBanco) {
					p.setLocation(x, y);
					return p;
				}
			}
			if (x < xMax) {
				x++;
			} else if (x == xMax) {
				x = xMin;
				y++;
			}
		}

		return null;
	}

	/**
	 * M�todo que retorna o poupador mais perto do ladr�o corrente.. ou seja, o
	 * poupador que dever� ser seguido
	 * 
	 * @param posicaoLadrao
	 * @param posicaoPoupador
	 * @return
	 */
	public Point ladraoPerseguindoPoupadorMaisPertoQueLadraoCorrente(
			List<Integer> posicaoLadrao, List<Integer> posicaoPoupador) {

		// Lista para armazenar as dist�ncias manhattan do ladr�o corrente para
		// cada poupador e o ponto do poupador
		Map<Integer, Point> posicaoLadraoCorrentePoupadorManhattanList = new HashMap<Integer, Point>();
		List<Integer> chavePosicaoLadraoCorrentePoupadorManhattanList = new ArrayList<Integer>();

		// Calcula a distancia do ladr�o corrente para cada poupador
		for (Integer p : posicaoPoupador) {
			// Recupera o ponto do poupador
			Point pPoupador = new Point();
			if (p >= 12) {
				pPoupador = getPointFromVisionMap(p + 1);
			} else {
				pPoupador = getPointFromVisionMap(p);
			}

			// Calcula a distancia manhatam do poupador para o ladr�o
			Integer manhattan = Movimentacao.distanciaManhattan(posicaoAtual, pPoupador);

			// Adiciona na lista de dist�ncias
			posicaoLadraoCorrentePoupadorManhattanList
					.put(manhattan, pPoupador);
			chavePosicaoLadraoCorrentePoupadorManhattanList.add(manhattan);

		}

		// Ordena a lista pela menor dist�ncia
		Collections.sort(chavePosicaoLadraoCorrentePoupadorManhattanList);

		for (Integer pCorrente : chavePosicaoLadraoCorrentePoupadorManhattanList) {
			boolean persegueAlgumPoupador = true;
			Point pPoupador = posicaoLadraoCorrentePoupadorManhattanList
					.get(pCorrente);
			for (Integer pLadraoInteger : posicaoLadrao) {
				// Recupera o ponto do ladr�o
				Point pLadrao = new Point();
				if (pLadraoInteger >= 12) {
					pLadrao = getPointFromVisionMap(pLadraoInteger + 1);
				} else {
					pLadrao = getPointFromVisionMap(pLadraoInteger);
				}

				// Calcula a distancia manhatam do poupador para o ladr�o
				Integer manhattan = Movimentacao.distanciaManhattan(pLadrao, pPoupador);

				if (pCorrente > manhattan) {
					persegueAlgumPoupador = false;
					// Ladr�o corrente est� a uma distancia maior do que outro
					// ladr�o
					break;
				}
			}

			if (persegueAlgumPoupador) {
				return pPoupador;
			}
		}

		return null;

	}

	public Integer getIndexOfPointOnVisionMap(Point p) {
		return visionMap.indexOf(p);
	}

	public List<Point> getPassosLadrao() {
		return passosLadrao;
	}

	public Point getPointFromVisionMap(Integer index) {
		return visionMap.get(index);
	}

	public List<Point> getVisionMap() {
		return visionMap;
	}

}
