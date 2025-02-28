package com.local.clientes.application.external;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.kafka.annotation.KafkaListener;

public interface ClienteKafkaConsumer {

    @KafkaListener(topics = "cliente-creation-topic", groupId = "grupo-consumidor")
    void consumeCreateClienteMessage(String mensaje) throws JsonProcessingException;
}
