package com.example.digitalline;

import java.io.Serializable;

public class LabDetails implements Serializable {
    private String labName;
    private Pair<Double, Double> coordinates;

    public LabDetails(String labName, Pair<Double, Double> coordinates) {
        this.labName = labName;
        this.coordinates = coordinates;
    }

    public Pair<Double, Double> getCoordinates() {
        return coordinates;
    }

    public String getLabName() {
        return labName;
    }
}
