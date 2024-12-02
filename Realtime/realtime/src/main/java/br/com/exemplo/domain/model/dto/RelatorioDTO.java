package br.com.exemplo.domain.model.dto;

import br.com.exemplo.domain.enums.StatusRelatorio;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RelatorioDTO {

	private Long id;
	private StatusRelatorio status;
}
