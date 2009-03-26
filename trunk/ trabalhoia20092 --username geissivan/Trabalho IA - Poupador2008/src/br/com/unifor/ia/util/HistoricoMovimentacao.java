package br.com.unifor.ia.util;

import java.util.Date;

public class HistoricoMovimentacao {

	private Integer posicao;
	private Integer movimento;
	private Date dataMovimentacao;
	private Integer contadorRodadas;

	public Integer getPosicao() {
		return posicao;
	}

	public void setPosicao(Integer posicao) {
		this.posicao = posicao;
	}

	public Integer getMovimento() {
		return movimento;
	}

	public void setMovimento(Integer movimento) {
		this.movimento = movimento;
	}

	public Date getDataMovimentacao() {
		return dataMovimentacao;
	}

	public void setDataMovimentacao(Date dataMovimentacao) {
		this.dataMovimentacao = dataMovimentacao;
	}

	public Integer getContadorRodadas() {
		return contadorRodadas;
	}

	public void setContadorRodadas(Integer contadorRodadas) {
		this.contadorRodadas = contadorRodadas;
	}

}
