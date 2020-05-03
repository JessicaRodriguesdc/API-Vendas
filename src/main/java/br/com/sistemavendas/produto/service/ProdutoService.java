package br.com.sistemavendas.produto.service;

import br.com.sistemavendas.produto.entity.Produto;

import java.util.List;
import java.util.Optional;

public interface ProdutoService {

    Produto salvar(Produto produto);

    Produto atualizar(Integer id, Produto produto);

    Optional<Produto> obterProduto(Integer id);

    List<Produto> listar(Produto produto);

    void deletar(Produto produto);
}
