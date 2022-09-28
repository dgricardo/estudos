package br.com.exemplo.api.v1.model;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RealizarCompraDTO {
	
	@NotBlank
	private String nomeCliente;
	
	@NotBlank
	private String descricao;
	
	@NotNull
	private BigDecimal valor;

}
