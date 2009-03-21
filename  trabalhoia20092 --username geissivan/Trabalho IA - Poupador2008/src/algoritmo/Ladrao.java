package algoritmo;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import controle.Constantes;

public class Ladrao extends ProgramaLadrao {

	private List<Integer> posicaoPoupador;

	public int acao() {

		int[] visao = sensor.getVisaoIdentificacao();
		int[] olfato = sensor.getAmbienteOlfatoPoupador();

		// Populando lista com a posicao dos poupadores
		verificarCampoVisaoLadrao(visao);

		// Se encontou algum poupador no campo de visao
		if (posicaoPoupador.size() > 0) {
			Point posicaoAtual = sensor.getPosicao();
			Heuristica.carregaMapa();
			Heuristica.carregaPassosLadrao();
			List<Point> list = Heuristica.getPassosLadrao();

			for (Point p : list) {
				Integer indexPNoMapa = Heuristica.getIndexOfPointOnVisionMap(p);
				boolean seguir = false;
				if (p.getX() + p.getY() == 7) {
					seguir = Heuristica.sondarCaminho(visao[indexPNoMapa - 1]);
				} else {
					seguir = Heuristica.sondarCaminho(visao[indexPNoMapa]);
				}
				if(seguir){
					//Calculo da Heuristica
					
				}
			}

		}

		return (int) (Math.random() * 5);
	}

	/*
	 * Mapeia posicao do poupador na visao do ladrao
	 */
	private void verificarCampoVisaoLadrao(int[] visao) {
		posicaoPoupador = new ArrayList<Integer>();
		for (int i = 0; i < visao.length; i++) {
			if (visao[i] == Constantes.numeroPoupador01
					|| visao[i] == Constantes.numeroPoupador02) {
				posicaoPoupador.add(i);
			}
		}
	}
}