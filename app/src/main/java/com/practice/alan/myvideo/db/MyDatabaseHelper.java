package com.practice.alan.myvideo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.practice.alan.myvideo.bean.MyDatas;
import com.practice.alan.myvideo.doit.Mylog;

/**
 * Created by Alan on 2017/9/21.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {

    public MyDatabaseHelper(Context context) {
        super(context, MyDatas.Databases.DBNAME, null, MyDatas.Databases.DBVERSION);
    }

    public MyDatabaseHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = null;
        Mylog.e("执行MyDatabaseHelper 的onCreate()方法");

        //创建user_info表
        sql = "create table user_info(user_id integer primary key autoincrement,user_name unique,user_pwd,nick_name,user_role,vip_limit,head)";
        Mylog.e("创建表user_info-->" + sql);
        sqLiteDatabase.execSQL(sql);
        Mylog.e("创建表user_info完成");
        //执行db添加管理员操作
        sql = "insert into user_info values(null,'Admin','Video','管理员','admin','2999.01.01',null)";
        Mylog.e("执行db添加操作-->" + sql);
        sqLiteDatabase.execSQL(sql);
        Mylog.e("执行添加操作完成");
        //创建video_info表
        sql = "create table video_info(video_id integer primary key autoincrement , video_name ," +
                " video_duration ,video_size, video_path unique,video_level, video_thumbnail, video_date)";
        Mylog.e("创建video_info表  执行" + sql);
        sqLiteDatabase.execSQL(sql);
        Mylog.e("创建video_info表 完毕");

        sql = "create table video_hist(hist_id integer primary key autoincrement ,user_id ,user_name , video_id , video_name ," +
                " video_duration ,video_size, video_path ,video_level, video_thumbnail, hist_time , position)";
        Mylog.e("video_hist  执行" + sql);
        sqLiteDatabase.execSQL(sql);
        Mylog.e("创建video_hist表 完毕");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        int oldVersion = i;
        int newVersion = i1;
        System.out.println("-------onUpdate Called----------" + oldVersion + "--->" + newVersion);
    }
}