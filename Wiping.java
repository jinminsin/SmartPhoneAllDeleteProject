package com.example.myapplication;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Wiping extends AppCompatActivity {
    private TextView drive;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wiping);
        progressBar = (ProgressBar)findViewById(R.id.progress);

        drive = findViewById(R.id.drive);

        DriveListItem k = (DriveListItem)getIntent().getSerializableExtra("DRIVE");

        drive.setText(k.getDriveName()+"\n"+k.getDrivePath()+"\n( "+k.getDriveFreeSize()+" / "+k.getDriveFullSize()+" )");

    }


}
