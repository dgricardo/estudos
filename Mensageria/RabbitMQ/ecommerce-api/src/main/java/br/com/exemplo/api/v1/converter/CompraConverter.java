package br.com.exemplo.api.v1.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.exemplo.api.v1.model.CompraDTO;
import br.com.exemplo.api.v1.model.RealizarCompraDTO;
import br.com.exemplo.domain.model.Compra;

@Component
public class CompraConverter {

	@Autowired
	private ModelMapper modelMapper;
	
	public Compra paraCompra(RealizarCompraDTO realizarCompraDTO) {
		return modelMapper.map(realizarCompraDTO, Compra.class);
	}
	
	public CompraDTO paraCompraDTO(Compra compra) {
		return modelMapper.map(compra, CompraDTO.class);
	}
}
