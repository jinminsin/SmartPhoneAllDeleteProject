package com.example.filecreateanddelete;

import android.util.Log;

import java.security.SecureRandom;

import static com.example.filecreateanddelete.MainActivity.BUF_SIZE;
import static com.example.filecreateanddelete.MainActivity.intbuffer;
import static com.example.filecreateanddelete.MainActivity.threadcount;

public class CreateTempFile1 implements Runnable{
    SecureRandom random = new SecureRandom();
    int a;

    public void run(){
        for(; threadcount<BUF_SIZE; threadcount++) {
            a = random.nextInt();
            intbuffer[threadcount] = (byte)a;
            Log.d("test", "thread2 : " + a);
        }
    }
}
