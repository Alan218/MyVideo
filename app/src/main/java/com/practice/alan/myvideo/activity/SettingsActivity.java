package com.practice.alan.myvideo.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.practice.alan.myvideo.R;
import com.practice.alan.myvideo.bean.MyDatas;
import com.practice.alan.myvideo.db.CleanHist;
import com.practice.alan.myvideo.doit.Mylog;

public class SettingsActivity extends AppCompatActivity {
    protected TextView logoutTextView, clearCacheTextView, aboutAppTextView, changeUserTextView;
    protected ActionBar actionBar;
    protected BroadcastReceiver receiver;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("设置");
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setContentView(R.layout.activity_settings);
        findViewById();
        initView();
        registerReceiver();

    }

    private void initView() {
        sp = getSharedPreferences(MyDatas.Preferences.PREFNAME, 0);
        editor = sp.edit();
        logoutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog= new AlertDialog.Builder(SettingsActivity.this);
                dialog.setTitle("提示");
                dialog.setMessage("    确定要退出该程序并且清除本地用户信息吗？");
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        editor.clear();
                        editor.commit();
                        Intent it = new Intent(MyDatas.BROADCAST_EXIT);
                        sendBroadcast(it);
                    }
                });
                dialog.create().show();
            }
        });
        clearCacheTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Mylog.e("点击清除历史记录");
                String userName = sp.getString(MyDatas.Preferences.USERNAME, null);
                CleanHist.cleanAll(SettingsActivity.this,userName);
                Toast.makeText(SettingsActivity.this, "历史记录已清除！", Toast.LENGTH_SHORT).show();
            }
        });
        aboutAppTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Mylog.e("关于应用");
                Intent intent = new Intent(SettingsActivity.this, AboutAppActivity.class);
                startActivity(intent);

            }
        });
        changeUserTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Mylog.e("点击更改用户");
                AlertDialog.Builder dialog= new AlertDialog.Builder(SettingsActivity.this);
                dialog.setTitle("提示");
                dialog.setMessage("    确定要切换到其他用户吗？");
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        editor.clear();
                        editor.commit();
                        Intent it = new Intent(MyDatas.BROADCAST_EXIT);
                        Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                        startActivity(intent);
                        sendBroadcast(it);
                    }
                });
                dialog.create().show();
            }
        });

    }

    private void findViewById() {
        logoutTextView = (TextView) findViewById(R.id.logoutTextView);
        clearCacheTextView = (TextView) findViewById(R.id.clearCacheTextView);
        aboutAppTextView = (TextView) findViewById(R.id.aboutTextView);
        changeUserTextView = (TextView) findViewById(R.id.changeUserTextView);
    }

    private void registerReceiver() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                finish();
            }
        };
        IntentFilter filter = new IntentFilter(MyDatas.BROADCAST_EXIT);
        registerReceiver(receiver, filter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return true;
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }
}
