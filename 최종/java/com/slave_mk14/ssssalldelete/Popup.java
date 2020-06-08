package com.slave_mk14.ssssalldelete;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class Popup extends Dialog {
    private TextView contentText;
    private Button mPositiveButton;
    private Button mNegativeButton;

    private View.OnClickListener mPositiveListener;
    private View.OnClickListener mNegativeListener;
    private int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);
        //셋팅
        mPositiveButton=findViewById(R.id.pButtonOk);
        mNegativeButton=findViewById(R.id.pButtonNo);
        contentText = findViewById(R.id.contentText);

        setCancelable(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //본문 내용 세팅
        if(mode == 0)
        {
            contentText.setText("경고, 이 애플리케이션은 공장초기화 이후 시행해야 확실한 효과를 볼 수 있습니다. \n공장 초기화를 하셨습니까?");
            mPositiveButton.setText("네");
            mNegativeButton.setText("도움말");
        }

        //클릭 리스너 셋팅
        mPositiveButton.setOnClickListener(mPositiveListener);
        mNegativeButton.setOnClickListener(mNegativeListener);
    }

    //생성자 생성
    public Popup(Context context,int mode , View.OnClickListener positiveListener, View.OnClickListener negativeListener) {
        super(context);
        this.mode = mode;
        this.mPositiveListener = positiveListener;
        this.mNegativeListener = negativeListener;
    }
}
