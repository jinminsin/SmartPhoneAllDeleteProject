package com.koia.smartphonealldelete;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.os.Build;
import android.os.Bundle;
import android.os.StatFs;
import android.widget.ListView;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView driveList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        driveList = findViewById(R.id.driveList);
        ArrayList<DriveListItem> list = new ArrayList<>();

        File[] fDir;

        if(Build.VERSION.SDK_INT > 18) {
            DriveListItem k = new DriveListItem();
            fDir = getExternalFilesDirs(null);

            for(File d:fDir) {
                d = d.getParentFile().getParentFile().getParentFile().getParentFile();
                k.setDriveName(d.getName().equals("0") ? "Internal Main Storage" : d.getName());
                k.setDrivePath(d.getAbsolutePath());
                k.setDriveFreeSize(getFileSize(checkStorageAllMemory(d.getAbsolutePath()) - checkAvailableMemory(d.getAbsolutePath())));
                k.setDriveFullSize(getFileSize(checkStorageAllMemory(d.getAbsolutePath())));

                list.add(k);
            }
        }else {
            DriveListItem k = new DriveListItem();
            fDir = ContextCompat.getExternalFilesDirs(this, null);

            for(File d:fDir) {
                d = d.getParentFile().getParentFile().getParentFile().getParentFile();
                k.setDriveName(d.getName().equals("0") ? "Internal Main Storage" : d.getName());
                k.setDrivePath(d.getAbsolutePath());
                k.setDriveFreeSize(getFileSize(checkStorageAllMemory(d.getAbsolutePath()) - checkAvailableMemory(d.getAbsolutePath())));
                k.setDriveFullSize(getFileSize(checkStorageAllMemory(d.getAbsolutePath())));

                list.add(k);
            }
        }
        DriveListAdapter listAdapter = new DriveListAdapter(list);
        driveList.setAdapter(listAdapter);
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
