package com.example.tutoriel.integration;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@EmbeddedKafka
@TestConfiguration("KafkaStreamConfig.class")
public class KafkaIntegrationTest {

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;
    BlockingQueue<ConsumerRecord<String,String>> records;
    KafkaMessageListenerContainer container;
    private static final String TOPIC="someTopicName";

    @BeforeEach
    public void setUp(){
        Map<String, Object> kafkaProperties = new HashMap(KafkaTestUtils.consumerProps("groupId", "false", embeddedKafkaBroker));
        DefaultKafkaConsumerFactory consumerFactory = new DefaultKafkaConsumerFactory<>(kafkaProperties, new StringDeserializer(), new StringDeserializer());
        ContainerProperties containerProperties = new ContainerProperties(TOPIC);
        container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
        records = new LinkedBlockingQueue<>();
        container.setupMessageListener((MessageListener<String,String>) records::add);
        container.start();
        ContainerTestUtils.waitForAssignment(container, embeddedKafkaBroker.getPartitionsPerTopic());
    }

    @AfterEach
    public void tearDown(){
      if(container.isRunning())
          container.stop();
    }
    @Test
    public void sendMessageToTopicAndReceiveIt() throws InterruptedException {

        Map<String, Object> configs = new HashMap<>(KafkaTestUtils.producerProps(embeddedKafkaBroker));
        Producer<String, String> producer = new DefaultKafkaProducerFactory<>(configs, new StringSerializer(), new StringSerializer()).createProducer();
        producer.send(new ProducerRecord<>(TOPIC, "key", "someValue"));

        // Assert
        ConsumerRecord<String, String> record = records.poll(100, TimeUnit.MILLISECONDS);
        Assertions.assertEquals("key",record.key());
        Assertions.assertEquals("someValue",record.value());
    }

}
