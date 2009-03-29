package br.com.unifor.ia.util;

import java.awt.Point;
import java.util.HashMap;

import controle.Constantes;

public class Ambiente {

	private static final Integer CONSTANTE_PONTO_NAO_INICIALIZADO = 631551;

	// Podemos manter o ambiente do mundo armazenado na memoria
	// de cada ladrao.
	// 30x30
	private HashMap<Point, Integer> hashAmbiente;

	public Ambiente() {

		hashAmbiente = new HashMap<Point, Integer>();

		for (int i = 0; i < 30; i++) {
			for (int j = 0; j < 30; j++) {
				Point p = new Point();
				p.setLocation(i, j);
				hashAmbiente.put(p, CONSTANTE_PONTO_NAO_INICIALIZADO);
			}
		}
	}

	public void inserirInformacaoPonto(Point point, int[] vetorVisao) {
		
		int xMax = point.x + 2;
		int xMin = point.x - 2;
		int yMin = point.y - 2;		
		
		int x = xMin;
		int y = yMin;		
		
		for(int i = 0; i < 25; i++){

			Point p = new Point();			
						
			if (i == 12) {
				hashAmbiente.put(point, Constantes.posicaoLivre);
			}else if (i > 12) {
				if(x < xMax){
					p.setLocation(x, y);
					hashAmbiente.put(p, vetorVisao[i-1]);
					x++;
				}else if (x == xMax){
					p.setLocation(x, y);
					hashAmbiente.put(p, vetorVisao[i-1]);
					x = xMin;
					y++;
				}
			} else {
				if(x < xMax){
					p.setLocation(x, y);
					hashAmbiente.put(p, vetorVisao[i]);
					x++;
				}else if (x == xMax){
					p.setLocation(x, y);
					hashAmbiente.put(p, vetorVisao[i]);
					x = xMin;
					y++;
				}
			}					
		}		
	}
	
	public Integer buscarSituacaoPonto(Point point){
		return hashAmbiente.get(point);
	}
			
	public HashMap<Point, Integer> getHashAmbiente() {
		return hashAmbiente;
	}

	public void setHashAmbiente(HashMap<Point, Integer> hashAmbiente) {
		this.hashAmbiente = hashAmbiente;
	}

}
