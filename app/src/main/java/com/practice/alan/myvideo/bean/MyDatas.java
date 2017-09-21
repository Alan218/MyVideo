package com.practice.alan.myvideo.bean;

import android.os.Environment;

/**
 * Created by Alan on 2017/9/21.
 */

public class MyDatas {
    public static final String BROADCAST_EXIT = "com.wlz.broadcasttest.EXIT";
    public static final String DIRNAME = "MiniVideoLib";
    public static final String DIRPATH = Environment.getExternalStorageDirectory() + "/" + DIRNAME;

    public static class Preferences {
        public static final String PREFNAME = "minivideo_pref";
        public static final String USERID = "user_id";
        public static final String USERNAME = "user_name";
        public static final String NICKNAME = "nick_name";
        public static final String USERROLE = "user_role";
        public static final String VIPLIMIT = "vip_limit";
        public static final String USERHADE = "head";
        public static final String TODAY = "today";
    }

    public static class Databases {
        public static final String DBNAME = "MiniVideoDatabase";
        public static final int DBVERSION = 1;
        public static final String USERTBNAME = "user_info";
        public static final String VIDEOTBNAME = "video_info";
        public static final String HISTTBNAME = "video_hist";

    }

    public static class UserColumns {
        public static final String USERID = "user_id";
        public static final String USERNAME = "user_name";
        public static final String USERPWD = "user_pwd";
        public static final String NICKNAME = "nick_name";
        public static final String USERROLE = "user_role";
        public static final String VIPLIMIT = "vip_limit";
        public static final String HEAD = "head";
    }

    public static class UserRoleValues {
        public static final String ADMIN = "admin";
        public static final String NORMAL = "normal";
        public static final String VIP = "visitant";
    }

    public static class VideoColumns {
        public static final String VIDEOID = "video_id";
        public static final String VIDEONAME = "video_name";
        public static final String VIDEODURATION = "video_duration";
        public static final String VIDEOSIZE = "video_size";
        public static final String VIDEOPATH = "video_path";
        public static final String VIDEOLEVEL = "video_level";
        public static final String VIDEOTHUMBNAIL = "video_thumbnail";
        public static final String VIDEODATE = "video_date";

    }

    public static class VideoLeverValues {
        public static final String NORMALLEVEL = "0";
        public static final String VIPLEVEL = "1";
    }

    public static class HistColumns {
        public static final String VIDEOID = "video_id";
        public static final String HISTID = "hist_id";
        public static final String VIDEONAME = "video_name";
        public static final String USERID = "user_id";
        public static final String USERNAME = "user_name";
        public static final String VIDEODURATION = "video_duration";
        public static final String VIDEOPATH = "video_path";
        public static final String VIDEOSIZE = "video_size";
        public static final String VIDEOLEVEL = "video_level";
        public static final String VIDEOTHUMBNAIL = "video_thumbnail";
        public static final String HISTTIME = "hist_time";
        public static final String POSITION = "position";

    }

}