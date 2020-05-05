package br.com.sistemavendas.produto.service;
import br.com.sistemavendas.produto.dto.ProdutoDTO;
import br.com.sistemavendas.produto.entity.Produto;
import br.com.sistemavendas.produto.repository.ProdutoRepository;
import br.com.sistemavendas.produto.service.impl.ProdutoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class ProdutoServiceTests {

    ProdutoService service;

    @MockBean
    ProdutoRepository repository;

    @BeforeEach
    public void setUp(){
        this.service = new ProdutoServiceImpl(repository);
    }


    @Test
    @DisplayName("Deve cadastrar Produto.")
    public void cadastrarProdutoTest(){
        //cenario
        Integer id = 1;
        ProdutoDTO dto = prepararProdutoDTO();
        Produto produtoFake = Produto.builder()
                .id(id)
                .descricao(dto.getDescricao())
                .preco(dto.getPreco())
                .build();

        Mockito.when(repository.save(Mockito.any(Produto.class))).thenReturn(produtoFake);

        //execulcao
        Produto produtoSalvo = service.salvar(produtoFake);

        //verificacao
        assertThat(produtoSalvo.getId()).isNotNull();
        assertThat(produtoSalvo.getId()).isEqualTo(produtoFake.getId());
        assertThat(produtoSalvo.getDescricao()).isEqualTo(produtoFake.getDescricao());
        assertThat(produtoSalvo.getPreco()).isEqualTo(produtoFake.getPreco());

    }

    @Test
    @DisplayName("Deve buscar o produto pelo id.")
    public void getProdutoByIdTest(){
        //cenario
        Integer id = 1;
        ProdutoDTO dto = prepararProdutoDTO();
        Produto produtoFake = Produto.builder()
                .id(id)
                .descricao(dto.getDescricao())
                .preco(dto.getPreco())
                .build();

        Mockito.when(repository.findById(Mockito.anyInt())).thenReturn(Optional.of(produtoFake));

        //execulcao
        Optional<Produto> produtoEncontrado = service.obterProduto(id);

        //verificacao
        assertThat(produtoEncontrado.get().getId()).isNotNull();
        assertThat(produtoEncontrado.get().getId()).isEqualTo(produtoFake.getId());
        assertThat(produtoEncontrado.get().getDescricao()).isEqualTo(produtoFake.getDescricao());
        assertThat(produtoEncontrado.get().getPreco()).isEqualTo(produtoFake.getPreco());
    }

    @Test
    @DisplayName("Deve atualizar um produto existente.")
    public void atualizarProdutoTest(){
        //cenario
        Integer id = 1;
        ProdutoDTO dto = prepararProdutoDTO();
        Produto produtoFake = Produto.builder()
                .id(id)
                .descricao(dto.getDescricao())
                .preco(dto.getPreco())
                .build();

        Produto produtoFakeModificado = produtoFake;
        produtoFakeModificado.setDescricao("Coxinha");

        Mockito.when(repository.findById(Mockito.anyInt())).thenReturn(Optional.of(produtoFake));
        Mockito.when(repository.save(Mockito.any(Produto.class))).thenReturn(produtoFakeModificado);

        //execulcao
        Produto produtoAtualizado = service.atualizar(id,produtoFakeModificado);

        //verificacao
        assertThat(produtoAtualizado.getId()).isNotNull();
        assertThat(produtoAtualizado.getId()).isEqualTo(produtoFakeModificado.getId());
        assertThat(produtoAtualizado.getDescricao()).isEqualTo(produtoFakeModificado.getDescricao());
        assertThat(produtoAtualizado.getPreco()).isEqualTo(produtoFakeModificado.getPreco());

    }

    @Test
    @DisplayName("Deve deletar um produto existente.")
    public void deletarProdutoTest(){
        //cenario
        Integer id = 1;
        ProdutoDTO dto = prepararProdutoDTO();
        Produto produtoFake = Produto.builder()
                .id(id)
                .descricao(dto.getDescricao())
                .preco(dto.getPreco())
                .build();

        Mockito.when(repository.findById(Mockito.anyInt())).thenReturn(Optional.of(produtoFake));

        //execulcao
        assertDoesNotThrow(()->service.deletar(produtoFake));

        //verificacao
        Mockito.verify(repository,Mockito.times(1)).delete(produtoFake);
    }

    @Test
    @DisplayName("Deve listar produtos.")
    public void listarProdutoTest(){
        //cenario
        Integer id = 1;
        ProdutoDTO dto = prepararProdutoDTO();
        Produto produtoFake = Produto.builder()
                .id(id)
                .descricao(dto.getDescricao())
                .preco(dto.getPreco())
                .build();

        List<Produto> listaProdutosFake = new ArrayList<Produto>();
        listaProdutosFake.add(produtoFake);

        Mockito.when(repository.findAll(Mockito.any(Example.class))).thenReturn(listaProdutosFake);
        //execulcao
        List<Produto> listaProdutos = service.listar(produtoFake);

        //verificacao
        assertThat(listaProdutos).isNotNull();
        assertThat(listaProdutos.get(0).getId()).isNotNull();
        assertThat(listaProdutos.get(0).getId()).isEqualTo(produtoFake.getId());
        assertThat(listaProdutos.get(0).getDescricao()).isEqualTo(produtoFake.getDescricao());
        assertThat(listaProdutos.get(0).getPreco()).isEqualTo(produtoFake.getPreco());
    }


    public ProdutoDTO prepararProdutoDTO(){
        return new ProdutoDTO("lasanha",new BigDecimal("40.0"));
    }


}
