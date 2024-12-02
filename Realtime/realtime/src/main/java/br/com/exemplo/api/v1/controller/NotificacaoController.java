package br.com.exemplo.api.v1.controller;

import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.exemplo.domain.service.SseService;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/v1/notificacoes")
public class NotificacaoController {

	private SseService sseService;

	public NotificacaoController(SseService sseService) {
		this.sseService = sseService;
	}
	
	@GetMapping(path = "/observar", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<Object>> observarNotificacoes() {
        return sseService.observarNotificacoes();
    }
	
	@PostMapping
	public void enviarNotificacao(@RequestBody String notificacao) {
		sseService.enviarNotificacao(notificacao);
	}
}
