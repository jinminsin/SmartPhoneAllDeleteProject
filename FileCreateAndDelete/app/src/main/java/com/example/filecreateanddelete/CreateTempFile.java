package com.example.filecreateanddelete;

import java.security.SecureRandom;

import static com.example.filecreateanddelete.MainActivity.intbuffer;

public class CreateTempFile implements Runnable{
    SecureRandom random = new SecureRandom();

    public void run(){
        random.nextBytes(intbuffer);
    }
}
