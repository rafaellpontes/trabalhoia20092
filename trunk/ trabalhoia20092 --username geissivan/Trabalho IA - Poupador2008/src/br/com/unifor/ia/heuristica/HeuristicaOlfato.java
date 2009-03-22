package br.com.unifor.ia.heuristica;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import controle.Constantes;

public class HeuristicaOlfato {

	private static List<Point> visionMap;
	private static List<Point> passosLadrao;

	/*
	 * Carregar mapa de olfato do ladrao
	 */
	public static void carregaMapa() {
		visionMap = new ArrayList<Point>();
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				Point p = new Point();
				p.setLocation(i + 1, j + 1);
				visionMap.add(p);
			}
		}
	}

	/*
	 * Verifica se o ladrao pode ir para o caminha escolhido
	 */
	public static boolean sondarCaminho(Integer objeto) {
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

	/*
	 * Carrega os possiveis movimentos do ladrao
	 */
	public static void carregaPassosLadrao() {
		
		Point p1 = new Point(2, 1); //(ESQUERDA)
		Point p2 = new Point(1, 2); //(SUBIR)
		Point p3 = new Point(3, 2);	//(DESCER)
		Point p4 = new Point(2, 3); //(DIREITA)

		passosLadrao = new ArrayList<Point>();
		passosLadrao.add(p1);
		passosLadrao.add(p2);
		passosLadrao.add(p3);
		passosLadrao.add(p4);
	}
	
	public static Point getPointFromVisionMap(Integer index) {
		return visionMap.get(index);
	}

	/**
	 * Calculo da distancia manhattan
	 * */
	public static Integer distanciaManhattan(Point a, Point b) {
		return new Double(Math.abs(a.getX() - b.getX())).intValue()
				+ new Double(Math.abs(a.getY() - b.getY())).intValue();
	}
	
	public static List<Point> getPassosLadrao() {
		return passosLadrao;
	}

	public static List<Point> getVisionMap() {
		return visionMap;
	}

	public static Integer getIndexOfPointOnVisionMap(Point p) {
		return visionMap.indexOf(p);
	}
}
