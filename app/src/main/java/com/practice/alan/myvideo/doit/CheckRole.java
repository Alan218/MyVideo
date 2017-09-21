package com.practice.alan.myvideo.doit;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

import com.practice.alan.myvideo.bean.MyDatas;
import com.practice.alan.myvideo.db.MyDatabaseHelper;

/**
 * Created by Alan on 2017/9/21.
 */

public class CheckRole {

    public static void judge(Context context) {
        Mylog.e("CheckRole.judge()");
        SharedPreferences sp = context.getSharedPreferences(MyDatas.Preferences.USERNAME, 0);

        String userNname = sp.getString(MyDatas.Preferences.USERNAME, "");
        String userRole = sp.getString(MyDatas.Preferences.USERROLE, "");
        String today = sp.getString(MyDatas.Preferences.TODAY, "");
        String vipLimit = sp.getString(MyDatas.Preferences.VIPLIMIT, "");

        if (MyDatas.UserRoleValues.VIP.equals(userRole)) {
            if (today.compareTo(vipLimit) > 0) {  //今天超过VIP期限时间  将会员收回
                SQLiteDatabase db = new MyDatabaseHelper(context).getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(MyDatas.UserColumns.USERROLE, MyDatas.UserRoleValues.NORMAL);
                //更新为普通会员
                db.update(MyDatas.UserColumns.USERROLE, values, MyDatas.UserColumns.USERNAME + " =? ",
                        new String[]{userNname});
            }
        }


    }
}