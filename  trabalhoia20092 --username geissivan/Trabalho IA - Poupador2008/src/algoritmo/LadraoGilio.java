package algoritmo;

import java.awt.Point;
import java.util.ArrayList;
import controle.Constantes;

public class LadraoGilio extends ProgramaLadrao {

	
	
	private final Point posBanco = new Point(Constantes.posicaoBanco);
	// valores inteiros que serao atribuidos ao status a visao do agente
	// 0 para qnd o agente nao ve nada de importante
	// 1 para qnd o agente avista um poupador
	private int statusVisao = 0;
	// valores inteiros que serao atribuidos ao status do olfato do agente
	// 0 para qnd o agente nao sente nada de importante
	// 1 para qnd o agente sente algum odor de um poupador
	private int statusOlfato = 0;
	private int numMoedas = 0;
	private ArrayList<Integer> posPoupador;

	private ArrayList<Integer> posLadrao;

	private ArrayList<Integer> cheiroPoupador;

	private ArrayList<Integer> movCelulasVazias;

	private int ultimoMov;

	private int tentativasFrustradas = 0;

	private int movimentosBanco = 0;

	Point ultPosXY = new Point();

	private Point ultimaPosXY;

	public int acao() {
		
		
		
		// Percebendo valores do ambiente
		/*
		 * sensor.getVisaoIdentificacao(); sensor.getAmbienteOlfatoLadrao();
		 * sensor.getAmbienteOlfatoPoupador(); sensor.getNumeroDeMoedas();
		 * sensor.getPosicao();
		 */
		// System.out.println("pos banco:" +posBanco.x +","+posBanco.y);
		int[] visao = sensor.getVisaoIdentificacao();
		int[] olfato = sensor.getAmbienteOlfatoPoupador();

		movCelulasVazias = movimentosParaCelulasVazias(visao);
		posPoupador = new ArrayList<Integer>();
		cheiroPoupador = new ArrayList<Integer>();
		statusVisao = sondarPoupadorVisao(visao);
		statusOlfato = sondarOlfato(olfato);

		// ultPosXY = sensor.getPosicao();

		if (statusVisao >= 1) {
			if (ultimaPosXY == sensor.getPosicao()/* || movimentosBanco <=5 */) {
				// movimentosBanco++;
				// if(movimentosBanco == 5)
				// {*/
				movimentosBanco = 0;
				// }
				ultPosXY = sensor.getPosicao();
				System.out.println("ultimaPosXY " + movimentosBanco);
				return irParaBanco();
			}
			// ultPosXY = sensor.getPosicao();
			if (sondarLadraoVisao(visao).size() >= 2 /*
													 * || ultimaPosXY ==
													 * sensor.getPosicao()
													 */) {
				ultPosXY = sensor.getPosicao();
				return irParaBanco();
			} else {
				tentativasFrustradas = 0;
				// System.out.println("viu poupador");
				// System.out.println("posPoupador " + posPoupador.get(0) +" "+
				// posPoupador.size());
				if (movimentarParaCelulaPoupador(visao) > 0) {
					if (sensor.getNumeroDeMoedas() != numMoedas) {
						System.out.println("Roubou");
						numMoedas = sensor.getNumeroDeMoedas();
						ultimoMov = irParaBanco();
						System.out.println("irParaBanco " + ultimoMov);
						ultPosXY = sensor.getPosicao();
						return ultimoMov;
					}
					// /tentativasAssalto++;
					// System.out.println(tentativasAssalto);
					/*
					 * if(tentativasAssalto == 50) { System.out.println("50");
					 * tentativasAssalto = 0; ultimoMov = irParaBanco();
					 * System.out.println("irParaBanco "+ultimoMov); return
					 * ultimoMov; } else {
					 */
					// System.out.println(movimentarParaCelulaPoupador(visao));
					ultimoMov = movimentarParaCelulaPoupador(visao);
					System.out.println("foi para posPoupador" + ultimoMov);
					ultPosXY = sensor.getPosicao();
					return ultimoMov;
					// }
				} else {
					ultimoMov = perseguirPoupador();
					System.out.println("perseguirPoupador" + ultimoMov);
					ultPosXY = sensor.getPosicao();
					return ultimoMov;
					// return 0;
				}

			}
		} else if (statusOlfato == 1) {
			tentativasFrustradas = 0;
			// ultPosXY = sensor.getPosicao();
			int mov = movimentoCheiro(posMelhorOlfato());
			if (mov > 0) {
				// System.out.println("foi para a posicao "+ mov);
				ultimoMov = mov;
				ultPosXY = sensor.getPosicao();
				return ultimoMov;
			} else {
				// System.out.println("foi para a posicao "+
				// movimentoCheiro2(posMelhorOlfato()));
				ultimoMov = movimentoCheiro2(posMelhorOlfato());
				System.out.println("sentiu cheiro poupador" + ultimoMov);
				ultPosXY = sensor.getPosicao();
				return ultimoMov;
			}
		} else {
			// ultPosXY = sensor.getPosicao();
			tentativasFrustradas++;
			int mov = 0;
			if (movCelulasVazias.size() == 0) {
				ultPosXY = sensor.getPosicao();
				return selecionaIntervalo(1, 4);
			}
			if (movCelulasVazias.size() == 1) {
				mov = movCelulasVazias.get(0);
			} else {
				mov = movCelulasVazias.get(selecionaIntervalo(0,
						movCelulasVazias.size() - 1));
				while (true) {
					if (ultimoMov % 2 == 0) {
						if (mov != ultimoMov - 1) {
							break;
						} else {
							mov = movCelulasVazias.get(selecionaIntervalo(0,
									movCelulasVazias.size() - 1));
						}
					} else {
						if (mov != ultimoMov + 1) {
							break;
						} else {
							mov = movCelulasVazias.get(selecionaIntervalo(0,
									movCelulasVazias.size() - 1));
						}
					}
				}
			}
			// System.out.println(mov);
			System.out.println("mov " + mov);
			ultPosXY = sensor.getPosicao();
			ultimoMov = mov;
			return ultimoMov;
			// }
		}

		// return selecionaIntervalo(0, 4);
	}

