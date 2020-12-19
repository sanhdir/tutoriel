package com.example.tutoriel.processor;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.regex.Pattern;

@Component
@Slf4j
public class KafkaStreamProcessor {

    private static final String inputTopic = "kstreamTutorielInputTopic";
    private static final String outputTopic = "kstreamTutorielOutputTopic";

    @Bean
    public KStream processor(@Qualifier("myStreamBuilder") StreamsBuilder kSBuilder) throws Exception {
        final KStream<String,String> stream = kSBuilder.stream(inputTopic);
        Pattern pattern = Pattern.compile("\\W+", Pattern.UNICODE_CHARACTER_CLASS);
        KTable<String, Long> carBrandCounter = stream
                .flatMapValues(carBrand -> Arrays.asList(pattern.split(carBrand)))
                .groupBy((key, carBrand) -> carBrand)
                .count();

        carBrandCounter.toStream()
                .foreach((carBrand,count) -> System.out.println("brand: " +carBrand + " -> " + count));

        carBrandCounter.toStream().to(outputTopic,Produced.with(Serdes.String(), Serdes.Long()));


        return stream;
    }
}
