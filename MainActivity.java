package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static String data;
    private ListView driveList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        driveList = findViewById(R.id.driveList);
        ArrayList<DriveListItem> list = new ArrayList<>();

        File[] fDir;
        //안드로이드 버전 19 이상
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
        }else {//18 이하
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
        final DriveListAdapter listAdapter = new DriveListAdapter(list);//리스트뷰 어뎁터
        driveList.setAdapter(listAdapter);//세팅
        driveList.setOnItemClickListener(new AdapterView.OnItemClickListener() {//클릭리스너
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent wiping = new Intent(MainActivity.this, Wiping.class);
                wiping.putExtra("DRIVE",(DriveListItem)listAdapter.getItem(i));
                startActivityForResult(wiping,0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //와이핑 성공/실패 반환값
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