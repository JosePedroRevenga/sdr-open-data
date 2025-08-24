package com.alipe.transportHub.repository;

import com.alipe.transportHub.model.BusStop;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusStopRepository extends MongoRepository<BusStop, String> {
    
     // Find by name
    List<BusStop> findByName(String name);

    // Find by colour
    List<BusStop> findByCode(String code);

    // Find by route
    List<BusStop> findByLatitudeAndLongitude(double latitude, double longitude);

    // Find by schedule
    List<BusStop> findByZone(String zoneId);
}