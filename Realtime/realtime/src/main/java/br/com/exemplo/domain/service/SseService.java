package br.com.exemplo.domain.service;

import java.time.Duration;
import java.util.UUID;

import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Slf4j
@Service
public class SseService {

	private final Sinks.Many<ServerSentEvent<Object>> sink;

    public SseService() {
		this.sink = Sinks.many().multicast().onBackpressureBuffer();
	}

    public void enviarNotificacao(String notificacao) {
		ServerSentEvent<Object> alerta = ServerSentEvent.builder()
				.event("alertas")
				.id(UUID.randomUUID().toString())
				.data(notificacao)
				.build();
    	
		log.info("Enviando notificacao...");
		sink.tryEmitNext(alerta);
    }
    
    public Flux<ServerSentEvent<Object>> observarNotificacoes() {
        Flux<ServerSentEvent<Object>> keepAliveFlux = criarMensagemKeepAlive();
        return Flux.merge(keepAliveFlux, sink.asFlux());
    }

    /**
     * Envia mensagens periodicas para manter a conexao aberta
     * 
     * @return
     */
	private Flux<ServerSentEvent<Object>> criarMensagemKeepAlive() {
		return Flux.interval(Duration.ofSeconds(5))
                .map(seq -> ServerSentEvent.builder()
    					.id(String.valueOf(seq))
    					.event("keep-alive")
    					.build());
	}
}
