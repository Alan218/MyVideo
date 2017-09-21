package com.practice.alan.myvideo.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.practice.alan.myvideo.R;
import com.practice.alan.myvideo.db.MyDatabaseHelper;
import com.practice.alan.myvideo.doit.Mylog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddUserActivity extends AppCompatActivity {
    protected EditText addUserEditText,addUserPwdEditText;
    protected Spinner vipLimitSpinner;
    protected int vipLimit;
    protected String role;
    protected Button addButton,resetButton;
    protected ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setTitle("增加用户");
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        addUserEditText=(EditText)findViewById(R.id.addUserEditText);
        addUserPwdEditText=(EditText)findViewById(R.id.addUserPwdEditText);
        vipLimitSpinner=(Spinner)findViewById(R.id.vipLimitSpinner);

        addButton =(Button)findViewById(R.id.addButton);
        resetButton=(Button)findViewById(R.id.resetButton);


//        vipLimit.
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.VipLimits,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vipLimitSpinner.setAdapter(adapter);
        vipLimitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    Mylog.e("默认不是会员");
                    role="normal";
                    vipLimit = 0;
                } else if(i==1){
                    Mylog.e("选择了3个月");
                    vipLimit = 3;
                    role="visitant";
                }
                else if (i == 2) {
                    Mylog.e("选择了6个月");
                    vipLimit = 6;
                    role="visitant";
                } else if (i == 3) {
                    Mylog.e("选择了12个月");
                    vipLimit = 12;
                    role="visitant";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Mylog.e("没选择，0个月");
                vipLimit=0;
                role="normal";
            }
        });
        //点击提交按钮
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Mylog.e("点击Add按钮");
                String user_name=addUserEditText.getText().toString();
                String user_pwd=addUserPwdEditText.getText().toString();
                String sql;
                if(user_name.equals("")||user_pwd.equals("")){
                    Toast.makeText(AddUserActivity.this, "用户名或密码不能为空！", Toast.LENGTH_SHORT).show();
                }else{
                    sql="select count(*) from user_info where user_name= ?";
                    Mylog.e("执行 db.rawQuery："+sql +"  ?="+user_name);
                    SQLiteDatabase db= new MyDatabaseHelper(AddUserActivity.this,"MiniVideoDatabase",1).getReadableDatabase();
//                    Cursor cursor=db.rawQuery(sql,new String[]{user_name});
                    Cursor cursor = db.query("user_info", new String[]{"user_pwd"}, "user_name=? ", new String[]{user_name}, "", "", "");
                    Mylog.e("查出用户名为"+user_name+"的人数为"+cursor.getCount());
                    if(cursor.getCount()==0){
                        Mylog.e("无此用户名"+user_name+" 添加该用户");
                        db=new MyDatabaseHelper(AddUserActivity.this,"MiniVideoDatabase",1).getWritableDatabase();
                        ContentValues values=new ContentValues();
                        values.put("user_name",user_name);
                        values.put("user_pwd",user_pwd);
                        values.put("nick_name",user_name);
                        values.put("user_role",role);

                        Calendar cal = Calendar.getInstance();
                        cal.add(Calendar.MONTH, vipLimit);
                        Date date = cal.getTime();

                        String sdfDate = new SimpleDateFormat("yyyy.MM.dd").format(date);

                        Mylog.e("会员时间为"+sdfDate);

                        values.put("vip_limit",sdfDate);
                        Mylog.e("执行插入操作");
                        db.insert("user_info",null,values);
                        Mylog.e("插入执行完毕");
                        Toast.makeText(AddUserActivity.this, user_name+"用户添加成功", Toast.LENGTH_SHORT).show();

                    }else{
                        Mylog.e("已有此用户名 不能添加");
                        Toast.makeText(AddUserActivity.this, "已有此用户名 不能添加!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Mylog.e("点击Reset按钮");
                addUserEditText.setText("");
                addUserPwdEditText.setText("");
                vipLimitSpinner.setSelection(0);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }
}
