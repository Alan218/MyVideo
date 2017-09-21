package com.practice.alan.myvideo.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.practice.alan.myvideo.R;
import com.practice.alan.myvideo.adapter.HistListAdapter;
import com.practice.alan.myvideo.bean.MyDatas;
import com.practice.alan.myvideo.bean.VideoBean;
import com.practice.alan.myvideo.db.CleanHist;
import com.practice.alan.myvideo.db.HistDB2List;
import com.practice.alan.myvideo.doit.Mylog;

import java.io.File;
import java.util.List;

public class HistoryListActivity extends AppCompatActivity {
    protected ListView historyListView;
    protected ActionBar actionBar;
    protected List<VideoBean> mVideoList;
    protected String userName;
    protected HistListAdapter mAdapter;
    protected static final int RELOAD_LIST_DELAY=0x123;
    protected static final int LOAD_LIST=0x1523;
    private Handler mHandler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RELOAD_LIST_DELAY:
                    mVideoList = HistDB2List.query(HistoryListActivity.this,userName);
                    mAdapter.setVideoList(mVideoList);
                    mAdapter.notifyDataSetChanged();
                    break;
                case LOAD_LIST:
                    userName = getSharedPreferences(MyDatas.Preferences.PREFNAME, 0).getString(MyDatas.Preferences.USERNAME, "");
                    mVideoList = HistDB2List.query(HistoryListActivity.this,userName);
                    mAdapter = new HistListAdapter(HistoryListActivity.this, mVideoList);
                    historyListView.setAdapter(mAdapter);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        actionBar=getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("播放历史");
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setContentView(R.layout.activity_history_list);

        findViewById();

        initView();

    }

    private void initView() {
        Mylog.d("HistoryActivity--initView()");
        new Thread() {
            @Override
            public void run() {
                super.run();
                mHandler.sendEmptyMessage(LOAD_LIST);
            }
        }.start();


        historyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Mylog.e(  "点击ListView" + "  view-->" + view + "  i-->" + i + "  L-->" + l);
                Intent intent = new Intent(HistoryListActivity.this, MediaActivity.class);
                String videoName = mVideoList.get(i).getVideoName();
                String videoPath = mVideoList.get(i).getVideoPath();
                intent.putExtra("videoPath", videoPath);
                intent.putExtra("videoName", videoName);
                intent.putExtra("videoId", mVideoList.get(i).getVideoId());
                intent.putExtra("videoLevel", mVideoList.get(i).getVideoLevel());
                intent.putExtra("videoSize", mVideoList.get(i).getVideoSize());

                intent.putExtra("currentPosition", Integer.parseInt(mVideoList.get(i).getVideoPosition())); //视频当前位置
                if (new File(videoPath).exists()) {
                    startActivity(intent);
                } else {
                    new AlertDialog.Builder(HistoryListActivity.this).setTitle("警告").setMessage("  该视频不存在!")
                            .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            }).create().show();
                }
            }
        });
        historyListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                Mylog.e(  "长按点击ListView" + "  view-->" + view + "  i-->" + i + "  L-->" + l);
                AlertDialog.Builder dialog = new AlertDialog.Builder(HistoryListActivity.this);
                dialog.setTitle("提示");
                dialog.setMessage("  确定要删除本条历史记录？\n  "+mVideoList.get(i).getVideoName());
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i1) {
                        String videoPath = mVideoList.get(i).getVideoPath();
                        CleanHist.cleanSingle(HistoryListActivity.this, userName, videoPath);
                        reloadList();
                    }
                });
                dialog.setNegativeButton("取消",null);
                dialog.create().show();
                return true;
            }
        });


    }

    public void reloadList() {
        mHandler.sendEmptyMessageDelayed(RELOAD_LIST_DELAY,200);
        Mylog.e("HistoryListActivity.reloadList()");
    }
    @Override
    protected void onRestart() {
        reloadList();
        super.onRestart();
    }


    private void findViewById() {
        historyListView=(ListView)findViewById(R.id.historyListView);
    }

    //menu 按钮
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}