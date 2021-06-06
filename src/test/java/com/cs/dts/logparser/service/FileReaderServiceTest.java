package com.cs.dts.logparser.service;

import com.cs.dts.logparser.exception.InvalidDataException;
import com.cs.dts.logparser.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class FileReaderServiceTest {

    private final List<String> TEST_DATA_VALID = Arrays.asList("{\"id\":\"scsmbstgra\", \"state\":\"STARTED\", \"type\":\"APPLICATION_LOG\", \"host\":\"12345\", \"timestamp\":1491377495212}\n" +
            "{\"id\":\"scsmbstgrb\", \"state\":\"STARTED\", \"timestamp\":1491377495213}\n" +
            "{\"id\":\"scsmbstgrc\", \"state\":\"FINISHED\", \"timestamp\":1491377495218}\n" +
            "{\"id\":\"scsmbstgra\", \"state\":\"FINISHED\", \"type\":\"APPLICATION_LOG\", \"host\":\"12345\", \"timestamp\":1491377495217}\n" +
            "{\"id\":\"scsmbstgrc\", \"state\":\"STARTED\", \"timestamp\":1491377495210}\n" +
            "{\"id\":\"scsmbstgrb\", \"state\":\"FINISHED\", \"timestamp\":1491377495216}");


    private final List<String> TEST_DATA_INVALID = Arrays.asList("{\"id\":\"scsmbstgra\", \"state\":\"STARTED\", \"type\":\"APPLICATION_LOG\", \"host\":\"12345\", \"timestamp\":1491377495212}\n" +
            "{\"id\":\"scsmbstgrb\", \"state\":\"STARTED\", \"timestamp\":1491377495213}\n" +
            "{\"id\":\"scsmbstgrc\", \"state\":\"FINISHED\", \"timestamp\":1491377495218}\n" +
            "{\"id\":\"scsmbstgra\", \"state\":\"FINISHED\", \"type\":\"APPLICATION_LOG\", \"host\":\"12345\", \"timestamp\":1491377495217}\n" +
            "{\"id\":\"scsmbstgrc\", \"state\":\"STARTED\", \"timestamp\":1491377495210}\n" +
            "{\"id\":\"scsmbstgrb\", \"state\":\"FINISHED\", \"timestamp\":1491377495216");

    @Spy
    ObjectMapper mapper;
    @Mock
    Path mockedFile;
    @InjectMocks
    FileReaderService fileReaderService;
    @TempDir
    File testLogFileDir;


    @Test
    public void testGetEventObjectsFromFileSuccess() throws IOException {

        File tempLogFile = new File(testLogFileDir, "logfile.txt");

        Files.write(tempLogFile.toPath(), TEST_DATA_VALID);

        List<BaseLogEvent> baseLogEvents = new ArrayList<>();

        ApplicationServerLog applicationServerLog = new ApplicationServerLog();
        applicationServerLog.setId("scsmbstgra");
        applicationServerLog.setType(EventType.APPLICATION_LOG);
        applicationServerLog.setTimestamp(1491377495212l);
        applicationServerLog.setState(EventState.STARTED);
        applicationServerLog.setHost("12345");

        baseLogEvents.add(applicationServerLog);

        applicationServerLog = new ApplicationServerLog();
        applicationServerLog.setId("scsmbstgra");
        applicationServerLog.setType(EventType.APPLICATION_LOG);
        applicationServerLog.setTimestamp(1491377495217L);
        applicationServerLog.setState(EventState.FINISHED);
        applicationServerLog.setHost("12345");

        baseLogEvents.add(applicationServerLog);

        ServerLog serverLog = new ServerLog();
        serverLog.setId("scsmbstgrb");
        serverLog.setTimestamp(1491377495213l);
        serverLog.setState(EventState.STARTED);

        baseLogEvents.add(serverLog);

        serverLog = new ServerLog();
        serverLog.setId("scsmbstgrb");
        serverLog.setTimestamp(1491377495216L);
        serverLog.setState(EventState.FINISHED);

        baseLogEvents.add(serverLog);

        serverLog = new ServerLog();
        serverLog.setId("scsmbstgrc");
        serverLog.setTimestamp(1491377495218L);
        serverLog.setState(EventState.FINISHED);

        baseLogEvents.add(serverLog);

        serverLog = new ServerLog();
        serverLog.setId("scsmbstgrc");
        serverLog.setTimestamp(1491377495210L);
        serverLog.setState(EventState.STARTED);

        baseLogEvents.add(serverLog);

        List<BaseLogEvent> baseLogEventsActual = fileReaderService.getEventObjectsFromFile(tempLogFile.toPath());
        for (BaseLogEvent event : baseLogEvents) {
            Assertions.assertThat(baseLogEventsActual).contains(event);
        }


    }

    @Test
    public void testGetEventObjectsFromFileWithReadFalse() throws IOException {
        File tempLogFile = new File(testLogFileDir, "logfile.txt");
        Files.write(tempLogFile.toPath(), TEST_DATA_VALID);
        tempLogFile.setReadable(false);

        InvalidDataException thrown = org.junit.jupiter.api.Assertions.assertThrows(InvalidDataException.class, () -> {
            fileReaderService.getEventObjectsFromFile(tempLogFile.toPath());
        });
        org.junit.jupiter.api.Assertions.assertTrue(thrown.getMessage().contains("Exception when trying to read from file at path"));
    }

    @Test
    public void testGetEventObjectsFromFileWithInvalidJsonData() throws IOException {
        File tempLogFile = new File(testLogFileDir, "logfile-invalid.txt");
        Files.write(tempLogFile.toPath(), TEST_DATA_INVALID);
        InvalidDataException thrown = org.junit.jupiter.api.Assertions.assertThrows(InvalidDataException.class, () -> {
            fileReaderService.getEventObjectsFromFile(tempLogFile.toPath());
        });
        org.junit.jupiter.api.Assertions.assertTrue(thrown.getMessage().contains("Invalid data format found in log file in line"));
    }
}
