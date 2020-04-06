package br.com.sistemavendas.service;

import br.com.sistemavendas.domain.entity.Pedido;
import br.com.sistemavendas.domain.enums.StatusPedido;
import br.com.sistemavendas.rest.dto.PedidoDTO;

import java.util.Optional;

public interface PedidoService {
    Pedido salvar(PedidoDTO dto);

    Optional<Pedido> obterPedidoCompleto(Integer id);

    void atualizaStatus(Integer id, StatusPedido statusPedido);
}
