package com.practice.alan.myvideo.doit;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import com.practice.alan.myvideo.bean.MyDatas;
import com.practice.alan.myvideo.db.MyDatabaseHelper;

/**
 * Created by Alan on 2017/9/21.
 */

public class UpLoadHead {
    public static void  upLoad(Context context, String username, Bitmap headBitmap) {
        String headBitmapStr= ImageDeal.Bitmap2String(headBitmap);
        SQLiteDatabase db=new MyDatabaseHelper(context).getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(MyDatas.UserColumns.HEAD,headBitmapStr);
        db.update(MyDatas.Databases.USERTBNAME,contentValues,MyDatas.UserColumns.USERNAME+"= ?",new String[]{ username});
        SharedPreferences sp=context.getSharedPreferences(MyDatas.Preferences.PREFNAME,0);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString(MyDatas.UserColumns.HEAD,headBitmapStr);
        editor.apply();
    }
}