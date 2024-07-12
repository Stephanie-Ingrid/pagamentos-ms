package com.foodapp.pagamentos.controller;

import com.foodapp.pagamentos.dto.PagamentoDTO;
import com.foodapp.pagamentos.service.PagamentoService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/pagamentos")
@AllArgsConstructor
public class PagamentoController {

    private PagamentoService service;

    private RabbitTemplate rabbitTemplate;

    @PatchMapping("/{id}/confirmar")
    @CircuitBreaker(name = "atualizaPedido", fallbackMethod = "pagamentoAutorizadoIntegracaoPendente")
    public void confirmarPagamento(@PathVariable @NotNull Long id){
        service.confirmarPagamento(id);
    }

    public void pagamentoAutorizadoIntegracaoPendente(Long id, Exception e){
        service.alteraStatus(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<PagamentoDTO> listar(@PageableDefault(size = 10) Pageable paginacao) {

        return service.obterTodos(paginacao);
    }

    @GetMapping("/{id}")
    @ResponseStatus (HttpStatus.OK)
    public PagamentoDTO detalhar(@PathVariable @NotNull Long id) {

        return service.obterPorId(id);
    }

    @PostMapping
    public ResponseEntity<PagamentoDTO> cadastrar(@RequestBody @Valid PagamentoDTO dto,
                                                  UriComponentsBuilder uriBuilder) {

        PagamentoDTO pagamento = service.criarPagamento(dto);
        URI endereco = uriBuilder.path("/pagamentos/{id}").buildAndExpand(pagamento.getId()).toUri();

        rabbitTemplate.convertAndSend("pagamentos.ex", "", pagamento);

        return ResponseEntity.created(endereco).body(pagamento);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PagamentoDTO> atualizar(@PathVariable @NotNull Long id,
                                                  @RequestBody @Valid PagamentoDTO dto) {

        PagamentoDTO atualizado = service.atualizarPagamento(id, dto);

        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PagamentoDTO> remover(@PathVariable @NotNull Long id) {

        service.excluirPagamento(id);
        return ResponseEntity.noContent().build();
    }


}