	public boolean posicaoRepetida(Point ultPosXY, int proxPos) {
		return false;
	}

	/**
	 * escolhe uma celula que esteja o poupador
	 * 
	 * @param visao
	 * @return
	 */
	public int movimentarParaCelulaPoupador(int[] visao) {
		int mov = 0;
		if (visao[7] >= 100 && visao[7] < 200) {
			mov = 1;
		}
		if (visao[11] >= 100 && visao[11] < 200) {
			mov = 4;
		}
		if (visao[12] >= 100 && visao[12] < 200) {
			mov = 3;
		}
		if (visao[13] >= 100 && visao[13] < 200) {
			mov = 2;
		}
		return mov;
	}

	public int selecionaIntervalo(int intervalo1, int intervalo2) {
		return (int) (intervalo1 + Math.random()
				* (intervalo2 - intervalo1 + 1));
	}

	/**
	 * recebe uma posicao e retorna um numero inteiro para o movimento
	 * 
	 * @param pos
	 * @return
	 */
	public int movimentar(int pos) {
		int mov = 0;
		if (pos == 7) {
			mov = 1;
		}
		if (pos == 11) {
			mov = 4;
		}
		if (pos == 12) {
			mov = 3;
		}
		if (pos == 16) {
			mov = 2;
		}

		return mov;
	}

