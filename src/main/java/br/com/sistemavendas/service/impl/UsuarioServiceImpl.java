package br.com.sistemavendas.service.impl;

import br.com.sistemavendas.domain.repository.Usuarios;
import br.com.sistemavendas.exception.SenhaInvalidaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioServiceImpl implements UserDetailsService {
    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private Usuarios repository;

    @Transactional
    public br.com.sistemavendas.domain.entity.Usuario salvar (br.com.sistemavendas.domain.entity.Usuario usuario){
        return repository.save(usuario);
    }

    public UserDetails autenticar(br.com.sistemavendas.domain.entity.Usuario usuario){
        UserDetails user = loadUserByUsername(usuario.getLogin());
        boolean senhasBatem =  encoder.matches(usuario.getSenha(),user.getPassword());
        if(senhasBatem){
            return user;
        }
        throw new SenhaInvalidaException();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        br.com.sistemavendas.domain.entity.Usuario usuario = repository.findByLogin(username)
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
