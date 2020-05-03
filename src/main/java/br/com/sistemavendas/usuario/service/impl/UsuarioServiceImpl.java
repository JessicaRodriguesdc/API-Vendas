package br.com.sistemavendas.usuario.service.impl;

import br.com.sistemavendas.usuario.repository.UsuarioRepository;
import br.com.sistemavendas.usuario.entity.Usuario;
import br.com.sistemavendas.usuario.service.UsuarioService;
import br.com.sistemavendas.util.exception.RegraNegocioException;
import br.com.sistemavendas.util.exception.SenhaInvalidaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService,UserDetailsService {
    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UsuarioRepository repository;

    @Override
    public Usuario salvar (Usuario usuario){

        return repository.save(usuario);
    }

    @Override
    public Usuario atualizar(Integer id, Usuario usuario) {
        Optional<Usuario> existe = obterUsuario(id);
        if(!existe.isPresent()){
            throw new RegraNegocioException("Usuario nao encontrado");
        }
        usuario.setId(id);
        Usuario usuarioSalvo = salvar(usuario);
        return usuarioSalvo;
    }

    @Override
    public Optional<Usuario> obterUsuario(Integer id) {
        Optional<Usuario> usuario = 
        return Optional.empty();
    }

    @Override
    public List<Usuario> listar(Usuario usuario) {
        return null;
    }

    @Override
    public void deletar(Usuario usuario) {

    }

    public UserDetails autenticar(Usuario usuario){
        UserDetails user = loadUserByUsername(usuario.getLogin());
        boolean senhasBatem =  encoder.matches(usuario.getSenha(),user.getPassword());
        if(senhasBatem){
            return user;
        }
        throw new SenhaInvalidaException();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Usuario usuario = repository.findByLogin(username)
                .orElseThrow(()-> new UsernameNotFoundException("Usuario nao encontrado na base de dados"));

        String[] roles = usuario.isAdmin() ?
                new String[]{"ADMIN","USER"}: new String[]{"USER"};

        return User
                .builder()
                .username(usuario.getLogin())
                .password(usuario.getSenha())
                .roles(roles)
                .build();
    }


}
