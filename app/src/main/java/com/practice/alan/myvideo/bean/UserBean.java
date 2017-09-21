package com.practice.alan.myvideo.bean;

import android.graphics.Bitmap;

/**
 * Created by Alan on 2017/9/21.
 */

public class UserBean {
    private int userId;
    private String userName;
    private String userPwd;
    private String nickName;
    private String userRole;
    private String vipLimit;
    private Bitmap headBitmap;

    public UserBean(int userId, String userName, String userPwd, String nickName, String userRole, String vipLimit, Bitmap headBitmap) {
        this.userId = userId;
        this.userName = userName;
        this.userPwd = userPwd;
        this.nickName = nickName;
        this.userRole = userRole;
        this.vipLimit = vipLimit;
        this.headBitmap = headBitmap;

    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getVipLimit() {
        return vipLimit;
    }

    public void setVipLimit(String vipLimit) {
        this.vipLimit = vipLimit;
    }

    public Bitmap getHeadBitmap() {
        return headBitmap;
    }

    public void setHeadBitmap(Bitmap headBitmap) {
        this.headBitmap = headBitmap;
    }
}