package com.koia.smartphonealldelete;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent loading = new Intent(MainActivity.this, LoadingMain.class);
        startActivity(loading);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //드라이드 리스트
        if(requestCode == 0) {
            if (resultCode == 0)//와이핑 시작
            {
                Intent wiping = new Intent(this,Wiping.class);
                wiping.putExtra("DRIVE",data.getSerializableExtra("DRIVE"));
                startActivityForResult(wiping,1);
            }

        }
        //와이핑 성공/실패 반환값
        if(requestCode == 1) {
            if (resultCode == 0)//성공
            {

            } else //실패
            {

            }
        }
    }

    public void startWiping(View view) {
        Intent listItem = new Intent(MainActivity.this, DriveList.class);
        startActivityForResult(listItem,0);
    }
}
