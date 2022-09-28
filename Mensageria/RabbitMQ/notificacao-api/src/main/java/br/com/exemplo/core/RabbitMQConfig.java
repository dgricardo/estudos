package br.com.exemplo.core;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class RabbitMQConfig {
	
	public static final String FILA_NOTIFICAR_COMPRA_REALIZADA = "notificacoes.v1.compra-realizada.enviar-notificacao";
	public static final String FILA_NOTIFICAR_COMPRA_CLIENTE_VIP = "notificacoes.v1.compra-cliente-vip.enviar-notificacao";
	public static final String FILA_NOTIFICAR_COMPRA_CLIENTE_NORMAL = "notificacoes.v1.compra-cliente-normal.enviar-notificacao";
	public static final String FILA_NOTIFICAR_COMPRA_PREMIADA = "notificacoes.v1.compra-premiada.enviar-notificacao";
	
	private static final String ROTA_CLIENTE_VIP = "cliente.vip";
	private static final String ROTA_CLIENTE_NORMAL = "cliente.normal";
	private static final String ROTA_CLIENTE = "cliente.#";
	
	private static final String EXCHANGE_FANOUT_COMPRA_REALIZADA = "compras.v1.compra-realizada";
	private static final String EXCHANGE_DIRECT_COMPRAS_EVENTOS = "compras.v1.eventos";
	private static final String EXCHANGE_TOPIC_COMPRAS = "compras.v1";
	private static final String EXCHANGE_HEADERS_COMPRA_PREMIADA = "compras.v1.compra-premiada";
	
	@Bean
	public Queue filaNotificarCompraRealizada() {
		return new Queue(FILA_NOTIFICAR_COMPRA_REALIZADA);
	}
	
	@Bean
	public Queue filaNotificarCompraClienteVIP() {
		return new Queue(FILA_NOTIFICAR_COMPRA_CLIENTE_VIP);
	}
	
	@Bean
	public Queue filaNotificarCompraClienteNormal() {
		return new Queue(FILA_NOTIFICAR_COMPRA_CLIENTE_NORMAL);
	}
	
	@Bean
	public Queue filaNotificarCompraPremiada() {
		return new Queue(FILA_NOTIFICAR_COMPRA_PREMIADA);
	}
	
	@Bean
	public Binding bindingNotificarCompraRealizada() {
		Queue queue = new Queue(FILA_NOTIFICAR_COMPRA_REALIZADA);
		FanoutExchange exchange = new FanoutExchange(EXCHANGE_FANOUT_COMPRA_REALIZADA);
		
		return BindingBuilder.bind(queue).to(exchange);
	}
	
	@Bean
	public Binding bindingNotificarCompraClienteVIP() {
		Queue queue = new Queue(FILA_NOTIFICAR_COMPRA_CLIENTE_VIP);
		DirectExchange exchange = new DirectExchange(EXCHANGE_DIRECT_COMPRAS_EVENTOS);
		
		return BindingBuilder.bind(queue).to(exchange).with(ROTA_CLIENTE_VIP);
	}
	
	@Bean
	public Binding bindingNotificarCompraClienteNormal() {
		Queue queue = new Queue(FILA_NOTIFICAR_COMPRA_CLIENTE_NORMAL);
		DirectExchange exchange = new DirectExchange(EXCHANGE_DIRECT_COMPRAS_EVENTOS);
		
		return BindingBuilder.bind(queue).to(exchange).with(ROTA_CLIENTE_NORMAL);
	}
	
	@Bean
	public Binding bindingNotificarCompraRealizadaComExchangeTopic() {
		Queue queue = new Queue(FILA_NOTIFICAR_COMPRA_REALIZADA);
		TopicExchange exchange = new TopicExchange(EXCHANGE_TOPIC_COMPRAS);
		
		return BindingBuilder.bind(queue).to(exchange).with(ROTA_CLIENTE);
	}
	
	@Bean
	public Binding bindingNotificarCompraPremiada() {
		Queue queue = new Queue(FILA_NOTIFICAR_COMPRA_PREMIADA);
		HeadersExchange exchange = new HeadersExchange(EXCHANGE_HEADERS_COMPRA_PREMIADA);
		
		return BindingBuilder.bind(queue).to(exchange).where("compra-premiada").matches(true);
	}
	
	@Bean
	public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
		return new RabbitAdmin(connectionFactory);
	}
	
	@Bean
	public ApplicationListener<ApplicationReadyEvent> applicationReadyEventApplicationListener(RabbitAdmin rabbitAdmin){
		return event -> rabbitAdmin.initialize();
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
