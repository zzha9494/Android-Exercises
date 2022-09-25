package com.example.assignment2;

public class MediaInfo {
    private String fileName;
    private double Latitude;
    private double Longitude;
    private String cityName;

    public MediaInfo(String fileName, double latitude, double longitude, String cityName) {
        this.fileName = fileName;
        this.Latitude = latitude;
        this.Longitude = longitude;
        this.cityName = cityName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
