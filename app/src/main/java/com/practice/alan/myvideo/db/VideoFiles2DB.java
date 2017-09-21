package com.practice.alan.myvideo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.practice.alan.myvideo.bean.MyDatas;
import com.practice.alan.myvideo.doit.Mylog;
import com.practice.alan.myvideo.doit.SimpleUtils;

import java.io.File;

import static com.practice.alan.myvideo.bean.MyDatas.DIRPATH;

/**
 * Created by Alan on 2017/9/21.
 */

public class VideoFiles2DB {
    public static void load(Context context) {



        File file = new File(DIRPATH);
        Mylog.e(file.listFiles()+"");
        if (!file.exists()) {
            Mylog.e( "MiniVideoLib路径不存在-->"+DIRPATH);
            return;
        } else {
            File[] files = file.listFiles();
            Mylog.e(files.length+"");

            for (File f : files) {
                Mylog.e("f.name-->"+f.getName());
                if (f.getName().lastIndexOf(".mp4") > 0
                        || f.getName().lastIndexOf(".3gp") > 0
                        || f.getName().lastIndexOf(".wmv") > 0
                        || f.getName().lastIndexOf(".ts") > 0
                        || f.getName().lastIndexOf(".rmvb") > 0
                        || f.getName().lastIndexOf(".mov") > 0
                        || f.getName().lastIndexOf(".m4v") > 0
                        || f.getName().lastIndexOf(".avi") > 0
                        || f.getName().lastIndexOf(".m3u8") > 0
                        || f.getName().lastIndexOf(".3gpp") > 0
                        || f.getName().lastIndexOf(".mkv") > 0
                        || f.getName().lastIndexOf(".flv") > 0
                        || f.getName().lastIndexOf(".divx") > 0
                        || f.getName().lastIndexOf(".f4v") > 0
                        || f.getName().lastIndexOf(".rm") > 0
                        || f.getName().lastIndexOf(".ram") > 0
                        || f.getName().lastIndexOf(".mpg") > 0
                        || f.getName().lastIndexOf(".ram") > 0
                        || f.getName().lastIndexOf(".ram") > 0
                        || f.getName().lastIndexOf(".asf") > 0) {

                    ContentValues values = new ContentValues();
                    String videoPath = f.getPath();
                    //自制缩略图
                    String videoThumbnail = SimpleUtils.getThumbnailStr(videoPath);
                    //            获取视频时长

                    String videoDuration = SimpleUtils.getDuration(videoPath)+"";//获取音频的时间
                    String videoSize = SimpleUtils.FormatFileSize(f.length());

                    values.put(MyDatas.VideoColumns.VIDEONAME, SimpleUtils.getFileNameNoEx(f.getName()));
                    values.put(MyDatas.VideoColumns.VIDEODURATION, videoDuration);
                    values.put(MyDatas.VideoColumns.VIDEOPATH, videoPath);
                    values.put(MyDatas.VideoColumns.VIDEOSIZE, videoSize);
                    values.put(MyDatas.VideoColumns.VIDEOTHUMBNAIL, videoThumbnail);
                    values.put(MyDatas.VideoColumns.VIDEOLEVEL, MyDatas.VideoLeverValues.NORMALLEVEL);
                    values.put(MyDatas.VideoColumns.VIDEODATE, f.lastModified());


                    //                    Mylog.e(values.toString());
                    SQLiteDatabase db = new MyDatabaseHelper(context).getWritableDatabase();
                    try {
                        db.insert(MyDatas.Databases.VIDEOTBNAME, null, values);
                    }catch (Exception e){

                    }
                }

            }
        }
    }
}