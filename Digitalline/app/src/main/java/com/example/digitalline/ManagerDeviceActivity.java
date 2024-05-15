package com.example.digitalline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

public class ManagerDeviceActivity extends AppCompatActivity implements OnMapReadyCallback {

    Spinner status;
    TextView deviceName;
    TextView deviceSerial;
    TextView timeOfSendToFix;
    DeviceWithEmails device;
    boolean first = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_device);
        FragmentUtils.openTopBarFragment(true, false, this);
        status = findViewById(R.id.status);
        deviceName = findViewById(R.id.device_name);
        deviceSerial = findViewById(R.id.device_serial);
        timeOfSendToFix = findViewById(R.id.time_of_send_to_fix);
        device = (DeviceWithEmails) getIntent().getExtras().get("device");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        deviceName.setText(device.getDevice().getName());
        deviceSerial.setText("serial: "+device.getDevice().getSerial());
        timeOfSendToFix.setText("arrived to the lab at: "+new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(new Date(device.getDevice().getTimeOfSendingToFix())));
        String[] statusTexts = new String[3];
        StatusUtils.Status currentStatus = device.getDevice().getFixingStatus();
        statusTexts[0] = StatusUtils.getTextFromStatus(currentStatus);
        LinkedList<StatusUtils.Status> statuses = new LinkedList<>();
        statuses.add(StatusUtils.Status.WaitingForFixingToStart);
        statuses.add(StatusUtils.Status.DuringFix);
        statuses.add(StatusUtils.Status.WasFixed);
        statuses.remove(currentStatus);
        for (int i = 1; i < 3; i++) {
            statusTexts[i] = StatusUtils.getTextFromStatus(statuses.get(i-1));
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.dropdown_item, statusTexts);
        status.setAdapter(arrayAdapter);
        status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!first) {
                    device.getDevice().setFixingStatus(StatusUtils.getStatusFromText(parent.getItemAtPosition(position).toString()));
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("device", device.getDevice());
                    map.put("clientEmail", device.getClientEmail().replaceAll("\\.", " "));
                    map.put("managerEmail", device.getOwnerEmail().replaceAll("\\.", " "));
                    FirebaseFirestore.getInstance().collection("devices").document(device.getDevice().getSerial()).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                                UserMessages.showToastMessage("Status updated successfully", ManagerDeviceActivity.this);
                        }
                    });
                } else
                    first = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
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