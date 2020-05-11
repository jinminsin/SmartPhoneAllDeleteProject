package com.koia.smartphonealldelete;
import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.security.SecureRandom;

public class FileController {
    private  FileOutputStream FOS;
    private File cacheDir;
    private  File cacheFile;
    private  SecureRandom random = new SecureRandom();

    /* current buffer size = 16KB
    해당 버퍼의 크기만큼 한 파일에 랜덤데이터 256회 생성 후 입력.
    current file size = 40MB */
    private byte[] buffer;
    // 16 * 1024 * 256

    public int getBufferSize(){
        return buffer.length;
    }

    public FileController(int inputnumber, int size, Context context)
    {
        buffer = new byte[4*size];
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
