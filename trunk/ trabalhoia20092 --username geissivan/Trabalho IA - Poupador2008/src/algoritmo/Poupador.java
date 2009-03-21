package algoritmo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

public class Poupador extends ProgramaPoupador {

    private int[] cima = new int[]{2, 7};
    private int[] baixo = new int[]{16, 21};
    private int[] esquerda = new int[]{0, 1, 5, 6, 10, 11, 14, 15, 19, 20};
    private int[] direita = new int[]{3, 4, 8, 9, 12, 13, 17, 18, 22, 23};
    // Constantes
    public static final int SEM_VISAO = -2;
    public static final int FORA_AMBIENTE = -1;
    public static final int CELULA_VAZIA = 0;
    public static final int PAREDE = 1;
    public static final int BANCO = 3;
    public static final int MOEDA = 4;
    public static final int PASTILHA = 5;
    public static final int POUPADOR = 100;
    public static final int LADRAO = 200;
    public static final int OBJETO_FORA_DE_VISAO = -1;
    public static final int QUANT_MOEDAS_ACUMULADAS = 5;
    // Sondagem do ambiente.
    // Visão
    private boolean paredeAoRedor;
    private boolean bancoProximo;
    private boolean bancoAoRedor;
    private boolean moedaProxima;
    private boolean pastilhaProxima;
    private boolean pastilhaAoRedor;
    private boolean poupadorProximo;
    private boolean ladraoProximo;
    // Olfato
    private boolean ladraoProximoOlfato;
    private boolean poupadorProximoOlfato;
    public static final int NADA_SENTIDO_PELO_OLFATO = -1;
    // Unid de olfato
    public static final int SEM_MARCA = 0;
    public static final int UMA_UNID_ATRAS = 1;
    public static final int DUAS_UNID_ATRAS = 2;
    public static final int TREIS_UNID_ATRAS = 3;
    public static final int QUAT_UNID_ATRAS = 4;
    public static final int CINC_UNID_ATRAS = 5;
    private int dirAnt;

    public int acao() {
        sondarComVisao();
        sondarComOlfato();
        return decisao();
    }

    /**
     * Sonda o ambiente visual e seta variáveis booleanas.
     */
    private void sondarComVisao() {
        // Sonda parede ao redor.
        if (detectarAoRedor(Poupador.PAREDE)) {
            paredeAoRedor = true;
        } else {
            paredeAoRedor = false;
        }
        // Sonda banco próximo.
        if (retornaPosObjetoMaisProximo(Poupador.BANCO) != OBJETO_FORA_DE_VISAO) {

            if (detectarAoRedor(Poupador.BANCO)) {
                bancoAoRedor = true;
            } else {
                bancoAoRedor = false;
            }
            bancoProximo = true;
        } else {
            bancoProximo = false;
        }

        // Sonda pastilha próximo.
        if (retornaPosObjetoMaisProximo(Poupador.PASTILHA) != OBJETO_FORA_DE_VISAO) {
            if (detectarAoRedor(Poupador.PASTILHA)) {
                pastilhaAoRedor = true;
            } else {
                pastilhaAoRedor = false;
            }
            pastilhaProxima = true;
        } else {
            pastilhaProxima = false;
        }
        // Sonda moeda ao redor.
        if (retornaPosObjetoMaisProximo(Poupador.MOEDA) != OBJETO_FORA_DE_VISAO) {
            moedaProxima = true;
        } else {
            moedaProxima = false;
        }
        // Sonda poupador próximo.
        if (retornaPosObjetoMaisProximo(Poupador.POUPADOR) != OBJETO_FORA_DE_VISAO) {
            poupadorProximo = true;
        } else {
            poupadorProximo = false;
        }
        // Sonda ladrão próximo.
        if (retornaPosLadraooMaisProximo() != OBJETO_FORA_DE_VISAO) {
            ladraoProximo = true;
        } else {
            ladraoProximo = false;
        }

    }

    /**
     * Sonda ambiente através do olfato.
     */
    private void sondarComOlfato() {
        // Sente presença do ladrão 
        if (checaPresencaObjetoOlfato(Poupador.LADRAO) != Poupador.NADA_SENTIDO_PELO_OLFATO) {
            ladraoProximoOlfato = true;
        } else {
            ladraoProximoOlfato = false;
        }
        // Sente presença do poupador
        if (checaPresencaObjetoOlfato(Poupador.POUPADOR) != Poupador.NADA_SENTIDO_PELO_OLFATO) {
            poupadorProximoOlfato = true;
        } else {
            poupadorProximoOlfato = false;
        }
    }

