package com.local.clientes.infrastructure.clients.jpa.externalservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.local.clientes.application.external.ClienteKafkaConsumer;
import com.local.clientes.application.model.ClienteModel;
import com.local.clientes.application.usecase.ClienteService;
import com.local.clientes.infrastructure.util.ObjectSerializer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ClienteKafkaConsumerImpl implements ClienteKafkaConsumer {

    private final ClienteService clienteService;
    private final ObjectSerializer objectSerializer;

    public ClienteKafkaConsumerImpl(ClienteService clienteService, ObjectSerializer objectSerializer) {
        this.clienteService = clienteService;
        this.objectSerializer = objectSerializer;
    }

    @KafkaListener(topics = "cliente-creation-topic", groupId = "grupo-consumidor")
    @Override
    public void consumeCreateClienteMessage(String mensaje) throws JsonProcessingException {
        ClienteModel clienteModel = objectSerializer.deserialize(mensaje, ClienteModel.class);
        System.out.println("Mensaje recibido: " + mensaje);
        clienteService.registrarCliente(clienteModel);
    }
}
