package br.com.sistemavendas.rest.controller;

import br.com.sistemavendas.domain.entity.Cliente;
import br.com.sistemavendas.domain.repository.Clientes;
import io.swagger.annotations.*;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@Api("Api Clientes")
public class ClienteController {

    private Clientes clientes;

    public ClienteController(Clientes clientes) {
        this.clientes = clientes;
    }

    @GetMapping("{id}")
    @ApiOperation("Obter detalhes de um cliente")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Cliente encontrado"),
            @ApiResponse(code = 404, message = "Cliente nao encontrado para o ID informado")
    })
    public Cliente getClienteById(@PathVariable @ApiParam("id do cliente") Integer id){
        return clientes
                .findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Cliente nao encontrado"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Salva um novo cliente")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Cliente salvo com sucesso"),
            @ApiResponse(code = 404, message = "Erro de validacao")
    })
    public Cliente save(@RequestBody @Valid Cliente cliente){
        return clientes.save(cliente);
    }


    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Deletar um cliente")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Cliente deletado com sucesso"),
            @ApiResponse(code = 404, message = "Erro de validacao")
    })
    public void delete (@PathVariable Integer id){
        clientes.findById((id))
                .map(cliente -> {
                    clientes.delete(cliente);
                    return cliente;
                })
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Cliente nao encontrado"));
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Atualizando um cliente")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Cliente atualizado com sucesso"),
            @ApiResponse(code = 404, message = "Erro de validacao")
    })
    public void update(@PathVariable Integer id, @RequestBody @Valid Cliente cliente){
         clientes
                .findById(id)
                .map( clienteExistente -> {
                    cliente.setId(clienteExistente.getId());
                    clientes.save(cliente);
                    return  clienteExistente;
                } ).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,
                 "Cliente nao encontrado"));
    }

    @GetMapping
    @ApiOperation("Listar cliente(s)")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Cliente(s) encontrado(s)"),
            @ApiResponse(code = 404, message = "Erro de validacao")
    })
    public List<Cliente> find(Cliente filtro){
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(
                        ExampleMatcher.StringMatcher.CONTAINING );
        Example example = Example.of(filtro,matcher);
        return clientes.findAll(example);
    }
}
