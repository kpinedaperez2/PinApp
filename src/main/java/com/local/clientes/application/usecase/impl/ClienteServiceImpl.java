package com.local.clientes.application.usecase.impl;

import com.local.clientes.application.mapper.ClienteMapper;
import com.local.clientes.application.model.ClienteModel;
import com.local.clientes.application.model.Metrica;
import com.local.clientes.application.usecase.ClienteService;
import com.local.clientes.domain.Cliente;
import com.local.clientes.infrastructure.clients.jpa.ClienteJpaRepository;
import com.local.clientes.router.dto.ClientePlusDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ClienteServiceImpl implements ClienteService {

    private final ClienteJpaRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    public ClienteServiceImpl(ClienteJpaRepository clienteRepository, ClienteMapper clienteMapper) {
        this.clienteRepository = clienteRepository;
        this.clienteMapper = clienteMapper;
    }

    @Override
    public void registrarCliente(ClienteModel clienteModel) {
        log.info("Creación de cliente: {}", clienteModel.getNombre());
        clienteRepository.save(clienteMapper.toCliente(clienteModel));
    }

    @Override
    public ClienteModel obtenerClientePorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        return clienteMapper.toClienteModel(cliente);
    }

    @Override
    public void borrarCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        clienteRepository.delete(cliente);
    }

    @Override
    public Metrica calcularMetricas() {
        List<Cliente> clientes = clienteRepository.findAll();
        double promedioEdad = clientes.stream().mapToInt(Cliente::getEdad).average().orElse(0);
        double desviacionEstandar = Math.sqrt(clientes.stream()
                .mapToDouble(cliente -> Math.pow(cliente.getEdad() - promedioEdad, 2))
                .average().orElse(0));

        return new Metrica(promedioEdad, desviacionEstandar);
    }

    private int calcularEsperanzaDeVida(int edad) {
        int esperanzaDeVidaPromedio = 80;
        return esperanzaDeVidaPromedio - edad;
    }


    @Override
    public List<ClientePlusDto> obtenerClientesConMetrica() {
        return clienteRepository.findAll().stream()
                .map(cliente -> {
                    int esperanzaDeVida = calcularEsperanzaDeVida(cliente.getEdad());
                    return clienteMapper.toClientePlusDto(cliente, esperanzaDeVida);
                })
                .collect(Collectors.toList());
    }

}
