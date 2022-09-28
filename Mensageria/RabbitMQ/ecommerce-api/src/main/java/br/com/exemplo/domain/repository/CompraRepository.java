package br.com.exemplo.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.exemplo.domain.model.Compra;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {

}
