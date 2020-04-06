package br.com.sistemavendas.domain.repository;

import br.com.sistemavendas.domain.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Produtos extends JpaRepository<Produto, Integer> {

}
