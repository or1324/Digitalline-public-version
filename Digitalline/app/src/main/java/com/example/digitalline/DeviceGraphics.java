package com.example.digitalline;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatButton;

public class DeviceGraphics extends AppCompatButton {
    public DeviceGraphics(@NonNull Context context) {
        super(context);
        setTextSize(40);
        setBackground(AppCompatResources.getDrawable(getContext(), R.drawable.app_button));
        setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setGravity(Gravity.CENTER);
    }
    public void setManagerDeviceFunctionality(DeviceWithEmails device) {
        setText(device.getDevice().getName());
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ManagerDeviceActivity.class);
                intent.putExtra("device", device);
                getContext().startActivity(intent);
            }
        });
    }

    public void setClientDeviceFunctionality(DeviceWithEmails device) {
        setText(device.getDevice().getName());
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ClientDeviceActivity.class);
                intent.putExtra("device", device);
                getContext().startActivity(intent);
            }
        });
    }
}
