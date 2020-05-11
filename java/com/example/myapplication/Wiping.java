package com.example.myapplication;

import android.app.Activity;
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

import com.example.myapplication.CreateTempFile;
import com.example.myapplication.DriveListItem;
import com.example.myapplication.R;

import org.w3c.dom.Text;

import java.io.File;

public class Wiping extends Activity {
    private TextView drive, comment;
    private ProgressBar bar;
    private Button stopBtn, returnBtn, turnoffBtn;
    private DriveListItem k;
    private Handler m;

    private Thread th;
    private serviceThread wRunnable;
    private boolean work = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wiping);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        drive = findViewById(R.id.drive);
        stopBtn = findViewById(R.id.stopBtn);
        returnBtn = findViewById(R.id.returnedmainBtn);
        turnoffBtn = findViewById(R.id.turnoffBtn);
        bar = findViewById(R.id.progress);
        comment = findViewById(R.id.comment);
        k = (DriveListItem) getIntent().getSerializableExtra("DRIVE");
        drive.setText(k.getDriveName() + "\n( " + k.getFileSize(k.getDriveFreeSize()) + " / " + k.getFileSize(k.getDriveFullSize()) + " )");
        bar.setProgress((int) (75 * ((double) k.getDriveFreeSize() / (double) k.getDriveFullSize())));

        m = new Handler(new Handler.Callback() {
            @Override
            public synchronized boolean handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case 0:
                        k.setDriveFreeSize(k.getDriveFreeSize() - (int) msg.obj);
                        drive.setText(k.getDriveName() + "\n( " + k.getFileSize(k.getDriveFreeSize()) + " / " + k.getFileSize(k.getDriveFullSize()) + " )");
                        bar.setProgress((int) (75 * ((double) k.getDriveFreeSize() / (double) k.getDriveFullSize())));
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

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            int count = 0;

            @Override
            public void run() {
                count++;
                if(count == 1){
                    comment.setText("용량을 확인하고 있습니다.");
                } else if(count == 2) {
                    comment.setText("용량을 확인하고 있습니다..");
                } else if(count == 3) {
                    comment.setText("용량을 확인하고 있습니다...");
                }
                if(count == 3)
                    count = 0;

                handler.postDelayed(this, 2 * 300);
            }
        };
        handler.postDelayed(runnable, 1 * 300);

        wRunnable = new serviceThread();
        th = new Thread(wRunnable);
        th.start();
    }

    /* cache 파일 삭제 함수 */
    public void clearApplicationData() {
        File cache = getExternalCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib") && !s.equals("files")) {
                    deleteDir(new File(appDir, s));
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        Log.d("test", "File " + dir.getName() + " DELETE " + DriveListItem.getFileSize(dir.length()));
        return dir.delete();
    }

    private void start() {
        stopBtn.setClickable(true);
    }

    private void complete() {
        stopBtn.setClickable(false);
        stopBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.wiping_button_off));
        returnBtn.setClickable(true);
        returnBtn.setVisibility(View.VISIBLE);
        turnoffBtn.setClickable(true);
        turnoffBtn.setVisibility(View.VISIBLE);
        returnBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.wiping_scale_ani));
        turnoffBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.wiping_scale_ani));
    }

    public void clickBtn(View view) {
        switch (view.getId()) {
            case R.id.stopBtn:
                work = false;// 작업 스레드 상황 종료
                for (int i = 0; i < wRunnable.getR().length; i++) {
                    if ((wRunnable.getThread())[i].isAlive())
                        (wRunnable.getR())[i].setWork(false); // 작업 스레드 내부 스레드 상황 종료
                }
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

    class serviceThread implements Runnable {
        private Thread CTF[];
        private CreateTempFile[] r;

        @Override
        public void run() {
            /* cache file 생성 시작 */
            CTF = new Thread[10];
            r = new CreateTempFile[10];
            long beforeTime = System.currentTimeMillis();
            m.sendEmptyMessage(1);
            for (int count = 0; work && count < 1; count++) {
                for (int i = 0; work && i < CTF.length; i++) {
                    r[i] = new CreateTempFile(count * 10 + i, getApplicationContext(), m);
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
            long afterTime = System.currentTimeMillis();
            long secDiffTime = (afterTime - beforeTime) / 1000;
            Log.d("test", "실행시간 : " + secDiffTime + "sec");
            /* cache file 생성 종료 */

            /* cache 파일 삭제함수 실행 */
            clearApplicationData();
            m.sendEmptyMessage(2);
        }

        public Thread[] getThread() {
            return CTF;
        }

        public CreateTempFile[] getR() {
            return r;
        }

    }

}
