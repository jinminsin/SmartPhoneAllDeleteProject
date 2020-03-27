package com.koia.smartphonealldelete;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.os.Build;
import android.os.Bundle;
import android.os.StatFs;
import android.widget.TextView;
import java.io.File;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    private TextView textSpace,textSpace2;
    private TextView internal,external;
    private File[] externalDrive;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT > 18) {
            externalDrive = getExternalFilesDirs(null);
            String dir = externalDrive[0].getParentFile().getParentFile().getParentFile().getParentFile().getPath();

            internal = findViewById(R.id.internal);
            internal.setText(dir);
            textSpace = findViewById(R.id.capacity);
            textSpace.setText("(" + getFileSize(checkStorageAllMemory(dir) - checkAvailableMemory(dir)) + "/" + getFileSize(checkStorageAllMemory(dir)) + ")");

            if (externalDrive.length > 1) {
                dir = externalDrive[1].getParentFile().getParentFile().getParentFile().getParentFile().getPath();
                external = findViewById(R.id.external);
                external.setText(dir);
                textSpace2 = findViewById(R.id.capacity2);
                textSpace2.setText("(" + getFileSize(checkStorageAllMemory(dir) - checkAvailableMemory(dir)) + "/" + getFileSize(checkStorageAllMemory(dir)) + ")");
            } else {
                external = findViewById(R.id.external);
                external.setText("대가리 깨짐");
                textSpace2 = findViewById(R.id.capacity2);
                textSpace2.setText("내 머리 머머리");
            }
        }else
        {
            externalDrive = ContextCompat.getExternalFilesDirs(this,null);
            String dir = externalDrive[0].getParentFile().getParentFile().getParentFile().getParentFile().getPath();

            internal = findViewById(R.id.internal);
            internal.setText(dir);
            textSpace = findViewById(R.id.capacity);
            textSpace.setText("(" + getFileSize(checkStorageAllMemory(dir) - checkAvailableMemory(dir)) + "/" + getFileSize(checkStorageAllMemory(dir)) + ")");

            if (externalDrive.length > 1) {
                dir = externalDrive[1].getParentFile().getParentFile().getParentFile().getParentFile().getPath();
                external = findViewById(R.id.external);
                external.setText(dir);
                textSpace2 = findViewById(R.id.capacity2);
                textSpace2.setText("(" + getFileSize(checkStorageAllMemory(dir) - checkAvailableMemory(dir)) + "/" + getFileSize(checkStorageAllMemory(dir)) + ")");
            } else {
                external = findViewById(R.id.external);
                external.setText("대가리 깨짐");
                textSpace2 = findViewById(R.id.capacity2);
                textSpace2.setText("내 머리 머머리");
            }
        }
    }

    private long checkStorageAllMemory(String dir) {
        StatFs stat= new StatFs(dir);
        return Build.VERSION.SDK_INT > 18 ? stat.getTotalBytes() : stat.getBlockSize() * stat.getBlockCount();
    }

    private long checkAvailableMemory(String dir) {
        StatFs stat= new StatFs(dir);
        return Build.VERSION.SDK_INT > 18 ? stat.getAvailableBytes() : stat.getAvailableBlocks() * stat.getBlockCount();
    }

    public String getFileSize(long size) {
        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log(size) / Math.log(1024));
        return new DecimalFormat("#,###.##").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}
