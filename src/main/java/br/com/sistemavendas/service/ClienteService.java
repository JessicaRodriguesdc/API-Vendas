package br.com.sistemavendas.service;

import br.com.sistemavendas.domain.entity.Cliente;
import br.com.sistemavendas.rest.dto.ClienteDTO;
import io.swagger.models.auth.In;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ClienteService {
    Cliente salvar(Cliente cliente);

    Cliente atualizar(Integer id,Cliente cliente);

    Optional<Cliente> obterCliente(Integer id);

    List<Cliente> listar(Cliente cliente);

    void deletar(Cliente cliente);
}