    /**
     * Tomar a decisão baseada na sondagem e retornar para onde ir.
     * @return Posição que o personagem deve ir.
     */
    private int decisao() {
        // Ladrão próximo
        if (ladraoProximo) {
            if (pastilhaProxima) {
                // Se o ladrão estiver por perto pega.
                System.out.println("Ir para a pastilha!");
                return irParaObjeto(Poupador.PASTILHA);
            } else {
                System.out.println("Rebater ladrão!");
                return andar(analisaSaidas(gerarSaidas()));
            }     
        }
        // Situação em que a parede está ao redor.
        if (paredeAoRedor) {
            // Se pastilha perto de parede não pega.
            if (detectarAoRedor(Poupador.PASTILHA) && !ladraoProximo) {
                System.out.println("Desviar de pastilha!");
                return desviar(detectarAoRedorPos(Poupador.PASTILHA));
            // Desviar da parede.
            } else {
                System.out.println("Desviar parede!");
                return desviar(detectarAoRedorPos(Poupador.PAREDE));
            }

        }
        // Perto da pastilha.
        if (pastilhaProxima) {
            // Se o ladrão estiver por perto pega.
            if (ladraoProximo) {
                System.out.println("Ir para a pastilha!");
                return irParaObjeto(Poupador.PASTILHA);
            } // Se não desviar da pastilha
            else {
                if (detectarAoRedor(Poupador.PASTILHA)) {
                    System.out.println("Desviar de pastilha!");
                    return desviar(detectarAoRedorPos(Poupador.PASTILHA));
                }
            }
        }
       
        // Ladrão perto pela visão
        if (ladraoProximoOlfato) {
            System.out.println("Rebater ladrão pelo olfato!");
            return andar(rebater(checaPresencaObjetoOlfato(Poupador.LADRAO)));
        }

        // Perto do banco.
        if (bancoProximo) {
            // Se a quantidade de moedas acumuladas for maior que a constante definida, deixar dinheiro no banco.
            if (sensor.getNumeroDeMoedas() >= Poupador.QUANT_MOEDAS_ACUMULADAS) {
                System.out.println("Ir deixar dinheiro no banco!");
                return irParaObjeto(Poupador.BANCO);
            } // Se não desviar do banco.
            else {
                if (detectarAoRedor(Poupador.BANCO)) {
                    System.out.println("Não deixa dinheiro no banco!");
                    return desviar(detectarAoRedorPos(Poupador.BANCO));
                }

            }
        }
        // Moeda Próximo.
        if (moedaProxima) {
            System.out.println("Ir para moeda!");
            return irParaObjeto(Poupador.MOEDA);
        }

        System.out.println("Randômico!");
        return randomicoRacional();
    }

    /**
     * Método para se encaminhar para um objeto.
     * @param objeto Objeto alvo.
     * @return Direcao que deve ser andada pra alcançar o objeto.
     */
    private int irParaObjeto(int objeto) {
        if (retornaPosObjetoMaisProximo(objeto) != Poupador.OBJETO_FORA_DE_VISAO) {
            return andar(retornaPosObjetoMaisProximo(objeto));
        } else {
            return 0;
        }
    }

    /**
     * Retorna o objeto mais próximo dentro da visão.
     * @param objeto Objeto alvo.
     * @return Retorna a posição relativa �  visão do objeto mais próximo.
     */
    private int retornaPosObjetoMaisProximo(int objeto) {
        for (int i = 11; i >= 6; i--) {
            if (sensor.getVisaoIdentificacao()[i] == objeto) {
                return i;
            }
        }
        for (int i = 12; i <= 17; i++) {
            if (sensor.getVisaoIdentificacao()[i] == objeto) {
                return i;
            }
        }
        for (int i = 5; i >= 0; i--) {
            if (sensor.getVisaoIdentificacao()[i] == objeto) {
                return i;
            }
        }
        for (int i = 18; i < sensor.getVisaoIdentificacao().length; i++) {
            if (sensor.getVisaoIdentificacao()[i] == objeto) {
                return i;
            }
        }
        return Poupador.OBJETO_FORA_DE_VISAO;
    }

