package com.practice.alan.myvideo.doit;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.widget.CheckBox;

import com.practice.alan.myvideo.bean.MyDatas;
import com.practice.alan.myvideo.bean.UserBean;
import com.practice.alan.myvideo.db.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alan on 2017/9/21.
 */

public class QueryUsers {
    public static List<UserBean> query(Context context) {
        List<UserBean> usersList=new ArrayList<>();
        SQLiteDatabase db=new MyDatabaseHelper(context).getReadableDatabase();;
        Cursor cursor = db.query(MyDatas.Databases.USERTBNAME, null, null, null, null, null, null);

        Mylog.e("搜索到所有用户数为"+cursor.getCount());
        while (cursor.moveToNext()) {
            int userId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(MyDatas.UserColumns.USERID)));
            String userName = cursor.getString(cursor.getColumnIndex(MyDatas.UserColumns.USERNAME));
//            Mylog.e("cursor搜索到"+userName);

            String userPwd = cursor.getString(cursor.getColumnIndex(MyDatas.UserColumns.USERPWD));
            String nickName = cursor.getString(cursor.getColumnIndex(MyDatas.UserColumns.NICKNAME));
            String userRole = cursor.getString(cursor.getColumnIndex(MyDatas.UserColumns.USERROLE));
            String vipLimit = cursor.getString(cursor.getColumnIndex(MyDatas.UserColumns.VIPLIMIT));
            String headStr= cursor.getString(cursor.getColumnIndex(MyDatas.UserColumns.HEAD));
            CheckBox checkBox=new CheckBox(context);
            Bitmap headBitmap= ImageDeal.String2Bitmap(headStr);

            UserBean user = new UserBean(userId,userName,userPwd,nickName,userRole,vipLimit,headBitmap);
            usersList.add(user);
        }
        return usersList;
    }
}