	public int irParaBanco() {
		int posicao = 0;
		ArrayList<Integer> posCelVazias = posicoesCelulasVazias(sensor
				.getVisaoIdentificacao());
		ArrayList<Integer> empate = new ArrayList<Integer>();
		Point xyCelVazia = posicaoXY(posCelVazias.get(0));
		Point xyBanco = posBanco;
		double menorDist = distancia(xyCelVazia, xyBanco);
		int movimento = movimentar(posCelVazias.get(0));

		for (int i = 1; i < posCelVazias.size(); i++) {
			xyCelVazia = posicaoXY(posCelVazias.get(i));
			// Point xyProxCelVazia = posicaoXY(posCelVazias.get(i+1));
			double dist = distancia(xyCelVazia, xyBanco);
			// double distProx = distancia(sensor.getPosicao(), xyProxCelVazia);
			if (menorDist < dist) {
				// menorDist = menorDist;
				// movimento = movimentar(posCelVazias.get(i));
			} else if (menorDist > dist) {
				menorDist = dist;
				movimento = movimentar(posCelVazias.get(i));
			} else {
				empate.add(movimento);
				empate.add(movimentar(posCelVazias.get(i)));
			}
		}
		if (empate.size() > 0) {
			movimento = empate.get(selecionaIntervalo(0, empate.size() - 1));
			System.out.println("empate banco" + movimento);
			// return ;
		}
		return movimento;
	}

	/**
	 * persegui um poupador calculando qual a celula q vai dar a menor distancia
	 * ate o poupador
	 * 
	 * @return
	 */
	public int perseguirPoupador() {
		int posicao = 0;
		ArrayList<Integer> posCelVazias = posicoesCelulasVazias(sensor
				.getVisaoIdentificacao());
		ArrayList<Integer> empate = new ArrayList<Integer>();
		if (posCelVazias.size() > 0) {
			Point xyCelVazia = posicaoXY(posCelVazias.get(0));
			Point xyPoupador = posicaoXY(posPoupador.get(0));
			double menorDist = distancia(xyCelVazia, xyPoupador);
			int movimento = movimentar(posCelVazias.get(0));

			for (int i = 1; i < posCelVazias.size(); i++) {
				xyCelVazia = posicaoXY(posCelVazias.get(i));
				// Point xyProxCelVazia = posicaoXY(posCelVazias.get(i+1));
				double dist = distancia(xyCelVazia, xyPoupador);
				// double distProx = distancia(sensor.getPosicao(),
				// xyProxCelVazia);
				if (menorDist < dist) {
					// menorDist = menorDist;
					// movimento = movimentar(posCelVazias.get(i));
				} else if (menorDist > dist) {
					menorDist = dist;
					movimento = movimentar(posCelVazias.get(i));
				} else {
					empate.add(movimento);
					empate.add(movimentar(posCelVazias.get(i)));
				}
			}
			if (empate.size() > 0) {
				movimento = empate
						.get(selecionaIntervalo(0, empate.size() - 1));
				System.out.println("empate size" + empate.size() + "movimento "
						+ movimento);
				// return empate.get(0);
			}
			return movimento;
		} else
			return 0;
	}

	/**
	 * converter a posicao inteira no vertor visao para uma coordenada x,y
	 * 
	 * @param posPoupador
	 * @return
	 */
	public Point posicaoXY(int posPoupador) {
		double linha = 0;
		double coluna = 0;
		int xPoupador = 0;
		int yPoupador = 0;
		Point xyPoupador = new Point();
		// verificar qual linha
		if (posPoupador == 0 || posPoupador == 1 || posPoupador == 2
				|| posPoupador == 3 || posPoupador == 4) {
			linha = 2; // primeira linha de cima para baixo
		} else if (posPoupador == 5 || posPoupador == 6 || posPoupador == 7
				|| posPoupador == 8 || posPoupador == 9) {
			linha = 1; // segunda linha de cima para baixo
		} else if (posPoupador == 10 || posPoupador == 11 || posPoupador == 12
				|| posPoupador == 13) {
			linha = 0; // msm linha que o ladrao esta
		} else if (posPoupador == 14 || posPoupador == 15 || posPoupador == 16
				|| posPoupador == 17 || posPoupador == 18) {
			linha = -1; // terceira linha de cima para baixo
		} else if (posPoupador == 19 || posPoupador == 20 || posPoupador == 21
				|| posPoupador == 22 || posPoupador == 23) {
			linha = -2;// quarta linha de cima para baixo
		}
		// verificar qual coluna
		if (posPoupador == 0 || posPoupador == 5 || posPoupador == 10
				|| posPoupador == 14 || posPoupador == 19) {
			coluna = -2; // primeira coluna da esquerda para a direita
		} else if (posPoupador == 1 || posPoupador == 6 || posPoupador == 11
				|| posPoupador == 15 || posPoupador == 20) {
			coluna = -1; // segunda coluna da esquerda para a direita
		} else if (posPoupador == 2 || posPoupador == 7 || posPoupador == 16
				|| posPoupador == 21) {
			coluna = 0; // msm coluna que o ladrao esta
		} else if (posPoupador == 3 || posPoupador == 8 || posPoupador == 12
				|| posPoupador == 17 || posPoupador == 22) {
			coluna = 1; // primeira coluna da esquerda para a direita
		} else if (posPoupador == 4 || posPoupador == 9 || posPoupador == 13
				|| posPoupador == 18 || posPoupador == 23) {
			coluna = 2; // primeira coluna da esquerda para a direita
		}
		xyPoupador.x = (int) (sensor.getPosicao().x + coluna);
		xyPoupador.y = (int) (sensor.getPosicao().y + linha);
		// System.out.println("x: "+xyPoupador.x+" y: "+xyPoupador.y);
		return xyPoupador;
	}

