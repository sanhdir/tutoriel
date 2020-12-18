package com.example.tutoriel.processor;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaStreamProcessor {

    private static final String inputTopic = "kstreamTutorielInputTopic";

    @Bean
    public KStream processor(@Qualifier("myStreamBuilder") StreamsBuilder kSBuilder) throws Exception {
        final KStream<String,String> stream = kSBuilder.stream(inputTopic);
        stream.foreach((k,v) -> System.out.println("message: "+ v));
        return stream;
    }
}
