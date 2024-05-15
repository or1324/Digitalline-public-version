package com.example.digitalline;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;


public class SignInActivity extends AppCompatActivity {

    public boolean isSignUp = true;
    public TextView title;
    public EditText email;
    public EditText password;
    public AppCompatImageButton isSignInButton;
    public AppCompatImageButton signInButton;
    public ProgressBar progressBar;
    public LinearLayout labDetailsLayout;
    public AppCompatButton addCoordinates;
    public CheckBox isManager;
    public ConstraintLayout coordinatesStuff;
    public boolean canClick = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_sign_in);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        isSignInButton = findViewById(R.id.is_sign_in);
        signInButton = findViewById(R.id.sign_in_button);
        progressBar = findViewById(R.id.progressBar);
        addCoordinates = findViewById(R.id.add_coordinate);
        labDetailsLayout = findViewById(R.id.coordinates);
        coordinatesStuff = findViewById(R.id.coordinates_stuff);
        title = findViewById(R.id.welcome2);
        isManager = findViewById(R.id.is_manager);
        addCoordinates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                labDetailsLayout.addView(getLayoutInflater().inflate(R.layout.lab_details, null));
            }
        });
        isManager.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    findViewById(R.id.coordinates_stuff).setVisibility(View.VISIBLE);
                else
                    findViewById(R.id.coordinates_stuff).setVisibility(View.GONE);
            }
        });
        isSignInButton.setOnClickListener(v -> {
            if (isSignUp) {
                isSignUp = false;
                title.setText("Login");
                isSignInButton.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.change_to_signup_button));
                signInButton.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.login_button));
                findViewById(R.id.coordinates_stuff).setVisibility(View.GONE);
                isManager.setVisibility(View.GONE);
            } else {
                isSignUp = true;
                title.setText("Register");
                isSignInButton.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.change_to_login_button));
                signInButton.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.signup_button));
                if (isManager.isChecked())
                    findViewById(R.id.coordinates_stuff).setVisibility(View.VISIBLE);
                isManager.setVisibility(View.VISIBLE);
            }
        });
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canClick) {
                    canClick = false;
                    progressBar.setVisibility(View.VISIBLE);
                    LinkedList<LabDetails> labs = new LinkedList<>();
                    String emailText = email.getText().toString();
                    String passwordText = password.getText().toString();
                    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                    try {
                        if (isSignUp) {
                            for (int i = 0; i < labDetailsLayout.getChildCount(); i++) {
                                View labDetailsView = labDetailsLayout.getChildAt(i);
//                                EditText firstCoordinateView = labDetailsView.findViewById(R.id.first);
//                                EditText secondCoordinateView = labDetailsView.findViewById(R.id.second);

                                Geocoder geocoder = new Geocoder(SignInActivity.this);
                                try {
                                    List<Address> addressList = geocoder.getFromLocationName(((EditText)labDetailsView.findViewById(R.id.address)).getText().toString(), 1);
                                    Address address = addressList.get(0);
                                    EditText labName = labDetailsView.findViewById(R.id.name);
                                    //labs.add(new LabDetails(labName.getText().toString(), new Pair<>(Double.parseDouble(firstCoordinateView.getText().toString()), Double.parseDouble(secondCoordinateView.getText().toString()))));
                                    labs.add(new LabDetails(labName.getText().toString(), new Pair<>(address.getLatitude(), address.getLongitude())));
                                } catch (IOException e) {
                                    UserMessages.showToastMessage("Address not found", SignInActivity.this);
                                }
                            }
                            CloudOperations.getAllLabsAsync(SignInActivity.this, new LabsDownloadListener() {
                                @Override
                                public void onLabsDownloaded(HashMap<String, LinkedList<LabDetails>> usersLabs) {
                                    boolean labExists = false;
                                    for (LabDetails labDetails : labs) {
                                        if (labExists)
                                            break;
                                        for (LinkedList<LabDetails> list : usersLabs.values()) {
                                            if (labExists)
                                                break;
                                            for (LabDetails lab : list) {
                                                if (lab.getLabName().equals(labDetails.getLabName())) {
                                                    UserMessages.showToastMessage("The lab " + lab.getLabName() + " already exists", SignInActivity.this);
                                                    labExists = true;
                                                } else if (Objects.equals(lab.getCoordinates().getRight(), labDetails.getCoordinates().getRight()) && Objects.equals(lab.getCoordinates().getLeft(), labDetails.getCoordinates().getLeft())) {
                                                    UserMessages.showToastMessage("There is already a lab at " + lab.getCoordinates().getLeft() + ", " + lab.getCoordinates().getRight(), SignInActivity.this);
                                                    labExists = true;
                                                }
                                                break;
                                            }
                                        }
                                    }
                                    if (!labExists) {
                                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailText, passwordText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    HashMap<String, Object> map = new HashMap<>();
                                                    if (isManager.isChecked())
                                                        map.put("labs", labs);
                                                    map.put("isManager", isManager.isChecked());
                                                    firestore.collection("users").document(emailText.replaceAll("\\.", " ")).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task2) {
                                                            if (task2.isSuccessful()) {
                                                                LinkedList<DeviceWithEmails> devices = new LinkedList<>();
                                                                ExternalStorage.saveObject(SignInActivity.this, "devices", devices);
                                                                ExternalStorage.saveObject(SignInActivity.this, "labs", labs);
                                                                SharedPreferencesStorage.saveString("email", emailText, SignInActivity.this);
                                                                SharedPreferencesStorage.saveBoolean("isManager", isManager.isChecked(), SignInActivity.this);
                                                                Intent intent;
                                                                if (isManager.isChecked())
                                                                    intent = new Intent(SignInActivity.this, ManagerActivity.class);
                                                                else
                                                                    intent = new Intent(SignInActivity.this, ClientActivity.class);
                                                                startActivity(intent);
                                                                progressBar.setVisibility(View.GONE);
                                                            } else
                                                                error();
                                                        }
                                                    });
                                                } else
                                                    error();
                                            }
                                        });
                                    }
                                }
                            });
                        } else {
                            firestore.collection("users").document(emailText.replaceAll("\\.", " ")).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        if (task.getResult().exists()) {
                                            boolean isManager = task.getResult().getBoolean("isManager");
                                            if (isManager) {
                                                List<HashMap<String, Object>> genericLabs = (List<HashMap<String, Object>>) task.getResult().get("labs");
                                                for (HashMap<String, Object> map : genericLabs) {
                                                    HashMap<String, Double> pair = ((HashMap<String, Double>) map.get("coordinates"));
                                                    labs.add(new LabDetails(map.get("labName").toString(), new Pair<>(pair.get("left"), pair.get("right"))));
                                                }
                                            }
                                            String deviceOwnerFieldName;
                                            if (isManager)
                                                deviceOwnerFieldName = "managerEmail";
                                            else
                                                deviceOwnerFieldName = "clientEmail";
                                            firestore.collection("devices").whereEqualTo(deviceOwnerFieldName, emailText.replaceAll("\\.", " ")).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        LinkedList<DeviceWithEmails> devices = new LinkedList<>();
                                                        for (DocumentSnapshot deviceDoc : task.getResult().getDocuments()) {
                                                            HashMap<String, Object> genericDevice = (HashMap<String, Object>) deviceDoc.get("device");
                                                            HashMap<String, Object> lab = (HashMap<String, Object>) genericDevice.get("lab");
                                                            HashMap<String, Object> pair = (HashMap<String, Object>) lab.get("coordinates");
                                                            devices.add(new DeviceWithEmails(new Device(genericDevice.get("name").toString(), genericDevice.get("serial").toString(), new LabDetails(lab.get("labName").toString(), new Pair<>((double)pair.get("left"), (double)pair.get("right"))), (long) genericDevice.get("timeOfSendingToFix"), StatusUtils.getStatusFromCloudText(genericDevice.get("fixingStatus").toString())), deviceDoc.get("clientEmail").toString().replaceAll(" ", "."), deviceDoc.get("managerEmail").toString().replaceAll(" ", ".")));
                                                        }
                                                        FirebaseAuth.getInstance().signInWithEmailAndPassword(emailText, passwordText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                                if (task.isSuccessful()) {
                                                                    ExternalStorage.saveObject(SignInActivity.this, "devices", devices);
                                                                    if (isManager)
                                                                        ExternalStorage.saveObject(SignInActivity.this, "labs", labs);
                                                                    SharedPreferencesStorage.saveString("email", emailText, SignInActivity.this);
                                                                    SharedPreferencesStorage.saveBoolean("isManager", isManager, SignInActivity.this);
                                                                    Intent intent;
                                                                    if (isManager)
                                                                        intent = new Intent(SignInActivity.this, ManagerActivity.class);
                                                                    else
                                                                        intent = new Intent(SignInActivity.this, ClientActivity.class);
                                                                    startActivity(intent);
                                                                    progressBar.setVisibility(View.GONE);
                                                                } else
                                                                    error();
                                                            }
                                                        });
                                                    } else
                                                        error();
                                                }
                                            });
                                        } else
                                            error();
                                    } else
                                        error();
                                }
                            });
                        }
                    } catch (Exception e) {
                        error();
                    }
                }
            }
        });
    }

    private void error() {
        UserMessages.showToastMessage("There was an error. Please try again", this);
        progressBar.setVisibility(View.GONE);
        canClick = true;
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}