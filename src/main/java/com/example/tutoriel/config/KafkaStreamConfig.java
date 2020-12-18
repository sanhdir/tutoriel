package com.example.tutoriel.config;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;


import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaStreamConfig {
    private final static String bootstrapServers = "localhost:9092";
    private final static String applicationId = "myApplicationID";



    @Bean("myStreamBuilder")
    public StreamsBuilderFactoryBean myStreamBuilderFactoryBean() {
        Map config = new HashMap();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, applicationId);
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, applicationId);
        return new StreamsBuilderFactoryBean( new KafkaStreamsConfiguration(config));
    }



}
