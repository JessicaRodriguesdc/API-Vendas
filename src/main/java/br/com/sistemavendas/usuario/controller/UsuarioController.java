package br.com.sistemavendas.usuario.controller;
import br.com.sistemavendas.usuario.dto.UsuarioDTO;
import br.com.sistemavendas.usuario.entity.Usuario;
import br.com.sistemavendas.util.exception.SenhaInvalidaException;
import br.com.sistemavendas.util.rest.dto.CredenciaisDTO;
import br.com.sistemavendas.util.rest.dto.TokenDTO;
import br.com.sistemavendas.security.jwt.JwtService;
import br.com.sistemavendas.usuario.service.impl.UsuarioServiceImpl;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
@Api("Api Usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioServiceImpl service;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Salva um novo usuario")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Usuario salvo com sucesso"),
            @ApiResponse(code = 404, message = "Erro de validacao")
    })
    public ResponseEntity salvar(@RequestBody @Valid UsuarioDTO usuarioDTO){
        Usuario usuario = converterEmEntity(usuarioDTO);
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());

        usuario.setSenha(senhaCriptografada);
        Usuario usuarioSalvo = service.salvar(usuario);

        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSalvo);
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

            UserDetails usuarioAutentificado = service.autenticar(usuario);

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
        return service
                .obterUsuario(id)
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
        service.obterUsuario(id)
                .map(usuario -> {
                    service.deletar(usuario);
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
    public ResponseEntity update(@PathVariable Integer id, @RequestBody @Valid UsuarioDTO usuarioDTO){

        Usuario usuarioAtualizado = service
                    .obterUsuario(id)
                    .map( usuarioExistente -> {
                     Usuario usuario = converterEmEntity(usuarioDTO);
                        usuario.setId(usuarioExistente.getId());
                        service.salvar(usuario);
                        return  usuarioExistente;
                    } ).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Usuario nao encontrado"));

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(usuarioAtualizado);
    }

    @GetMapping
    @ApiOperation("Listar usuario(s)")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Usuario(s) encontrado(s)"),
            @ApiResponse(code = 404, message = "Erro de validacao")
    })
    public List<UsuarioDTO> find(UsuarioDTO usuarioDTO){
        Usuario usuario = converterEmEntity(usuarioDTO);
        List<Usuario> usuarios = service.listar(usuario);

        List<UsuarioDTO> usuariosDTO = usuarios
                .stream()
                .map(entity -> converterEmDTO(entity))
                .collect(Collectors.toList());

        return usuariosDTO;
    }

    private UsuarioDTO converterEmDTO(Usuario usuario){
        UsuarioDTO dto = modelMapper.map(usuario,UsuarioDTO.class);

        return dto;
    }

    private Usuario converterEmEntity(UsuarioDTO dto){
        Usuario usuario = modelMapper.map(dto,Usuario.class);

        return usuario;
    }
}

