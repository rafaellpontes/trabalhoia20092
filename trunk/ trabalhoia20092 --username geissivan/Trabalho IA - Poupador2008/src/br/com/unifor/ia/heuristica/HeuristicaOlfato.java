package br.com.unifor.ia.heuristica;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import controle.Constantes;

public class HeuristicaOlfato {

	private List<Point> passosLadrao;
	private Point posicaoAtual;
	private List<Point> visionMap;

	/*
	 * Carregar mapa de olfato do ladrao
	 */
	public void carregaMapa(Point posicaoAtual) {

		visionMap = new ArrayList<Point>();
		this.posicaoAtual = posicaoAtual;

		/*
		 * for (int i = 0; i < 3; i++) { for (int j = 0; j < 3; j++) { Point p =
		 * new Point(); p.setLocation(i + 1, j + 1); visionMap.add(p); } }
		 */

		for (int y = posicaoAtual.y - 1; y <= posicaoAtual.y + 1; y++) {
			for (int x = posicaoAtual.x - 1; x <= posicaoAtual.x + 1; x++) {
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

	/*
	 * Verifica se o ladrao pode ir para o caminha escolhido
	 */
	public boolean sondarCaminho(Integer objeto) {
		boolean result = false;
		// verifica se estah vazio
		if (Constantes.posicaoLivre == objeto) {
			result = true;
		} else if (Constantes.posicaoLivre != objeto
				&& (Constantes.numeroPoupador01 == objeto || Constantes.numeroPoupador02 == objeto)) {
			// verifica se nao esta vazio e se eh poupador
			result = true;
		}
		// ignora o resto

		return result;
	}

	/**
	 * Calculo da distancia manhattan
	 * */
	public Integer distanciaManhattan(Point a, Point b) {
		return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
	}

	public Integer getIndexOfPointOnSmellMap(Point p) {
		return visionMap.indexOf(p);
	}

	public List<Point> getPassosLadrao() {
		return passosLadrao;
	}

	public Point getPointFromVisionMap(Integer index) {
		return this.visionMap.get(index);
	}

	public List<Point> getVisionMap() {
		return visionMap;
	}

}
