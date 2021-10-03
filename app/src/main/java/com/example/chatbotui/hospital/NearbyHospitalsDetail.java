package com.example.chatbotui.hospital;

import java.util.Arrays;

public class NearbyHospitalsDetail {

    private String hospitalName;
    private String rating;
    private String openingHours;
    private String address;
    private double[] geometry;

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double[] getGeometry() {
        return geometry;
    }

    public void setGeometry(double[] geometry) {
        this.geometry = geometry;
    }

    @Override
    public String toString() {
        return "NearbyHospitalsDetail{" +
                ", hospitalName='" + hospitalName + '\'' +
                ", rating=" + rating +
                ", openingHours='" + openingHours + '\'' +
                ", address='" + address + '\'' +
                ", geometry=" + Arrays.toString(geometry) +
                '}';
    }
}
