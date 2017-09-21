package com.practice.alan.myvideo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.practice.alan.myvideo.R;
import com.practice.alan.myvideo.adapter.UserListAdapter;
import com.practice.alan.myvideo.bean.UserBean;
import com.practice.alan.myvideo.db.DeleteUser;
import com.practice.alan.myvideo.doit.Mylog;
import com.practice.alan.myvideo.doit.QueryUsers;

import java.util.ArrayList;
import java.util.List;

public class UserManageActivity extends AppCompatActivity {
    protected ListView userManageListView;
    protected ActionBar actionBar;
    private List<UserBean> mUsersList = new ArrayList<>();
    private Button deleteUserButton;
    private CheckBox setAllCheckBox;
    private UserListAdapter mAdapter;
    private boolean isSetAll=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("用户管理");
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);

        }

        setContentView(R.layout.activity_user_manage);
        findViewById();

        Activity a;
        initView();

    }

    private void findViewById() {
        userManageListView = (ListView) findViewById(R.id.userManageListView);
        deleteUserButton = (Button) findViewById(R.id.deleteUserButton);
        setAllCheckBox = (CheckBox) findViewById(R.id.setAllCheckBox);
    }

    private void initView() {
        //将数据装到集合中去
        mUsersList = QueryUsers.query(UserManageActivity.this);
        //为数据绑定适配器
        mAdapter = new UserListAdapter(this, mUsersList);
        userManageListView.setAdapter(mAdapter);

        userManageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        //给Item设置长按可选定
        userManageListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mAdapter.getIsVisiable() == View.VISIBLE) {
                    mAdapter.setIsVisiable(View.GONE);
                    deleteUserButton.setVisibility(View.GONE);
                    setAllCheckBox.setVisibility(View.GONE);
                    setAllCheckBox.setChecked(false);
                    //                    videoall.setVisibility(View.GONE);

                } else {
                    mAdapter.setIsVisiable(View.VISIBLE);
                    deleteUserButton.setVisibility(View.VISIBLE);
                    setAllCheckBox.setVisibility(View.VISIBLE);
                    //                    videoall.setVisibility(View.VISIBLE);
                }
                reLoadList();
                return true;
            }
        });
        setAllCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mAdapter.setAll(true);
                    isSetAll=true;
                    mAdapter.notifyDataSetChanged();
                } else {
                    mAdapter.setAll(false);
                    isSetAll=false;
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
        deleteUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //                        去除删除按钮
                if (mAdapter.getIsVisiable() == View.VISIBLE) {
                    DeleteUser.delete(UserManageActivity.this, mAdapter.getUserCheckedList());
                    deleteUserButton.setVisibility(View.GONE);
                    setAllCheckBox.setVisibility(View.GONE);
                    setAllCheckBox.setChecked(false);
                    mAdapter.setIsVisiable(View.GONE);
                } else {
                    deleteUserButton.setVisibility(View.VISIBLE);
                    setAllCheckBox.setVisibility(View.VISIBLE);
                    mAdapter.setIsVisiable(View.VISIBLE);
                }
                reLoadList();
            }
        });
    }

    private void reLoadList() {
        mUsersList = QueryUsers.query(this);
        mAdapter.setUsersList(mUsersList);
        mAdapter.initSelected();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
        reLoadList();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 1, "添加用户");
        menu.add(0, 2, 2, "删除用户");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Mylog.e("点击菜单");
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (item.getItemId() == 1) {
            Mylog.e("进入添加用户菜单界面");
            Intent intent = new Intent(UserManageActivity.this, AddUserActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == 2) { //点击删除用户菜单
            initView();
            //显示 去掉 删除按钮
            if (mAdapter.getIsVisiable() == View.VISIBLE) {
                DeleteUser.delete(this, mAdapter.getUserCheckedList());
                deleteUserButton.setVisibility(View.GONE);
                setAllCheckBox.setVisibility(View.GONE);
                setAllCheckBox.setChecked(false);
                mAdapter.setIsVisiable(View.GONE);
            } else {
                mAdapter.setIsVisiable(View.VISIBLE);
                deleteUserButton.setVisibility(View.VISIBLE);
                setAllCheckBox.setVisibility(View.VISIBLE);
            }
            mAdapter.notifyDataSetChanged();

        } else if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mAdapter.getIsVisiable() == View.VISIBLE) {
            mAdapter.setIsVisiable(View.GONE);
            deleteUserButton.setVisibility(View.GONE);
            setAllCheckBox.setChecked(false);
            setAllCheckBox.setVisibility(View.GONE);
            reLoadList();
        } else {
            super.onBackPressed();
        }
    }
}