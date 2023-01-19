package com.example.autolancher;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class AppUtils {

    public static void startApp(Context context, String packname) {
        PackageManager packageManager = context.getPackageManager();//获取设备应用信息
        Intent intent = packageManager.getLaunchIntentForPackage(packname);//唤起app
        context.startActivity(intent);
    }

    // 检查包名是否存在
    public static boolean checkPackInfo(Context context, String packname) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packname, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo != null;
    }

}
