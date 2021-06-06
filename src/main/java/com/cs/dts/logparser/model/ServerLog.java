package com.cs.dts.logparser.model;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.EqualsAndHashCode;

@JsonDeserialize(using = JsonDeserializer.None.class)
@EqualsAndHashCode
public class ServerLog extends BaseLogEvent {

}
