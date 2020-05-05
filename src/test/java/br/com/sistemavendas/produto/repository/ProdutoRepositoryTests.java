package br.com.sistemavendas.produto.repository;

import br.com.sistemavendas.produto.dto.ProdutoDTO;
import br.com.sistemavendas.produto.entity.Produto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("dev")
@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class ProdutoRepositoryTests {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    ProdutoRepository repository;


    @Test
    @DisplayName("Deve cadastrar Produto.")
    public void cadastrarProdutoTest(){
        //cenario
        ProdutoDTO dto = prepararProdutoDTO();
        Produto produtoFake = Produto.builder()
                .descricao(dto.getDescricao())
                .preco(dto.getPreco())
                .build();

        entityManager.persist(produtoFake);

        //execulcao
        Produto produtoSalvo = repository.save(produtoFake);

        //verificacao
        assertThat(produtoSalvo.getId()).isNotNull();

    }

    @Test
    @DisplayName("Deve buscar o produto pelo id.")
    public void getProdutoByIdTest(){
        //cenario
        ProdutoDTO dto = prepararProdutoDTO();
        Produto produtoFake = Produto.builder()
                .descricao(dto.getDescricao())
                .preco(dto.getPreco())
                .build();

        entityManager.persist(produtoFake);

        //execulcao
        Optional<Produto> produtoEncontrado = repository.findById(produtoFake.getId());

        //verificacao
        assertThat(produtoEncontrado.isPresent()).isTrue();
        assertThat(produtoEncontrado.get().getId()).isNotNull();
        assertThat(produtoEncontrado.get().getId()).isEqualTo(produtoFake.getId());
    }

    @Test
    @DisplayName("Deve atualizar um produto existente.")
    public void atualizarProdutoTest(){
        //cenario
        ProdutoDTO dto = prepararProdutoDTO();
        Produto produtoFake = Produto.builder()
                .descricao(dto.getDescricao())
                .preco(dto.getPreco())
                .build();

        entityManager.persist(produtoFake);
        Optional<Produto> produtoEncontrado = repository.findById(produtoFake.getId());
        produtoEncontrado.get().setDescricao("Coxinha");

        //execulcao
        Produto produtoAlterado = repository.save(produtoEncontrado.get());

        //verificacao
        assertThat(produtoAlterado.getId()).isNotNull();
        assertThat(produtoAlterado.getId()).isEqualTo(produtoEncontrado.get().getId());
        assertThat(produtoAlterado.getDescricao()).isEqualTo(produtoEncontrado.get().getDescricao());
    }

    @Test
    @DisplayName("Deve deletar um produto existente.")
    public void deletarProdutoTest(){
        //cenario
        Integer id = 1;
        ProdutoDTO dto = prepararProdutoDTO();
        Produto produtoFake = Produto.builder()
                .descricao(dto.getDescricao())
                .preco(dto.getPreco())
                .build();

        entityManager.persist(produtoFake);

        repository.findById(id);

        //execulcao
        repository.delete(produtoFake);
        Optional<Produto> produtoDeletado =  repository.findById(id);

        //verificacao
        assertThat(produtoDeletado).isEmpty();
        assertThat(produtoDeletado.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Deve listar produtos.")
    public void listarProdutoTest(){
        //cenario
        ProdutoDTO dto = prepararProdutoDTO();
        Produto produtoFake = Produto.builder()
                .descricao(dto.getDescricao())
                .preco(dto.getPreco())
                .build();

        entityManager.persist(produtoFake);
        Optional<Produto> produtoEncontrado = repository.findById(produtoFake.getId());

        Example<Produto> exampleProdutoFake = Example.of(produtoEncontrado.get());

        //execulcao
        List<Produto> listaProdutosEncontrada = repository.findAll(exampleProdutoFake);

        //verificacao
        assertThat(listaProdutosEncontrada.isEmpty()).isFalse();
        assertThat(listaProdutosEncontrada.size()).isEqualTo(1);
        assertThat(listaProdutosEncontrada.indexOf(produtoEncontrado.get().getId())).isNotNull();
    }

    public ProdutoDTO prepararProdutoDTO(){
        return new ProdutoDTO("lasanha",new BigDecimal("40.0"));
    }

}
