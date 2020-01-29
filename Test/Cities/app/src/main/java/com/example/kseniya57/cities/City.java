package com.example.kseniya57.cities;

import android.location.Location;

import java.io.Serializable;
import java.util.Objects;

class Coordinates {
    double lon;
    double lat;

    public Coordinates(double lon, double lat) {
        this.lon = lon;
        this.lat = lat;
    }

    @Override
    public String toString() {
        return lat + "," + lon;
    }
}

public class City {
    private int id;
    private String name;
    private String country;
    private Coordinates coord;

    public City() {
    }

    public City(String cityString) {
        String[] parts = cityString.split(",");
        id = Integer.parseInt(parts[0]);
        name = parts[1];
        country = parts[2];
        coord = new Coordinates(Double.parseDouble(parts[3]), Double.parseDouble(parts[4]));
    }

    public City(int id, String name, String country, Coordinates coord) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.coord = coord;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public double getLat() {
        return coord.lat;
    }

    public double getLon() {
        return coord.lon;
    }

    public float distanceTo(City other) {
        float[] results = {0};
        Location.distanceBetween(coord.lat, coord.lon, other.coord.lat, other.coord.lon, results);
        return results[0];
    }

    @Override
    public String toString() {
        return name;
    }

    public String serialize() {
        return String.format("%d,%s,%s,%s", id, name, country, coord.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return id == city.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
