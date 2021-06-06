package com.cs.dts.logparser;

import com.cs.dts.logparser.entity.EventDetails;
import com.cs.dts.logparser.exception.InvalidDataException;
import com.cs.dts.logparser.repository.EventDetailRepository;
import com.cs.dts.logparser.service.LogFileParserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;

@Slf4j
@SpringBootApplication
public class LogParserApplication implements CommandLineRunner {

    @Autowired
    private LogFileParserService logFileParserService;
    @Autowired
    private EventDetailRepository eventDetailRepository;
    @Autowired
    private ConfigurableApplicationContext context;

    public static void main(String[] args) {
        SpringApplication.run(LogParserApplication.class, args).close();
    }

    @Override
    public void run(String... args) {
        try {
            List<EventDetails> eventDetailsList = logFileParserService.parseLogFileForEventDetails();
            log.debug("Event detail objects being persisted {}", Arrays.toString(eventDetailsList.toArray()));
            eventDetailRepository.saveAll(eventDetailsList);
        } catch (InvalidDataException e) {
            log.error("Exception occurred during program execution. Program will exit now", e);
            context.close();
            System.exit(-1);
        }
    }

    @Bean
    public ObjectMapper createObjectMapper() {
        return new ObjectMapper();
    }

}
