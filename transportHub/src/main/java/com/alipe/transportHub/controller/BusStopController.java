package com.alipe.transportHub.controller;

import com.alipe.transportHub.model.BusStop;
import com.alipe.transportHub.service.BusStopService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BusStopController {

    private final BusStopService busStopService;

    public BusStopController(BusStopService busStopService) {
        this.busStopService = busStopService;
    }

    @GetMapping("/busstops")
    public List<BusStop> getAllBusStops() {
        return busStopService.getAllBusStops();
    }

    @GetMapping("/refreshBusstops")
    public List<BusStop> refreshBusstops() {
        busStopService.refreshBusStops();
        return busStopService.getAllBusStops();
    }
}