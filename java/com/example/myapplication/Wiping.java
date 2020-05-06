package com.example.myapplication;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Wiping extends Activity {
    private TextView drive;
    private ProgressBar bar;
    Button btn2;
    Button btn3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wiping);
        drive = findViewById(R.id.drive);

        bar = findViewById(R.id.progress);
        DriveListItem k = (DriveListItem) getIntent().getSerializableExtra("DRIVE");
        drive.setText(k.getDriveName() + "\n( " + k.getFileSize(k.getDriveFreeSize()) + " / " + k.getFileSize(k.getDriveFullSize()) + " )");
        bar.setMax(100);
        bar.setProgress((int) (100 * ((double) k.getDriveFreeSize() / (double) k.getDriveFullSize())));
    }




    public void onClickButton(View view) {
        Button button = (Button)findViewById(R.id.stop);
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.button_scale);
        button.startAnimation(myAnim);
        Button btn2 = (Button)findViewById(R.id.returnmain);
        Button btn3 = (Button)findViewById(R.id.turnoff);
        if(btn2.getVisibility()  == View.VISIBLE && btn3.getVisibility() == View.VISIBLE){
            btn2.setVisibility(View.INVISIBLE);
            btn3.setVisibility(View.INVISIBLE);
        }
        else {
            btn2.setVisibility(View.VISIBLE);
            btn3.setVisibility(View.VISIBLE);
        }
    }
}



