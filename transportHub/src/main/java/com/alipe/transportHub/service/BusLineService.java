package com.alipe.transportHub.service;

import com.alipe.transportHub.model.BusLine;
import com.alipe.transportHub.repository.BusLineRepository;
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
public class BusLineService {

    private static final Logger LOGGER = Logger.getLogger(BusLineService.class.getName());
    private final BusLineRepository busLineRepository;
    
    public BusLineService(BusLineRepository busLineRepository) {
        this.busLineRepository = busLineRepository;
    }

    // In BusLineService.java
    @Value("${busline.json.url}")
    private String jsonUrl = "http://datos.santander.es/api/datos/lineas_bus.json"; // Default URL, can be overridden by application.properties

    // Fetch raw JSON from URL
    private JsonNode fetchBusLinesJson() {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream input = URI.create(jsonUrl).toURL().openStream()) {
            return mapper.readTree(input);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to fetch or parse bus lines JSON from: " + jsonUrl, e);
            return null;
        }
    }

    // Parse JsonNode to List<BusLine>
    private List<BusLine> parseBusLines(JsonNode root) {
        List<BusLine> busLines = new ArrayList<>();
        if (root == null) {
            LOGGER.warning("Input JSON root is null. Returning empty bus line list.");
            return busLines;
        }

        JsonNode results = root.path("resources");
        if (!results.isArray()) {
            LOGGER.warning("Expected 'items' array in JSON but did not find one. Returning empty bus line list.");
            return busLines;
        }

        for (JsonNode node : results) {
            try {
                String id = node.path("dc:identifier").asText("");
                String name = node.path("dc:name").asText("");
                String colour = null;
                String route = null;
                String schedule = null;

                if (id.isEmpty() || name.isEmpty()) {
                    LOGGER.warning("BusLine entry missing required fields: id or name. Skipping entry.");
                    continue;
                }

                busLines.add(new BusLine(id, name, colour, route, schedule));
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Error parsing BusLine entry. Skipping entry.", e);
            }
        }
        return busLines;
    }

    // Convenience method to fetch and parse
    public List<BusLine> fetchAndParseBusLines() {
        JsonNode root = fetchBusLinesJson();
        return parseBusLines(root);
    }
    public void refreshBusLines() {
        List<BusLine> busLines = fetchAndParseBusLines();
        if (busLines.isEmpty()) {
            LOGGER.warning("No BusLine entries found to save.");
            return;
        }
        saveBusLines(busLines);
    }

    public List<BusLine> getAllBusLines() {
        return busLineRepository.findAll();
    }

    // Save parsed BusLine list to MongoDB
    private void saveBusLines(List<BusLine> busLines) {
        if (busLines == null || busLines.isEmpty()) {
            LOGGER.warning("No BusLine entries to save to MongoDB.");
            return;
        }
        try {
            busLineRepository.saveAll(busLines);
            LOGGER.info("Saved " + busLines.size() + " BusLine entries to MongoDB.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to save BusLine entries to MongoDB.", e);
        }
    }
}