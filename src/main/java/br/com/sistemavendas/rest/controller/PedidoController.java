package br.com.sistemavendas.rest.controller;

import br.com.sistemavendas.domain.entity.ItemPedido;
import br.com.sistemavendas.domain.entity.Pedido;
import br.com.sistemavendas.rest.dto.AtualizacaoStatusPedidoDTO;
import br.com.sistemavendas.rest.dto.InformacaoItemPedidoDTO;
import br.com.sistemavendas.rest.dto.InformacoesPedidoDTO;
import br.com.sistemavendas.rest.dto.PedidoDTO;
import br.com.sistemavendas.domain.enums.StatusPedido;
import br.com.sistemavendas.service.PedidoService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @ApiOperation("Salvar um novo pedido")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Pedido salvo com sucesso"),
            @ApiResponse(code = 404, message = "Erro de validacao")
    })
    public Integer save(@RequestBody @Valid PedidoDTO dto){
        Pedido pedido =  service.salvar(dto);
        return pedido.getId();

    }

    @GetMapping("{id}")
    @ApiOperation("Obter detalhes de um pedido")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Produto encontrado"),
            @ApiResponse(code = 404, message = "Produto nao encontrado para o ID informado")
    })
    public InformacoesPedidoDTO getById(@PathVariable Integer id ){
        return service
                .obterPedidoCompleto(id)
                .map( p -> converter(p) )
                .orElseThrow(() ->
                        new ResponseStatusException(NOT_FOUND, "Pedido não encontrado."));
    }

    @PatchMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    @ApiOperation("Atualizando status")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Status atualizado com sucesso"),
            @ApiResponse(code = 404, message = "Erro de validacao")
    })
    private void updateStatus(@PathVariable Integer id,
                                  @RequestBody @Valid AtualizacaoStatusPedidoDTO dto){
        String novoStatus = dto.getNovoStatus();
        service.atualizaStatus(id, StatusPedido.valueOf(novoStatus));

    }

    private InformacoesPedidoDTO converter(Pedido pedido){
        return InformacoesPedidoDTO.builder()
                .codigo(pedido.getId())
                .dataPedido(pedido.getDataPedido().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .cpf(pedido.getCliente().getCpf())
                .nomeCliente(pedido.getCliente().getNome())
                .total(pedido.getTotal())
                .status(pedido.getStatus().name())
                .items(converter(pedido.getItens()))
                .build();

    }

    private List<InformacaoItemPedidoDTO> converter(List<ItemPedido> itens){
        if(CollectionUtils.isEmpty(itens)){
            return Collections.emptyList();
        }
        return itens.stream().map(
                item -> InformacaoItemPedidoDTO
                        .builder()
                        .descricaoProduto(item.getProduto().getDescricao())
                        .precoUnitario(item.getProduto().getPreco())
                        .quantidade(item.getQuantidade())
                        .build()

        ).collect(Collectors.toList());
    }
}
