package br.com.exemplo.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import br.com.exemplo.core.RabbitMQConfig;
import br.com.exemplo.domain.model.CompraDTO;

@Component
public class PagamentoListener {

	@RabbitListener(queues = RabbitMQConfig.FILA_PAGAR_COMPRA_REALIZADA)
	public void aoRealizarCompraPorFila(CompraDTO compraDTO) {
		System.out.println(String.format("Pagamento do pedido %d realizado com sucesso!", compraDTO.getId()));
	}
	
	@RabbitListener(queues = RabbitMQConfig.FILA_PAGAR_COMPRA_CLIENTE_VIP)
	public void aoRealizarCompraClienteVIP(CompraDTO compraDTO) {
		System.out.println(String.format("Pagamento do pedido %d realizado com 5%% de desconto!", compraDTO.getId()));
	}
	
	@RabbitListener(queues = RabbitMQConfig.FILA_PAGAR_COMPRA_CLIENTE_NORMAL)
	public void aoRealizarCompraClienteNormal(CompraDTO compraDTO) {
		System.out.println(String.format("Pagamento do pedido %d realizado com sucesso. Ganhou 2%% de cashback!", compraDTO.getId()));
	}
}
