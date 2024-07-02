package com.foodapp.pagamentos.service;

import com.foodapp.pagamentos.dto.PagamentoDTO;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PagamentoService {

    Page obterTodos (Pageable paginacao );

    PagamentoDTO obterPorId (Long id);

    PagamentoDTO criarPagamento(PagamentoDTO pagamentoDTO);

    PagamentoDTO atualizarPagamento (Long id, PagamentoDTO pagamentoDTO);

    void excluirPagamento (Long id);

}