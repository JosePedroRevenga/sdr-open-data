package com.alipe.transportHub.repository;

import com.alipe.transportHub.model.BusLine;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusLineRepository extends MongoRepository<BusLine, String> {
    
     // Find by name
    List<BusLine> findByName(String name);

    // Find by colour
    List<BusLine> findByColour(String colour);

    // Find by route
    List<BusLine> findByRoute(String route);

    // Find by schedule
    List<BusLine> findBySchedule(String schedule);
}