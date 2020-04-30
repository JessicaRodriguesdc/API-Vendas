package br.com.sistemavendas.cliente.service;

import br.com.sistemavendas.cliente.entity.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteService {
    Cliente salvar(Cliente cliente);

    Cliente atualizar(Integer id,Cliente cliente);

    Optional<Cliente> obterCliente(Integer id);

    List<Cliente> listar(Cliente cliente);

    void deletar(Cliente cliente);
}
