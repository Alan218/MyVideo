package com.practice.alan.myvideo.doit;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.practice.alan.myvideo.bean.MyDatas;
import com.practice.alan.myvideo.bean.VideoBean;
import com.practice.alan.myvideo.db.MyDatabaseHelper;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Alan on 2017/9/21.
 */

public class VideoEditor {
    private List<VideoBean> videoList;
    private Context context;

    public VideoEditor(Context context, List<VideoBean> videoList) {
        this.videoList = videoList;
        this.context = context;
    }

    public void delete() {
        SQLiteDatabase db = new MyDatabaseHelper(context).getWritableDatabase();
        Iterator it = videoList.iterator();
        while (it.hasNext()) {
            VideoBean video = (VideoBean) it.next();
            db.delete(MyDatas.Databases.VIDEOTBNAME, MyDatas.VideoColumns.VIDEOID + "=?", new String[]{video.getVideoId() + ""});
        }
    }

    public void setVip() {
        SQLiteDatabase db = new MyDatabaseHelper(context).getWritableDatabase();
        Iterator it = videoList.iterator();
        ContentValues values = new ContentValues();
        values.put(MyDatas.VideoColumns.VIDEOLEVEL, MyDatas.VideoLeverValues.VIPLEVEL);
        while (it.hasNext()) {
            VideoBean video = (VideoBean) it.next();
            db.update(MyDatas.Databases.VIDEOTBNAME, values, MyDatas.VideoColumns.VIDEOID + "=?", new String[]{video.getVideoId() + ""});
        }
    }

    public void setNormal() {
        SQLiteDatabase db = new MyDatabaseHelper(context).getWritableDatabase();
        Iterator it = videoList.iterator();
        ContentValues values = new ContentValues();
        values.put(MyDatas.VideoColumns.VIDEOLEVEL, MyDatas.VideoLeverValues.NORMALLEVEL);
        while (it.hasNext()) {
            VideoBean video = (VideoBean) it.next();
            db.update(MyDatas.Databases.VIDEOTBNAME, values, MyDatas.VideoColumns.VIDEOID + "=?", new String[]{video.getVideoId() + ""});
        }
    }


}