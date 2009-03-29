/**
 * 
 */
package br.com.unifor.ia.util;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import controle.Constantes;

/**
 * @author Geissivan Falcao
 * 
 */
public class Movimentacao {

	/*
	 * Define a direcao que o ladrao ira tomar com base na visao do ladrao
	 */
	public static Integer selecionarDirecaoLadraoBaseadoVisao(Point posicaoLadrao, Point p) {
		Integer mov = 0;

		if (p.x == posicaoLadrao.x && p.y == posicaoLadrao.y-1) {
			// Cima
			mov = 1;
		} else if (p.x == posicaoLadrao.x && p.y == posicaoLadrao.y+1) {
			// Baixo
			mov = 2;
		} else if (p.x == posicaoLadrao.x+1 && p.y == posicaoLadrao.y) {
			// Direita
			mov = 3;
		} else if (p.x == posicaoLadrao.x-1 && p.y == posicaoLadrao.y) {
			// Esquerda
			mov = 4;
		}

		return mov;
	}

	/*
	 * Define a direcao que o ladrao ira tomar com base no olfato do ladrao
	 */
	public static Integer selecionarDirecaoLadraoBaseadoOlfato(Point posicaoLadrao, Point p) {
		Integer mov = 0;

		if (p.x == posicaoLadrao.x && p.y == posicaoLadrao.y-1) {
			// Cima
			mov = 1;
		} else if (p.x == posicaoLadrao.x && p.y == posicaoLadrao.y+1) {
			// Baixo
			mov = 2;
		} else if (p.x == posicaoLadrao.x+1 && p.y == posicaoLadrao.y) {
			// Direita
			mov = 3;
		} else if (p.x == posicaoLadrao.x-1 && p.y == posicaoLadrao.y) {
			// Esquerda
			mov = 4;
		}

		return mov;
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
	
	/**
	 * Calculo da distancia manhattan
	 */
	public static Integer distanciaManhattan(Point a, Point b) {
		return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
	}
	
	/*
	 * Carrega os possiveis movimentos
	 */
	public static List<Point> carregaPassosLadrao(Point posicaoAtual) {
		
		Point p1 = new Point(posicaoAtual.x - 1, posicaoAtual.y); // (ESQUERDA)
		Point p2 = new Point(posicaoAtual.x, posicaoAtual.y - 1); // (SUBIR)
		Point p3 = new Point(posicaoAtual.x, posicaoAtual.y + 1); // (DESCER)
		Point p4 = new Point(posicaoAtual.x + 1, posicaoAtual.y); // (DIREITA)

		List<Point> passosLadrao = new ArrayList<Point>();
		passosLadrao.add(p1);
		passosLadrao.add(p2);
		passosLadrao.add(p3);
		passosLadrao.add(p4);
		
		return passosLadrao;
	}

}
