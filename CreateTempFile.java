package com.example.filecreateanddelete;

import android.content.Context;
import android.util.Log;

public class CreateTempFile implements Runnable{
    FileController cachefile;
    int filenumber;

    public CreateTempFile (int inputnumber, Context context){
        cachefile = new FileController(inputnumber, context);
        filenumber = inputnumber;
    }
    public void run(){
        int counter = 0;
        while(counter < 256){
            cachefile.fillbuffer();
            cachefile.writedata();
            counter++;
        }

        Log.d("test", "File " + filenumber + " CREATE");
        cachefile.close();
    }
}
