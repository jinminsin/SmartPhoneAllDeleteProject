package com.example.filecreateanddelete;

import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.StatFs;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class DriveList extends Activity {
    private TextView driveLength;
    private ListView driveList;
    private Popup dialog;
    private DriveListItem key_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive_list);

        driveList = findViewById(R.id.driveList);
        driveLength = findViewById(R.id.drivelength);
        ArrayList<DriveListItem> list = new ArrayList<>();

        File[] fDir;
        //안드로이드 버전 19 이상
        if (Build.VERSION.SDK_INT > 18) {
            fDir = getExternalFilesDirs(null);

            for (File d : fDir) {
                DriveListItem k = new DriveListItem();
                d = d.getParentFile().getParentFile().getParentFile().getParentFile();
                k.setDriveName(d.getName().equals("0") ? "내부저장소" : d.getName());
                k.setDrivePath(d.getAbsolutePath());
                k.setDriveFreeSize(checkStorageAllMemory(d.getAbsolutePath()) - checkAvailableMemory(d.getAbsolutePath()));
                k.setDriveFullSize(checkStorageAllMemory(d.getAbsolutePath()));

                list.add(k);
            }
        } else {//18 이하
            fDir = ContextCompat.getExternalFilesDirs(this, null);

            for (File d : fDir) {
                DriveListItem k = new DriveListItem();
                d = d.getParentFile().getParentFile().getParentFile().getParentFile();
                k.setDriveName(d.getName().equals("0") ? "내부저장소" : d.getName());
                k.setDrivePath(d.getAbsolutePath());
                k.setDriveFreeSize(checkStorageAllMemory(d.getAbsolutePath()) - checkAvailableMemory(d.getAbsolutePath()));
                k.setDriveFullSize(checkStorageAllMemory(d.getAbsolutePath()));

                list.add(k);
            }
        }

        final DriveListAdapter listAdapter = new DriveListAdapter(list);//리스트뷰 어뎁터
        driveList.setAdapter(listAdapter);//세팅
        driveList.setOnItemClickListener(new AdapterView.OnItemClickListener() {//클릭리스너
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               key_item = (DriveListItem)listAdapter.getItem(i);
               showAlertDialog();
            }
        });

        driveLength.setText("( 내부 : " + (listAdapter.getCount() > 0 ? 1 : 0) + " / 외부 : " + (listAdapter.getCount() > 1 ? listAdapter.getCount() - 1 : 0) + " )");
    }

    private long checkStorageAllMemory(String dir) {
        StatFs stat = new StatFs(dir);
        return Build.VERSION.SDK_INT > 18 ? stat.getTotalBytes() : stat.getBlockSize() * stat.getBlockCount();
    }

    private long checkAvailableMemory(String dir) {
        StatFs stat = new StatFs(dir);
        return Build.VERSION.SDK_INT > 18 ? stat.getAvailableBytes() : stat.getAvailableBlocks() * stat.getBlockCount();
    }

    private void showAlertDialog() {
        dialog = new Popup(this,okClick,noClick);
        dialog.show();
    }

    private View.OnClickListener okClick = new View.OnClickListener()
    {
        @Override
        public void onClick(View view) {
            getIntent().putExtra("DRIVE", key_item);
            setResult(0, getIntent());
            dialog.dismiss();
            finish();
        }
    };

    private View.OnClickListener noClick = new View.OnClickListener()
    {
        @Override
        public void onClick(View view) {
            dialog.dismiss();
            setResult(1);
            finish();
        }
    };
}
