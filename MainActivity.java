package com.koia.smartphonealldelete;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.text.DecimalFormat;


public class MainActivity extends AppCompatActivity {
    private TextView textSpace;
    private String dir = Environment.getDataDirectory().getPath();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textSpace = findViewById(R.id.capacity);
        textSpace.setText("(" + getFileSize(checkInternalStorageAllMemory(dir)-checkInternalAvailableMemory(dir)) + "/" + getFileSize(checkInternalStorageAllMemory(dir)) + ")");
    }

    private long checkInternalStorageAllMemory(String dir) {
        StatFs stat= new StatFs(dir);
        return stat.getBlockSizeLong() *stat.getBlockCountLong();
    }

    private long checkInternalAvailableMemory(String dir) {
        StatFs stat= new StatFs(dir);
        return stat.getBlockSizeLong() *stat.getAvailableBlocksLong();
    }

    public String getFileSize(long size) {
        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log(size) / Math.log(1024));
        return new DecimalFormat("#,###.##").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}
