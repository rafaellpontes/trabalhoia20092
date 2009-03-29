/**
 * 
 */
package br.com.unifor.ia.util;

import java.awt.Point;

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
	public static Integer selecionarDirecaoLadraoBaseadoOlfato(Point p) {
		Integer mov = 0;

		if (p.getX() == 1 && p.getY() == 2) {
			// Cima
			mov = 1;
		} else if (p.getX() == 3 && p.getY() == 2) {
			// Baixo
			mov = 2;
		} else if (p.getX() == 2 && p.getY() == 3) {
			// Direita
			mov = 3;
		} else if (p.getX() == 2 && p.getY() == 1) {
			// Esquerda
			mov = 4;
		}

		return mov;
	}

}
