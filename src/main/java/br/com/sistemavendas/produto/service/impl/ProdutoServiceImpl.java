package br.com.sistemavendas.produto.service.impl;

import br.com.sistemavendas.produto.entity.Produto;
import br.com.sistemavendas.produto.service.ProdutoService;
import br.com.sistemavendas.util.exception.RegraNegocioException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import br.com.sistemavendas.produto.repository.ProdutoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoServiceImpl implements ProdutoService {

    private ProdutoRepository repository;

    public ProdutoServiceImpl(ProdutoRepository repository) {
        this.repository = repository;
    }

    @Override
    public Produto salvar(Produto produto) {
        return repository.save(produto);
    }

    @Override
    public Produto atualizar(Integer id, Produto produto) {
        Optional<Produto> existe = obterProduto(id);
        if(!existe.isPresent()){
            throw new RegraNegocioException("Produto nao encontrado");
        }
        produto.setId(id);
        Produto produtoSalvo = repository.save(produto);

        return produtoSalvo;
    }

    @Override
    public Optional<Produto> obterProduto(Integer id) {
        Optional<Produto> produto = repository.findById(id);
        return produto;
    }

    @Override
    public List<Produto> listar(Produto produto) {
        Example<Produto> example = Example.of(produto,
                ExampleMatcher
                        .matching()
                        .withIgnoreCase()
                        .withIgnoreNullValues()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
        );
        return repository.findAll(example);
    }

    @Override
    public void deletar(Produto produto) {
        Optional<Produto> existe = obterProduto(produto.getId());
        if(!existe.isPresent()){
            throw new RegraNegocioException("Produto nao encontrado");
        }
        repository.delete(produto);
    }
}
