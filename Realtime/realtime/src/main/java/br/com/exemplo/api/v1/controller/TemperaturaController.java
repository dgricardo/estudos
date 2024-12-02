package br.com.exemplo.api.v1.controller;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
@RequestMapping(path = "/v1/temperaturas", produces = MediaType.APPLICATION_JSON_VALUE)
public class TemperaturaController {
	
	private Short temperatura = 0;
    private ExecutorService temperaturas = Executors.newFixedThreadPool(5);
    
    @GetMapping
    public DeferredResult<String> getTemperatura() {
        DeferredResult<String> output = new DeferredResult<>();
        temperaturas.execute(() -> {
            try {
            	buscarTemperatura();
                output.setResult(temperatura.toString());
            } catch (InterruptedException e) {
                output.setErrorResult("Erro ao buscar temperatura");
                Thread.currentThread().interrupt();
            }
        });
        
        output.onTimeout(() -> output.setErrorResult("Timeout ao buscar temperatura!"));
        
        return output;
    }
    
    private Short buscarTemperatura() throws InterruptedException {
    	Thread.sleep(Duration.ofSeconds(10));
    	return temperatura;
    }
    
	@PutMapping("/{temperatura}")
    public void atualizarTemperatura(@PathVariable Short temperatura) {
        this.temperatura = temperatura;
    }
}
