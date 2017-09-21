package com.practice.alan.myvideo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.practice.alan.myvideo.R;
import com.practice.alan.myvideo.bean.UserBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Alan on 2017/9/21.
 */

public class UserListAdapter extends BaseAdapter {
    private HashMap<Integer, Boolean> isSelected;
    private List<UserBean> mUsers;
    public List<UserBean> mUserCheckedList = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context contex;
    private int isVisiable;
    private boolean isSetAll;


    public UserListAdapter(Context context, List<UserBean> mUsers) {
        this.isVisiable = View.GONE;
        this.mUsers = mUsers;
        this.contex = context;
        mInflater = LayoutInflater.from(context);
        isSelected = new HashMap<>();
        initSelected();
        isSetAll=false;
    }

    public void initSelected(){
        for (int i = 0; i < mUsers.size(); i++) {
            this.isSelected.put(i, false);
        }
    }
    public void setAll(boolean b){
        if (b) {
            isSetAll = true;
            for (UserBean u : mUsers) {
                mUserCheckedList.add(u);
            }
        } else {
            isSetAll = false;
            mUserCheckedList.clear();
        }
    }

    @Override
    public int getCount() {
        return mUsers.size();
    }

    @Override
    public Object getItem(int i) {
        return mUsers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public ViewHolder getViewHolder() {
        ViewHolder holder = new ViewHolder();
        return holder;
    }

    public void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        this.isSelected = isSelected;
    }

    public HashMap<Integer, Boolean> getIsSelected() {
        return this.isSelected;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        View convertView=null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_user, parent, false); //加载布局
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
            holder.checkBox.setClickable(false);//让checkbox不可被选中   选中需要选中整个view
            //            holder.checkBox.setEnabled(false);
            holder.userId = (TextView) convertView.findViewById(R.id.userId);
            holder.userRoleTextView = (TextView) convertView.findViewById(R.id.userRoleTextView);
            holder.userNameTextView = (TextView) convertView.findViewById(R.id.userNameTextView);
            holder.nickNameTextView = (TextView) convertView.findViewById(R.id.nickNameTextView);
            holder.vipTimeLimit = (TextView) convertView.findViewById(R.id.vipTimeLimit);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final UserBean userBean = mUsers.get(position);
        final CheckBox checkBox = holder.checkBox;

        holder.checkBox.setChecked(isSelected.get(position));
        if (isSetAll) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }
        if(holder.checkBox.isChecked()){
            mUserCheckedList.add(userBean);
        }else{
            mUserCheckedList.remove(userBean);
        }
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mUserCheckedList.add(userBean);
                } else {
                    mUserCheckedList.remove(userBean);
                }
            }
        });
        if (isVisiable == View.VISIBLE) {
            holder.checkBox.setVisibility(View.VISIBLE);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkBox.toggle();
                    isSelected.put(position, !isSelected.get(position));
                }
            });
        } else {
            holder.checkBox.setVisibility(View.GONE);
        }

        holder.userId.setText(userBean.getUserId() + "");
        holder.userRoleTextView.setText(userBean.getUserRole());
        holder.userNameTextView.setText(userBean.getUserName());
        holder.nickNameTextView.setText(userBean.getNickName());
        holder.vipTimeLimit.setText(userBean.getVipLimit());

        return convertView;
    }

    public int getIsVisiable() {
        return this.isVisiable;
    }

    public void setIsVisiable(int isVisiable) {
        this.isVisiable = isVisiable;
    }

    public List<UserBean> getUserCheckedList() {
        return mUserCheckedList;
    }

    public void setUsersList(List<UserBean> mUsersList) {
        this.mUsers=mUsersList;
    }

    //这个ViewHolder只能服务于当前这个特定的adapter，因为ViewHolder里会指定item的控件，不同的ListView，item可能不同，所以ViewHolder写成一个私有的类
    private class ViewHolder {
        CheckBox checkBox;
        TextView userId;
        TextView userRoleTextView;
        TextView userNameTextView;
        TextView nickNameTextView;
        TextView vipTimeLimit;
    }
}