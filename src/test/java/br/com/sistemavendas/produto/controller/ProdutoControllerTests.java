package br.com.sistemavendas.produto.controller;

import br.com.sistemavendas.produto.dto.ProdutoDTO;
import br.com.sistemavendas.produto.entity.Produto;
import br.com.sistemavendas.produto.service.ProdutoService;
import br.com.sistemavendas.security.jwt.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = {ProdutoController.class, ProdutoService.class,
        PasswordEncoder.class, JwtService.class, ProdutoDTO.class, ModelMapper.class})
@WebMvcTest(controllers = ProdutoController.class)
@AutoConfigureMockMvc
public class ProdutoControllerTests {

    private static final String api_produto = "/api/produtos";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProdutoService service;

    @Test
    @DisplayName("Deve cadastrar Produto.")
    @WithMockUser
    public void cadastrarProdutoTest() throws Exception{
        //cenario
        Integer id = 1;
        ProdutoDTO dto = prepararProdutoDTO();
        Produto produtoFake = Produto.builder()
                .id(id)
                .descricao(dto.getDescricao())
                .preco(dto.getPreco())
                .build();

        BDDMockito.given(service.salvar(Mockito.any(Produto.class)))
                .willReturn(produtoFake);

        String json = new ObjectMapper().writeValueAsString(dto);

        //execulcao
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(api_produto)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        //verificacao
        mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("id").value(produtoFake.getId()))
                .andExpect(jsonPath("descricao").value(produtoFake.getDescricao()))
                .andExpect(jsonPath("preco").value(produtoFake.getPreco()));

    }


    @Test
    @DisplayName("Deve buscar o produto pelo id.")
    @WithMockUser
    public void getProdutoByIdTest() throws Exception{
        //cenario
        Integer id = 1;
        ProdutoDTO dto = prepararProdutoDTO();
        Produto produtoFake = Produto.builder()
                .id(id)
                .descricao(dto.getDescricao())
                .preco(dto.getPreco())
                .build();

        BDDMockito.given(service.obterProduto(Mockito.anyInt()))
                .willReturn(Optional.of(produtoFake));

        //exception
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(api_produto+"/"+id)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON);

        //verificacao
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("id").value(produtoFake.getId()))
                .andExpect(jsonPath("descricao").value(produtoFake.getDescricao()))
                .andExpect(jsonPath("preco").value(produtoFake.getPreco()));

    }

    @Test
    @DisplayName("Deve atualizar um produto existente.")
    @WithMockUser
    public void atualizarProdutoTest() throws Exception{
        //cenario
        Integer id = 1;
        ProdutoDTO dto = prepararProdutoDTO();
        Produto produtoFake = Produto.builder()
                .id(id)
                .descricao("coxinha")
                .preco(dto.getPreco())
                .build();

        BDDMockito.given(service.obterProduto(Mockito.anyInt()))
                .willReturn(Optional.of(produtoFake));
        BDDMockito.given(service.atualizar(Mockito.anyInt(),Mockito.any(Produto.class)))
                .willReturn(produtoFake);

        String json = new ObjectMapper().writeValueAsString(dto);

        //execucao
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(api_produto+"/"+id)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        //verificacao
        mvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("id").value(produtoFake.getId()))
                .andExpect(jsonPath("descricao").value(produtoFake.getDescricao()))
                .andExpect(jsonPath("preco").value(produtoFake.getPreco()));
    }


    @Test
    @DisplayName("Deve deletar um produto existente.")
    @WithMockUser
    public void deletarProdutoTest() throws Exception{
        //cenario
        Integer id = 1;
        ProdutoDTO dto = prepararProdutoDTO();
        Produto produtoFake = Produto.builder()
                .id(id)
                .descricao(dto.getDescricao())
                .preco(dto.getPreco())
                .build();


        BDDMockito.given(service.obterProduto(Mockito.anyInt()))
                .willReturn(Optional.of(produtoFake));

        //execucao
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(api_produto+"/"+id)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON);

        //verificacao
        mvc.perform(request)
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve listar produtos.")
    @WithMockUser
    public void listarClienteTest() throws Exception{
        //cenario
        Integer id = 1;
        ProdutoDTO dto = prepararProdutoDTO();
        Produto produtoFake = Produto.builder()
                .id(id)
                .descricao(dto.getDescricao())
                .preco(dto.getPreco())
                .build();

        List<Produto> ListaProdutosFakes = new ArrayList<Produto>();
        ListaProdutosFakes.add(produtoFake);

        BDDMockito.given(service.listar(Mockito.any(Produto.class)))
                .willReturn(ListaProdutosFakes);

        //execucao
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(api_produto)
                .accept(MediaType.APPLICATION_JSON);

        //verificacao
        mvc.perform(request)
                .andExpect(status().isOk());
    }

    public ProdutoDTO prepararProdutoDTO(){
        return new ProdutoDTO("lasanha",new BigDecimal("40.0"));
    }
}
