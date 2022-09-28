package br.com.exemplo.api.v1.controller;

import java.math.BigDecimal;

import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.exemplo.api.v1.converter.CompraConverter;
import br.com.exemplo.api.v1.model.CompraDTO;
import br.com.exemplo.api.v1.model.RealizarCompraDTO;
import br.com.exemplo.core.RabbitMQConfig;
import br.com.exemplo.domain.model.Compra;
import br.com.exemplo.domain.service.CompraService;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/v1/compras")
public class CompraController {

	@Autowired
	private CompraService compraService;
	
	@Autowired
	private CompraConverter compraConverter;
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CompraDTO comprarUtilizandoFila(@Valid @RequestBody RealizarCompraDTO realizarCompraDTO) {
		Compra compra = compraService.comprar(realizarCompraDTO);
		CompraDTO compraDTO = compraConverter.paraCompraDTO(compra);
		
		//Envio basico de mensagem
//		org.springframework.amqp.core.Message mensagem = new org.springframework.amqp.core.Message(compra.getId().toString().getBytes());
//		rabbitTemplate.send("compras.v1.compra-realizada", mensagem);
		
		int prioridade;
		if(compraDTO.getValor().compareTo(new BigDecimal("10000")) >= 0) {
			prioridade = 5;
		}else {
			prioridade = 1;
		}
		
		//Envio de mensagem com prioridade
		MessagePostProcessor processor = message -> {
			MessageProperties properties = message.getMessageProperties();
			properties.setPriority(prioridade);
			
			return message;
		};
		
		rabbitTemplate.convertAndSend(RabbitMQConfig.FILA_PAGAR_COMPRA_REALIZADA, compraDTO, processor);
		
		return compraDTO;
	}
	
	@PostMapping("/exchange-fanout")
	@ResponseStatus(HttpStatus.CREATED)
	public CompraDTO comprarUtilizandoExchangeFanout(@Valid @RequestBody RealizarCompraDTO realizarCompraDTO) {
		Compra compra = compraService.comprar(realizarCompraDTO);
		CompraDTO compraDTO = compraConverter.paraCompraDTO(compra);
		
		rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_FANOUT_COMPRA_REALIZADA, "", compraDTO);
		
		return compraDTO;
	}
	
	@PostMapping("/exchange-direct")
	@ResponseStatus(HttpStatus.CREATED)
	public CompraDTO comprarUtilizandoExchangeDirect(@Valid @RequestBody RealizarCompraDTO realizarCompraDTO) {
		Compra compra = compraService.comprar(realizarCompraDTO);
		CompraDTO compraDTO = compraConverter.paraCompraDTO(compra);
		
		String rota;
		if(compraDTO.getValor().compareTo(new BigDecimal("10000")) >= 0) {
			rota = RabbitMQConfig.ROTA_CLIENTE_VIP;
		}else {
			rota = RabbitMQConfig.ROTA_CLIENTE_NORMAL;
		}
		
		rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_DIRECT_COMPRAS_EVENTOS, rota, compraDTO);
		
		return compraDTO;
	}
	
	@PostMapping("/exchange-topic")
	@ResponseStatus(HttpStatus.CREATED)
	public CompraDTO comprarUtilizandoExchangeTopic(@Valid @RequestBody RealizarCompraDTO realizarCompraDTO) {
		Compra compra = compraService.comprar(realizarCompraDTO);
		CompraDTO compraDTO = compraConverter.paraCompraDTO(compra);
		
		rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_TOPIC_COMPRAS, RabbitMQConfig.ROTA_CLIENTE_VIP, compraDTO);
		
		return compraDTO;
	}
	
	@PostMapping("/exchange-headers")
	@ResponseStatus(HttpStatus.CREATED)
	public CompraDTO comprarUtilizandoExchangeHeaders(@Valid @RequestBody RealizarCompraDTO realizarCompraDTO) {
		Compra compra = compraService.comprar(realizarCompraDTO);
		CompraDTO compraDTO = compraConverter.paraCompraDTO(compra);
		
		MessagePostProcessor processor = message -> {
			MessageProperties properties = message.getMessageProperties();
			
			if(compraDTO.getNomeCliente().equalsIgnoreCase("Diego")) {
				properties.setHeader("compra-premiada", true);
			}else {
				properties.setHeader("compra-premiada", false);
			}
			
			return message;
		};
		
		rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_HEADERS_COMPRA_PREMIADA, "", compraDTO, processor);
		
		return compraDTO;
	}
}
