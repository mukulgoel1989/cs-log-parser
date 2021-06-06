package com.cs.dts.logparser.service;

import com.cs.dts.logparser.exception.InvalidDataException;
import com.cs.dts.logparser.model.BaseLogEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
@Slf4j
public class FileReaderService {

    @Autowired
    private ObjectMapper mapper;

    public List<BaseLogEvent> getEventObjectsFromFile(Path file) {
        List<BaseLogEvent> logEventEntries = new ArrayList<>();
        try (Stream<String> lines = Files.lines(file, StandardCharsets.UTF_8)) {
            for (String line : (Iterable<String>) lines::iterator) {
                logEventEntries.add(parseLineToObjectAndAddToList(line));
            }
        } catch (IOException e) {
            throw new InvalidDataException(String.format("Exception when trying to read from file at path %s", file.toAbsolutePath()), e);
        }
        return logEventEntries;
    }

    private BaseLogEvent parseLineToObjectAndAddToList(String line) {
        BaseLogEvent event = null;
        try {
            event = mapper.readValue(line, BaseLogEvent.class);
        } catch (JsonProcessingException e) {
            throw new InvalidDataException(String.format("Invalid data format found in log file in line %s", line), e);
        }
        return event;
    }


}
