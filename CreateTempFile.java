package com.koia.smartphonealldelete;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

public class CreateTempFile implements Runnable{
    private FileController cachefile;
    private int filenumber;
    private Handler m;
    private  boolean work = true;

    public CreateTempFile (int inputnumber, Context context, Handler m){
        cachefile = new FileController(inputnumber, context);
        filenumber = inputnumber;
        this.m = m;
    }
    public void run(){
        int counter = 0;
        while(work && counter < 256){
            cachefile.fillbuffer();
            cachefile.writedata();
            m.sendMessage(m.obtainMessage(0,cachefile.getBufferSize()));
            counter++;
        }

        Log.d("test", "File " + filenumber + " CREATE");
        cachefile.close();
    }

    public void setWork(boolean work){
        this.work = work;
    }
}