package com.koia.smartphonealldelete;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Wiping extends AppCompatActivity {
    private TextView drive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wiping);

        drive = findViewById(R.id.drive);

        DriveListItem k = (DriveListItem)getIntent().getSerializableExtra("DRIVE");

        drive.setText(k.getDriveName()+"\n"+k.getDrivePath()+"\n( "+k.getDriveFreeSize()+" / "+k.getDriveFullSize()+" )");
    }
}
