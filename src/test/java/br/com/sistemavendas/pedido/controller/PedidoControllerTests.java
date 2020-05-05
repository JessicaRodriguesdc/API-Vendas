package br.com.sistemavendas.pedido.controller;

import br.com.sistemavendas.cliente.dto.ClienteDTO;
import br.com.sistemavendas.cliente.entity.Cliente;
import br.com.sistemavendas.itemPedido.dto.InformacaoItemPedidoDTO;
import br.com.sistemavendas.itemPedido.entity.ItemPedido;
import br.com.sistemavendas.pedido.dto.InformacoesPedidoDTO;
import br.com.sistemavendas.pedido.dto.PedidoDTO;
import br.com.sistemavendas.pedido.entity.Pedido;
import br.com.sistemavendas.pedido.service.PedidoService;
import br.com.sistemavendas.produto.dto.ProdutoDTO;
import br.com.sistemavendas.produto.entity.Produto;
import br.com.sistemavendas.security.jwt.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = {PedidoController.class, PedidoService.class
        ,PasswordEncoder.class, JwtService.class, PedidoDTO.class,ClienteDTO.class, ProdutoDTO.class
        })
@WebMvcTest(controllers = PedidoController.class)
@AutoConfigureMockMvc
public class PedidoControllerTests {

    private static final String api_pedido = "/api/pedidos";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PedidoService service;

    @Autowired
    private ModelMapper modelMapper;


    @DisplayName("Deve cadastrar pedido.")
    @WithMockUser
    public void cadastrarPedidoTest() throws Exception{
        //cenario
        Integer id =1;
        Pedido pedidoFake = prepararPedido();
        InformacoesPedidoDTO infoDTO = prepararInformacoesPedidoDTO(pedidoFake);

        PedidoDTO dto = PedidoDTO.builder()
                .cliente(infoDTO.getCodigo())
                .items(infoDTO.getItems())
                .total(infoDTO.getTotal())
                .build();


        BDDMockito.given(service.salvar(Mockito.any(PedidoDTO.class))).
                willReturn(pedidoFake);

        String json = new ObjectMapper().writeValueAsString(dto);

        //execulcao
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(api_pedido)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        //verificacao
        mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("id").value(pedidoFake.getId()))
                .andExpect(jsonPath("cliente").value(pedidoFake.getCliente()))
                .andExpect(jsonPath("items").value(pedidoFake.getItens()))
                .andExpect(jsonPath("total").value(pedidoFake.getTotal()))
        ;
    }

    @DisplayName("Deve cadastrar pedido.")
    @WithMockUser
    public void getPedidoByIdTest() throws Exception{
        //cenario

        //execulcao

        //verificacao
    }

    @DisplayName("Deve cadastrar pedido.")
    @WithMockUser
    public void atualizarStatusTest() throws Exception{
        //cenario

        //execulcao

        //verificacao
    }

    public Pedido prepararPedido(){


        Cliente clienteFake = Cliente.builder()
                .id(1)
                .nome("jessica")
                .cpf("93229667000")
                .build();

        Produto produtoFake = Produto.builder()
                .id(1)
                .descricao("lasanha")
                .preco(new BigDecimal("40.0"))
                .build();

        ItemPedido itemFake = ItemPedido.builder()
                .produto(produtoFake)
                .quantidade(2)
                .build();

        List<ItemPedido> listaItensPedidos = new ArrayList<ItemPedido>();
        listaItensPedidos.add(itemFake);


        Pedido pedidoFake =  Pedido.builder()
                .cliente(clienteFake)
                .total(new BigDecimal("40.0"))
                .itens(listaItensPedidos)
                .build();

         return pedidoFake;

    }

    /*
    private List<ItemPedidoDTO> converterDTO(List<InformacaoItemPedidoDTO> itens){
        if(CollectionUtils.isEmpty(itens)){
            return Collections.emptyList();
        }
        return itens.stream().map(
                item -> ItemPedidoDTO
                        .builder()
                        .produto(item.getDescricaoProduto().getId())
                        .quantidade(item.getQuantidade())
                        .build()

        ).collect(Collectors.toList());
    }
    */


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


    public InformacoesPedidoDTO prepararInformacoesPedidoDTO(Pedido pedido){
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
}