    private int retornaPosLadraooMaisProximo() {
        for (int i = 11; i >= 6; i--) {
            if (sensor.getVisaoIdentificacao()[i] >= Poupador.LADRAO) {
                return i;
            }
        }
        for (int i = 12; i <= 17; i++) {
            if (sensor.getVisaoIdentificacao()[i] >= Poupador.LADRAO) {
                return i;
            }
        }
        for (int i = 5; i >= 0; i--) {
            if (sensor.getVisaoIdentificacao()[i] >= Poupador.LADRAO) {
                return i;
            }
        }
        for (int i = 18; i < sensor.getVisaoIdentificacao().length; i++) {
            if (sensor.getVisaoIdentificacao()[i] >= Poupador.LADRAO) {
                return i;
            }
        }
        return Poupador.OBJETO_FORA_DE_VISAO;
    }

    /**
     * Método pra calcular pra que lado deve ir de acordo com a visão.
     * @param pos Posição da visão alvo.
     * @return Retorna pra que direcao deve se andar. 
     */
    private int andar(int pos) {
        for (int i = 0; i < cima.length; i++) {
            if (cima[i] == pos) {
                return 1;
            }
        }
        for (int i = 0; i < baixo.length; i++) {
            if (baixo[i] == pos) {
                return 2;
            }
        }
        for (int i = 0; i < esquerda.length; i++) {
            if (esquerda[i] == pos) {
                return 4;
            }
        }
        for (int i = 0; i < direita.length; i++) {
            if (direita[i] == pos) {
                return 3;
            }
        }
        return 0;
    }

    /**
     * Detecta se o objeto passado por parâmetro está ao redor do personagem.
     * @param objeto Objeto alvo.
     * @return Booleano indicando se a afirmação é verdade ou falsa. 
     */
    private boolean detectarAoRedor(int objeto) {
        int up, down, left, right;
        up = sensor.getVisaoIdentificacao()[7];
        down = sensor.getVisaoIdentificacao()[16];
        left = sensor.getVisaoIdentificacao()[11];
        right = sensor.getVisaoIdentificacao()[12];
        if (up == objeto) {
            return true;
        } else if (down == objeto) {
            return true;
        } else if (left == objeto) {
            return true;
        } else if (right == objeto) {
            return true;
        }
        return false;

    }

    /**
     * Retorna em que posição ao redor está o objeto.
     * @param objeto Objeto a ser testado..
     * @return Posição em que o objeto se encontra..
     */
    private int detectarAoRedorPos(int objeto) {
        int up, down, left, right;
        up = sensor.getVisaoIdentificacao()[7];
        down = sensor.getVisaoIdentificacao()[16];
        left = sensor.getVisaoIdentificacao()[11];
        right = sensor.getVisaoIdentificacao()[12];
        if (up == objeto) {
            return 1;
        } else if (down == objeto) {
            return 2;
        } else if (left == objeto) {
            return 4;
        } else if (right == objeto) {
            return 3;
        }
        return 0;

    }

    /**
     * Método com intuito de fugir de algum objeto de acordo com a visão.
     * @param pos Posição em qual se deseja fugir.
     * @return Retorna a posição para que se deve ir para fugir.
     */
    private int rebater(int pos) {
        switch (pos) {
            case 6: {
                return 23;
            }
            case 7: {
                return 21;
            }
            case 8: {
                return 19;
            }
            case 11: {
                return 13;
            }
            case 12: {
                return 10;
            }
            case 17: {
                return 0;
            }
            case 16: {
                return 2;
            }
            case 15: {
                return 4;
            }
            default: {
                return 23 - pos;
            }

        }
    }

    /**
     * Rebater uma direção a ser seguida.
     * @param dir Direção a ser seguida
     * @return Direção repatida. 
     */
    private int rebaterDirProx(int dir) {
        switch (dir) {
            case 1: {
                return 2;
            }
            case 2: {
                return 1;
            }
            case 3: {
                return 4;
            }
            case 4: {
                return 3;
            }
            default: {
                return 1;
            }

        }
    }

    /**
     * Retorna direção aleatória para andar.
     * @return Direção aleatória para andar.
     */
    private int randomDir() {
        return new Random().nextInt(5);
    }

