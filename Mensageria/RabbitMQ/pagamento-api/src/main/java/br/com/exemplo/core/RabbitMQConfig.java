package br.com.exemplo.core;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class RabbitMQConfig {
	
	public static final String FILA_PAGAR_COMPRA_REALIZADA = "pagamentos.v1.realizar-pagamento";
	public static final String FILA_PAGAR_COMPRA_CLIENTE_VIP = "pagamentos.v1.compra-cliente-vip.realizar-pagamento";
	public static final String FILA_PAGAR_COMPRA_CLIENTE_NORMAL = "pagamentos.v1.compra-cliente-normal.realizar-pagamento";
	
	private static final String ROTA_CLIENTE_VIP = "cliente.vip";
	private static final String ROTA_CLIENTE_NORMAL = "cliente.normal";
	private static final String ROTA_CLIENTE = "cliente.#";
	
	private static final String EXCHANGE_FANOUT_COMPRA_REALIZADA = "compras.v1.compra-realizada";
	private static final String EXCHANGE_DIRECT_COMPRAS_EVENTOS = "compras.v1.eventos";
	private static final String EXCHANGE_TOPIC_COMPRAS = "compras.v1";
	private static final String EXCHANGE_HEADERS_COMPRA_PREMIADA = "compras.v1.compra-premiada";
	
	//Criar fila
	@Bean
	public Queue filaCompraRealizada() {
		Map<String, Object> args = new HashMap<>();
		args.put("x-max-priority", 10);
		
		return new Queue(FILA_PAGAR_COMPRA_REALIZADA, true, false, false, args);
	}
	
	@Bean
	public Queue filaCompraClienteVIP() {
		return new Queue(FILA_PAGAR_COMPRA_CLIENTE_VIP);
	}
	
	@Bean
	public Queue filaCompraClienteNormal() {
		return new Queue(FILA_PAGAR_COMPRA_CLIENTE_NORMAL);
	}
	
	@Bean
	public Binding bindingPagarCompraRealizada() {
		Queue queue = new Queue(FILA_PAGAR_COMPRA_REALIZADA);
		FanoutExchange exchange = new FanoutExchange(EXCHANGE_FANOUT_COMPRA_REALIZADA);
		
		return BindingBuilder.bind(queue).to(exchange);
	}
	
	@Bean
	public Binding bindingPagarCompraClienteVIP() {
		Queue queue = new Queue(FILA_PAGAR_COMPRA_CLIENTE_VIP);
		DirectExchange exchange = new DirectExchange(EXCHANGE_DIRECT_COMPRAS_EVENTOS);
		
		return BindingBuilder.bind(queue).to(exchange).with(ROTA_CLIENTE_VIP);
	}
	
	@Bean
	public Binding bindingPagarCompraClienteNormal() {
		Queue queue = new Queue(FILA_PAGAR_COMPRA_CLIENTE_NORMAL);
		DirectExchange exchange = new DirectExchange(EXCHANGE_DIRECT_COMPRAS_EVENTOS);
		
		return BindingBuilder.bind(queue).to(exchange).with(ROTA_CLIENTE_NORMAL);
	}
	
	@Bean
	public Binding bindingNotificarCompraRealizadaComExchangeTopic() {
		Queue queue = new Queue(FILA_PAGAR_COMPRA_REALIZADA);
		TopicExchange exchange = new TopicExchange(EXCHANGE_TOPIC_COMPRAS);
		
		return BindingBuilder.bind(queue).to(exchange).with(ROTA_CLIENTE);
	}
	
	@Bean
	public Binding bindingNotificarCompraPremiada() {
		Queue queue = new Queue(FILA_PAGAR_COMPRA_REALIZADA);
		HeadersExchange exchange = new HeadersExchange(EXCHANGE_HEADERS_COMPRA_PREMIADA);
		
		return BindingBuilder.bind(queue).to(exchange).where("compra-premiada").matches(false);
	}

	//Configuracao para enviar objetos e converter automaticamente para json
	@Bean
	public Jackson2JsonMessageConverter messageConverter() {
		ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
		return new Jackson2JsonMessageConverter(mapper);
	}
	
	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter messageConverter) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(messageConverter);
		
		return rabbitTemplate;
	}
}
