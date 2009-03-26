package br.com.unifor.ia.util;

import java.util.Date;

public class RegistroPoupador {

	private Integer numeroPoupador;
	private Integer quantidadeRoubo;
	private Date ultimoAssalto;
	
	public Integer getNumeroPoupador() {
		return numeroPoupador;
	}
	public void setNumeroPoupador(Integer numeroPoupador) {
		this.numeroPoupador = numeroPoupador;
	}
	public Integer getQuantidadeRoubo() {
		return quantidadeRoubo;
	}
	public void setQuantidadeRoubo(Integer quantidadeRoubo) {
		this.quantidadeRoubo = quantidadeRoubo;
	}
	public Date getUltimoAssalto() {
		return ultimoAssalto;
	}
	public void setUltimoAssalto(Date ultimoAssalto) {
		this.ultimoAssalto = ultimoAssalto;
	}
	
	
	
}
