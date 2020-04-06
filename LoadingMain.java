package com.koia.smartphonealldelete;

import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class LoadingMain extends Activity {
    private ConstraintLayout loadingLayout;
    private TextView clickText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_main);

        loadingLayout=findViewById(R.id.loading);
        clickText=findViewById(R.id.clickable);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                clickText.setVisibility(View.VISIBLE);
                Animation startAnimation = AnimationUtils.loadAnimation(LoadingMain.this, R.anim.blink);
                clickText.startAnimation(startAnimation);
                loadingLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
            }
        }, 2000);
    }


}
