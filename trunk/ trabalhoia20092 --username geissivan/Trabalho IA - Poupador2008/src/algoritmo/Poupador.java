package algoritmo;

import controle.Constantes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Poupador extends ProgramaPoupador {

    public static final int CIMA = 7;
    public static final int BAIXO = 16;
    public static final int ESQUERDA = 11;
    public static final int DIREITA = 12;
    public static final int POSICAO_ATUAL = 99;
    public static final int[] COORDENADA_CIMA = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    public static final int[] COORDENADA_BAIXO = {14, 15, 16, 17, 18, 19, 20, 21, 22, 23};
    public static final int[] COORDENADA_ESQUERDA = {0, 1, 5, 6, 10, 11, 14, 15, 19, 20};
    public static final int[] COORDENADA_DIREITA = {3, 4, 8, 9, 12, 13, 17, 18, 22, 23};
    public static final int POS_SEM_VISAO = -2;
    public static final int POS_FORA_AMBIENTE = -1;
    public static final int POS_CELULA_VAZIA = 0;
    public static final int POS_PAREDE = 1;
    public static final int POS_BANCO = 3;
    public static final int POS_MOEDA = 4;
    public static final int POS_PASTILHA = 5;
    public static final int POS_POUPADOR = 100;
    public static final int POS_LADRAO = 200;
    public static final int MOEDAS_PARA_BANCO = 30;

    @Override
    public int acao() {
        // 1: Determinar objetivo
        // 2: Sortear direcao
        // 3: Calcular custo + distância
        // 4: Escolher direcao
        int direcao = 0;

        //if (sensor.getPosicao().getX() == 29 && sensor.getPosicao().getY() == 8) return 0;
        determinarObjetivo();
        direcao = sortearDirecao();
        if (objetivo.equals("FUGIR")) {
            System.out.println("probabilidade=" + probabilidadeRoubo());
        }

        return direcao;
    }

    String objetivo = "";
    public void determinarObjetivo() {
        /*
         * < 15 moedas
         *  - probabilidade roubo <= 50%: MOEDA
         *  - probabilidade roubo >  50%: FUGIR
         * >= 15 moedas
         *  - probabilidade roubo <  30%: BANCO
         *  - probabilidade roubo >= 30%: FUGIR
         */
//        if (sensor.getNumeroDeMoedas() < MOEDAS_PARA_BANCO) {
//            if (probabilidadeRoubo() <= 0.2) objetivo = "MOEDA";
//            else objetivo = "FUGIR";
//        } else if (sensor.getNumeroDeMoedas() > MOEDAS_PARA_BANCO) {
//            if (probabilidadeRoubo() < 0.2) objetivo = "BANCO";
//            else objetivo = "FUGIR";
//        }
        if (probabilidadeRoubo() > 0.15) objetivo = "FUGIR";
        else {
            if (sensor.getNumeroDeMoedas() < MOEDAS_PARA_BANCO) objetivo = "MOEDA";
            else objetivo = "BANCO";
        }
    }

    public static final double[] PROBABILIDADE_VISAO = {1, 0.75, 0.3, 0.2, 0.1};
    public static final double[] PROBABILIDADE_OLFATO = {0, 0.9, 0.4, 0.2, 0.09, 0.02};
    public double probabilidadeRoubo() {
        // P(L) = (V(L) + F(L))/2
        // V(L) = max distancia(L)
        // F(L) = max ferormonio(L)
        // Média ponderada de V(L) e F(L) onde o peso é a distância e o olfato.

        double pesosVisao = 0, probVisao = 0;
        double pesosOlfato = 0, probOlfato = 0;

        int visao[] = sensor.getVisaoIdentificacao();
        for (int i = 0; i < visao.length; i++) {
            if (visao[i] >= POS_LADRAO) {
                int distancia = distanciaManhattan(i, POSICAO_ATUAL);
                if (distancia == 0) {
                    pesosVisao += 1;
                    probVisao += 1;
                } else {
                    pesosVisao += distancia;
                    probVisao += distancia * PROBABILIDADE_VISAO[distancia];
                }
            }
        }

        int olfato[] = sensor.getAmbienteOlfatoLadrao();
        for (int i = 0; i < olfato.length; i++) {
            if (olfato[i] > 0) {
                pesosOlfato += olfato[i];
                probOlfato += olfato[i] * PROBABILIDADE_OLFATO[olfato[i]];
            }
        }

        if (pesosVisao == 0) pesosVisao = 1;
        if (pesosOlfato == 0) pesosOlfato = 1;
        
        return ((probVisao/pesosVisao)+(probOlfato/pesosOlfato))/2;
    }

    List<Integer> direcoesPossiveis;
    HashMap<Integer, Double> custoDirecoes;
    public int sortearDirecao() {
        // 1: Determinr quais direções eu posso me mover
        // 2: Estabelecer custo de acordo com o objetivo
        // 3: Escolher direcao de acordo com o maior custo
        //    - Caso haja empate, realizar sorteio das direções
        // 4: Retornar direção
        int direcao = 0;

        determinarDirecoesPossiveis();
        estabelecerCustoDirecoes();
        if (objetivo.equals("FUGIR")) {
            System.out.println(sensor.getPosicao() + " - custo direcoes = " + custoDirecoes);
        }
        Set<Entry<Integer, Double>> entries = custoDirecoes.entrySet();

        if (custoDirecoes.size() > 1) {
            Random r = new Random();
            int pos = r.nextInt(custoDirecoes.size()), i = 0;

            for (Entry<Integer, Double> e : entries) {
                if (pos == i) {
                    direcao = e.getKey();

                    break;
                } else i++;
            }
        } else {
            for (Entry<Integer, Double> e : entries) {
                direcao = e.getKey();

                break;
            }
        }

        return traduzirDirecao(direcao);
    }

    public void determinarDirecoesPossiveis() {
        direcoesPossiveis = new ArrayList();

        if (podeMover(CIMA)) direcoesPossiveis.add(CIMA);
        if (podeMover(BAIXO)) direcoesPossiveis.add(BAIXO);
        if (podeMover(ESQUERDA)) direcoesPossiveis.add(ESQUERDA);
        if (podeMover(DIREITA)) direcoesPossiveis.add(DIREITA);
    }

    public boolean podeMover(int direcao) {
        int posicao = sensor.getVisaoIdentificacao()[direcao];

        if (!querPastilha() && posicao == POS_PASTILHA) return false;
        else if (sensor.getNumeroDeMoedas() == 0 && posicao == POS_BANCO) return false;

        return (posicao != POS_FORA_AMBIENTE && posicao != POS_SEM_VISAO && posicao != POS_PAREDE);
    }

    public void estabelecerCustoDirecoes() {
        // Estabelecer os custos obtendo as possíveis posições randomicamente
        custoDirecoes = new HashMap();
        while (direcoesPossiveis.size() > 0) {
            Random r = new Random();

            int pos = r.nextInt(direcoesPossiveis.size());
            int valor = direcoesPossiveis.get(pos);
            double custo = funcao(valor);

            //System.out.println(sensor.getPosicao() + " - posicao=" + valor + ", funcao=" + custo);
            custoDirecoes.put(valor, custo);

            direcoesPossiveis.remove(pos);
        }

        // Deixar somente no hashmap valores que têm custo igual
        HashMap<Integer,Double> mapa = new HashMap();
        double maiorCusto = 0;
        Set<Entry<Integer, Double>> entries = custoDirecoes.entrySet();

        for (Entry<Integer, Double> e : entries) {
            if (e.getValue() > maiorCusto) maiorCusto = e.getValue();
        }

        for (Entry<Integer, Double> e : entries) {
            if (e.getValue() == maiorCusto) mapa.put(e.getKey(), e.getValue());
        }

        custoDirecoes = mapa;
    }

    public double funcao(int direcao) {
        if (objetivo.equals("MOEDA") || objetivo.equals("BANCO")) {
            return funcaoMoeda(direcao);
        } else if (objetivo.equals("FUGIR")) {
            return funcaoFugir(direcao);
        } else if (objetivo.equals("BANCO")) {

        }

        return 0;
    }

    public static final double[] PESOS_MOEDA = {1, 0.75, 0.3, 0.1};
    public double funcaoMoeda(int direcao) {
        double f = 0;
        int visao[] = sensor.getVisaoIdentificacao();
        int coord[] = null;

        if (direcao == CIMA) coord = COORDENADA_CIMA;
        else if (direcao == BAIXO) coord = COORDENADA_BAIXO;
        else if (direcao == ESQUERDA) coord = COORDENADA_ESQUERDA;
        else if (direcao == DIREITA) coord = COORDENADA_DIREITA;

        for (int i = 0; i < coord.length; i++) {
            if (visao[coord[i]] == POS_MOEDA) {
                int distancia = distanciaManhattan(coord[i], direcao);

                if (distancia == 0) f += 1;
                else f += distancia * PESOS_MOEDA[distancia];

                //System.out.println(direcao + ": ponto=" + coord[i] + ", distancia=" + distancia + ", final=" + (distancia * PESOS_MOEDA[distancia]));
            }
        }

        return f;
    }

    public static final double[] PESOS_FUGIR = {-1, -0.75, -0.3, -0.1};
    public static final double[] PESOS_OLFATO = {0, -0.9, -0.4, -0.2, -0.09, -0.02};
    public double funcaoFugir(int direcao) {
        double f = 7; // Função começa com valor 7 e é penalizada durante a execução
        int visao[] = sensor.getVisaoIdentificacao();
        int coord[] = null;

        if (direcao == CIMA) coord = COORDENADA_CIMA;
        else if (direcao == BAIXO) coord = COORDENADA_BAIXO;
        else if (direcao == ESQUERDA) coord = COORDENADA_ESQUERDA;
        else if (direcao == DIREITA) coord = COORDENADA_DIREITA;

        for (int i = 0; i < coord.length; i++) {
            if (visao[coord[i]] >= POS_LADRAO) {
                int distancia = distanciaManhattan(coord[i], direcao);

                if (distancia == 0) f += -1;
                else f += distancia * PESOS_FUGIR[distancia] * 2;

                System.out.println(direcao + ": ponto=" + coord[i] + ", distancia=" + distancia + ", final=" + (distancia * PESOS_FUGIR[distancia]));
            }
        }

        // Avaliar olfato
        if (direcao == CIMA) coord = new int[]{0, 1, 2};
        else if (direcao == BAIXO) coord = new int[]{5, 6, 7};
        else if (direcao == ESQUERDA) coord = new int[]{0, 3, 5};
        else if (direcao == DIREITA) coord = new int[]{2, 4, 7};

        int olfato[] = sensor.getAmbienteOlfatoLadrao();
        for (int i = 0; i < coord.length; i++) {
            if (olfato[i] > 0) {
                System.out.println(direcao + "(olfato): tempo=" + olfato[i]);
                f += olfato[i] * PESOS_OLFATO[olfato[i]];

            } 
        }

        return f;
    }

    public HashMap matrizVisao = new HashMap();
    {
        matrizVisao.put("0", new int[]{1, 1});
        matrizVisao.put("1", new int[]{1, 2});
        matrizVisao.put("2", new int[]{1, 3});
        matrizVisao.put("3", new int[]{1, 4});
        matrizVisao.put("4", new int[]{1, 5});
        matrizVisao.put("5", new int[]{2, 1});
        matrizVisao.put("6", new int[]{2, 2});
        matrizVisao.put("7", new int[]{2, 3});
        matrizVisao.put("8", new int[]{2, 4});
        matrizVisao.put("9", new int[]{2, 5});
        matrizVisao.put("10", new int[]{3, 1});
        matrizVisao.put("11", new int[]{3, 2});
        matrizVisao.put("99", new int[]{3, 3}); // Posicao Atual
        matrizVisao.put("12", new int[]{3, 4});
        matrizVisao.put("13", new int[]{3, 5});
        matrizVisao.put("14", new int[]{4, 1});
        matrizVisao.put("15", new int[]{4, 2});
        matrizVisao.put("16", new int[]{4, 3});
        matrizVisao.put("17", new int[]{4, 4});
        matrizVisao.put("18", new int[]{4, 5});
        matrizVisao.put("19", new int[]{5, 1});
        matrizVisao.put("20", new int[]{5, 2});
        matrizVisao.put("21", new int[]{5, 3});
        matrizVisao.put("22", new int[]{5, 4});
        matrizVisao.put("23", new int[]{5, 5});
    }

    public int distanciaManhattan(int destino, int origem) {
        int distancia = 0;
        int coordOrigem[] = null;
        int coordDestino[] = null;

        coordDestino = (int[]) matrizVisao.get(destino + "");
        coordOrigem = (int[]) matrizVisao.get(origem + "");

        distancia = Math.abs(coordOrigem[0] - coordDestino[0]) + Math.abs(coordOrigem[1] - coordDestino[1]);

        return distancia;
    }

    public int traduzirDirecao(int direcao) {
        int real = (int) (Math.random() * 5);

        switch (direcao) {
            case CIMA:
                real = 1;
                break;
            case BAIXO:
                real = 2;
                break;
            case DIREITA:
                real = 3;
                break;
            case ESQUERDA:
                real = 4;
                break;
        }

        return real;
    }

    public boolean querPastilha() {
        return (sensor.getNumeroDeMoedas() > MOEDAS_PARA_BANCO+5 && (objetivo.equals("FUGIR") || objetivo.equals("BANCO")));
    }

    public int direcaoOposta(int direcao) {
        if (direcao == CIMA) return BAIXO;
        else if (direcao == BAIXO) return CIMA;
        else if (direcao == ESQUERDA) return DIREITA;
        else if (direcao == DIREITA) return ESQUERDA;

        return 0;
    }
}