package com.example.autolancher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;

public class MainActivity extends Activity {
    //延时时间:分钟
    private long delayTime = 150;
    Handler mHandler = new Handler();

    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            //do something
            //每隔1s循环执行run方法
            Log.e("MainActivity= ","runnable..");
            AppUtils.startApp(MainActivity.this, Constant.wework);
            mHandler.postDelayed(this, 1000000000);
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        Button timer_tv = findViewById(R.id.timer_tv);
        EditText time_et = findViewById(R.id.time_et);

        Button qw_btn = findViewById(R.id.qw_btn);
        //延时
        qw_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                delayTime = Long.parseLong(time_et.getText().toString().trim());
                Log.e("MainActivity=","timer_tv onClick delayTime= "+delayTime);
                mHandler.removeCallbacks(runnable);
                mHandler.postDelayed(runnable,delayTime*60*1000);//延时 delayTime 分钟
            }
        });

        //定时
        timer_tv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(AppUtils.checkPackInfo(MainActivity.this,Constant.wework)) {
                    Intent intent = new Intent(MainActivity.this, ClockActivity.class);
                    intent.putExtra("type", 1);
                    startActivity(intent);
                }else {
                    Toast.makeText(MainActivity.this,"设备没有安装qw",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
