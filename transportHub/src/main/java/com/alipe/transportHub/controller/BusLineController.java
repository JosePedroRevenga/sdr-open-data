package com.alipe.transportHub.controller;

import com.alipe.transportHub.model.BusLine;
import com.alipe.transportHub.service.BusLineService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BusLineController {

    private final BusLineService busLineService;

    public BusLineController(BusLineService busLineService) {
        this.busLineService = busLineService;
    }

    @GetMapping("/buslines")
    public List<BusLine> getAllBusLines() {
        return busLineService.getAllBusLines();
    }

    @PutMapping("/refreshBuslines")
    public List<BusLine> refreshBuslines() {
        busLineService.refreshBusLines();
        return busLineService.getAllBusLines();
    }
}