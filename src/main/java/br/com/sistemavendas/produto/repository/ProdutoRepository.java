package br.com.sistemavendas.produto.repository;

import br.com.sistemavendas.produto.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

}
