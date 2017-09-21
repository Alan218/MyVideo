package com.practice.alan.myvideo.activity;

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
import android.widget.Toast;

import com.practice.alan.myvideo.R;
import com.practice.alan.myvideo.adapter.VideoManageAdapter;
import com.practice.alan.myvideo.bean.VideoBean;
import com.practice.alan.myvideo.db.VideoDB2List;
import com.practice.alan.myvideo.db.VideoFiles2DB;
import com.practice.alan.myvideo.doit.VideoEditor;

import java.util.List;

public class VideoManageActivity extends AppCompatActivity {
    private ListView mVideoManageListView;
    private boolean mIsCheckable = false;
    private VideoManageAdapter mVideoManageAdapter;
    private List<VideoBean> mVideoList;
    private ActionBar actionBar;
    private Handler myHandler;
    private ProgressBar progressBar;
    private static final int UPDATE_VIDEO_LIST = 0;
    private static final int UPDATE_VIDEO_LIST_STOP = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("视频管理");
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);

        }
        setContentView(R.layout.activity_video_manage);
        mVideoManageListView = (ListView) findViewById(R.id.videoManageListView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        initView();

    }

    private void initView() {
        mVideoList = VideoDB2List.scan(VideoManageActivity.this);
        mVideoManageAdapter = new VideoManageAdapter(VideoManageActivity.this, mVideoList, mIsCheckable);
        mVideoManageListView.setAdapter(mVideoManageAdapter);
        myHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case UPDATE_VIDEO_LIST:
                        progressBar.setVisibility(View.VISIBLE);
                        progressBar.setIndeterminate(false);
                        break;
                    case UPDATE_VIDEO_LIST_STOP:
                        progressBar.setVisibility(View.GONE);
                        reloadList(false);
                        Toast.makeText(VideoManageActivity.this, "更新完成！", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        };
        mVideoManageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        mVideoManageListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mIsCheckable) {
                    reloadList(false);
                } else {
                    reloadList(true);
                }
                return false;
            }
        });
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 100, R.string.VideoList_Update);
        menu.add(0, 2, 200, "设为VIP视频");
        menu.add(0, 3, 300, "设为普通视频");
        menu.add(0, 4, 400, "删除视频");
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (1 == item.getItemId()) {//更新视频
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    myHandler.sendEmptyMessage(UPDATE_VIDEO_LIST);
                    VideoFiles2DB.load(VideoManageActivity.this);
                    myHandler.sendEmptyMessage(UPDATE_VIDEO_LIST_STOP);
                }
            }.start();

        } else if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        } else if (mIsCheckable) {
            List<VideoBean> checkedVideos = mVideoManageAdapter.getCheckedVideos();  //被选中的视频
            VideoEditor videoEditor = new VideoEditor(this, checkedVideos);

            if (2 == item.getItemId()) {   //设为VIP视频

                videoEditor.setVip();
                reloadList(false);
            } else if (3 == item.getItemId()) {   //设为普通视频
                videoEditor.setNormal();
                reloadList(false);
            } else if (4 == item.getItemId()) {    //删除视频
                videoEditor.delete();
                reloadList(false);
            }
            return super.onOptionsItemSelected(item);
        } else {
            Toast.makeText(this, "请长按列表进选择要编辑的视频", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    public void reloadList(boolean isCheckable) {
        if (isCheckable) {
            mIsCheckable = true;
            mVideoManageAdapter.setIsCheckable(true);
            mVideoManageAdapter.notifyDataSetChanged();
            mVideoManageAdapter.initChecked();
        } else {
            mVideoManageAdapter.initChecked();
            mIsCheckable = false;
            mVideoManageAdapter.setIsCheckable(false);
            mVideoList = VideoDB2List.scan(VideoManageActivity.this);
            mVideoManageAdapter.setVideoList(mVideoList);
            mVideoManageAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {

        if (mIsCheckable) {
            mIsCheckable = !mIsCheckable;
            reloadList(false);
        } else {
            super.onBackPressed();
        }
    }

}