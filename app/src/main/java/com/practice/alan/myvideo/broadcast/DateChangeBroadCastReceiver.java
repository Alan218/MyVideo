package com.practice.alan.myvideo.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.practice.alan.myvideo.bean.MyDatas;
import com.practice.alan.myvideo.doit.CheckRole;
import com.practice.alan.myvideo.doit.Mylog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Alan on 2017/9/21.
 */

public class DateChangeBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sp = context.getSharedPreferences(MyDatas.Preferences.PREFNAME, 0);
        SharedPreferences.Editor editor=sp.edit();
        Date today= Calendar.getInstance().getTime();
        String todayStr= new SimpleDateFormat("yyyy.MM.dd").format(today);
        editor.putString(MyDatas.Preferences.TODAY,todayStr);
        editor.commit();
        CheckRole.judge(context);
        Mylog.e("-------------DateChangeBroadCastReceiver. onReceive()执行--------------");
    }
}