package com.example.filecreateanddelete;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;

public class Wiping extends Activity {
    private TextView drive;
    private ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wiping);

        drive = findViewById(R.id.drive);
        bar = findViewById(R.id.progress);

        DriveListItem k = (DriveListItem)getIntent().getSerializableExtra("DRIVE");
        drive.setText(k.getDriveName()+"\n( "+k.getFileSize(k.getDriveFreeSize())+" / "+k.getFileSize(k.getDriveFullSize())+" )");
        bar.setMax(100);
        bar.setProgress((int)(100 * ((double)k.getDriveFreeSize()/(double)k.getDriveFullSize())));

        /* cache file 생성 시작 */
        Thread CTF[] = new Thread[10];
        long beforeTime = System.currentTimeMillis();
        for(int count = 0; count<1; count++){
            for(int i=0; i<CTF.length; i++){
                CTF[i] = new Thread(new CreateTempFile(count*10 + i, this));
                CTF[i].start();
            }
            for(int i=0; i<CTF.length; i++){
                try{
                    CTF[i].join();
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
        long afterTime = System.currentTimeMillis();
        long secDiffTime = (afterTime - beforeTime)/1000;
        Log.d("test", "실행시간 : " + secDiffTime + "sec");
        /* cache file 생성 종료 */

        /* cache 파일 삭제함수 실행 */
        clearApplicationData();

    }

    /* cache 파일 삭제 함수 */
    public void clearApplicationData(){
        File cache = getExternalCacheDir();
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
    /* 여기까지가 삭제함수 */
	
/* 진행상황 표기 방법
 0~90% ( 임시파일 생성 )
 91~95% ( 대기 )
 96~성공 ( 데이터 삭제 )  */

/* 
데이터 형식
와이핑 -> 스레드 하나 종료할 때마다 파일의 데이터가 인식됨(파일스트림 종료 및 생성) -> 스레드 도중에 파일을 생성하기 때문에 동기화 필요
-> 어떻게? 멀티스레드를 동적할당 방식으로 고려 -> 하나 종료될 때마다 % 증가, 혹은 핸들러를 이용하여 파일 입력을 하나 할 때마다 % 증가 ( 성능상 비효율을 고려하면 전자가 나음 )
*/
}
