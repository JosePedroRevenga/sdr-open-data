package com.alipe.transportHub.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tuslines")
public class BusLine {
    @Id
    private String id;
    private String name;
    private String colour;
    private String route;
    private String schedule;

    public BusLine() {}

    public BusLine(String id, String name, String colour, String route, String schedule) {
        this.id = id;
        this.name = name;
        this.colour = colour;
        this.route = route;
        this.schedule = schedule;
    }

    // Getters and setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    @Override
    public String toString() {
        return "BusLine{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", colour='" + colour + '\'' +
                ", route='" + route + '\'' +
                ", schedule='" + schedule + '\'' +
                '}';
    }
}