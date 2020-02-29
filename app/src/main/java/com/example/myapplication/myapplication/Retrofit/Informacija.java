package com.example.myapplication.myapplication.Retrofit;

import com.google.gson.annotations.SerializedName;

public class Informacija {

    public Informacija(String ime, double latitude, double longitude) {
        this.ime = ime;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    @SerializedName("ime")
    private String ime;
    @SerializedName("latitude")
    private double latitude;
    @SerializedName("longitude")
    private double longitude;



    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
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
}
