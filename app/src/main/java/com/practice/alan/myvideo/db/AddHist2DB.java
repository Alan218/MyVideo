package com.practice.alan.myvideo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.practice.alan.myvideo.bean.MyDatas;
import com.practice.alan.myvideo.doit.Mylog;

/**
 * Created by Alan on 2017/9/21.
 */

public class AddHist2DB {
    static public void add(Context context, ContentValues values) {
        Mylog.e("AddHist2DB.执行add（）方法");
        String  userName,videoName,videoPath;
        userName = values.getAsString(MyDatas.HistColumns.USERNAME);
        videoName = values.getAsString(MyDatas.HistColumns.VIDEONAME);
        videoPath = values.getAsString(MyDatas.HistColumns.VIDEOPATH);


        SQLiteDatabase db = new MyDatabaseHelper(context).getReadableDatabase();
        Cursor cursor = db.query(MyDatas.Databases.HISTTBNAME, null,
                MyDatas.HistColumns.USERNAME + "=? and " + MyDatas.HistColumns.VIDEOPATH + "=?",
                new String[]{userName, videoPath}, null, null, null);

        if (cursor.moveToFirst()) {   //更新表格
            Mylog.e("更新历史表 videoPath-->" + videoPath + "  userName-->" + userName);
            db.update(MyDatas.Databases.HISTTBNAME, values, MyDatas.HistColumns.HISTID + "=?",
                    new String[]{cursor.getString(cursor.getColumnIndex(MyDatas.HistColumns.HISTID))});
        } else {                         //插入表格
            Mylog.e("插入历史表 videoName-->" + videoName + "  userName-->" + userName);
            db.insert(MyDatas.Databases.HISTTBNAME, null, values);
        }

    }
}