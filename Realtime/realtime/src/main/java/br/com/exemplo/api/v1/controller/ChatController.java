package br.com.exemplo.api.v1.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/chat")
public class ChatController {

	private SimpMessagingTemplate messagingTemplate;
	
	public ChatController(SimpMessagingTemplate messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
	}

	@MessageMapping("/enviar-mensagem") // Rota para receber mensagens
	@SendTo("/topic/mensagem") // Rota para enviar mensagens
	public String enviarMensagem(String mensagem) {
		return mensagem;
	}

	@PostMapping("/enviar-mensagem-backend")
	public void enviarMensagemViaBackend(@RequestBody String mensagem) {
		messagingTemplate.convertAndSend("/topic/mensagem", mensagem);
	}
}
