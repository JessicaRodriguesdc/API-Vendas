package br.com.sistemavendas.rest.dto;

import br.com.sistemavendas.validation.NotEmptyList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoDTO {
    //@NotNull(message = "Informe o codigo do cliente")
    @NotNull(message = "{campo.codigo-cliente.obrigatorio}")
    private Integer cliente;
    //@NotNull(message = "Campo Total do pedido e obrigatorio")
    @NotNull(message = "{campo.total-pedido.obrigatorio}")
    private BigDecimal total;
    //@NotEmptyList(message = "Pedido nao pode ser realizado sem itens.")
    @NotEmptyList(message = "{campo.items-pedido.obrigatorio}")
    private List<ItemPedidoDTO> items;


}
