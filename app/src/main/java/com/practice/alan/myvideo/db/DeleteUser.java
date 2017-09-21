package com.practice.alan.myvideo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.practice.alan.myvideo.bean.MyDatas;
import com.practice.alan.myvideo.bean.UserBean;
import com.practice.alan.myvideo.doit.Mylog;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Alan on 2017/9/21.
 */

public class DeleteUser {
    public static void delete(Context context, List<UserBean> mUsersList) {
        SQLiteDatabase db = new MyDatabaseHelper(context).getWritableDatabase();
        Iterator it = mUsersList.iterator();
        int num=0;
        while (it.hasNext()) {
            UserBean userBean = (UserBean) it.next();
            Mylog.e("迭代出用户-->" + userBean.getUserName());
            if (userBean.getUserRole().equals("admin")) {
                Toast.makeText(context, "管理员不可删除，已忽略！", Toast.LENGTH_SHORT).show();
            } else {
                num+=db.delete(MyDatas.Databases.USERTBNAME, MyDatas.UserColumns.USERNAME + "=?", new String[]{userBean.getUserName()});
            }
        }
        Toast.makeText(context, "删除"+num+"个用户！", Toast.LENGTH_SHORT).show();
    }

}