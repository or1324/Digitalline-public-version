package com.example.digitalline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.LinkedList;

public class ClientDeviceActivity extends AppCompatActivity implements OnMapReadyCallback {

    TextView status;
    TextView deviceName;
    TextView deviceSerial;
    DeviceWithEmails device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_device);
        FragmentUtils.openTopBarFragment(true, false, this);
        status = findViewById(R.id.status);
        deviceName = findViewById(R.id.device_name);
        deviceSerial = findViewById(R.id.device_serial);
        device = (DeviceWithEmails) getIntent().getExtras().get("device");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        deviceName.setText(device.getDevice().getName());
        deviceSerial.setText("serial: "+device.getDevice().getSerial());
        status.setText("Status: " + StatusUtils.getTextFromStatus(device.getDevice().getFixingStatus()));
        if (device.getDevice().getFixingStatus() == StatusUtils.Status.WasFixed) {
            try {
                LinkedList<DeviceWithEmails> devices = ExternalStorage.restoreObject(this, "devices");
                devices.remove(device);
                ExternalStorage.saveObject(this, "devices", devices);
                FirebaseFirestore.getInstance().collection("devices").document(device.getDevice().getSerial()).delete();
                new AlertDialog.Builder(this).setMessage("The device was fixed! :)").show().setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        finish();
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        //TODO create activity.
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Pair<Double, Double> coordinates = device.getDevice().getLab().getCoordinates();
        LatLng deviceLab = new LatLng(coordinates.getLeft(), coordinates.getRight());
        googleMap.addMarker(new MarkerOptions()
                .position(deviceLab)
                .title(device.getDevice().getLab().getLabName()));
    }
}