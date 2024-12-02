package br.com.exemplo.domain.repository;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import br.com.exemplo.domain.model.Relatorio;

@Repository
public interface RelatorioRepository extends ListCrudRepository<Relatorio, Long> {

}
