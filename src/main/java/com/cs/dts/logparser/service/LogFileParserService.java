package com.cs.dts.logparser.service;

import com.cs.dts.logparser.entity.EventDetail;
import com.cs.dts.logparser.model.ApplicationServerLog;
import com.cs.dts.logparser.model.BaseLogEvent;
import com.cs.dts.logparser.model.EventState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
@Service
public class LogFileParserService {

    private Long alertThreshold;
    private String logFilePath;
    private FileReaderService fileReaderService;

    public LogFileParserService(@Value(("${app.alert.threshold.milliseconds}")) Long alertThreshold, @Value("${app.logfile.path}") String logFilePath, FileReaderService fileReaderService) {
        this.alertThreshold = Objects.requireNonNull(alertThreshold, "value cannot be null");
        this.logFilePath = Objects.requireNonNull(logFilePath, "logfile path cannot be null");
        this.fileReaderService = Objects.requireNonNull(fileReaderService, "object cannot be null");
    }

    public List<EventDetail> parseLogFileForEventDetails() {
        log.info("Log file parser application is configured to alert for events that took more than {}ms and is started with log file path = {}", alertThreshold, logFilePath);
        Path file = Paths.get(logFilePath);
        List<BaseLogEvent> baseEntries = fileReaderService.getEventObjectsFromFile(file);
        log.debug("Raw Log events from file {}", Arrays.toString(baseEntries.toArray()));
        return buildEventDetailsFromLogEntries(baseEntries);
    }

    private List<EventDetail> buildEventDetailsFromLogEntries(List<BaseLogEvent> baseEntries) {
        Map<String, EventDetail> eventDetailsMap = new HashMap<>();
        for (BaseLogEvent entry : baseEntries) {
            processLogEntryForEventDetail(eventDetailsMap, entry);
        }
        return new ArrayList<>(eventDetailsMap.values());
    }

    private void processLogEntryForEventDetail(Map<String, EventDetail> eventDetailsMap, BaseLogEvent entry) {
        if (eventDetailsMap.containsKey(entry.getId())) {
            EventDetail eventDetail;
            if (entry.getState().equals(EventState.FINISHED)) {
                eventDetail = updateEventDetailWithFinishedEvent(eventDetailsMap, entry);
            } else {
                eventDetail = updateEventDetailWithStartEvent(eventDetailsMap, entry);
            }
            if (eventDetail.getEventDuration() > alertThreshold) {
                eventDetail.setAlert(true);
                log.warn("Alert threshold of {}ms exceeded for event detail with id {}", alertThreshold, eventDetail.getEventId());
            }
        } else {
            EventDetail eventDetail = prepareEventDetailForEntry(entry);
            eventDetailsMap.put(entry.getId(), eventDetail);
        }
    }

    private EventDetail prepareEventDetailForEntry(BaseLogEvent entry) {
        EventDetail.EventDetailBuilder eventDetailBuilder = EventDetail.builder()
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

    private EventDetail updateEventDetailWithStartEvent(Map<String, EventDetail> eventDetailsMap, BaseLogEvent entry) {
        EventDetail eventDetail;
        eventDetail = eventDetailsMap.get(entry.getId());
        eventDetail.setEventDuration(eventDetail.getEventDuration() - entry.getTimestamp());
        return eventDetail;
    }

    private EventDetail updateEventDetailWithFinishedEvent(Map<String, EventDetail> eventDetailsMap, BaseLogEvent entry) {
        EventDetail eventDetail;
        eventDetail = eventDetailsMap.get(entry.getId());
        eventDetail.setEventDuration(entry.getTimestamp() - eventDetail.getEventDuration());
        return eventDetail;
    }

}
