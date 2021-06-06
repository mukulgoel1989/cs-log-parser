package com.cs.dts.logparser.entity;

import com.cs.dts.logparser.model.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "event_details")
public class EventDetails {

    @Id
    private String eventId;
    private Long eventDuration;
    private EventType type;
    private String host;
    private Boolean alert;
}
