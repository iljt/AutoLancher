package com.example.autolancher;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.annotation.Nullable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ClockActivity extends Activity {
    private TextView clock_tv;
    AlarmManager alarmManager = null;
    Calendar calendar = Calendar.getInstance();
    int type=1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_clock);
        clock_tv = findViewById(R.id.clock_tv);
        type = getIntent().getIntExtra("type", 1);
        if(type == 1){
            clock_tv.setText("拉起qw闹钟");
        }
        alarmManager=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
        setClock();
    }

    private void setClock(){
        Dialog dialog = new TimePickerDialog(ClockActivity.this,new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // TODO Auto-generated method stub
                Calendar c=Calendar.getInstance();              //获取日期对象
                c.setTimeInMillis(System.currentTimeMillis());  //设置Calendar对象
                c.set(Calendar.HOUR_OF_DAY, hourOfDay);         //设置闹钟小时数
                c.set(Calendar.MINUTE, minute);                 //设置闹钟的分钟数
                c.set(Calendar.SECOND, 0);                      //设置闹钟的秒数
                c.set(Calendar.MILLISECOND, 0);                 //设置闹钟的毫秒数

                String realTime = new SimpleDateFormat("HH:mm").format(c.getTime());
                clock_tv.setText(realTime);

                Intent intent = new Intent(ClockActivity.this, AlarmReceiver.class);
                intent.putExtra("type", type);
                // intent.setFlags(Integer.parseInt(id));//作为取消时候的标识
                PendingIntent pi = PendingIntent.getBroadcast(ClockActivity.this, 0,
                        intent, PendingIntent.FLAG_CANCEL_CURRENT);    //创建PendingIntent
                //设置一次性闹钟，第一个参数表示闹钟类型，第二个参数表示闹钟执行时间，第三个参数表示闹钟响应动作。
                if(c.getTimeInMillis() < System.currentTimeMillis()){
                    Log.e("clock", "设置时间要推迟24小时,不然立刻会响");
                    alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis()+24*60*60*1000, pi);
                }else{
                    alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);        //设置闹钟，当前时间就唤醒
                }

            }
        },calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false);
        dialog.show();
    }

}
