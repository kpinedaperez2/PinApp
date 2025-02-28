package com.local.clientes;

import com.local.clientes.application.mapper.ClienteMapper;
import com.local.clientes.application.model.ClienteModel;
import com.local.clientes.application.model.Metrica;
import com.local.clientes.application.usecase.impl.ClienteServiceImpl;
import com.local.clientes.domain.Cliente;
import com.local.clientes.infrastructure.clients.jpa.ClienteJpaRepository;
import com.local.clientes.router.dto.ClientePlusDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceImplTest {

    @Mock
    private ClienteJpaRepository clienteRepository;

    @Mock
    private ClienteMapper clienteMapper;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    private ClienteModel clienteModel;
    private Cliente cliente;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        clienteModel = new ClienteModel();
        clienteModel.setNombre("Juan");
        clienteModel.setApellido("Flores");
        clienteModel.setFechaNacimiento(LocalDate.now());
        clienteModel.setEdad(30);

        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Juan");
        cliente.setApellido("Flores");
        cliente.setFechaNacimiento(LocalDate.now());
        cliente.setEdad(30);
    }

    @Test
    public void testRegistrarCliente() {
        when(clienteMapper.toCliente(clienteModel)).thenReturn(cliente);
        when(clienteRepository.save(cliente)).thenReturn(cliente);

        clienteService.registrarCliente(clienteModel);

        verify(clienteRepository, times(1)).save(cliente);
    }

    @Test
    public void testObtenerClientePorId_ClienteExistente() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteMapper.toClienteModel(cliente)).thenReturn(clienteModel);

        ClienteModel result = clienteService.obtenerClientePorId(1L);

        assertEquals(clienteModel.getNombre(), result.getNombre());
        assertEquals(clienteModel.getEdad(), result.getEdad());
    }

    @Test
    public void testObtenerClientePorId_ClienteNoExistente() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            clienteService.obtenerClientePorId(1L);
        });

        assertEquals("Cliente no encontrado", exception.getMessage());
    }

    @Test
    public void testBorrarCliente_ClienteExistente() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        clienteService.borrarCliente(1L);

        verify(clienteRepository, times(1)).delete(cliente);
    }

    @Test
    public void testBorrarCliente_ClienteNoExistente() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            clienteService.borrarCliente(1L);
        });

        assertEquals("Cliente no encontrado", exception.getMessage());
    }

    @Test
    public void testCalcularMetricas() {
        Cliente cliente1 = new Cliente();
        cliente1.setEdad(30);

        Cliente cliente2 = new Cliente();
        cliente2.setEdad(40);

        List<Cliente> clientes = List.of(cliente1, cliente2);

        when(clienteRepository.findAll()).thenReturn(clientes);

        Metrica metrica = clienteService.calcularMetricas();

        assertEquals(35.0, metrica.getPromedioEdad());
        assertEquals(7.0710678118654755, metrica.getDesviacionEstandar(), 0.0001);
    }

    @Test
    public void testObtenerClientesConMetrica() {
        Cliente cliente1 = new Cliente();
        cliente1.setEdad(30);
        cliente1.setId(1L);

        Cliente cliente2 = new Cliente();
        cliente2.setEdad(40);
        cliente2.setId(2L);

        List<Cliente> clientes = List.of(cliente1, cliente2);
        List<ClientePlusDto> expected = clientes.stream()
                .map(c -> new ClientePlusDto( c.getNombre(), c.getApellido(), c.getEdad(), c.getFechaNacimiento(), 80 - c.getEdad()))
                .collect(Collectors.toList());

        when(clienteRepository.findAll()).thenReturn(clientes);
        when(clienteMapper.toClientePlusDto(cliente1, 50)).thenReturn(expected.get(0));
        when(clienteMapper.toClientePlusDto(cliente2, 40)).thenReturn(expected.get(1));

        List<ClientePlusDto> result = clienteService.obtenerClientesConMetrica();

        assertEquals(2, result.size());
        assertEquals(expected, result);
    }
}

