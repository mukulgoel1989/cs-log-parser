package com.cs.dts.logparser.model;

import com.cs.dts.logparser.deserializer.CustomDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@JsonDeserialize(using = CustomDeserializer.class)
@ToString
public abstract class BaseLogEvent {
    @JsonProperty("id")
    private String id;
    @JsonProperty("state")
    private EventState state;
    @JsonProperty("timestamp")
    private long timestamp;
}
