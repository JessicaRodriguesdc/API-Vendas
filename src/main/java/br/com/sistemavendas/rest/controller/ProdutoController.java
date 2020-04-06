package br.com.sistemavendas.rest.controller;

import br.com.sistemavendas.domain.entity.Produto;
import br.com.sistemavendas.domain.repository.Produtos;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private Produtos repository;

    public ProdutoController(Produtos repository) {
        this.repository = repository;
    }


    @PostMapping
    @ResponseStatus(CREATED)
    @ApiOperation("Salva um novo produto")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Produto salvo com sucesso"),
            @ApiResponse(code = 404, message = "Erro de validacao")
    })
    public Produto save(@RequestBody @Valid Produto produto){
        return repository.save(produto);
    }

    @PutMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    @ApiOperation("Atualizando um produto")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Produto atualizado com sucesso"),
            @ApiResponse(code = 404, message = "Erro de validacao")
    })
    public void update(@PathVariable Integer id, @RequestBody @Valid Produto produto){
        repository
                .findById(id)
                .map( p -> {
                    produto.setId(p.getId());
                    repository.save(produto);
                    return  produto;
                } ).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Produto nao encontrado"));
    }


    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    @ApiOperation("Deletar um produto")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Produto deletado com sucesso"),
            @ApiResponse(code = 404, message = "Erro de validacao")
    })
    public void delete (@PathVariable Integer id){
        repository.findById((id))
                .map(p -> {
                    repository.delete(p);
                    return Void.TYPE;
                })
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Produto nao encontrado"));
    }


    @GetMapping("{id}")
    @ApiOperation("Obter detalhes de um produto")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Produto encontrado"),
            @ApiResponse(code = 404, message = "Produto nao encontrado para o ID informado")
    })
    public Produto getById(@PathVariable Integer id){
        return repository
                .findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Produto nao encontrado"));
    }


    @GetMapping
    @ApiOperation("Listar Produto(s)")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Produto(s) encontrado(s)"),
            @ApiResponse(code = 404, message = "Erro de validacao")
    })
    public List<Produto> find(Produto filtro){
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(
                        ExampleMatcher.StringMatcher.CONTAINING );
        Example example = Example.of(filtro,matcher);
        return repository.findAll(example);
    }
}
