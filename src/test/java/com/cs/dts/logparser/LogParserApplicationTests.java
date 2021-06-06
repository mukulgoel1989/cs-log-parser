package com.cs.dts.logparser;

import com.cs.dts.logparser.entity.EventDetails;
import com.cs.dts.logparser.repository.EventDetailRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
class LogParserApplicationTests {

    @Autowired
    private EventDetailRepository eventDetailRepository;

    @Test
    void contextLoads() throws Exception {

        List<EventDetails> eventDetailsList = (List<EventDetails>) eventDetailRepository.findAll();
        System.out.println(eventDetailsList);

    }

}
