package com.example.myapplication;


import android.app.Activity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

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
    }

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
