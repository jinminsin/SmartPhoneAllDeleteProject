package com.example.filecreateanddelete;

import java.security.SecureRandom;

import static com.example.filecreateanddelete.MainActivity.BUF_SIZE;
import static com.example.filecreateanddelete.MainActivity.intbuffer;
import static com.example.filecreateanddelete.MainActivity.threadcount;

public class CreateTempFile implements Runnable{
    SecureRandom random = new SecureRandom();

    public void run(){
        for(; threadcount<BUF_SIZE; threadcount++) {
            intbuffer[threadcount] = (byte)random.nextInt();
        }
    }
}
