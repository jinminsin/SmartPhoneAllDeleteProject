package com.koia.smartphonealldelete;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Wiping extends Activity {
    private TextView drive;
    private ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wiping);

        drive = findViewById(R.id.drive);
        bar = findViewById(R.id.progress);

        DriveListItem k = (DriveListItem)getIntent().getSerializableExtra("DRIVE");
        drive.setText(k.getDriveName()+"\n"+k.getDrivePath()+"\n( "+k.getFileSize(k.getDriveFreeSize())+" / "+k.getFileSize(k.getDriveFullSize())+" )");
        bar.setMax(100);
        bar.setProgress((int)(100 * ((double)k.getDriveFreeSize()/(double)k.getDriveFullSize())));
    }


}
