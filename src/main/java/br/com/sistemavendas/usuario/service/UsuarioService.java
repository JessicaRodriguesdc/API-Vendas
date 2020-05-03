package br.com.sistemavendas.usuario.service;

import br.com.sistemavendas.usuario.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {

    Usuario salvar (Usuario usuario);

    Usuario atualizar(Integer id, Usuario usuario);

    Optional<Usuario> obterUsuario(Integer id);

    List<Usuario> listar(Usuario usuario);

    void deletar(Usuario usuario);


}
