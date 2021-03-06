package com.koia.smartphonealldelete;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.text.DecimalFormat;

public class Wiping extends Activity {
    private long checkData = 0;
    private TextView stage, percentage, drive, comment;
    private ProgressBar bar;
    private Button stopBtn, returnBtn, turnoffBtn;
    private DriveListItem k;
    private Handler  m;

    private Thread th;
    private serviceThread wRunnable;
    private boolean work = true;

    private final int mode_Check = 0;
    private final int mode_BufferStart = 1;
    private final int mode_Buffer = 2;
    private final int mode_DeleteStart = 3;
    private final int mode_Delete = 4;
    private final int mode_Start = 5;
    private final int mode_Complete = 6;
    private final int mode_Stop = 7;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wiping);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        drive = findViewById(R.id.drive);
        stage = findViewById(R.id.stage);
        percentage = findViewById(R.id.percentage);
        comment = findViewById(R.id.comment);
        stopBtn = findViewById(R.id.stopBtn);
        returnBtn = findViewById(R.id.returnedmainBtn);
        turnoffBtn = findViewById(R.id.turnoffBtn);
        bar = findViewById(R.id.progress);

        k = (DriveListItem)getIntent().getSerializableExtra("DRIVE");
        drive.setText(k.getDriveName()+"\nWiping Storage : "+k.getFileSize( k.getDriveFreeSize()));
        bar.setProgress((int)(75 * ((double)checkData/(double)k.getDriveFreeSize())));

        m = new Handler(new Handler.Callback() {
            @Override
            public synchronized boolean handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case mode_Check:
                           checkData +=(int)msg.obj;
                            stage.setText(DriveListItem.getFileSize(checkData) + " 용량을 확인했습니다." );
                            percentage.setText(roundTwoDecimals(90 * (double)checkData/(double)k.getDriveFreeSize()) +"%");
                            bar.setProgress((int)(75  * 0.9 *((double)checkData/(double)k.getDriveFreeSize())));
                        break;
                    case mode_BufferStart:
                        comment.setText("대기 중...");
                        percentage.setText(roundTwoDecimals(90 ) +"%");
                        stage.setText("삭제를 위한 시스템 대기 중입니다.");
                        break;
                    case mode_Buffer:
                        percentage.setText(roundTwoDecimals(90 + ((int)msg.obj/(double)12)) + "%");
                        bar.setProgress((int)(75  * (0.9 + (int)msg.obj /(double)1200)));
                        break;
                    case mode_DeleteStart:
                        percentage.setText(roundTwoDecimals(95 ) +"%");
                        comment.setText("제거 중...");
                        checkData = 0;
                        break;
                    case mode_Delete:
                        checkData += (long)msg.obj;
                        stage.setText(DriveListItem.getFileSize(checkData) + " 제거 완료했습니다." );
                        percentage.setText(roundTwoDecimals(95 + 5 * (double)checkData/(double)k.getDriveFreeSize()) +"%");
                        bar.setProgress((int)(75  * (0.9 + 0.05 * ((double)checkData/(double)k.getDriveFreeSize()))));
                        break;
                    case mode_Start:
                        comment.setText("작업 중...");
                        start();
                        break;
                    case mode_Complete:
                        percentage.setText( "100%");
                        comment.setText("삭제 완료");
                        stage.setText("모든 데이터가 삭제되었습니다.");
                        complete();
                        break;
                    case mode_Stop:
                        comment.setText("중지 완료");
                        percentage.setText("중지");
                        stage.setText("시스템 중지를 완료하였습니다.");
                        bar.setProgress(75);
                }
                return true;
            }
        });

        wRunnable = new serviceThread();
        th = new Thread(wRunnable);
        th.start();
    }

    public double roundTwoDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
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
    public boolean deleteDir(File dir){
        if (dir != null && dir.isDirectory()){
            String[] children = dir.list();
            for (int i=0; i<children.length; i++){
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success){
                    return false;
                }
            }
        }
        m.sendMessage(m.obtainMessage(mode_Delete,dir.length()));
        return dir.delete();
    }

	private void start()
    {
        stopBtn.setClickable(true);
    }

    private void complete()
    {
        stopBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.wiping_button_off));
        returnBtn.setClickable(true);
        returnBtn.setVisibility(View.VISIBLE);
        turnoffBtn.setClickable(true);
        turnoffBtn.setVisibility(View.VISIBLE);
        returnBtn.startAnimation(AnimationUtils.loadAnimation(this,R.anim.wiping_scale_ani));
        turnoffBtn.startAnimation(AnimationUtils.loadAnimation(this,R.anim.wiping_scale_ani));
    }

    public void clickBtn(View view)
    {
        switch(view.getId())
        {
            case R.id.stopBtn:
                stopBtn.setClickable(false);
                work = false;// 작업 스레드 상황 종료
                try {
                    th.join();//작업 스레드 상황 종료 대기
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                m.sendEmptyMessage(mode_Stop);
                break;
            case R.id.returnedmainBtn:
                setResult(0);
                finish();
                break;
            case R.id.turnoffBtn:
                setResult(1);
                finish();
                break;
        }
    }

    //작업 스레드
    class serviceThread implements Runnable{
        private  Thread CTF[];
        private CreateTempFile[] r;
        long storageSize;
        private int[] size = {1024*1024, 1024};
        private int fileindex=0;

        @Override
        public void run() {
            storageSize = k.getDriveFreeSize();

            for(int i=0; work && i<size.length; i++)
                insert(size[i]);
            /* cache file 생성 시작 */

            /* cache file 생성 종료 */
            m.sendEmptyMessage(mode_BufferStart);
            for(int i=0;work && i<60;i++)
                try {
                    m.sendMessage(m.obtainMessage(mode_Buffer,i+1));
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
            }

            /* cache 파일 삭제함수 실행 */
            m.sendEmptyMessage(mode_DeleteStart);
            clearApplicationData();
            m.sendEmptyMessage(mode_Complete);
        }

        public void insert(int size) {
            CTF = new Thread[10];
            r = new CreateTempFile[10];
            m.sendEmptyMessage(mode_Start);
            int repeat = (int) (storageSize / (size * 1024));

            for (int count = 0; work && count < (repeat / 10); count++) {
                for (int i = 0; i < CTF.length; i++) {
                    r[i] = new CreateTempFile(fileindex++, size);
                    CTF[i] = new Thread(r[i]);
                    CTF[i].start();
                }

                for (int i = 0; i < CTF.length; i++) {
                    try {
                        CTF[i].join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(work) {
                storageSize = k.getDriveFreeSize() - checkData;
                repeat = (int) (storageSize / (size * 1024));
                CTF = new Thread[repeat];
                r = new CreateTempFile[repeat];

                for (int i = 0; i < CTF.length; i++) {
                    r[i] = new CreateTempFile(fileindex++, size);
                    CTF[i] = new Thread(r[i]);
                    CTF[i].start();
                }

                for (int i = 0; i < CTF.length; i++) {
                    try {
                        CTF[i].join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                storageSize = k.getDriveFreeSize() - checkData;
            }
        }

        public Thread[] getThread(){
            return CTF;
        }

        public CreateTempFile[] getR() {
            return r;
        }

    }

    //파일 생성 스레드
    class CreateTempFile implements Runnable{
        private int mode;
        private FileController cachefile;
        private int filenumber;

        public CreateTempFile (int inputnumber,int size){
            mode = size == 1024*1024 ? 0 : 1;
            cachefile = new FileController(inputnumber, mode == 0  ? 4 * size :  size * 1024, getApplicationContext());
            //GB 추가일 때 4메가씩, MB 추가일 시엔 1메가씩
            filenumber = inputnumber;
        }
        public void run(){
            int counter = 0;
            while(work && counter <  (mode == 0 ? 256  : 1)){
                cachefile.fillbuffer();
                cachefile.writedata();
                m.sendMessage(m.obtainMessage(mode_Check,cachefile.getBufferSize()));
                counter++;
            }
            cachefile.close();
        }

    }

    @Override
    public void onBackPressed() {
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
