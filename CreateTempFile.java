package com.example.filecreateanddelete;

import java.util.concurrent.ThreadLocalRandom;

public class CreateTempFile implements Runnable{

    public void run(){
        for(int i=0; i<100; i++) {
            MainActivity.data = MainActivity.data + Integer.toString(ThreadLocalRandom.current().nextInt(100));
        }
    }
}
