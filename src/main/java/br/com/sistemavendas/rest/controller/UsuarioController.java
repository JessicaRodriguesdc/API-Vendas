package br.com.sistemavendas.rest.controller;

import br.com.sistemavendas.domain.entity.Cliente;
import br.com.sistemavendas.domain.entity.Usuario;
import br.com.sistemavendas.domain.repository.Usuarios;
import br.com.sistemavendas.exception.SenhaInvalidaException;
import br.com.sistemavendas.rest.dto.CredenciaisDTO;
import br.com.sistemavendas.rest.dto.TokenDTO;
import br.com.sistemavendas.security.jwt.JwtService;
import br.com.sistemavendas.service.impl.UsuarioServiceImpl;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioServiceImpl usuarioService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final Usuarios usuarios;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Salva um novo usuario")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Usuario salvo com sucesso"),
            @ApiResponse(code = 404, message = "Erro de validacao")
    })
    public Usuario salvar(@RequestBody @Valid Usuario usuario){
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);
        return usuarioService.salvar(usuario);
    }

    @PostMapping("/auth")
    @ApiOperation("Logar")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Usuario logado com sucesso"),
            @ApiResponse(code = 404, message = "Erro de validacao")
    })
    public TokenDTO autenticar(@RequestBody CredenciaisDTO credenciais){
        try{
            Usuario usuario = Usuario.builder()
                    .login(credenciais.getLogin())
                    .senha(credenciais.getSenha()).build();

            UserDetails usuarioAutentificado = usuarioService.autenticar(usuario);

            String token = jwtService.gerarToken(usuario);

            return new TokenDTO(usuario.getLogin(), token);

        }catch (UsernameNotFoundException  | SenhaInvalidaException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }



    @GetMapping("{id}")
    @ApiOperation("Obter detalhes de um usuario")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Usuario encontrado"),
            @ApiResponse(code = 404, message = "Usuario nao encontrado para o ID informado")
    })
    public Usuario getById(@PathVariable @ApiParam("id do usuario") Integer id){
        return usuarios
                .findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Usuario nao encontrado"));
    }


    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Deletar um usuario")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Usuario deletado com sucesso"),
            @ApiResponse(code = 404, message = "Erro de validacao")
    })
    public void delete (@PathVariable Integer id){
        usuarios.findById((id))
                .map(usuario -> {
                    usuarios.delete(usuario);
                    return usuario;
                })
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Usuario nao encontrado"));
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Atualizando um usuario")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Usuario atualizado com sucesso"),
            @ApiResponse(code = 404, message = "Erro de validacao")
    })
    public void update(@PathVariable Integer id, @RequestBody @Valid Usuario usuario){
        usuarios
                .findById(id)
                .map( usuarioExistente -> {
                    usuario.setId(usuarioExistente.getId());
                    usuarios.save(usuario);
                    return  usuarioExistente;
                } ).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Usuario nao encontrado"));
    }

    @GetMapping
    @ApiOperation("Listar usuario(s)")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Usuario(s) encontrado(s)"),
            @ApiResponse(code = 404, message = "Erro de validacao")
    })
    public List<Cliente> find(Usuario filtro){
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(
                        ExampleMatcher.StringMatcher.CONTAINING );
        Example example = Example.of(filtro,matcher);
        return usuarios.findAll(example);
    }
}

  eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqZXNzaWNhIiwiZXhwIjoxNTg2MjI0NDEzfQ.s9MTdCMZfkU7l8gTFNTlZRisqrgOlw_QKwT5B0r_71XmZ4f33oKYs0ssjFIEvINNALjuJsem8EBxkMEV1PkFKQ