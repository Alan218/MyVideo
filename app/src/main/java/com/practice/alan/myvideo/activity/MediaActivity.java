package com.practice.alan.myvideo.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.practice.alan.myvideo.R;
import com.practice.alan.myvideo.bean.MyDatas;
import com.practice.alan.myvideo.db.AddHist2DB;
import com.practice.alan.myvideo.doit.Mylog;
import com.practice.alan.myvideo.doit.SimpleUtils;

import java.util.Calendar;
import java.util.Date;

public class MediaActivity extends AppCompatActivity {

    private SurfaceView surfaceview;
    private Button videoStartOrPause;
    private SeekBar seekBar;
    private TextView tvTime, totalTime; //总时长
    private MediaPlayer mediaPlayer;
    private LinearLayout ll_control;
    private int currentPosition = 0;

    private Handler myHandler;
    private String videoName, videoLevel, videoPath, videoThumbnail, videoSize;
    private int videoId;
    private int videoDuration;
    private boolean isControlShow = false;
    private GestureDetector gestureDetector;

    private final static int UPDATE_TIME_CODE = 100;
    private final static int HIDE_DISPLAY_CONTROL = 500;
    private final static int TO_PLAY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mylog.e("MediaActivity--onCreate()");
        setContentView(R.layout.activity_media);
        findViewById();
        initView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pausePlay();
    }

    private void initView() {
        if (currentPosition == 0) {
            currentPosition = getIntent().getIntExtra("currentPosition", 0);
        }
        Mylog.e("MediaActivity--initView");
        Intent intent = this.getIntent();
        videoPath = intent.getStringExtra("videoPath");
        videoId = intent.getIntExtra("videoId", -1);
        videoName = intent.getStringExtra("videoName");
        videoLevel = intent.getStringExtra("videoLevel");
        videoSize = intent.getStringExtra("videoSize");

        videoThumbnail = SimpleUtils.getThumbnailStr(videoPath);

        mediaPlayer = new MediaPlayer();

        surfaceview.getHolder().setKeepScreenOn(true);
        surfaceview.getHolder().addCallback(new SurfaceViewLis());
        videoStartOrPause.setVisibility(View.VISIBLE);
        videoStartOrPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Mylog.e("videoStartOrPause被按下");
                if (mediaPlayer.isPlaying()) {
                    pausePlay();
                } else {
                    startPlay();
                }
            }

        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvTime.setText(SimpleUtils.stringForTimeMills(seekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                myHandler.removeMessages(HIDE_DISPLAY_CONTROL);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
                showControl();
            }
        });
        myHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case UPDATE_TIME_CODE:
                        currentPosition=mediaPlayer.getCurrentPosition();
                        tvTime.setText(SimpleUtils.stringForTimeMills(currentPosition));
                        break;
                    case TO_PLAY:
                        videoPlay(currentPosition);
                        break;
                    case HIDE_DISPLAY_CONTROL:
                        hideControl();
                        break;
                }
            }
        };
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                Mylog.e("单击");
                if (isControlShow) {
                    hideControl();
                } else {
                    showControl();
                }
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                Mylog.e("长按");
                super.onLongPress(e);
                if (mediaPlayer.isPlaying()) {
                    pausePlay();
                } else {
                    startPlay();
                }
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                Mylog.e("双击");
                super.onDoubleTap(e);
                return true;
            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);
        return true;
    }

    private void startPlay() {
        mediaPlayer.start();
        videoStartOrPause.setBackgroundResource(R.drawable.ic_pause_stat);
    }

    private void pausePlay() {
        mediaPlayer.pause();
        videoStartOrPause.setBackgroundResource(R.drawable.ic_play_stat);
    }


    private void findViewById() {
        surfaceview = (SurfaceView) findViewById(R.id.surface);
        videoStartOrPause = (Button) findViewById(R.id.videoStartOrPause);
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        tvTime = (TextView) findViewById(R.id.positionTime);
        totalTime = (TextView) findViewById(R.id.totalTime);
        ll_control = (LinearLayout) findViewById(R.id.ll_control);

    }

    //隐藏 显示控制栏
    private void hideControl() {
        ll_control.setVisibility(View.INVISIBLE);
//        full(true);
        isControlShow = false;
    }

    private void showControl() {
        myHandler.removeMessages(HIDE_DISPLAY_CONTROL);
        ll_control.setVisibility(View.VISIBLE);
//        full(false);
        isControlShow = true;
        myHandler.sendEmptyMessageDelayed(HIDE_DISPLAY_CONTROL, 5000);
    }

    private class SurfaceViewLis implements SurfaceHolder.Callback {

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            // 创建SurfaceHolder的时候，如果存在上次播放的位置，则按照上次播放位置进行播放
            videoPlay(currentPosition);
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // 销毁SurfaceHolder的时候记录当前的播放位置并停止播放
            Mylog.e("执行surfaceDestroyed（）方法");
            currentPosition = mediaPlayer.getCurrentPosition();
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }
            SharedPreferences sp = getSharedPreferences(MyDatas.Preferences.PREFNAME, 0);

            ContentValues values = new ContentValues();
            values.put(MyDatas.HistColumns.USERID, sp.getInt(MyDatas.Preferences.USERID, -1));
            values.put(MyDatas.HistColumns.USERNAME, sp.getString(MyDatas.Preferences.USERNAME, null));
            values.put(MyDatas.HistColumns.VIDEOID, videoId);
            values.put(MyDatas.HistColumns.VIDEONAME, videoName);
            values.put(MyDatas.HistColumns.VIDEOPATH, videoPath);
            values.put(MyDatas.HistColumns.VIDEOLEVEL, videoLevel);
            values.put(MyDatas.HistColumns.POSITION, currentPosition);
            values.put(MyDatas.HistColumns.VIDEOTHUMBNAIL, videoThumbnail);

            Date date = Calendar.getInstance().getTime();
            String histTime = SimpleUtils.FormatDateTime(date);

            values.put(MyDatas.HistColumns.HISTTIME, histTime);
            values.put(MyDatas.HistColumns.VIDEOSIZE, videoSize);
            values.put(MyDatas.HistColumns.VIDEODURATION, videoDuration);


            AddHist2DB.add(MediaActivity.this, values);

        }


    }

    private void videoPlay(final int currentPosition) {
        // 获取视频文件地址
        try {
            mediaPlayer = new MediaPlayer();
            //设置音频流类型
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            // 设置播放的视频源
            mediaPlayer.setDataSource(videoPath);
            // 设置显示视频的SurfaceHolder
            mediaPlayer.setDisplay(surfaceview.getHolder());//这一步是关键，制定用于显示视频的SurfaceView对象（通过setDisplay（））
            //默认隐藏控制栏
            hideControl();
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    Mylog.e("  ----onPrepared()--------");
                    full(true);
                    //                    mediaPlayer.start();    // 按照初始位置播放
                    mediaPlayer.seekTo(currentPosition);
                    startPlay();
                    // 设置进度条的最大进度为视频流的最大播放时长
                    videoDuration = mediaPlayer.getDuration();
                    seekBar.setMax(videoDuration);
                    totalTime.setText("/" + SimpleUtils.stringForTimeMills(mediaPlayer.getDuration()));
                    // 开始线程，更新进度条的刻度
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                while (mediaPlayer.isPlaying()) {
                                    int current = mediaPlayer.getCurrentPosition();
                                    seekBar.setProgress(current);
                                    myHandler.sendEmptyMessage(UPDATE_TIME_CODE);
                                    sleep(500);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    // 在播放完毕被回调
                }
            });

            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    // 发生错误重新播放
                    videoPlay(currentPosition);
                    Mylog.e("mediaPlayer.onError()");
                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        seekBar.destroyDrawingCache();
        ll_control.destroyDrawingCache();
    }

    /**
     * 控制状态栏 隐藏（全屏）/显示
     *
     * @param enable
     */
    private void full(boolean enable) {
        if (enable) {

            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(lp);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {

            WindowManager.LayoutParams attr = getWindow().getAttributes();
            attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(attr);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

}
