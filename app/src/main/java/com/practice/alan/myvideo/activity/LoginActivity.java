package com.practice.alan.myvideo.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.practice.alan.myvideo.R;
import com.practice.alan.myvideo.bean.MyDatas;
import com.practice.alan.myvideo.db.MyDatabaseHelper;
import com.practice.alan.myvideo.doit.Mylog;

public class LoginActivity extends AppCompatActivity {
    protected String userName = null;
    protected String passWord = null;
    protected EditText userEditText, passwordEditText;
    protected TextView forgotPwd;
    protected Button loginButton,resetLoginButton;

    protected SQLiteOpenHelper dbHelper;
    protected SQLiteDatabase db;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences sp=getSharedPreferences(MyDatas.Preferences.PREFNAME,0);
        if(sp.getString(MyDatas.Preferences.USERNAME,null)!=null){
            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        dbHelper = new MyDatabaseHelper(this,MyDatas.Databases.DBNAME, 1);
        db = dbHelper.getWritableDatabase();

        userEditText = (EditText) findViewById(R.id.UserEditText);
        passwordEditText = (EditText) findViewById(R.id.PwdEditText);
        loginButton = (Button) findViewById(R.id.LoginButton);
        resetLoginButton=(Button)findViewById(R.id.resetLoginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userName = userEditText.getText().toString();
                passWord = passwordEditText.getText().toString();

                Mylog.e("UserName=" + userName + " Password=" + passWord);

                Cursor cursor = db.query("user_info", null, "user_name=?", new String[]{userName}, "", "", "");
                //                "select * from account_info where account_id=?", new String[]{userName});

                if (cursor.getCount() == 0) {
                    Toast.makeText(getApplicationContext(), "没有此账号 ！", Toast.LENGTH_SHORT).show();
                } else {
                    cursor.moveToFirst();
                    if (passWord.equals(cursor.getString(cursor.getColumnIndex("user_pwd")))) {

                        Mylog.e("cursor.getString(0))--->" + cursor.getString(0));

                        //写入SharedPreferences
                        SharedPreferences sp = getSharedPreferences(MyDatas.Preferences.PREFNAME, 0);
                        SharedPreferences.Editor editor = sp.edit();

                        editor.putInt(MyDatas.Preferences.USERID, cursor.getInt(cursor.getColumnIndex(MyDatas.UserColumns.USERID)));
                        editor.putString(MyDatas.Preferences.USERNAME, userName);
                        editor.putString(MyDatas.Preferences.NICKNAME, cursor.getString(cursor.getColumnIndex(MyDatas.UserColumns.NICKNAME)));
                        editor.putString(MyDatas.Preferences.USERROLE, cursor.getString(cursor.getColumnIndex(MyDatas.UserColumns.USERROLE)));
                        editor.putString(MyDatas.Preferences.VIPLIMIT, cursor.getString(cursor.getColumnIndex(MyDatas.UserColumns.VIPLIMIT)));
                        editor.putString(MyDatas.Preferences.USERHADE, cursor.getString(cursor.getColumnIndex(MyDatas.UserColumns.HEAD)));

                        editor.commit();
                        //跳转
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(getApplicationContext(), userName + "登陆成功 ！", Toast.LENGTH_SHORT).show();

                        cursor.close();
                        if (dbHelper != null) {
                            dbHelper.close();
                        }
                        if (db != null) {
                            db.close();
                        }

                    } else {
                        Mylog.e("账号密码错误 ！");
                        Toast.makeText(getApplicationContext(), "账号或密码错误 ！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        });
        resetLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Mylog.e("点击重置按钮");
                userEditText.setText("");
                passwordEditText.setText("");
            }
        });

        forgotPwd = (TextView) findViewById(R.id.ForgotPwdCheckedTextView);
        forgotPwd.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Mylog.e("点击忘记密码");
                Toast.makeText(LoginActivity.this, "请联系管理员重置密码！", Toast.LENGTH_SHORT).show();
            }
        });
    }
}