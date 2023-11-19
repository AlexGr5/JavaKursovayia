package ru.javaschool.documentviewer.documents.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import ru.javaschool.documentviewer.configuration.kafka.KafkaTopicConfig;
import ru.javaschool.documentviewer.documents.controller.dto.DocumentDto;
import ru.javaschool.documentviewer.documents.exception.SendMessageException;

import java.util.concurrent.ExecutionException;

@Component
@Slf4j
public class KafkaSender {

    private final KafkaTemplate<String, DocumentDto> kafkaTemplate;

    public KafkaSender(KafkaTemplate<String, DocumentDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Отправляет сообщение в кафку для обработки
     * @param message сообщение
     * @throws SendMessageException не удалось отправить сообщение
     */
    public void sendMessage(DocumentDto message) {

        ListenableFuture<SendResult<String, DocumentDto>> future =
                kafkaTemplate.send(KafkaTopicConfig.DOCUMENTS_FOR_PROCESSING_TOPIC, message);
        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("failed to send message", ex);
            }

            @Override
            public void onSuccess(SendResult<String, DocumentDto> result) {
                log.info("sent message: {}", result.getProducerRecord());
            }
        });

        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new SendMessageException("failed to send message", e);
        }
    }
}
