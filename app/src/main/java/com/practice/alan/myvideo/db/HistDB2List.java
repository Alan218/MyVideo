package com.practice.alan.myvideo.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.practice.alan.myvideo.bean.MyDatas;
import com.practice.alan.myvideo.bean.VideoBean;
import com.practice.alan.myvideo.doit.ImageDeal;
import com.practice.alan.myvideo.doit.Mylog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alan on 2017/9/21.
 */

public class HistDB2List {
    public static List<VideoBean> query(Context context, String userName) {
        Mylog.e("HistDB2List--query()");
        List<VideoBean> videoList = new ArrayList<>();

        SQLiteDatabase db = new MyDatabaseHelper(context).getReadableDatabase();
        Cursor cursor = db.query(MyDatas.Databases.HISTTBNAME,
                null, MyDatas.HistColumns.USERNAME + " =?", new String[]{userName},
                null, null, MyDatas.HistColumns.HISTTIME + " desc");

        while (cursor.moveToNext()) {

            VideoBean video = new VideoBean();
            video.setVideoId(cursor.getInt(cursor.getColumnIndex(MyDatas.HistColumns.VIDEOID)));
            video.setVideoName(cursor.getString(cursor.getColumnIndex(MyDatas.HistColumns.VIDEONAME)));
            video.setVideoPosition(cursor.getString(cursor.getColumnIndex(MyDatas.HistColumns.POSITION)));
            video.setVideoDuration(cursor.getString(cursor.getColumnIndex(MyDatas.HistColumns.VIDEODURATION)));
            video.setVideoSize(cursor.getString(cursor.getColumnIndex(MyDatas.HistColumns.VIDEOSIZE)));
            video.setVideoPath(cursor.getString(cursor.getColumnIndex(MyDatas.HistColumns.VIDEOPATH)));
            video.setVideoLevel(cursor.getString(cursor.getColumnIndex(MyDatas.HistColumns.VIDEOLEVEL)));
            video.setVideoHistTime(cursor.getString(cursor.getColumnIndex(MyDatas.HistColumns.HISTTIME)));
            video.setVideoThumbnail(ImageDeal.String2Bitmap(cursor.getString(cursor.getColumnIndex(MyDatas.HistColumns.VIDEOTHUMBNAIL))));

            videoList.add(video);
        }

        return videoList;
    }
}