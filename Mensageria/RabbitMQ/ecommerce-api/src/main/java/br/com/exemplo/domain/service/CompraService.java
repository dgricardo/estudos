package br.com.exemplo.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.exemplo.api.v1.converter.CompraConverter;
import br.com.exemplo.api.v1.model.RealizarCompraDTO;
import br.com.exemplo.domain.model.Compra;
import br.com.exemplo.domain.repository.CompraRepository;

@Service
public class CompraService {
	
	@Autowired
	private CompraRepository compraRepository;
	
	@Autowired
	private CompraConverter compraConverter;
	
	public Compra comprar(RealizarCompraDTO realizarCompraDTO) {
		Compra compra = compraConverter.paraCompra(realizarCompraDTO);
		
		return compraRepository.save(compra);
	}
}
