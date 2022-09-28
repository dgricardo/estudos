package br.com.exemplo.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import br.com.exemplo.core.RabbitMQConfig;
import br.com.exemplo.domain.model.CompraDTO;

@Component
public class NotificacaoListener {

	@RabbitListener(queues = RabbitMQConfig.FILA_NOTIFICAR_COMPRA_REALIZADA)
	public void aoRealizarCompra(CompraDTO compraDTO) {
		System.out.println(String.format("Olá %s! Seu pedido de ID %d de um %s está sendo processado e logo você receberá um email com o status.", 
				compraDTO.getNomeCliente(), compraDTO.getId(), compraDTO.getDescricao()));
	}
	
	@RabbitListener(queues = RabbitMQConfig.FILA_NOTIFICAR_COMPRA_CLIENTE_VIP)
	public void aoRealizarCompraClienteVIP(CompraDTO compraDTO) {
		System.out.println(String.format("Olá %s! Seu pedido de ID %d de um %s foi processado e você ganhou 10%% de desconto na sua próxima compra.", 
				compraDTO.getNomeCliente(), compraDTO.getId(), compraDTO.getDescricao()));
	}
	
	@RabbitListener(queues = RabbitMQConfig.FILA_NOTIFICAR_COMPRA_CLIENTE_NORMAL)
	public void aoRealizarCompraClienteNormal(CompraDTO compraDTO) {
		System.out.println(String.format("Olá %s! Seu pedido de ID %d de um %s foi processado e você ganhou 5%% de desconto na sua próxima compra.", 
				compraDTO.getNomeCliente(), compraDTO.getId(), compraDTO.getDescricao()));
	}
	
	@RabbitListener(queues = RabbitMQConfig.FILA_NOTIFICAR_COMPRA_PREMIADA)
	public void aoRealizarCompraPremiada(CompraDTO compraDTO) {
		System.out.println(String.format("Parabéns, %s! Seu pedido de ID %d de um %s foi premiado.", 
				compraDTO.getNomeCliente(), compraDTO.getId(), compraDTO.getDescricao()));
	}
}
