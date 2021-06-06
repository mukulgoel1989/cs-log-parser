package com.cs.dts.logparser.deserializer;

import com.cs.dts.logparser.model.ApplicationServerLog;
import com.cs.dts.logparser.model.BaseLogEvent;
import com.cs.dts.logparser.model.ServerLog;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomDeserializerTest {

    private final String SERVER_LOG_ENTRY_RECORD = "{\"id\":\"scsmbstgra\", \"state\":\"STARTED\", \"type\":\"APPLICATION_LOG\", \"host\":\"12345\", \"timestamp\":1491377495212}";
    private final String LOG_ENTRY_RECORD = "{\"id\":\"scsmbstgrb\", \"state\":\"STARTED\", \"timestamp\":1491377495213}";

    @Test
    public void testDesrializerForServerLogEntryBaseClass() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BaseLogEvent instance = mapper.readValue(SERVER_LOG_ENTRY_RECORD, BaseLogEvent.class);

        assertEquals(ApplicationServerLog.class, instance.getClass());
    }


    @Test
    public void testDesrializerForLogEntryClass() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BaseLogEvent instance = mapper.readValue(LOG_ENTRY_RECORD, BaseLogEvent.class);

        assertEquals(ServerLog.class, instance.getClass());
    }
}
