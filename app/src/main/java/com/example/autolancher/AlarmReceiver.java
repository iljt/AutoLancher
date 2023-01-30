package com.example.autolancher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int type = intent.getIntExtra("type", 1);
        if (type == 1) {
            Log.e("AlarmReceiver= ","onReceive");
            AppUtils.startApp(context, Constant.wework);
        } else if (type == 2) {
            AppUtils.startApp(context, "com.sina.weibo");
        }
    }
}
