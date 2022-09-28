package br.com.exemplo.api.v1.model;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompraDTO {

	private Long id;
	private String descricao;
	private BigDecimal valor;
	private String nomeCliente;
}
