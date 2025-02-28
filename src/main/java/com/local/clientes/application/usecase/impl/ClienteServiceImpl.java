package com.local.clientes.application.usecase.impl;

import com.local.clientes.application.exception.ClienteNoEncontradoException;
import com.local.clientes.application.exception.ClienteServiceException;
import com.local.clientes.application.mapper.ClienteMapper;
import com.local.clientes.application.model.ClienteModel;
import com.local.clientes.application.model.Metrica;
import com.local.clientes.application.usecase.ClienteService;
import com.local.clientes.domain.Cliente;
import com.local.clientes.infrastructure.clients.jpa.ClienteJpaRepository;
import com.local.clientes.router.dto.ClientePlusDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Async;
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

    @Async
    @Override
    public void registrarCliente(ClienteModel clienteModel) {
        try {
            log.info("Creacion de cliente: {}", clienteModel.getNombre());
            clienteRepository.save(clienteMapper.toCliente(clienteModel));
        } catch (Exception e) {
            log.error("Error inesperado al crear el cliente: {}", clienteModel.getNombre(), e);
            throw new ClienteServiceException("No se pudo registrar el cliente debido a un error", e);
        }
    }

    @Override
    public ClienteModel obtenerClientePorId(Long id) {
        try {
            Cliente cliente = clienteRepository.findById(id)
                    .orElseThrow(() -> new ClienteNoEncontradoException("Cliente no encontrado con id: " + id));
            return clienteMapper.toClienteModel(cliente);
        } catch (ClienteNoEncontradoException e) {
            log.error("Cliente no encontrado con el id: {}", id, e);
            throw e;
        } catch (DataAccessException e) {
            log.error("Ocurrio un error al acceder a la bdd con id: {}", id, e);
            throw new ClienteServiceException("No se pudo obtener el cliente debido a un error de acceso a datos", e);
        } catch (Exception e) {
            log.error("Error inesperado al obtener el cliente con id: {}", id, e);
            throw new ClienteServiceException("No se pudo obtener el cliente debido a un error desconocido", e);
        }
    }

    @Override
    public void borrarCliente(Long id) {
        try {
            log.info("Eliminacion de cliente: {}", id);
            Cliente cliente = clienteRepository.findById(id)
                    .orElseThrow(() -> new ClienteNoEncontradoException("Cliente no encontrado con id: " + id));
            clienteRepository.delete(cliente);
        } catch (ClienteNoEncontradoException e) {
            log.error("Cliente no encontrado con id: {}", id, e);
            throw e;
        }
    }

    @Override
    public Metrica calcularMetricas() {
        try {
            List<Cliente> clientes = clienteRepository.findAll();

            double promedioEdad = clientes.stream().mapToInt(Cliente::getEdad).average().orElse(0);
            double desviacionEstandar = Math.sqrt(clientes.stream()
                    .mapToDouble(cliente -> Math.pow(cliente.getEdad() - promedioEdad, 2))
                    .average().orElse(0));

            return new Metrica(promedioEdad, desviacionEstandar);
        } catch (DataAccessException e) {
            log.error("Error de acceso a datos al calcular metricas", e);
            throw new ClienteServiceException("Ocurrio un error al acceder a la bdd", e);
        } catch (Exception e) {
            log.error("Error inesperado al calcular metricas", e);
            throw new ClienteServiceException("No se pudieron calcular las metricas debido a un error desconocido", e);
        }
    }

    private int calcularEsperanzaDeVida(int edad) {
        int esperanzaDeVidaPromedio = 80;
        return esperanzaDeVidaPromedio - edad;
    }

    @Override
    public List<ClientePlusDto> obtenerClientesConMetrica() {
        try {
            log.info("Consulta general de clientes:");
            return clienteRepository.findAll().stream()
                    .map(cliente -> {
                        int esperanzaDeVida = calcularEsperanzaDeVida(cliente.getEdad());
                        return clienteMapper.toClientePlusDto(cliente, esperanzaDeVida);
                    })
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            log.error("Error de acceso a datos al obtener clientes", e);
            throw new ClienteServiceException("No se pudo obtener la lista de clientes con metricas debido a un error de acceso a bdd", e);
        } catch (Exception e) {
            log.error("Error inesperado al obtener clientes con métrica", e);
            throw new ClienteServiceException("No se pudo obtener la lista de clientes con metricas debido a un error desconocido", e);
        }
    }
}
