package br.com.sistemavendas.pedido.service;

import br.com.sistemavendas.pedido.entity.Pedido;
import br.com.sistemavendas.pedido.enums.StatusPedido;
import br.com.sistemavendas.pedido.dto.PedidoDTO;

import java.util.Optional;

public interface PedidoService {
    Pedido salvar(PedidoDTO dto);

    Optional<Pedido> obterPedidoCompleto(Integer id);

    void atualizaStatus(Integer id, StatusPedido statusPedido);
}
