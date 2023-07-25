package com.example.autolancher;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.List;

public class AppUtils {

    //根据包名启动App
    public static void startApp(Context context, String packname) {
        PackageManager packageManager = context.getPackageManager();//获取设备应用信息
        Intent intent = packageManager.getLaunchIntentForPackage(packname);//唤起app
        context.startActivity(intent);
    }

    public static void restartApp(Context context, String packname)    {
        Intent i = context.getPackageManager()
                .getLaunchIntentForPackage(packname);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(i);
    }


 /*   private void forceStopApp(Context context,String packageName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        try {
            am.forceStopPackage(packageName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/


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


    //url跳转pdd首页
    public  static void openPdd(Context context, String url) {
      //  boolean b = checkPackInfo(context, "com.xunmeng.pinduoduo");
     //   if (b) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                  //  intent.setPackage("com.xunmeng.pinduoduo");
                intent.setClassName("com.xunmeng.pinduoduo","com.xunmeng.pinduoduo.ui.activity.MainFrameActivity");
                context.startActivity(intent);
            }catch (Exception e){
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setPackage("com.xunmeng.pinduoduo");
                    context.startActivity(intent);
                }catch (Exception e3){
                    Log.e("e3= ",e3.getMessage());
                }
            }
      //  }
    }


    //通过url启动第三方app并跳转到url界面，按back按键从url界面返回到第三方界面的首页时，再点跳转url不会跳到url界面，会回到第三方app首页，所以还是用上面的方法
    public  static void launchApp(Context paramContext,String packageName,String url) {
        if (paramContext == null) {
            return;
        }
        try {
            Intent intent1 = paramContext.getPackageManager().getLaunchIntentForPackage(packageName);
            if(intent1!=null){
                intent1.setData(Uri.parse(url));
            }
            Intent intent2 = intent1;
            if (intent1 == null) {
                Intent intent = new Intent("android.intent.action.MAIN");
                intent.addCategory("android.intent.category.LAUNCHER");
                intent.setPackage(packageName);
                intent.setData(Uri.parse(url));
                List<ResolveInfo> list = paramContext.getPackageManager().queryIntentActivities(intent, 0);
                if (list != null && list.size() > 0 && list.get(0).activityInfo != null) {
                    String activityName = list.get(0).activityInfo.name;
                    intent.setClassName(packageName, activityName);
                } else {
                    //com.xunmeng.pinduoduo.ui.activity.MainFrameActivity 拼多多启动页
                    intent.setClassName(packageName, "com.xunmeng.pinduoduo.ui.activity.MainFrameActivity");
                }
                intent2 = intent;
            }

            try {
                paramContext.startActivity(intent2);
            } finally {
                paramContext = null;
            }
        }catch (Exception e){
            Log.e("e= ",e.getMessage());
        }


    }

}
