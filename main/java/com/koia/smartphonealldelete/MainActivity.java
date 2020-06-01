package com.koia.smartphonealldelete;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends Activity {
    private Popup dialog;

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
                if(resultCode == 0) {
                    Intent wiping = new Intent(this, Wiping.class);
                    wiping.putExtra("DRIVE", data.getSerializableExtra("DRIVE"));
                    startActivityForResult(wiping, 1);
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
            showAlertDialog();
    }

    private void showAlertDialog() {
        dialog = new Popup(this,0,okClick,noClick);
        dialog.show();
    }

    private View.OnClickListener okClick = new View.OnClickListener()
    {
        @Override
        public void onClick(View view) {
            dialog.dismiss();
            Intent listItem = new Intent(MainActivity.this, DriveList.class);
            startActivityForResult(listItem, 0);
        }
    };

    private View.OnClickListener noClick = new View.OnClickListener()
    {
        @Override
        public void onClick(View view) {
            dialog.dismiss();
            Intent help = new Intent(MainActivity.this, HelpContent.class);
            startActivity(help);
        }
    };
}