    /**
     * Retorna qualquer direção diferente da passada por parametro.
     * @param dir Posição. 
     * @return Posição diferente.
     */
    private int desviar(int dir) {
        int dirRan = 0;
        do {
            dirRan = randomDir();
        } while (dirRan == dir);
        return dirRan;
    }

    /**
     *  Retorna qtos elementos de um determinado objeto estão perto, e também retorna a sua posição.
     * @param objeto
     * @return Lista com a posição dos Objetos
     */
    private List<Integer> retornaQtdObjetoMaisProximo(int objeto) {
        List ret = new ArrayList<Integer>();
        for (int i = 11; i >= 0; i--) {
            if (sensor.getVisaoIdentificacao()[i] == objeto) {
                ret.add(i);
            }
        }
        for (int i = 12; i < sensor.getVisaoIdentificacao().length; i++) {
            if (sensor.getVisaoIdentificacao()[i] == objeto) {
                ret.add(i);
            }
        }
        return ret;
    }

    private List<Integer> retornaQtdLadraoMaisProximo() {
        List ret = new ArrayList<Integer>();
        for (int i = 11; i >= 0; i--) {
            if (sensor.getVisaoIdentificacao()[i] >= Poupador.LADRAO) {
                ret.add(i);
            }
        }
        for (int i = 12; i < sensor.getVisaoIdentificacao().length; i++) {
            if (sensor.getVisaoIdentificacao()[i] >= Poupador.LADRAO) {
                ret.add(i);
            }
        }
        return ret;
    }

    /**
     * Novo Método rebater, agora retornando mais de 1 opção de saida.
     * @param pos
     * @return
     */
    private List<Integer> rebaterNSaidas(int pos) {
        List<Integer> ret = new ArrayList<Integer>();
        switch (pos) {
            case 0: {
                ret.add(19);
                ret.add(20);
                ret.add(21);
                ret.add(22);
                ret.add(23);
                return ret;
            }
            case 1: {
                ret.add(19);
                ret.add(20);
                ret.add(21);
                ret.add(22);
                ret.add(23);
                return ret;
            }
            case 2: {
                ret.add(19);
                ret.add(20);
                ret.add(21);
                ret.add(22);
                ret.add(23);
                return ret;
            }
            case 3: {
                ret.add(19);
                ret.add(20);
                ret.add(21);
                ret.add(22);
                ret.add(23);
                return ret;
            }
            case 4: {
                ret.add(19);
                ret.add(20);
                ret.add(21);
                ret.add(22);
                ret.add(23);
                return ret;
            }
            case 5: {
                ret.add(19);
                ret.add(20);
                ret.add(21);
                ret.add(22);
                ret.add(23);
                ret.add(3);
                ret.add(4);
                return ret;
            }
            case 6: {
                ret.add(19);
                ret.add(20);
                ret.add(21);
                ret.add(22);
                ret.add(23);
                ret.add(3);
                ret.add(4);
                return ret;
            }
            case 7: {
                ret.add(19);
                ret.add(20);
                ret.add(21);
                ret.add(22);
                ret.add(23);
                ret.add(0);
                ret.add(4);
                return ret;
            }
            case 8: {
                ret.add(19);
                ret.add(20);
                ret.add(21);
                ret.add(22);
                ret.add(23);
                ret.add(0);
                ret.add(1);
                return ret;
            }
            case 9: {
                ret.add(19);
                ret.add(20);
                ret.add(21);
                ret.add(22);
                ret.add(23);
                ret.add(0);
                ret.add(1);
                return ret;
            }
            case 14: {
                ret.add(0);
                ret.add(1);
                ret.add(2);
                ret.add(3);
                ret.add(4);
                ret.add(22);
                ret.add(23);
                return ret;
            }
            case 15: {
                ret.add(0);
                ret.add(1);
                ret.add(2);
                ret.add(3);
                ret.add(4);
                ret.add(22);
                ret.add(23);
                return ret;
            }
            case 16: {
                ret.add(0);
                ret.add(1);
                ret.add(2);
                ret.add(3);
                ret.add(4);
                ret.add(19);
                ret.add(23);
                return ret;
            }
            case 17: {
                ret.add(0);
                ret.add(1);
                ret.add(2);
                ret.add(3);
                ret.add(4);
                ret.add(19);
                ret.add(20);
                return ret;
            }
            case 18: {
                ret.add(0);
                ret.add(1);
                ret.add(2);
                ret.add(3);
                ret.add(4);
                ret.add(19);
                ret.add(20);
                return ret;
            }
            case 19: {
                ret.add(0);
                ret.add(1);
                ret.add(2);
                ret.add(3);
                ret.add(4);
                return ret;
            }
            case 20: {
                ret.add(0);
                ret.add(1);
                ret.add(2);
                ret.add(3);
                ret.add(4);
                return ret;
            }
            case 21: {
                ret.add(0);
                ret.add(1);
                ret.add(2);
                ret.add(3);
                ret.add(4);
                return ret;
            }
            case 22: {
                ret.add(0);
                ret.add(1);
                ret.add(2);
                ret.add(3);
                ret.add(4);
                return ret;
            }
            case 23: {
                ret.add(0);
                ret.add(1);
                ret.add(2);
                ret.add(3);
                ret.add(4);
                return ret;
            }
            default: {
                ret.add(0);
                ret.add(1);
                ret.add(2);
                ret.add(3);
                ret.add(4);
                ret.add(19);
                ret.add(20);
                ret.add(21);
                ret.add(22);
                ret.add(23);
                return ret;
            }

        }
    }

