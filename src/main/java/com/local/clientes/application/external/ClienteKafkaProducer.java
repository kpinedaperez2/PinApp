package com.local.clientes.application.external;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.local.clientes.router.dto.ClienteDTO;

public interface ClienteKafkaProducer {

    void sendCreateClienteMessage(ClienteDTO mensaje) throws JsonProcessingException;
}