	/**
	 * checar quais a posicoes em que o agente pode andar
	 * 
	 * @param visao
	 * @return
	 */
	public ArrayList<Integer> movimentosParaCelulasVazias(int[] visao) {
		ArrayList<Integer> movCelulasVazias = new ArrayList<Integer>();
		if (visao[7] == 0) {
			movCelulasVazias.add(1);
		}
		if (visao[11] == 0) {
			movCelulasVazias.add(4);
		}
		if (visao[12] == 0) {
			movCelulasVazias.add(3);
		}
		if (visao[16] == 0) {
			movCelulasVazias.add(2);
		}
		return movCelulasVazias;
	}

	/**
	 * checar quais as posicoes em que as celulas estao vazias
	 * 
	 * @param visao
	 * @return
	 */
	public ArrayList<Integer> posicoesCelulasVazias(int[] visao) {
		ArrayList<Integer> posCelulasVazias = new ArrayList<Integer>();
		if (visao[7] == 0) {
			posCelulasVazias.add(7);
		}
		if (visao[11] == 0) {
			posCelulasVazias.add(11);
		}
		if (visao[12] == 0) {
			posCelulasVazias.add(12);
		}
		if (visao[16] == 0) {
			posCelulasVazias.add(16);
		}
		return posCelulasVazias;
	}

