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
    private long stageData = 0;
    private TextView stage;
    private TextView percentage;
    private TextView drive;
    private ProgressBar bar;
    private Button stopBtn, returnBtn, turnoffBtn;
    private DriveListItem k;
    private Handler  m;

    private Thread th;
    private serviceThread wRunnable;
    private boolean work = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wiping);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        drive = findViewById(R.id.drive);
        stage = findViewById(R.id.stage);
        percentage = findViewById(R.id.percentage);
        stopBtn = findViewById(R.id.stopBtn);
        returnBtn = findViewById(R.id.returnedmainBtn);
        turnoffBtn = findViewById(R.id.turnoffBtn);
        bar = findViewById(R.id.progress);

        k = (DriveListItem)getIntent().getSerializableExtra("DRIVE");
        drive.setText(k.getDriveName()+"\n( "+k.getFileSize(k.getDriveFullSize() - k.getDriveFreeSize())+" / "+k.getFileSize(k.getDriveFullSize())+" )");
        bar.setProgress((int)(75 * ((double)stageData/(double)k.getDriveFreeSize())));

        m = new Handler(new Handler.Callback() {
            @Override
            public synchronized boolean handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case 0:
                            stageData +=(int)msg.obj;
                            stage.setText(DriveListItem.getFileSize(stageData) + " 용량을 확인했습니다." );
                            percentage.setText(roundTwoDecimals(90 * (double)stageData/(double)k.getDriveFreeSize()) +"%");
                            bar.setProgress((int)(75  * 0.9 *((double)stageData/(double)k.getDriveFreeSize())));
                        break;
                    case 1:
                        start();
                        break;
                    case 2:
                        complete();
                        break;

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
        Log.d("test","File "+dir.getName()+" DELETE " );
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
            long beforeTime = System.currentTimeMillis();

            for(int i=0;i<size.length;i++)
                insert(size[i]);
            /* cache file 생성 시작 */

            long afterTime = System.currentTimeMillis();
            long secDiffTime = (afterTime - beforeTime) / 1000;
            Log.d("test", "실행시간 : " + secDiffTime + "sec");
            /* cache file 생성 종료 */

            /* cache 파일 삭제함수 실행 */
            clearApplicationData();
            m.sendEmptyMessage(2);
        }

        public void insert(int size) {
            CTF = new Thread[10];
            r = new CreateTempFile[10];
            m.sendEmptyMessage(1);
            int repeat = (int) (storageSize / (size * 1024)); // 384 GB
            Log.d("반복", "" + repeat);

            for (int count = 0; work && count < (repeat / 10); count++) { // 38
                for (int i = 0; i < CTF.length; i++) {//10GB
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
                storageSize = k.getDriveFreeSize() - stageData;
                Log.d("스토리지", "" + DriveListItem.getFileSize(storageSize));
                repeat = (int) (storageSize / (size * 1024));//4GB
                Log.d("반복2", "" + repeat);
                CTF = new Thread[repeat];// 9gb
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
                storageSize = k.getDriveFreeSize() - stageData;
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
                m.sendMessage(m.obtainMessage(0,cachefile.getBufferSize()));
                counter++;
            }

            Log.d("test", "File " + filenumber + " CREATE");
            cachefile.close();
        }

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
