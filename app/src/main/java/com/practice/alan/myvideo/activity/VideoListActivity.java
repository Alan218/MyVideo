package com.practice.alan.myvideo.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.practice.alan.myvideo.R;
import com.practice.alan.myvideo.adapter.VideoListAdapter;
import com.practice.alan.myvideo.bean.MyDatas;
import com.practice.alan.myvideo.bean.VideoBean;
import com.practice.alan.myvideo.db.VideoDB2List;
import com.practice.alan.myvideo.doit.Mylog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VideoListActivity extends AppCompatActivity {
    protected ListView videoListView;
    protected ProgressBar videoListProgressBar;
    protected ArrayList<HashMap<String, Object>> list;
    protected List<VideoBean> mVideoList = new ArrayList<>();
    protected ActionBar actionBar;
    protected Handler mHandler;
    protected static final int LIST_LOADING = 0x45245;
    protected static final int LIST_LOADED = 0x45258;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("视频列表");
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setContentView(R.layout.activity_video_list);
        videoListView = (ListView) findViewById(R.id.videoListView);
        videoListProgressBar = (ProgressBar) findViewById(R.id.videoListProgressBar);

        initData();

        videoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Mylog.e("点击Item view-->" + view + " i");
                VideoBean video = mVideoList.get(i);
                if (MyDatas.VideoLeverValues.VIPLEVEL.equals(video.getVideoLevel())) {
                    SharedPreferences sp = getSharedPreferences(MyDatas.Preferences.PREFNAME, 0);
                    String userRole = sp.getString(MyDatas.Preferences.USERROLE, null);
                    if (!(userRole.equals(MyDatas.UserRoleValues.VIP) || userRole.equals(MyDatas.UserRoleValues.ADMIN))) {
                        showDialog();
                        return;
                    }
                }
                Intent intent = new Intent(VideoListActivity.this, MediaActivity.class);
                String videoName = mVideoList.get(i).getVideoName();
                String videoPath = mVideoList.get(i).getVideoPath();
                intent.putExtra("videoPath", videoPath);
                intent.putExtra("videoName", videoName);
                intent.putExtra("videoId", mVideoList.get(i).getVideoId());
                intent.putExtra("videoLevel", mVideoList.get(i).getVideoLevel());
                intent.putExtra("videoSize", mVideoList.get(i).getVideoSize());

                if (new File(videoPath).exists()) {
                    startActivity(intent);
                } else {
                    new AlertDialog.Builder(VideoListActivity.this).setTitle("警告").setMessage("该视频不存在!")
                            .setNegativeButton("确定", null).create().show();
                }
            }
        });
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(VideoListActivity.this);  //先得到构造器
        builder.setTitle("提示"); //设置标题
        builder.setMessage("对不起，只有VIP用户可以观看该视频！"); //设置内容
        builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); //关闭dialog
            }
        });
        //参数都设置完成了，创建并显示出来
        builder.create().show();
    }

    private void initData() {
        Mylog.e("VideoList--initData");
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case LIST_LOADING:
                        videoListProgressBar.setVisibility(View.VISIBLE);
                        mVideoList = VideoDB2List.scan(VideoListActivity.this);
                        VideoListAdapter vlAdapter = new VideoListAdapter(VideoListActivity.this, mVideoList);
                        videoListView.setAdapter(vlAdapter);
                        videoListProgressBar.setVisibility(View.GONE);
                        break;

                    default:
                        break;
                }
            }
        };
        new Thread() {
            @Override
            public void run() {
                super.run();
                mHandler.sendEmptyMessage(LIST_LOADING);
            }
        }.start();
    }


    //menu 按钮
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 1, R.string.VideoList_Update);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                initData();
                break;
            case android.R.id.home:
                this.finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}