	public int procurarPosCheiro(int cheiro) {
		for (int i = 0; i < sensor.getAmbienteOlfatoPoupador().length; i++) {
			if (sensor.getAmbienteOlfatoPoupador()[i] == cheiro) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * se movimentar baseando se pelo cheiro deixado pelo poupador
	 * 
	 * @param posCheiro
	 * @return
	 */
	public int movimentoCheiro(int posCheiro) {
		int mov = 0;
		if (posCheiro == 1) {
			mov = 1;
		} else if (posCheiro == 3) {
			mov = 4;
		} else if (posCheiro == 4) {
			mov = 3;
		} else if (posCheiro == 6) {
			mov = 2;
		} else // (posCheiro==0 || posCheiro==2 || posCheiro==5 ||
				// posCheiro==7)cheiros nas diagonais
		{
			mov = -1;// alerta o agente para procurar outra estrategia para
						// seguir o cheiro
		}

		return mov;
	}

	/**
	 * quando o cheiro esta na diagonal , é usada outra estrategia para seguir o
	 * cheiro
	 * 
	 * @param posCheiro
	 * @return
	 */
	public int movimentoCheiro2(int posCheiro) {
		int mov = 0;
		if (posCheiro == 0) {
			// sensor.getVisaoIdentificacao()[11]==0||
			if (sensor.getAmbienteOlfatoPoupador()[3] != 0) {
				mov = 4;// mover para esquerda
			} else if (sensor.getAmbienteOlfatoPoupador()[1] != 0) {
				mov = 1;// mover para cima
			} else if (sensor.getVisaoIdentificacao()[11] == 0) {
				mov = 4;// mover para esquerda
			} else if (sensor.getVisaoIdentificacao()[7] == 0) {
				mov = 1;// mover para cima
			}
		} else if (posCheiro == 2) {
			if (sensor.getAmbienteOlfatoPoupador()[4] != 0) {
				mov = 3;// mover para direita
			} else if (sensor.getAmbienteOlfatoPoupador()[1] != 0) {
				mov = 1;// mover para cima
			} else if (sensor.getVisaoIdentificacao()[12] == 0) {
				mov = 3;// mover para direita
			} else if (sensor.getVisaoIdentificacao()[7] == 0) {
				mov = 1;// mover para cima
			}
		} else if (posCheiro == 5) {
			if (sensor.getAmbienteOlfatoPoupador()[3] != 0) {
				mov = 4;// mover para esquerda
			} else if (sensor.getAmbienteOlfatoPoupador()[6] != 0) {
				mov = 2;// mover para baixo
			} else if (sensor.getVisaoIdentificacao()[3] == 0) {
				mov = 4;// mover para esquerda
			} else if (sensor.getVisaoIdentificacao()[6] == 0) {
				mov = 2;// mover para baixo
			}
		} else if (posCheiro == 7) {
			if (sensor.getAmbienteOlfatoPoupador()[3] != 0) {
				mov = 3;// mover para direita
			} else if (sensor.getAmbienteOlfatoPoupador()[6] != 0) {
				mov = 2;// mover para baixo
			} else if (sensor.getVisaoIdentificacao()[3] == 0) {
				mov = 3;// mover para direita
			} else if (sensor.getVisaoIdentificacao()[6] == 0) {
				mov = 2;// mover para baixo
			}
		}
		return mov;
	}

	public int posMelhorOlfato() {
		int posMenorCheiro = cheiroPoupador.get(0);
		for (int i = 1; i < cheiroPoupador.size(); i++) {
			if (posMenorCheiro >= cheiroPoupador.get(i)) {
				posMenorCheiro = cheiroPoupador.get(i);
			}
		}
		// System.out.println("posicao melhor cheiro " + posMenorCheiro);
		return posMenorCheiro;
	}

	/**
	 * sonda o olfato do ladrao e retorna uma resposta pra informar se encontrou
	 * algum cheiro de poupador ou nao
	 * 
	 * @param olfato
	 * @return
	 */
	public int sondarOlfato(int[] olfato) {
		for (int i = 0; i < olfato.length; i++) {
			if (olfato[i] >= 1 && olfato[i] <= 5) {
				cheiroPoupador.add(i);
			}
		}
		if (cheiroPoupador.size() == 0) {
			return 0;
		} else {
			return 1;
		}
	}

	/**
	 * sonda a visao do ladrao e retorna uma resposta pra informar se encontrou
	 * poupador ou nao
	 * 
	 * @param visao
	 * @return
	 */
	public ArrayList<Integer> sondarLadraoVisao(int[] visao) {
		posLadrao = new ArrayList<Integer>();
		for (int i = 0; i < visao.length; i++) {
			if (visao[i] >= 200 && visao[i] < 300) {
				posLadrao.add(i);
			}
		}
		return posLadrao;
	}

	/**
	 * sonda a visao do ladrao e retorna uma resposta pra informar se encontrou
	 * poupador ou nao
	 * 
	 * @param visao
	 * @return
	 */
	public int sondarPoupadorVisao(int[] visao) {
		// ArrayList<Integer> posValidas = movimentosValidos(visao);
		for (int i = 0; i < visao.length; i++) {
			if (visao[i] >= 100 && visao[i] < 200) {
				posPoupador.add(i);
			}
		}
		/*
		 * if(posPoupador.size()==0) { return 0; } else { return 1; }
		 */
		return posPoupador.size();
	}

	/**
	 * calcula a distancia entre 2 pontos
	 * 
	 * @param inicio
	 * @param fim
	 * @return
	 */
	public double distancia(Point inicio, Point fim) {
		double dAB = Math.sqrt(Math.pow((fim.x - inicio.x), 2)
				+ Math.pow((fim.y - inicio.y), 2));
		return dAB;
	}

}
