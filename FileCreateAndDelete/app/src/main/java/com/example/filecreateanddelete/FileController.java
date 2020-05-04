package com.example.filecreateanddelete;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.security.SecureRandom;

public class FileController {
    FileOutputStream FOS;
    File cacheDir;
    File cacheFile;
    SecureRandom random = new SecureRandom();

    /* current buffer size = 16KB
    해당 버퍼의 크기만큼 한 파일에 랜덤데이터 256회 생성 후 입력.
    current file size = 40MB */
    byte[] buffer = new byte[4*1024*40];

    public FileController(int inputnumber, Context context)
    {
        /* cache file 생성 경로 설정 */
        cacheDir = context.getExternalCacheDir();
        cacheFile = new File(cacheDir.getAbsolutePath(), "File " + inputnumber);
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
