package com.example.digitalline;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class Device implements Serializable {
    private String name;
    private String serial;
    private LabDetails lab;
    private long timeOfSendingToFix;
    private StatusUtils.Status fixingStatus;

    public Device(String name, String serial, LabDetails lab, long timeOfSendingToFix, StatusUtils.Status fixingStatus) {
        this.name = name;
        this.serial = serial;
        this.lab = lab;
        this.timeOfSendingToFix = timeOfSendingToFix;
        this.fixingStatus = fixingStatus;
    }

    public void setFixingStatus(StatusUtils.Status fixingStatus) {
        this.fixingStatus = fixingStatus;
    }

    public void setLab(LabDetails lab) {
        this.lab = lab;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public void setTimeOfSendingToFix(long timeOfSendingToFix) {
        this.timeOfSendingToFix = timeOfSendingToFix;
    }

    public long getTimeOfSendingToFix() {
        return timeOfSendingToFix;
    }

    public LabDetails getLab() {
        return lab;
    }

    public StatusUtils.Status getFixingStatus() {
        return fixingStatus;
    }

    public String getName() {
        return name;
    }

    public String getSerial() {
        return serial;
    }

    @Override
    public int hashCode() {
        return serial.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return hashCode() == obj.hashCode();
    }
}
