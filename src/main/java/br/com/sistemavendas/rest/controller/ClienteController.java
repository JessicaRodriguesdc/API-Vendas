package br.com.sistemavendas.rest.controller;

import br.com.sistemavendas.domain.entity.Cliente;
import br.com.sistemavendas.domain.repository.Clientes;
import br.com.sistemavendas.rest.dto.ClienteDTO;
import br.com.sistemavendas.service.ClienteService;
import io.swagger.annotations.*;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@Api("Api Clientes")
public class ClienteController {

    private ModelMapper modelMapper;
    private ClienteService service;

    public ClienteController(ModelMapper modelMapper,ClienteService service) {
        this.modelMapper = modelMapper;
        this.service = service;
    }

    @GetMapping("{id}")
    @ApiOperation("Obter detalhes de um cliente")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Cliente encontrado"),
            @ApiResponse(code = 404, message = "Cliente nao encontrado para o ID informado")
    })
    public Cliente getClienteById(@PathVariable @ApiParam("id do cliente") Integer id){
        return service
                .obterCliente(id)
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
    public ResponseEntity save(@RequestBody @Valid ClienteDTO clienteDTO){

        Cliente cliente = modelMapper.map(clienteDTO,Cliente.class);

        Cliente clienteSalvo = service.salvar(cliente);

        return ResponseEntity.status(HttpStatus.CREATED).body(clienteSalvo);
    }


    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Deletar um cliente")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Cliente deletado com sucesso"),
            @ApiResponse(code = 404, message = "Erro de validacao")
    })
    public void delete (@PathVariable Integer id){
        service.obterCliente((id))
                .map(cliente -> {
                    service.deletar(cliente);
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
    public void update(@PathVariable Integer id, @RequestBody @Valid ClienteDTO clienteDTO){
         service
                .obterCliente(id)
                .map( clienteExistente -> {
                    Cliente cliente = converterEmEnity(clienteDTO);
                    cliente.setId(clienteExistente.getId());
                    service.salvar(cliente);
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
        return service.listar(example);
    }


    private ClienteDTO converterEmDTO(Cliente cliente){
        ClienteDTO dto = modelMapper.map(cliente,ClienteDTO.class);

        return dto;
    }

    private Cliente converterEmEnity(ClienteDTO clienteDTO){
        Cliente cliente = modelMapper.map(clienteDTO,Cliente.class);

        return cliente;
    }
}
