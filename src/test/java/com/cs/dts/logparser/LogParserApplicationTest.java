package com.cs.dts.logparser;

import com.cs.dts.logparser.entity.EventDetail;
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
class LogParserApplicationTest {

    @Mock
    private LogFileParserService logFileParserService;
    @Mock
    private EventDetailRepository eventDetailRepository;
    @Mock
    private ConfigurableApplicationContext context;
    @Captor
    private ArgumentCaptor<ArrayList<EventDetail>> eventDetailListCaptor;

    @InjectMocks
    LogParserApplication logParserApplication;

    @Test
    void testRun() {

        EventDetail detailA = EventDetail.builder().eventId("scsmbstgrmg")
                .type(EventType.APPLICATION_LOG)
                .host("12345")
                .eventDuration(5l)
                .alert(true).build();

        List<EventDetail> eventDetailList = Arrays.asList(detailA);
        Mockito.when(logFileParserService.parseLogFileForEventDetails()).thenReturn(eventDetailList);
        logParserApplication.run();
        Mockito.verify(eventDetailRepository).saveAll(eventDetailListCaptor.capture());
        Assertions.assertEquals(eventDetailList, eventDetailListCaptor.getValue());
    }

    @Test
    void testRunWithException() {

        Mockito.when(logFileParserService.parseLogFileForEventDetails()).thenThrow(new InvalidDataException("Exception"));
        logParserApplication.run();
        Mockito.verify(eventDetailRepository, Mockito.never()).saveAll(eventDetailListCaptor.capture());
        Mockito.verify(context, Mockito.times(1)).close();
    }

}
