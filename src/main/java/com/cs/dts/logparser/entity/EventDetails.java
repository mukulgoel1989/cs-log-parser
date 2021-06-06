package com.cs.dts.logparser.entity;

import com.cs.dts.logparser.model.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
    @Enumerated(EnumType.STRING)
    private EventType type;
    private String host;
    private Boolean alert;
}
