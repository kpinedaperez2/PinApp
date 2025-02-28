package com.local.clientes.infrastructure.clients.jpa.externalservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.local.clientes.application.external.ClienteKafkaProducer;
import com.local.clientes.router.dto.ClienteDTO;
import org.springframework.kafka.core.KafkaTemplate;

public class ClienteKafkaProducerImpl implements ClienteKafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "cliente-creation-topic";
    private final ObjectMapper objectMapper;

    public ClienteKafkaProducerImpl(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void sendCreateClienteMessage(ClienteDTO mensaje) throws JsonProcessingException {
        kafkaTemplate.send(TOPIC, objectMapper.writeValueAsString(mensaje));
    }
}
