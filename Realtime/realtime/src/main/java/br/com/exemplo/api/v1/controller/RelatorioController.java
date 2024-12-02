package br.com.exemplo.api.v1.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.exemplo.domain.model.Relatorio;
import br.com.exemplo.domain.model.dto.RelatorioDTO;
import br.com.exemplo.domain.service.RelatorioService;

@RestController
@RequestMapping(path = "/v1/relatorios", produces = MediaType.APPLICATION_JSON_VALUE)
public class RelatorioController {
	
	private RelatorioService relatorioService;
	
	public RelatorioController(RelatorioService relatorioService) {
		this.relatorioService = relatorioService;
	}

	@GetMapping
	public List<RelatorioDTO> consultar() {
		List<RelatorioDTO> resultado = new ArrayList<>();
		
		List<Relatorio> relatorios = relatorioService.consultarRelatorio();
		
		for (Relatorio relatorio : relatorios) {
			RelatorioDTO dto = new RelatorioDTO();
			dto.setId(relatorio.getId());
			dto.setStatus(relatorio.getStatus());
			
			resultado.add(dto);
		}
		
		return resultado;
	}
}
