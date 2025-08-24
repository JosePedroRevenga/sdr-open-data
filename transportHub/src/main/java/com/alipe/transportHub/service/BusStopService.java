package com.alipe.transportHub.service;

import com.alipe.transportHub.model.BusStop;
import com.alipe.transportHub.repository.BusStopRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class BusStopService {

    private static final Logger LOGGER = Logger.getLogger(BusStopService.class.getName());
    private final BusStopRepository busStopRepository;
    
    public BusStopService(BusStopRepository busStopRepository) {
        this.busStopRepository = busStopRepository;
    }

    // In BusStopService.java
    @Value("${busstop.json.url}")
    private String jsonUrl = "https://datos.santander.es/api/rest/datasets/paradas_bus.json"; // Default URL, can be overridden by application.properties

    // Fetch raw JSON from URL
    private JsonNode fetchBusStopsJson() {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream input = URI.create(jsonUrl).toURL().openStream()) {
            return mapper.readTree(input);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to fetch or parse bus stops JSON from: " + jsonUrl, e);
            return null;
        }
    }

    // Parse JsonNode to List<BusStop>
    private List<BusStop> parseBusStops(JsonNode root) {
        List<BusStop> busStops = new ArrayList<>();
        if (root == null) {
            LOGGER.warning("Input JSON root is null. Returning empty bus stop list.");
            return busStops;
        }

        JsonNode results = root.path("resources");
        if (!results.isArray()) {
            LOGGER.warning("Expected 'items' array in JSON but did not find one. Returning empty bus stop list.");
            return busStops;
        }

        for (JsonNode node : results) {
            try {
                String id = node.path("dc:identifier").asText("");
                String code = node.path("ayto:numero").asText("");
                String name = node.path("ayto:parada").asText("");
                double latitude = node.path("wgs84_pos:lat").asDouble(0.0);
                double longitude = node.path("wgs84_pos:long").asDouble(0.0);
                String address = node.path("vivo:address1").asText("");
                String zoneId = node.path("ayto:zona").asText("");
                String url = node.path("uri").asText("");

                // Validate required fields
                if (id.isEmpty() || name.isEmpty() || code.isEmpty() || latitude == 0.0 || longitude == 0.0) {
                    LOGGER.warning("BusStop entry missing required fields: id or name. Skipping entry.");
                    continue;
                }

                busStops.add(new BusStop(id, code, name, latitude, longitude, address, zoneId, url));
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Error parsing BusStop entry. Skipping entry.", e);
            }
        }
        return busStops;
    }

    // Convenience method to fetch and parse
    public List<BusStop> fetchAndParseBusStops() {
        JsonNode root = fetchBusStopsJson();
        return parseBusStops(root);
    }
    public void refreshBusStops() {
        List<BusStop> busStops = fetchAndParseBusStops();
        if (busStops.isEmpty()) {
            LOGGER.warning("No BusStop entries found to save.");
            return;
        }
        saveBusStops(busStops);
    }

    public List<BusStop> getAllBusStops() {
        return busStopRepository.findAll();
    }

    // Save parsed BusStop list to MongoDB
    private void saveBusStops(List<BusStop> busStops) {
        if (busStops == null || busStops.isEmpty()) {
            LOGGER.warning("No BusStop entries to save to MongoDB.");
            return;
        }
        try {
            busStopRepository.saveAll(busStops);
            LOGGER.info("Saved " + busStops.size() + " BusStop entries to MongoDB.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to save BusStop entries to MongoDB.", e);
        }
    }
}