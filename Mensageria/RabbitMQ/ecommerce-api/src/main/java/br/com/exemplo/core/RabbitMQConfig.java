package br.com.exemplo.core;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.HeadersExchange;
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

	public static final String FILA_PAGAR_COMPRA_REALIZADA = "pagamentos.v1.realizar-pagamento";
	
	public static final String ROTA_CLIENTE_NORMAL = "cliente.normal";
	public static final String ROTA_CLIENTE_VIP = "cliente.vip";
	
	public static final String EXCHANGE_FANOUT_COMPRA_REALIZADA = "compras.v1.compra-realizada";
	public static final String EXCHANGE_DIRECT_COMPRAS_EVENTOS = "compras.v1.eventos";
	public static final String EXCHANGE_TOPIC_COMPRAS = "compras.v1";
	public static final String EXCHANGE_HEADERS_COMPRA_PREMIADA = "compras.v1.compra-premiada";
	
	//Criar exchanges
	@Bean
	public FanoutExchange fanoutExchange() {
		return new FanoutExchange(EXCHANGE_FANOUT_COMPRA_REALIZADA);
	}
	
	@Bean
	public DirectExchange directExchange() {
		return new DirectExchange(EXCHANGE_DIRECT_COMPRAS_EVENTOS);
	}
	
	@Bean
	public TopicExchange topicExchange() {
		return new TopicExchange(EXCHANGE_TOPIC_COMPRAS);
	}
	
	@Bean
	public HeadersExchange headersExchange() {
		return new HeadersExchange(EXCHANGE_HEADERS_COMPRA_PREMIADA);
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
