package com.alipe.transportHub.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tusstops")
public class BusStop {
    @Id
    private String id;
    private String code;
    private String name;
    private double latitude;
    private double longitude;
    private String address;
    private String zone;
    private String url;

    public BusStop() {
    }

    public BusStop(String id, String code, String name, double latitude, double longitude, String address, String zone, String url) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.zone = zone;
        this.url = url;

    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

        public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

        public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getzone() {
        return zone;
    }

    public void setzone(String zone) {
        this.zone = zone;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    // @Override
    // public String toString() {
    //     return "BusStop{" +
    //             "id='" + id + '\'' +
    //             ", name='" + name + '\'' +
    //             ", ='" + colour + '\'' +
    //             ", route='" + route + '\'' +
    //             ", schedule='" + schedule + '\'' +
    //             '}';
    // }
}