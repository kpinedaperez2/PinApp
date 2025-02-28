package com.local.clientes.application.mapper;

import com.local.clientes.application.model.ClienteModel;
import com.local.clientes.domain.Cliente;
import com.local.clientes.router.dto.ClienteDTO;
import com.local.clientes.router.dto.ClientePlusDto;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface ClienteMapper {

    ClienteDTO toClienteDTO(ClienteModel clienteModel);

    ClienteModel toClienteModel(ClienteDTO clienteDTO);

    Cliente toCliente(ClienteModel clienteModel);

    ClienteModel toClienteModel(Cliente cliente);

    default ClientePlusDto toClientePlusDto(Cliente cliente, int esperanzaDeVida) {
        ClientePlusDto dto = new ClientePlusDto();
        dto.setNombre(cliente.getNombre());
        dto.setApellido(cliente.getApellido());
        dto.setEdad(cliente.getEdad());
        dto.setFechaNacimiento(cliente.getFechaNacimiento());
        dto.setEsperanzaDeVida(esperanzaDeVida);
        return dto;
    }
}
