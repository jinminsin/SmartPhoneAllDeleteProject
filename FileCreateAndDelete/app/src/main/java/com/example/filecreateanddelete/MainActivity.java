package com.example.filecreateanddelete;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Random random = new Random();
        String filename;
        String data;

        for (int count=0; count<100; count++) {
            filename = "TestFile"+count;
            data = Integer.toString(random.nextInt());

            try {
                File cacheDir = getCacheDir();
                File cacheFile = new File(cacheDir.getAbsolutePath(), filename);
                FileOutputStream FOS = new FileOutputStream(cacheFile.getAbsolutePath());
                FOS.write(data.getBytes());
                Log.d("test", "File " + filename + " CREATE");
                FOS.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        clearApplicationData();
    }

    public void clearApplicationData(){
        File cache = getCacheDir();
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
}
