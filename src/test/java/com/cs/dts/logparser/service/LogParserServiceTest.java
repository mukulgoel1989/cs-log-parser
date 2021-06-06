package com.cs.dts.logparser.service;

import com.cs.dts.logparser.entity.EventDetails;
import com.cs.dts.logparser.exception.InvalidDataException;
import com.cs.dts.logparser.model.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class LogParserServiceTest {

    private final String logFilePath = "src/test/resources/logfile.txt";

    @Mock
    FileReaderService fileReaderService;
    @InjectMocks
    LogFileParserService logFileParserService;


    @BeforeEach
    public void setup() {

        ReflectionTestUtils.setField(logFileParserService, "alertThreshold", 3L);
        ReflectionTestUtils.setField(logFileParserService, "logFilePath", logFilePath);
    }

    @Test
    public void testLogParserServiceSuccessScenario() throws IOException {

        List<BaseLogEvent> baseLogEvents = new ArrayList<>();

        ServerLog serverLog = new ServerLog();
        serverLog.setId("id1");
        serverLog.setTimestamp(1234);
        serverLog.setState(EventState.STARTED);

        baseLogEvents.add(serverLog);

        serverLog = new ServerLog();
        serverLog.setId("id1");
        serverLog.setTimestamp(1237);
        serverLog.setState(EventState.FINISHED);

        baseLogEvents.add(serverLog);

        ApplicationServerLog applicationServerLog = new ApplicationServerLog();
        applicationServerLog.setId("id2");
        applicationServerLog.setTimestamp(1239);
        applicationServerLog.setState(EventState.FINISHED);
        applicationServerLog.setHost("host");
        applicationServerLog.setType(EventType.APPLICATION_LOG);

        baseLogEvents.add(applicationServerLog);

        applicationServerLog = new ApplicationServerLog();
        applicationServerLog.setId("id2");
        applicationServerLog.setTimestamp(1230);
        applicationServerLog.setState(EventState.STARTED);
        applicationServerLog.setHost("host");
        applicationServerLog.setType(EventType.APPLICATION_LOG);
        baseLogEvents.add(applicationServerLog);

        Mockito.when(fileReaderService.getEventObjectsFromFile(Paths.get(logFilePath))).thenReturn(baseLogEvents);


        EventDetails detailA = EventDetails.builder().eventId("id1")
                .eventDuration(3l)
                .alert(false).build();


        EventDetails detailB = EventDetails.builder().eventId("id2")
                .type(EventType.APPLICATION_LOG)
                .host("host")
                .eventDuration(9l)
                .alert(true).build();

        List<EventDetails> eventDetailsList = logFileParserService.parseLogFileForEventDetails();

        Assertions.assertThat(eventDetailsList).contains(detailA, detailB);
    }

    @Test
    public void testLogParserServiceFileNotFound() {
        Path actualPath = Paths.get(logFilePath);
        Mockito.when(fileReaderService.getEventObjectsFromFile(actualPath)).thenThrow(new InvalidDataException("Exception when trying to read from file at path"));
        org.junit.jupiter.api.Assertions.assertThrows(InvalidDataException.class, () -> {
            logFileParserService.parseLogFileForEventDetails();
        });


    }
}
