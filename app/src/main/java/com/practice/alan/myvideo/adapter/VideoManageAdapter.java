package com.practice.alan.myvideo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.practice.alan.myvideo.R;
import com.practice.alan.myvideo.bean.MyDatas;
import com.practice.alan.myvideo.bean.VideoBean;
import com.practice.alan.myvideo.doit.SimpleUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Alan on 2017/9/21.
 */

public class VideoManageAdapter extends BaseAdapter {
    private LayoutInflater miLayoutInflater;
    private List<VideoBean> mVideoList;
    private HashMap<Integer,Boolean> mIsChecked;
    private List<VideoBean> videosCheckedList=new LinkedList<>();
    private boolean mIsCheckable;
    private Context mContext;


    public VideoManageAdapter(Context context, List<VideoBean> mVideoList, boolean mIsCheckable) {
        this.mIsCheckable = mIsCheckable;
        mContext = context;
        this.miLayoutInflater = LayoutInflater.from(mContext);
        this.mVideoList = mVideoList;
        initChecked();
    }

    public void initChecked(){
        mIsChecked = new HashMap<>();
        for (int i=0;i<mVideoList.size();i++){
            mIsChecked.put(i, false);
        }
        videosCheckedList=new LinkedList<>();
    }

    public void setVideoList(List<VideoBean> mVideoList) {
        this.mVideoList = mVideoList;
    }

    public boolean isCheckable() {
        return mIsCheckable;
    }

    public void setIsCheckable(boolean mIsCheckable) {
        initChecked();
        this.mIsCheckable = mIsCheckable;
    }

    @Override
    public int getCount() {
        return mVideoList.size();
    }

    @Override
    public Object getItem(int i) {
        return mVideoList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view=null;
        ViewHolder viewHolder = new ViewHolder();
        if (view == null) {
            view = miLayoutInflater.inflate(R.layout.item_video_manage, viewGroup, false);

            viewHolder.checkBox = (CheckBox) view.findViewById(R.id.videoManageCheckBox);
            viewHolder.checkBox.setClickable(false);
            viewHolder.videoManageImageView = (ImageView) view.findViewById(R.id.videoManageImageView);
            viewHolder.videoManageDurationTextView = (TextView) view.findViewById(R.id.videoManageDurationTextView);
            viewHolder.videoManageVipTextView = (TextView) view.findViewById(R.id.videoManageVipTextView);
            viewHolder.videoManageSizeTextView = (TextView) view.findViewById(R.id.videoManageSizeTextView);
            viewHolder.videoManageNameTextView = (TextView) view.findViewById(R.id.videoManageNameTextView);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final VideoBean video = mVideoList.get(i);
        viewHolder.videoManageDurationTextView.setText(SimpleUtils.stringForTimeMills(video.getVideoDuration()));
        viewHolder.videoManageNameTextView.setText(video.getVideoName() + "");
        viewHolder.videoManageSizeTextView.setText(video.getVideoSize() + "");
        viewHolder.videoManageImageView.setImageBitmap(video.getVideoThumbnail());

        viewHolder.videoManageVipTextView.setText(R.string.VIP);

        if (MyDatas.VideoLeverValues.VIPLEVEL.equals(video.getVideoLevel())) {
            viewHolder.videoManageVipTextView.setVisibility(View.VISIBLE);
        }

        if (mIsCheckable) {
            viewHolder.checkBox.setVisibility(View.VISIBLE);
            viewHolder.checkBox.setChecked(mIsChecked.get(i));
            viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        videosCheckedList.add(video);
                        mIsChecked.put(i, true);
                    } else {
                        videosCheckedList.remove(video);
                        mIsChecked.put(i, false);
                    }
                }
            });
            final ViewHolder finalViewHolder = viewHolder;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finalViewHolder.checkBox.toggle();
                }
            });
        }


        return view;
    }

    public List<VideoBean> getCheckedVideos() {
        if (mIsCheckable) {
            return videosCheckedList;
        } else {
            Toast.makeText(mContext, "没有选择任何Video", Toast.LENGTH_SHORT).show();
            return null;
        }
    }


    private class ViewHolder {
        ImageView videoManageImageView;
        TextView videoManageVipTextView;
        TextView videoManageDurationTextView;
        TextView videoManageSizeTextView;
        TextView videoManageNameTextView;
        CheckBox checkBox;

    }
}