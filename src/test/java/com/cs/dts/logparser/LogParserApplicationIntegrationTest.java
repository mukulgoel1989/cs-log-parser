package com.cs.dts.logparser;

import com.cs.dts.logparser.entity.EventDetail;
import com.cs.dts.logparser.model.EventType;
import com.cs.dts.logparser.repository.EventDetailRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
class LogParserApplicationIntegrationTest {
    @Autowired
    private EventDetailRepository eventDetailRepository;

    @Test
    void testSuccessRunValidateInMemoryDatabase() throws Exception {

        List<EventDetail> eventDetailList = (List<EventDetail>) eventDetailRepository.findAll();

        EventDetail detailA = EventDetail.builder().eventId("scsmbstgrmg")
                .type(EventType.APPLICATION_LOG)
                .host("12345")
                .eventDuration(5l)
                .alert(true).build();

        EventDetail detailB = EventDetail.builder().eventId("scsmbstgrb")
                .eventDuration(3l)
                .alert(false).build();

        EventDetail detailC = EventDetail.builder().eventId("scsmbstgrc")
                .eventDuration(8l)
                .alert(true).build();

        Assertions.assertThat(eventDetailList).contains(detailA, detailB, detailC);

    }

}
