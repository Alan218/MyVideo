package com.practice.alan.myvideo.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.practice.alan.myvideo.R;
import com.practice.alan.myvideo.bean.MyDatas;
import com.practice.alan.myvideo.db.MyDatabaseHelper;
import com.practice.alan.myvideo.doit.ImageDeal;
import com.practice.alan.myvideo.doit.Mylog;
import com.practice.alan.myvideo.doit.UpLoadHead;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.os.Build.VERSION_CODES.M;


public class MainActivity extends AppCompatActivity {

    protected TextView nickNameTextView, userNameTextView, vipLimitTextView, userRoleTextView, videoListTextView, historyTextView;
    protected TextView userManageTextView, videoManageTextView;
    protected ImageView head;
    public static final int SELECT_PIC = 0;//从相册选择
    public static final int CHOOSE_PICTURE = 0x445;
    public static final int CROP_SMALL_PICTURE = 0x645;
    private String userName, nickName;
    private Uri tempUri;
    private BroadcastReceiver receiver;

    //上传头像
    @RequiresApi(api = M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("MiniVideo");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById();
        initView();
        registerReceiver();

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

    @RequiresApi(api = M)
    private void initView() {

        head.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(MainActivity.this).setTitle("更换头像")
                        .setItems(new String[]{"从相册选择"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case SELECT_PIC://从相册选择

                                        Intent openAlbumIntent = new Intent(
                                                Intent.ACTION_GET_CONTENT);
                                        openAlbumIntent.setType("image/*");
                                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                                        break;

                                }
                            }
                        }).create().show();
                return false;
            }
        });

        SharedPreferences sp = getSharedPreferences(MyDatas.Preferences.PREFNAME, 0);
        //用户头像
        String headStr = sp.getString(MyDatas.Preferences.USERHADE, null);
        if (headStr != null) {
            head.setImageBitmap(ImageDeal.String2Bitmap(headStr));
        }
        userName = sp.getString(MyDatas.Preferences.USERNAME, null);
        userNameTextView.setText("用户:" + userName);
        nickName = sp.getString(MyDatas.Preferences.NICKNAME, null);
        nickNameTextView.setText("昵称:" + nickName);
        nickNameTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Mylog.e(" nickNameTextView.OnLongClickListener");
                showDialog();
                return true;
            }
        });

        if (MyDatas.UserRoleValues.ADMIN.equals(sp.getString(MyDatas.Preferences.USERROLE, null))) {
            userRoleTextView.setText("admin");
            userManageTextView.setVisibility(View.VISIBLE);
            videoManageTextView.setVisibility(View.VISIBLE);
        } else if (MyDatas.UserRoleValues.VIP.equals(sp.getString(MyDatas.Preferences.USERROLE, null))) {
            Calendar cal = Calendar.getInstance();
            Date date = cal.getTime();
            //            String sdfDate = new SimpleDateFormat("yyyyMMdd").format(date);
            String nowday = new SimpleDateFormat("yyyy.MM.dd").format(date);

            if (sp.getString(MyDatas.Preferences.VIPLIMIT, null).compareTo(nowday) >= 0) {
                userRoleTextView.setText("VIP");
                userRoleTextView.setTextColor(getColor(R.color.colorRed));
                userNameTextView.setTextColor(getColor(R.color.colorRed));
                nickNameTextView.setTextColor(getColor(R.color.colorRed));
                vipLimitTextView.setText(sp.getString(MyDatas.Preferences.VIPLIMIT, null) + "到期");
                vipLimitTextView.setTextColor(getColor(R.color.colorRed));
                vipLimitTextView.setVisibility(View.VISIBLE);

            }
        } else {
            userRoleTextView.setText("普通会员用户");
        }

        videoListTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, VideoListActivity.class);
                Mylog.e("进入VideoListActivity");
                startActivity(intent);
            }
        });

        historyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HistoryListActivity.class);
                Mylog.e("进入HistoryListActivity");
                startActivity(intent);
            }
        });

        userManageTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Mylog.e("点击用户管理");
                Intent intent = new Intent(MainActivity.this, UserManageActivity.class);
                startActivity(intent);
            }
        });

        videoManageTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Mylog.e("点击视频管理");
                Intent intent = new Intent(MainActivity.this, VideoManageActivity.class);
                startActivity(intent);
            }
        });
    }

    private void findViewById() {
        head = (ImageView) findViewById(R.id.headImageView);
        userNameTextView = (TextView) findViewById(R.id.userNameTextView);
        nickNameTextView = (TextView) findViewById(R.id.nickNameTextView);
        userRoleTextView = (TextView) findViewById(R.id.userRoleTextView);
        vipLimitTextView = (TextView) findViewById(R.id.vipLimitTextView);
        userManageTextView = (TextView) findViewById(R.id.userManageTextView);
        historyTextView = (TextView) findViewById(R.id.historyTextView);
        videoListTextView = (TextView) findViewById(R.id.videoListTextView);
        videoManageTextView = (TextView) findViewById(R.id.videoManageTextView);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 1, R.string.Settings);
        MenuItem item = menu.getItem(0);
        item.setIcon(R.drawable.action_bar_icon_setting);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {

                case CHOOSE_PICTURE:
                    startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    protected void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }

    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            photo = ImageDeal.toRoundBitmap(photo); // 这个时候的图片已经被处理成圆形的了
            head.setImageBitmap(photo);

            UpLoadHead.upLoad(MainActivity.this, userName, photo);//上传
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private void showDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);  //先得到构造器
        builder.setTitle("修改昵称："); //设置标题
        final EditText editText = new EditText(MainActivity.this);
        editText.setText(nickName);
        builder.setView(editText);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                String newNickName = editText.getText().toString();
                if (newNickName.trim().equals("") || newNickName.equals(nickName)) {
                    return;
                }
                SQLiteDatabase db = new MyDatabaseHelper(MainActivity.this).getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(MyDatas.UserColumns.NICKNAME, newNickName);
                int res = db.update(MyDatas.Databases.USERTBNAME
                        , values, MyDatas.UserColumns.USERNAME + " =?", new String[]{userName});
                if (res == 1) {
                    SharedPreferences sp = getSharedPreferences(MyDatas.Preferences.PREFNAME, 0);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(MyDatas.Preferences.NICKNAME, newNickName);
                    editor.commit();
                    nickNameTextView.setText("昵称:" + newNickName);
                    Toast.makeText(MainActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    nickName = newNickName;
                } else {
                    Toast.makeText(MainActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                }

                dialog.dismiss(); //关闭dialog
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        //参数都设置完成了，创建并显示出来
        builder.create().show();
    }
}
