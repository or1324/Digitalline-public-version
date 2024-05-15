package com.example.digitalline;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class DeviceWithEmails implements Serializable {
    private String clientEmail;
    private String ownerEmail;
    private Device device;
    public DeviceWithEmails(Device device, String clientEmail, String ownerEmail) {
        this.device = device;
        this.clientEmail = clientEmail;
        this.ownerEmail = ownerEmail;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public Device getDevice() {
        return device;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    @Override
    public int hashCode() {
        return device.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return hashCode() == obj.hashCode();
    }
}
