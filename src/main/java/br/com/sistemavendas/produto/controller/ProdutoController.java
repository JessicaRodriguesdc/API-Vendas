package br.com.sistemavendas.produto.controller;

import br.com.sistemavendas.produto.dto.ProdutoDTO;
import br.com.sistemavendas.produto.entity.Produto;
import br.com.sistemavendas.produto.service.ProdutoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/produtos")
@Api("Api Produtos")
public class ProdutoController {

    private ModelMapper modelMapper;
    private ProdutoService service;

    public ProdutoController(ProdutoService service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @ApiOperation("Salva um novo produto")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Produto salvo com sucesso"),
            @ApiResponse(code = 404, message = "Erro de validacao")
    })
    public ResponseEntity save(@RequestBody @Valid ProdutoDTO produtoDTO){
        Produto produto = convertrEmEntity(produtoDTO);

        Produto produtoSalvo = service.salvar(produto);

        return ResponseEntity.status(HttpStatus.CREATED).body(produtoSalvo);
    }

    @PutMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    @ApiOperation("Atualizando um produto")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Produto atualizado com sucesso"),
            @ApiResponse(code = 404, message = "Erro de validacao")
    })
    public ResponseEntity update(@PathVariable Integer id, @RequestBody @Valid ProdutoDTO produtoDTO){
        Produto produtoAtualizado = service.
                obterProduto(id)
                .map( produtoExiste -> {
                    Produto produto = convertrEmEntity(produtoDTO);
                    produto.setId(produtoExiste.getId());
                    service.salvar(produto);
                    return  produtoExiste;
                } ).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Produto nao encontrado"));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(produtoAtualizado);
    }


    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    @ApiOperation("Deletar um produto")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Produto deletado com sucesso"),
            @ApiResponse(code = 404, message = "Erro de validacao")
    })
    public void delete (@PathVariable Integer id){
        service.obterProduto((id))
                .map(produto -> {
                    service.deletar(produto);
                    return produto;
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
        return service
                .obterProduto(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Produto nao encontrado"));
    }


    @GetMapping
    @ApiOperation("Listar Produto(s)")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Produto(s) encontrado(s)"),
            @ApiResponse(code = 404, message = "Erro de validacao")
    })
    public List<ProdutoDTO> find(ProdutoDTO produtoDTO){
        Produto produto = convertrEmEntity(produtoDTO);
        List<Produto> produtos = service.listar(produto);

        List<ProdutoDTO> produtosDTO = produtos
                .stream()
                .map(entity -> converterEmDTO(entity))
                .collect(Collectors.toList());

        return produtosDTO;
    }

    private ProdutoDTO converterEmDTO(Produto produto){
        ProdutoDTO dto = modelMapper.map(produto,ProdutoDTO.class);
        return dto;
    }

    private Produto convertrEmEntity(ProdutoDTO dto){
        Produto produto = modelMapper.map(dto,Produto.class);
        return produto;
    }
}
