package com.cs.dts.logparser;

import com.cs.dts.logparser.entity.EventDetails;
import com.cs.dts.logparser.exception.InvalidDataException;
import com.cs.dts.logparser.model.EventType;
import com.cs.dts.logparser.repository.EventDetailRepository;
import com.cs.dts.logparser.service.LogFileParserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class LogParserApplicationTest {

    @Mock
    private LogFileParserService logFileParserService;
    @Mock
    private EventDetailRepository eventDetailRepository;
    @Mock
    private ConfigurableApplicationContext context;
    @Captor
    private ArgumentCaptor<ArrayList<EventDetails>> eventDetailListCaptor;

    @InjectMocks
    LogParserApplication logParserApplication;

    @Test
    public void testRun() {

        EventDetails detailA = EventDetails.builder().eventId("scsmbstgrmg")
                .type(EventType.APPLICATION_LOG)
                .host("12345")
                .eventDuration(5l)
                .alert(true).build();

        List<EventDetails> eventDetailsList = Arrays.asList(detailA);
        Mockito.when(logFileParserService.parseLogFileForEventDetails()).thenReturn(eventDetailsList);
        logParserApplication.run();
        Mockito.verify(eventDetailRepository).saveAll(eventDetailListCaptor.capture());
        Assertions.assertEquals(eventDetailsList, eventDetailListCaptor.getValue());
    }

    @Test
    public void testRunWithException() {

        Mockito.when(logFileParserService.parseLogFileForEventDetails()).thenThrow(new InvalidDataException("Exception"));
        logParserApplication.run();
        Mockito.verify(eventDetailRepository, Mockito.never()).saveAll(eventDetailListCaptor.capture());
        Mockito.verify(context, Mockito.times(1)).close();
    }

}
