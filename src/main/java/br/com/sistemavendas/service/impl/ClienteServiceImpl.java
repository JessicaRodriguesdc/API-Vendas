package br.com.sistemavendas.service.impl;

import br.com.sistemavendas.domain.entity.Cliente;
import br.com.sistemavendas.domain.repository.Clientes;
import br.com.sistemavendas.exception.RegraNegocioException;
import br.com.sistemavendas.service.ClienteService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService {

    private Clientes repository;

    public ClienteServiceImpl(Clientes repository) {
        this.repository = repository;
    }

    @Override
    public Cliente salvar(Cliente cliente) {
        return repository.save(cliente);
    }

    @Override
    public Cliente atualizar(Integer id, Cliente cliente) {
        Optional<Cliente> existe = obterCliente(id);
        if(!existe.isPresent()){
            throw  new RegraNegocioException("Cliente nao encontrado");
        }
        cliente.setId(id);
        Cliente clinteSalvo = repository.save(cliente);

        return clinteSalvo;
    }


    @Override
    public Optional<Cliente> obterCliente(Integer id) {
        Optional<Cliente> cliente = repository.findById(id);
        return cliente;
    }

    @Override
    public List<Cliente> listar(Cliente cliente) {
        Example<Cliente> example = Example.of(cliente,
                ExampleMatcher
                        .matching()
                        .withIgnoreCase()
                        .withIgnoreNullValues()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
        );
        return repository.findAll(example);
    }

    @Override
    public void deletar(Cliente cliente) {
        Optional<Cliente> existe = obterCliente(cliente.getId());
        if(!existe.isPresent()){
        }
        repository.delete(cliente);
    }


}
