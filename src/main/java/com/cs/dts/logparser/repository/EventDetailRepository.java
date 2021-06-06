package com.cs.dts.logparser.repository;

import com.cs.dts.logparser.entity.EventDetails;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventDetailRepository extends CrudRepository<EventDetails, String> {
    
}
