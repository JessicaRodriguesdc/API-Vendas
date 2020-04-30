package br.com.sistemavendas.itemPedido.repository;

import br.com.sistemavendas.itemPedido.entity.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Integer> {
}
