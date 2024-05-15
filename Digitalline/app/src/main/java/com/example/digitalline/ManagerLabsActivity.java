package com.example.digitalline;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.LinkedList;

public class ManagerLabsActivity extends AppCompatActivity {

    LinearLayout labsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_labs);
        FragmentUtils.openTopBarFragment(true, false, this);
        labsLayout = findViewById(R.id.labs);
        try {
            LinkedList<LabDetails> labs = ExternalStorage.restoreObject(this, "labs");
            for (LabDetails lab : labs) {
                TextView textView = new TextView(this);
                textView.setTextSize(30);
                textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                ((LinearLayout.LayoutParams)textView.getLayoutParams()).topMargin = 10;
                textView.setBackgroundColor(Color.rgb(220, 40, 40));
                textView.setTextColor(Color.BLACK);
                textView.setText(lab.getLabName()+":\n"+lab.getCoordinates().getLeft()+", "+lab.getCoordinates().getRight());
                labsLayout.addView(textView);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}