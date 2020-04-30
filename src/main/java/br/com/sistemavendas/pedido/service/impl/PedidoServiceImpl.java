package br.com.sistemavendas.pedido.service.impl;

import br.com.sistemavendas.cliente.entity.Cliente;
import br.com.sistemavendas.itemPedido.entity.ItemPedido;
import br.com.sistemavendas.pedido.entity.Pedido;
import br.com.sistemavendas.produto.entity.Produto;
import br.com.sistemavendas.pedido.enums.StatusPedido;
import br.com.sistemavendas.cliente.repository.ClienteRepository;
import br.com.sistemavendas.itemPedido.repository.ItemPedidoRepository;
import br.com.sistemavendas.pedido.repository.PedidoRepository;
import br.com.sistemavendas.produto.repository.ProdutoRepository;
import br.com.sistemavendas.util.exception.PedidoNaoEncontradoException;
import br.com.sistemavendas.util.exception.RegraNegocioException;
import br.com.sistemavendas.itemPedido.dto.ItemPedidoDTO;
import br.com.sistemavendas.pedido.dto.PedidoDTO;
import br.com.sistemavendas.pedido.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl  implements PedidoService {

    private final PedidoRepository repository;
    private final ClienteRepository clienteRepositoryRepository;
    private final ProdutoRepository produtoRepositoryRepository;
    private final ItemPedidoRepository itemPedidoRepository;

    @Override
    @Transactional
    public Pedido salvar(PedidoDTO dto) {
        Integer idCliente = dto.getCliente();
        Cliente cliente = clienteRepositoryRepository
                .findById(idCliente)
                .orElseThrow(() -> new RegraNegocioException("Codigo de cliente invalido."));

        Pedido pedido = new Pedido();
        pedido.setTotal(dto.getTotal());
        pedido.setDataPedido(LocalDate.now());
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.REALIZADO);

        List<ItemPedido> itemsPedido = converterItems(pedido, dto.getItems());
        repository.save(pedido);
        itemPedidoRepository.saveAll(itemsPedido);
        pedido.setItens(itemsPedido);
        return pedido;
    }

    @Override
    public Optional<Pedido> obterPedidoCompleto(Integer id) {
        return repository.findByIdFetchItens(id);
    }

    @Override
    @Transactional
    public void atualizaStatus(Integer id, StatusPedido statusPedido) {
        repository
                .findById(id)
                .map(pedido -> {
                    pedido.setStatus(statusPedido);
                    return repository.save(pedido);
                }).orElseThrow( () -> new PedidoNaoEncontradoException());
    }

    private List<ItemPedido> converterItems(Pedido pedido, List<ItemPedidoDTO> items){
        if(items.isEmpty()){
            throw new RegraNegocioException("Nao e possivel realizar um pedido sem items.");
        }
        return items
                .stream()
                .map(dto -> {
                    Integer idProduto = dto.getProduto();
                    Produto produto = produtoRepositoryRepository
                            .findById(idProduto)
                            .orElseThrow(
                                    () -> new RegraNegocioException(
                                            "Codigo de produto invalido: "+idProduto
                                    ));

                    ItemPedido itemPedido = new ItemPedido();
                    itemPedido.setQuantidade(dto.getQuantidade());
                    itemPedido.setPedido(pedido);
                    itemPedido.setProduto(produto);
                    return itemPedido;
                }).collect(Collectors.toList());
    }
}
