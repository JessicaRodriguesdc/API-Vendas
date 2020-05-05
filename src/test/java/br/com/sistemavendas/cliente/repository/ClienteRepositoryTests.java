package br.com.sistemavendas.cliente.repository;

import br.com.sistemavendas.cliente.entity.Cliente;
import br.com.sistemavendas.cliente.repository.ClienteRepository;
import br.com.sistemavendas.cliente.dto.ClienteDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("dev")
@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class ClienteRepositoryTests {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    ClienteRepository repository;

    @Test
    @DisplayName("Deve cadastrar cliente.")
    public void cadastrarClienteTest(){
        //cenario
        ClienteDTO dto = prepararClienteDTO();
        Cliente clienteFake = Cliente.builder()
                .nome(dto.getNome())
                .cpf(dto.getCpf())
                .build();

        entityManager.persist(clienteFake);

        //execulcao
        Cliente clienteSalvo = repository.save(clienteFake);

        //verificacao
        assertThat(clienteSalvo.getId()).isNotNull();
    }

    @Test
    @DisplayName("Deve buscar o cliente pelo id.")
    public void getClienteByIdTest(){
        //cenario
        ClienteDTO dto = prepararClienteDTO();
        Cliente clienteFake = Cliente.builder()
                .nome(dto.getNome())
                .cpf(dto.getCpf())
                .build();

        entityManager.persist(clienteFake);

        //execulcao
        Optional<Cliente> clienteEncontrado = repository.findById(clienteFake.getId());

        //verificacao
        assertThat(clienteEncontrado.isPresent()).isTrue();
        assertThat(clienteEncontrado.get().getId()).isNotNull();
        assertThat(clienteEncontrado.get().getId()).isEqualTo(clienteFake.getId());

    }

    @Test
    @DisplayName("Deve atualizar um cliente existente.")
    public void atualizarClienteTest(){
        //cenario
        ClienteDTO dto = prepararClienteDTO();
        Cliente clienteFake = Cliente.builder()
                .nome(dto.getNome())
                .cpf(dto.getCpf())
                .build();
        entityManager.persist(clienteFake);
        Optional<Cliente> clienteEncontrado = repository.findById(clienteFake.getId());
        clienteEncontrado.get().setNome("Jessica Rodrigues");

        //execulcao
        Cliente clienteAlterado = repository.save(clienteEncontrado.get());

        //verificacao
        assertThat(clienteAlterado.getId()).isNotNull();
        assertThat(clienteAlterado.getId()).isEqualTo(clienteEncontrado.get().getId());
        assertThat(clienteAlterado.getNome()).isEqualTo(clienteEncontrado.get().getNome());
    }

    @Test
    @DisplayName("Deve deletar um cliente existente.")
    public void deletarClienteTest(){
        //cenario
        Integer id = 1;
        ClienteDTO dto = prepararClienteDTO();
        Cliente clienteFake = Cliente.builder()
                .nome(dto.getNome())
                .cpf(dto.getCpf())
                .build();

        entityManager.persist(clienteFake);

        repository.findById(id);

        //execulcao
        repository.delete(clienteFake);
        Optional<Cliente> clienteDeletado =  repository.findById(id);

        //verificacao
        assertThat(clienteDeletado).isEmpty();
        assertThat(clienteDeletado.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Deve listar clientes.")
    public void listarClienteTest(){
        //cenario
        ClienteDTO dto = prepararClienteDTO();
        Cliente clienteFake = Cliente.builder()
                .nome(dto.getNome())
                .cpf(dto.getCpf())
                .build();

        entityManager.persist(clienteFake);
        Optional<Cliente> clienteEncontrado = repository.findById(clienteFake.getId());

        Example<Cliente> exampleClienteFake = Example.of(clienteEncontrado.get());

        //execulcao
        List<Cliente> listaClientesEncontrada = repository.findAll(exampleClienteFake);

        //verificacao
        assertThat(listaClientesEncontrada.isEmpty()).isFalse();
        assertThat(listaClientesEncontrada.size()).isEqualTo(1);
        assertThat(listaClientesEncontrada.indexOf(clienteEncontrado.get().getId())).isNotNull();

    }

    public ClienteDTO prepararClienteDTO(){
        return new ClienteDTO("jessica","93229667000");
    }

}
