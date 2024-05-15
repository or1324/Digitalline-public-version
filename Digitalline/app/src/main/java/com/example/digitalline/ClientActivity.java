package com.example.digitalline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import java.io.IOException;
import java.util.LinkedList;

public class ClientActivity extends AppCompatActivity {

    LinearLayout deviceViews;
    AppCompatButton addDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        FragmentUtils.openTopBarFragment(false, true, this);
        deviceViews = findViewById(R.id.devices);
        addDevice = findViewById(R.id.add_device);
        addDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClientActivity.this, AddDeviceActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            deviceViews.removeAllViews();
            LinkedList<DeviceWithEmails> devices = ExternalStorage.restoreObject(this, "devices");
            for (DeviceWithEmails device : devices) {
                DeviceGraphics graphics = new DeviceGraphics(this);
                graphics.setClientDeviceFunctionality(device);
                deviceViews.addView(graphics);
            }
        } catch (IOException ignored) {
        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}