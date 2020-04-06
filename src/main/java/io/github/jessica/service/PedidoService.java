package io.github.jessica.service;

import io.github.jessica.domain.entity.Pedido;
import io.github.jessica.domain.enums.StatusPedido;
import io.github.jessica.rest.dto.PedidoDTO;

import java.util.Optional;

public interface PedidoService {
    Pedido salvar(PedidoDTO dto);

    Optional<Pedido> obterPedidoCompleto(Integer id);

    void atualizaStatus(Integer id, StatusPedido statusPedido);
}