    public List<List<Integer>> gerarSaidas() {
        List<List<Integer>> ret = new Vector<List<Integer>>();
        List<Integer> ladroes = retornaQtdLadraoMaisProximo();
        for (Integer i : ladroes) {
            ret.add(rebaterNSaidas(i));
        }
        return ret;
    }

    public int analisaSaidas(List<List<Integer>> saidas) {
        int retorno = 0;
        Vector<Integer> ret = new Vector<Integer>();
        if (saidas.size() < 2) {
            Random r = new Random();
            int temp = r.nextInt(saidas.get(0).size());
            retorno = saidas.get(0).get(temp);
            System.out.println("desviei do lado para " + retorno);

        } else {

            for (int i = 0; i < saidas.size(); i++) {
                for (int k = i + 1; k < saidas.size(); k++) {
                    for (int l = 0; l < saidas.get(k).size(); l++) {
                        if (saidas.get(i).contains(saidas.get(k).get(l))) {
                            ret.add(saidas.get(k).get(l));
                        }
                    }
                }
            }
            if (ret.size() == 0) {
                System.out.println("hitek, não sei o q fazer.... :S");
                retorno = 1;
            } else {
                Random r = new Random();
                int temp = r.nextInt(ret.size());
                retorno = ret.get(temp);
                System.out.println("desviei do lado para " + retorno);

            }
        }

        return retorno;
    }

    /**
     * Checa presença de objeto através do olfato.
     * @param objeto Objeto a ser checado.
     * @return Retorna a informação se o bojeto está por ali
     */
    private int checaPresencaObjetoOlfato(int objeto) {
        for (int i = 3; i >= 0; i--) {
            if (escolherObjetoOlfato(objeto)[i] >= Poupador.UMA_UNID_ATRAS) {
                return i;
            }
        }
        for (int i = 4; i < sensor.getAmbienteOlfatoLadrao().length; i++) {
            if (escolherObjetoOlfato(objeto)[i] >= Poupador.UMA_UNID_ATRAS) {
                return i;
            }
        }
        return Poupador.NADA_SENTIDO_PELO_OLFATO;
    }

    /**
     * Retorna o olfato requisitado
     * @param objeto objeto a ser identificado pelo olfato.
     * @return Retorna o vetor correto.
     */
    private int[] escolherObjetoOlfato(int objeto) {
        if (objeto == Poupador.LADRAO) {
            return sensor.getAmbienteOlfatoLadrao();
        } else {
            return sensor.getAmbienteOlfatoPoupador();
        }
    }

    /**
     * Randomico pra não voltar.
     * @return Retorna direção que deve ser andada.
     */
    private int randomicoRacional() {
        int dirAtual;
        int dirAtualCont;
        do {
            dirAtual = randomDir();
            dirAtualCont = rebaterDirProx(dirAtual);
        } while (dirAtual == dirAnt);
        dirAnt = dirAtualCont;
        return dirAtual;
    }
}
