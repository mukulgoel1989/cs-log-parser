package com.cs.dts.logparser.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@JsonDeserialize(using = JsonDeserializer.None.class)
@EqualsAndHashCode
@ToString(callSuper = true)
public class ApplicationServerLog extends BaseLogEvent {
    @JsonProperty("type")
    private EventType type;
    @JsonProperty("host")
    private String host;

}
