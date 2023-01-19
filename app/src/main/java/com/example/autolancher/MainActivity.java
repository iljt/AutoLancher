package com.example.autolancher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;

public class MainActivity extends Activity {

    Handler mHandler = new Handler();

    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            //do something
            //每隔1s循环执行run方法
            Log.e("MainActivity= ","runnable..");
            AppUtils.startApp(MainActivity.this, "com.tencent.wework");
            mHandler.postDelayed(this, 5000);
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        TextView qw_tv = findViewById(R.id.qw_tv);
        qw_tv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(AppUtils.checkPackInfo(MainActivity.this,"com.tencent.wework")) {
                    Intent intent = new Intent(MainActivity.this, ClockActivity.class);
                    intent.putExtra("type", 1);
                    startActivity(intent);
                }else {
                    Toast.makeText(MainActivity.this,"设备没有安装qw",Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button timer_tv = findViewById(R.id.timer_tv);
        timer_tv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.e("MainActivity=","timer_tv onClick");
                mHandler.postDelayed(runnable,70*60*1000);//延时100毫秒
            }
        });
    }

}
