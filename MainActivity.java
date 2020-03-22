package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.io.File;

public class MainActivity extends AppCompatActivity {
    private TextView textSpace,textSpace2;
    private TextView internal,external;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        File[] externalDrive = getExternalFilesDirs(null);
        String dir = externalDrive[0].getPath();

        internal = findViewById(R.id.internal);
        internal.setText(dir);
        textSpace = findViewById(R.id.text);
        textSpace.setText("(" + getFileSize(checkStorageAllMemory(dir)-checkAvailableMemory(dir)) + "/" + getFileSize(checkStorageAllMemory(dir)) + ")");

        if(externalDrive.length > 1) {
            dir = externalDrive[1].getPath();
            external = findViewById(R.id.external);
            external.setText(dir);
            textSpace2 = findViewById(R.id.text2);
            textSpace2.setText("(" + getFileSize(checkStorageAllMemory(dir) - checkAvailableMemory(dir)) + "/" + getFileSize(checkStorageAllMemory(dir)) + ")");
        }else
        {
            external = findViewById(R.id.external);
            external.setText("머머리");
            textSpace2 = findViewById(R.id.text2);
            textSpace2.setText("머머리는죄악");
        }
    }

    private long checkStorageAllMemory(String dir) {
        StatFs stat= new StatFs(dir);
        return stat.getTotalBytes();
    }

    private long checkAvailableMemory(String dir) {
        StatFs stat= new StatFs(dir);
        return stat.getAvailableBytes();
    }

    public String getFileSize(long size) {
        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log(size) / Math.log(1024));
        return new DecimalFormat("#,###.##").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}