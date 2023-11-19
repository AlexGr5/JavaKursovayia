package ru.javaschool.documentviewer.documents.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.javaschool.documentviewer.configuration.kafka.KafkaTopicConfig;
import ru.javaschool.documentviewer.documents.controller.dto.DocumentDto;

@Component
@Slf4j
public class KafkaConsumer {

    private final DocumentService documentService;

    public KafkaConsumer(DocumentService documentService) {
        this.documentService = documentService;
    }

    @KafkaListener(topics = KafkaTopicConfig.PROCESSED_DOCUMENTS_TOPIC, groupId = "group_id")
    public void consume(@Payload DocumentDto message) {
        documentService.updateAfterProcessing(message.getId(), message.getStatus());
    }
}
