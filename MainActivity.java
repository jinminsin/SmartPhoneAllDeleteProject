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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //드라이브 리스트
        if(requestCode == 0) {
            if (resultCode == 0)//와이핑 시작
            {
                Intent wiping = new Intent(this,Wiping.class);
                wiping.putExtra("DRIVE",data.getSerializableExtra("DRIVE"));
                startActivityForResult(wiping,1);
            }

        }
        //wiping 이후 작동
        if(requestCode == 1) {
            // resultCode == 0  메인으로 돌아가기
            if (resultCode == 1)// -> 애플리케이션 종료
            {
                    finish();
            }
        }
    }

    public void startWiping(View view) {
        Intent listItem = new Intent(MainActivity.this, DriveList.class);
        startActivityForResult(listItem,0);
    }
}
