package com.local.clientes.application.usecase;

import com.local.clientes.application.model.ClienteModel;
import com.local.clientes.application.model.Metrica;
import com.local.clientes.router.dto.ClientePlusDto;

import java.util.List;

public interface ClienteService {

    void registrarCliente(ClienteModel cliente);

    ClienteModel obtenerClientePorId(Long id);

    void borrarCliente(Long id);

    Metrica calcularMetricas();

    List<ClientePlusDto> obtenerClientesConMetrica();
}
