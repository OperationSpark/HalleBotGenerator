package org.operationspark.hallebotgenerator.models;

public class Location {
    public final double latitude;
    public final double longitude;
    public final double accuracy;

    public Location(double latitude, double longitude, double accuracy) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.accuracy = accuracy;
    }
}
