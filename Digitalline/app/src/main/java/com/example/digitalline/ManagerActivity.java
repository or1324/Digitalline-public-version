package com.example.digitalline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import java.io.IOException;
import java.util.LinkedList;

public class ManagerActivity extends AppCompatActivity {

    LinearLayout deviceViews;
    AppCompatButton labs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        FragmentUtils.openTopBarFragment(false, true, this);
        deviceViews = findViewById(R.id.devices);
        labs = findViewById(R.id.labs);
        labs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagerActivity.this, ManagerLabsActivity.class);
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
                graphics.setManagerDeviceFunctionality(device);
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