package com.practice.alan.myvideo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.practice.alan.myvideo.bean.MyDatas;
import com.practice.alan.myvideo.doit.Mylog;

/**
 * Created by Alan on 2017/9/21.
 */

public class CleanHist {
    public static void cleanAll(Context context, String userName) {
        Mylog.e("----------------CleanHist.cleanAll()-------------");
        SQLiteDatabase db = new MyDatabaseHelper(context).getWritableDatabase();
        db.delete(MyDatas.Databases.HISTTBNAME, MyDatas.HistColumns.USERNAME + " =?", new String[]{userName});
        db.close();
    }

    public static void cleanSingle(Context context, String userName, String videoPath) {
        Mylog.e("------CleanHist.cleanSingle()----userName->"+userName+ " videoPath-->"+videoPath);
        SQLiteDatabase db = new MyDatabaseHelper(context).getWritableDatabase();
        if(db.delete(MyDatas.Databases.HISTTBNAME, MyDatas.HistColumns.USERNAME + " =? and "
                + MyDatas.HistColumns.VIDEOPATH + "= ?", new String[]{userName, videoPath})>0){
            Toast.makeText(context, "删除成功！", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "删除失败！", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }
}
