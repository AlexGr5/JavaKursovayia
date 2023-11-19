package ru.javaschool.documentviewer.configuration.kafka;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class    KafkaTopicConfig {

    public static final String DOCUMENTS_FOR_PROCESSING_TOPIC = "documents-for-processing";
    public static final String PROCESSED_DOCUMENTS_TOPIC = "processed-documents";

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    /**
     * Топик для отправки документов на обработку
     */
    @Bean
    public NewTopic documentsInTopic() {
        return new NewTopic(DOCUMENTS_FOR_PROCESSING_TOPIC, 1, (short) 1);
    }

    /**
     * Топик для чтения обработанных документов
     */
    @Bean
    public NewTopic documentsOutTopic() {
        return new NewTopic(PROCESSED_DOCUMENTS_TOPIC, 1, (short) 1);
    }
}
