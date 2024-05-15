package com.example.digitalline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {
    boolean twoSecondsPassed = false;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_splash);
        progressBar = findViewById(R.id.progressBar);
        countTwoSecondsWithProgress();
        doImportantStuff();
    }

    private void doImportantStuff() {
        boolean isAuthenticated = checkIfUserIsSignedIn();
        BackgroundRunningHelper.runCodeInBackgroundAsync(new Runnable() {
            @Override
            public void run() {
                waitUntilTwoSecondsPassed();
                if (isAuthenticated) {
                    boolean isManager = SharedPreferencesStorage.getBoolean(SharedPreferencesStorage.isManagerPreference, false, SplashActivity.this);
                    if (isManager)
                        moveToManagerPage();
                    else
                        moveToClientPage();
                } else {
                    moveToSignIn();
                }
            }
        });
    }

    private void moveToManagerPage() {
        Intent intent = new Intent(SplashActivity.this, ManagerActivity.class);
        startActivity(intent);
    }

    private void moveToClientPage() {
        Intent intent = new Intent(SplashActivity.this, ClientActivity.class);
        startActivity(intent);
    }

    private void moveToSignIn() {
        Intent intent = new Intent(SplashActivity.this, SignInActivity.class);
        startActivity(intent);
    }

    private void waitUntilTwoSecondsPassed() {
        while (!twoSecondsPassed) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private boolean checkIfUserIsSignedIn() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null;
    }

    private void countTwoSecondsWithProgress() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                progressBar.setProgress(progressBar.getProgress()+5);
                if (progressBar.getProgress() == 100) {
                    cancel();
                    twoSecondsPassed = true;
                }
            }
        }, 100, 100);
    }
}