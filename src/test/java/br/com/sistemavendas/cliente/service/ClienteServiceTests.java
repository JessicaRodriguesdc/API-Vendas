package br.com.sistemavendas.cliente.service;

import br.com.sistemavendas.cliente.entity.Cliente;
import br.com.sistemavendas.cliente.repository.ClienteRepository;
import br.com.sistemavendas.cliente.dto.ClienteDTO;
import br.com.sistemavendas.cliente.service.ClienteService;
import br.com.sistemavendas.cliente.service.impl.ClienteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static  org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class ClienteServiceTests {

    ClienteService service;

    @MockBean
    ClienteRepository repository;

    @BeforeEach
    public void setUp(){
        this.service = new ClienteServiceImpl(repository);
    }

    @Test
    @DisplayName("Deve cadastrar cliente.")
    public void cadastrarClienteTest(){
        //cenario
        Integer id = 1;
        ClienteDTO dto = prepararClienteDTO();
        Cliente clienteFake = Cliente.builder()
                .id(id)
                .nome(dto.getNome())
                .cpf(dto.getCpf())
                .build();

        Mockito.when(repository.save(Mockito.any(Cliente.class))).thenReturn(clienteFake);
        //execulcao
        Cliente clienteSalvo =  service.salvar(clienteFake);

        //verificacao
        assertThat(clienteSalvo.getId()).isNotNull();
        assertThat(clienteSalvo.getId()).isEqualTo(clienteFake.getId());
        assertThat(clienteSalvo.getNome()).isEqualTo(clienteFake.getNome());
        assertThat(clienteSalvo.getCpf()).isEqualTo(clienteFake.getCpf());
    }

    @Test
    @DisplayName("Deve buscar o cliente pelo id.")
    public void getClienteByIdTest(){
        //cenario
        Integer id = 1;
        ClienteDTO dto = prepararClienteDTO();
        Cliente clienteFake = Cliente.builder()
                .id(id)
                .nome(dto.getNome())
                .cpf(dto.getCpf())
                .build();

        Mockito.when(repository.findById(Mockito.anyInt())).thenReturn(Optional.of(clienteFake));

        //execulcao
        Optional<Cliente> clienteEncontrado = service.obterCliente(id);

        //verificacao
        assertThat(clienteEncontrado.get().getId()).isNotNull();
        assertThat(clienteEncontrado.get().getId()).isEqualTo(clienteFake.getId());
        assertThat(clienteEncontrado.get().getNome()).isEqualTo(clienteFake.getNome());
        assertThat(clienteEncontrado.get().getCpf()).isEqualTo(clienteFake.getCpf());

    }

    @Test
    @DisplayName("Deve atualizar um cliente existente.")
    public void atualizarClienteTest(){
        //cenario
        Integer id =1;
        ClienteDTO dto = prepararClienteDTO();
        Cliente clienteFake = Cliente.builder()
                .id(id)
                .nome(dto.getNome())
                .cpf(dto.getCpf())
                .build();

        Cliente clienteFakeModificado = clienteFake;
        clienteFakeModificado.setNome("jessica rodrigues");

        Mockito.when(repository.findById(Mockito.anyInt())).thenReturn(Optional.of(clienteFake));
        Mockito.when(repository.save(Mockito.any(Cliente.class))).thenReturn(clienteFakeModificado);

        //execulcao
        Cliente clienteAtualizado = service.atualizar(id,clienteFakeModificado);

        //verificacao
        assertThat(clienteAtualizado.getId()).isNotNull();
        assertThat(clienteAtualizado.getId()).isEqualTo(clienteFakeModificado.getId());
        assertThat(clienteAtualizado.getNome()).isEqualTo(clienteFakeModificado.getNome());
        assertThat(clienteAtualizado.getCpf()).isEqualTo(clienteFakeModificado.getCpf());

    }

    @Test
    @DisplayName("Deve deletar um cliente existente.")
    public void deletarClienteTest(){
        //cenario
        Integer id =1;
        ClienteDTO dto = prepararClienteDTO();
        Cliente clienteFake = Cliente.builder()
                .id(id)
                .nome(dto.getNome())
                .cpf(dto.getCpf())
                .build();

        Mockito.when(repository.findById(Mockito.anyInt())).thenReturn(Optional.of(clienteFake));

        //execulcao
        assertDoesNotThrow(()->service.deletar(clienteFake));

        //verificacao
        Mockito.verify(repository,Mockito.times(1)).delete(clienteFake);
    }

    @Test
    @DisplayName("Deve listar clientes.")
    public void listarClienteTest(){
        //cenario
        Integer id = 1;
        ClienteDTO dto = prepararClienteDTO();
        Cliente clienteFake = Cliente.builder()
                .id(id)
                .nome(dto.getNome())
                .cpf(dto.getCpf())
                .build();

        List<Cliente> listaClientesFake = new ArrayList<Cliente>();
        listaClientesFake.add(clienteFake);

        Mockito.when(repository.findAll(Mockito.any(Example.class))).thenReturn(listaClientesFake);
        //execulcao
        List<Cliente> listaClientes = service.listar(clienteFake);

        //verificacao
        assertThat(listaClientes).isNotNull();
        assertThat(listaClientes.get(0).getId()).isNotNull();
        assertThat(listaClientes.get(0).getId()).isEqualTo(clienteFake.getId());
        assertThat(listaClientes.get(0).getNome()).isEqualTo(clienteFake.getNome());
        assertThat(listaClientes.get(0).getCpf()).isEqualTo(clienteFake.getCpf());
    }

    public ClienteDTO prepararClienteDTO(){
        return new ClienteDTO("jessica","93229667000");
    }

}
