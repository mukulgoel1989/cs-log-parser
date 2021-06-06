package com.cs.dts.logparser.service;

import com.cs.dts.logparser.entity.EventDetails;
import com.cs.dts.logparser.model.ApplicationServerLog;
import com.cs.dts.logparser.model.BaseLogEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class LogFileParserService {

    @Value(("${app.alert.threshold}"))
    private Long alertThreshold;

    @Value("${app.logfile.path}")
    private String logFilePath;

    @Autowired
    private FileReaderService fileReaderService;


    public List<EventDetails> parseLogFileForEventDetails() {
        Path file = Paths.get(logFilePath);
        List<BaseLogEvent> baseEntries = fileReaderService.getEventObjectsFromFile(file);
        return buildEntityFromObjects(baseEntries);
    }

    private List<EventDetails> buildEntityFromObjects(List<BaseLogEvent> baseEntries) {

        Map<String, EventDetails> eventDetailsMap = new HashMap<>();

        for (BaseLogEvent entry : baseEntries) {
            if (eventDetailsMap.containsKey(entry.getId())) {
                EventDetails eventDetail;
                if (entry.getState().equals("FINISHED")) {
                    eventDetail = updateEventDetailWithFinishedEvent(eventDetailsMap, entry);
                } else {
                    eventDetail = updateEventDetailWithStartEvent(eventDetailsMap, entry);
                }
                if (eventDetail.getEventDuration() > alertThreshold) {
                    eventDetail.setAlert(true);
                    log.warn("Alert threshold exceeded for event detail with id {}", eventDetail.getEventId());
                }
            } else {
                EventDetails eventDetail = prepareEventDetailForEntry(entry);
                eventDetailsMap.put(entry.getId(), eventDetail);
            }
        }
        return new ArrayList<>(eventDetailsMap.values());
    }

    private EventDetails prepareEventDetailForEntry(BaseLogEvent entry) {
        EventDetails.EventDetailsBuilder eventDetailBuilder = EventDetails.builder()
                .eventId(entry.getId())
                .eventDuration(entry.getTimestamp())
                .alert(false);
        if (entry instanceof ApplicationServerLog) {
            ApplicationServerLog applicationServerLog = (ApplicationServerLog) entry;
            eventDetailBuilder.host(applicationServerLog.getHost());
            eventDetailBuilder.type(applicationServerLog.getType());
        }
        return eventDetailBuilder.build();
    }

    private EventDetails updateEventDetailWithStartEvent(Map<String, EventDetails> eventDetailsMap, BaseLogEvent entry) {
        EventDetails eventDetail;
        eventDetail = eventDetailsMap.get(entry.getId());
        eventDetail.setEventDuration(eventDetail.getEventDuration() - entry.getTimestamp());
        return eventDetail;
    }

    private EventDetails updateEventDetailWithFinishedEvent(Map<String, EventDetails> eventDetailsMap, BaseLogEvent entry) {
        EventDetails eventDetail;
        eventDetail = eventDetailsMap.get(entry.getId());
        eventDetail.setEventDuration(entry.getTimestamp() - eventDetail.getEventDuration());
        return eventDetail;
    }

}
