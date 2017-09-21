package com.practice.alan.myvideo.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import com.practice.alan.myvideo.bean.MyDatas;
import com.practice.alan.myvideo.bean.VideoBean;
import com.practice.alan.myvideo.doit.ImageDeal;
import com.practice.alan.myvideo.doit.Mylog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alan on 2017/9/21.
 */

public class VideoDB2List {
    public static List<VideoBean> scan(Context context) {
        List<VideoBean> videoList = new ArrayList<>();
        SQLiteDatabase db = new MyDatabaseHelper(context).getReadableDatabase();
        Cursor cursor = db.query(MyDatas.Databases.VIDEOTBNAME, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                try {
                    Mylog.e("cursor.getCount()" + cursor.getCount() + "  " + cursor.getColumnCount());
                    int videoId = cursor.getInt(cursor.getColumnIndex(MyDatas.VideoColumns.VIDEOID));
                    String videoName = cursor.getString(cursor.getColumnIndex(MyDatas.VideoColumns.VIDEONAME));
                    String videoSize = cursor.getString(cursor.getColumnIndex(MyDatas.VideoColumns.VIDEOSIZE));
                    String videoPath = cursor.getString(cursor.getColumnIndex(MyDatas.VideoColumns.VIDEOPATH));
                    String videoLevel = cursor.getString(cursor.getColumnIndex(MyDatas.VideoColumns.VIDEOLEVEL));
                    Bitmap videoThumbnail = ImageDeal.String2Bitmap(cursor.getString(cursor.getColumnIndex(MyDatas.VideoColumns.VIDEOTHUMBNAIL)));
                    String videoDuration = cursor.getString(cursor.getColumnIndex(MyDatas.VideoColumns.VIDEODURATION));

                    VideoBean videoBean = new VideoBean(videoId, videoName, videoDuration, videoSize, videoPath, videoLevel, videoThumbnail);
                    videoList.add(videoBean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());

        }
        return videoList;
    }
}