package com.example.autolancher;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;

public class MainActivity extends Activity {
    private  int brightnessValue =3;
    // 声明一个请求码用于权限回调
    private static final int REQUEST_CODE_WRITE_SETTINGS = 1;
    //延时时间:分钟
    private long delayTime = 150;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Intent i = getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage("com.example.autolancher");
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    Log.e("MainActivity= ","handleMessage..");
                   // AppUtils.restartApp(MainActivity.this,"com.example.autolancher");
                    break;
                default:
                    break;
            }
        }

    };

    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            //do something
            //每隔1s循环执行run方法
            Log.e("MainActivity= ","runnable..");
            AppUtils.startApp(MainActivity.this, Constant.wework);
            mHandler.postDelayed(this, 1000000000);
           // mHandler.postDelayed(runnable2, /*60*60**/5000);//x小时
        }
    };

    Runnable runnable2 = new Runnable() {

        @Override
        public void run() {
            //do something
            Log.e("MainActivity= ","runnable2..");
            finish();
            mHandler.sendEmptyMessageDelayed(1, 50);
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        Button turn_tv = findViewById(R.id.turn_tv);
        EditText time_et = findViewById(R.id.time_et);

        Button qw_btn = findViewById(R.id.qw_btn);
        //延时
        qw_btn.setOnClickListener(view -> {
            delayTime = Long.parseLong(time_et.getText().toString().trim());
            Log.e("MainActivity=","timer_tv onClick delayTime= "+delayTime);
            mHandler.removeCallbacks(runnable);
            mHandler.postDelayed(runnable,delayTime*60*1000);//延时 delayTime 分钟
        });

        //定时
        turn_tv.setOnClickListener(view -> {
           // AppUtils.launchApp(this,"com.xunmeng.pinduoduo","https://mobile.yangkeduo.com/promotion_op.html?type=5&id=193906&pid=35038664_253294389&_x_ddjb_dmn=%7B%22cpsSign%22%3A%22CM_230724_35038664_253294389_f099b0cc80444014b7e6eb9f6a741f47%22%2C%22id%22%3A%22193906%22%2C%22type%22%3A%225%22%7D&customParameters=%7B%22uid%22%3A%229587436266%22%2C%22buySrc%22%3A%222%22%7D&cpsSign=CM_230724_35038664_253294389_f099b0cc80444014b7e6eb9f6a741f47&_x_ddjb_act=%7B%22st%22%3A%223%22%7D&duoduo_type=2&launch_pdd=1&campaign=ddjb&cid=launch_");
            AppUtils.openPdd(this,"https://mobile.yangkeduo.com/promotion_op.html?type=5&id=193906&pid=35038664_253294389&_x_ddjb_dmn=%7B%22cpsSign%22%3A%22CM_230724_35038664_253294389_f099b0cc80444014b7e6eb9f6a741f47%22%2C%22id%22%3A%22193906%22%2C%22type%22%3A%225%22%7D&customParameters=%7B%22uid%22%3A%229587436266%22%2C%22buySrc%22%3A%222%22%7D&cpsSign=CM_230724_35038664_253294389_f099b0cc80444014b7e6eb9f6a741f47&_x_ddjb_act=%7B%22st%22%3A%223%22%7D&duoduo_type=2&launch_pdd=1&campaign=ddjb&cid=launch_");

            /*if(AppUtils.checkPackInfo(MainActivity.this,Constant.wework)) {
                Intent intent = new Intent(MainActivity.this, ClockActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
            }else {
                Toast.makeText(MainActivity.this,"设备没有安装qw",Toast.LENGTH_SHORT).show();
            }*/
        });

       // 检查是否有WRITE_SETTINGS权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.System.canWrite(this)) {
            // 请求WRITE_SETTINGS权限
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + this.getPackageName()));
            startActivityForResult(intent, REQUEST_CODE_WRITE_SETTINGS);
        } else {
            // 已有WRITE_SETTINGS权限，执行相关操作
            // ...
            setSystemBrightLight(brightnessValue);
        }

    }

    // 处理权限回调结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_WRITE_SETTINGS) {
            // 检查WRITE_SETTINGS权限是否被授予
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.System.canWrite(this)) {
                // WRITE_SETTINGS权限已被授予，执行相关操作
               setSystemBrightLight(brightnessValue);
            } else {
                // WRITE_SETTINGS权限被拒绝，执行相应处理
                // ...
                Log.e("onActivityResult ","没有设置WRITE_SETTINGS权限");
            }
        }
    }


    public void setSystemBrightLight(int brightnessValue){
        // 获取系统亮度设置的ContentResolver
        ContentResolver resolver = getContentResolver();
        // 获取当前系统亮度模式
        int mode = 0;
        try {
            mode = Settings.System.getInt(resolver, Settings.System.SCREEN_BRIGHTNESS_MODE);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        // 判断是否为自动调节亮度模式
        if (mode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
            // 如果是自动调节亮度模式，则先关闭自动调节亮度
            Settings.System.putInt(resolver, Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        }
        // 设置屏幕亮度值（0-255）
        Settings.System.putInt(resolver, Settings.System.SCREEN_BRIGHTNESS, brightnessValue);
        // 可选：通知系统亮度值已改变（需要WRITE_SETTINGS权限）
        Settings.System.putInt(resolver, Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        // 刷新屏幕亮度（更新显示）
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.screenBrightness = brightnessValue / 255f;
        getWindow().setAttributes(params);
        // 获取系统亮度设置的ContentResolver
        // 获取当前系统亮度值（0-255）
        try {
            brightnessValue = Settings.System.getInt(resolver, Settings.System.SCREEN_BRIGHTNESS);
            Log.e("brightnessValue= "," "+brightnessValue);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }

}
