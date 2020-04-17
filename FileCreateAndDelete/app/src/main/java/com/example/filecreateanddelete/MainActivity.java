package com.example.filecreateanddelete;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.os.Build;
import android.os.Bundle;
import android.os.StatFs;
import android.widget.ListView;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

import android.util.Log;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private ListView driveList;
    public static final int BUF_SIZE = 4096;
    public static byte[] intbuffer = new byte[BUF_SIZE*1000];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* 쓰레기값의 cache file 생성 */
        String filename;
        Runnable CTF = new CreateTempFile();
        Thread[] CTFt = new Thread[1];

        long beforeTime = System.currentTimeMillis();
        for (int count=0; count<100; count++) {
            filename = "TestFile"+count;

            try {
                for (int i=0; i<1; i++) {
                    CTFt[i] = new Thread(CTF);
                    CTFt[i].start();
                }
                for (int i=0; i<1; i++) {
                    try {
                        CTFt[i].join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                File cacheDir = getExternalCacheDir();
                File cacheFile = new File(cacheDir.getAbsolutePath(), filename);
                FileOutputStream FOS = new FileOutputStream(cacheFile.getAbsolutePath());

                FOS.write(intbuffer);
                Log.d("test", "File " + filename + " CREATE");
                FOS.flush();
                FOS.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        /* cache file 생성 루틴 종료 */
        long afterTime = System.currentTimeMillis();
        long secDiffTime = (afterTime - beforeTime)/1000;
        Log.d("test", "실행시간 : " + secDiffTime + "sec");

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



        /* cache 파일 삭제함수 실행 */
        clearApplicationData();


    }

    /* cache 파일 삭제 함수 */
    public void clearApplicationData(){
        File cache = getExternalCacheDir();
        File appDir = new File(cache.getParent());
        if(appDir.exists()){
            String[] children = appDir.list();
            for (String s : children){
                if(!s.equals("lib") && !s.equals("files")){
                    deleteDir(new File(appDir, s));
                }
            }
        }
    }
    public static boolean deleteDir(File dir){
        if (dir != null && dir.isDirectory()){
            String[] children = dir.list();
            for (int i=0; i<children.length; i++){
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success){
                    return false;
                }
            }
        }
        Log.d("test","File "+dir.getName()+" DELETE");
        return dir.delete();
    }
    /* 여기까지가 삭제함수 */

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