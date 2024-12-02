package br.com.exemplo.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.exemplo.domain.model.Relatorio;
import br.com.exemplo.domain.repository.RelatorioRepository;

@Service
public class RelatorioService {

	private RelatorioRepository relatorioRepository;
	
	public RelatorioService(RelatorioRepository relatorioRepository) {
		this.relatorioRepository = relatorioRepository;
	}

	public List<Relatorio> consultarRelatorio(){
		return relatorioRepository.findAll();
	}
}
