package com.slave_mk14.ssssalldelete;
import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.security.SecureRandom;

public class FileController {
    private  FileOutputStream FOS;
    private File cacheDir;
    private  File cacheFile;
    private  SecureRandom random = new SecureRandom();

    private byte[] buffer;

    public int getBufferSize(){
        return buffer.length;
    }

    public FileController(int numbering, int size, Context context)
    {
        buffer = new byte[size];
        /* cache file 생성 경로 설정 */
        cacheDir = context.getExternalCacheDir();
        cacheFile = new File(cacheDir.getAbsolutePath(), "File " + numbering);
        try {
            FOS = new FileOutputStream(cacheFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fillbuffer()
    {
        /* buffer size 만큼 random data 생성 */
        random.nextBytes(buffer);
    }

    public void close()
    {
        try {
            FOS.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writedata()
    {
        try {
            FOS.write(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
