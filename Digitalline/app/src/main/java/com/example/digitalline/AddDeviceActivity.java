package com.example.digitalline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

public class AddDeviceActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    EditText deviceName;
    EditText deviceSerial;
    TextView chosenLab;
    HashMap<String, LinkedList<LabDetails>> labs;
    String labOwnerEmail = null;
    LabDetails lab = null;
    AppCompatButton createDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        FragmentUtils.openTopBarFragment(true, false, this);
        deviceName = findViewById(R.id.device_name);
        deviceSerial = findViewById(R.id.device_serial);
        createDevice = findViewById(R.id.create_device);
        chosenLab = findViewById(R.id.chosen_lab);
        CloudOperations.getAllLabsAsync(this, new LabsDownloadListener() {
            @Override
            public void onLabsDownloaded(HashMap<String, LinkedList<LabDetails>> usersLabs) {
                labs = usersLabs;
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(AddDeviceActivity.this);
            }
        });
        createDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chosenLab.getText().equals("choose a lab:"))
                    UserMessages.showToastMessage("You must choose a lab", AddDeviceActivity.this);
                FirebaseFirestore.getInstance().collection("devices").document(deviceSerial.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().exists()) {
                                UserMessages.showToastMessage("A device with this IMEI is already in fixing process", AddDeviceActivity.this);
                            } else {
                                String myEmail = SharedPreferencesStorage.getString("email", null, AddDeviceActivity.this);
                                Device device = new Device(deviceName.getText().toString(), deviceSerial.getText().toString(), lab, System.currentTimeMillis(), StatusUtils.Status.WaitingForFixingToStart);
                                HashMap<String, Object> map = new HashMap<>();
                                map.put("managerEmail", labOwnerEmail.replaceAll("\\.", " "));
                                map.put("clientEmail", myEmail.replaceAll("\\.", " "));
                                map.put("device", device);
                                FirebaseFirestore.getInstance().collection("devices").document(deviceSerial.getText().toString()).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        LinkedList<DeviceWithEmails> devices = null;
                                        try {
                                            devices = ExternalStorage.restoreObject(AddDeviceActivity.this, "devices");
                                        } catch (IOException e) {
                                            devices = new LinkedList<>();
                                        }
                                        devices.add(new DeviceWithEmails(device, labOwnerEmail, myEmail));
                                        ExternalStorage.saveObject(AddDeviceActivity.this, "devices", devices);
                                        UserMessages.showToastMessage("Device was created successfully", AddDeviceActivity.this);
                                        finish();
                                    }
                                });
                            }
                        } else
                            UserMessages.showToastMessage("There was an error. Please try again", AddDeviceActivity.this);
                    }
                });
            }
        });
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        for (String email : labs.keySet())
            for (LabDetails labDetails : labs.get(email))
                if (labDetails.getLabName().equals(marker.getTitle())) {
                    labOwnerEmail = email;
                    lab = labDetails;
                    chosenLab.setText("Chosen lab: "+lab.getLabName());
                    break;
                }
        return false;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        findViewById(R.id.progressBar).setVisibility(View.GONE);
        findViewById(R.id.after_loading).setVisibility(View.VISIBLE);
        googleMap.setOnMarkerClickListener(this);
        for (String email : labs.keySet()) {
            for (LabDetails labDetails : labs.get(email)) {
                Pair<Double, Double> coordinates = labDetails.getCoordinates();
                LatLng deviceLab = new LatLng(coordinates.getLeft(), coordinates.getRight());
                googleMap.addMarker(new MarkerOptions()
                        .position(deviceLab)
                        .title(labDetails.getLabName()));
            }
        }
    }
}