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

}
