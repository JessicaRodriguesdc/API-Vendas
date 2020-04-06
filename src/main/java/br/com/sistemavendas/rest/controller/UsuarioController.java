package br.com.sistemavendas.rest.controller;

import br.com.sistemavendas.domain.entity.Usuario;
import br.com.sistemavendas.exception.SenhaInvalidaException;
import br.com.sistemavendas.rest.dto.CredenciaisDTO;
import br.com.sistemavendas.rest.dto.TokenDTO;
import br.com.sistemavendas.security.jwt.JwtService;
import br.com.sistemavendas.service.impl.UsuarioServiceImpl;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioServiceImpl usuarioService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

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
}
