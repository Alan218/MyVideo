package com.practice.alan.myvideo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.practice.alan.myvideo.R;
import com.practice.alan.myvideo.bean.MyDatas;
import com.practice.alan.myvideo.bean.VideoBean;
import com.practice.alan.myvideo.doit.SimpleUtils;

import java.util.List;

public class HistListAdapter extends BaseAdapter {
    private List<VideoBean> mVideoList;
    private LayoutInflater mInflater;
    public HistListAdapter(Context context, List<VideoBean> mVideoList) {
        this.mVideoList = mVideoList;
        mInflater = LayoutInflater.from(context);
    }

    public void setVideoList(List<VideoBean> mVideoList) {
        this.mVideoList = mVideoList;
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
        return mVideoList.get(i).getVideoId();
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        VideoBean video = mVideoList.get(i);
        view=null;
        ViewHolder holder=new ViewHolder();
        if (view == null) {
            view=mInflater.inflate(R.layout.item_history_list, viewGroup, false);
            holder.histThumbImageView = (ImageView) view.findViewById(R.id.histThumbImageView);
            holder.histVipImageView = (ImageView) view.findViewById(R.id.histVipImageView);
            holder.lastTimeTextView = (TextView) view.findViewById(R.id.lastTimeTextView);
            holder.histVideoNameTextView = (TextView) view.findViewById(R.id.histVideoNameTextView);
            holder.histVideoPositionTextView = (TextView) view.findViewById(R.id.histVideoPositionTextView);
            holder.histVideoDurationTextView = (TextView) view.findViewById(R.id.histVideoDurationTextView);
            holder.histVideoSizeTextView = (TextView) view.findViewById(R.id.histVideoSizeTextView);

            view.setTag(holder);
        }
        else {
            holder = (ViewHolder)view.getTag();
        }

        holder.histThumbImageView.setImageBitmap(video.getVideoThumbnail());
        if (MyDatas.VideoLeverValues.VIPLEVEL.equals(video.getVideoLevel())) {
            holder.histVipImageView.setVisibility(View.VISIBLE);
        }
        holder.histVideoNameTextView.setText(video.getVideoName());
        holder.histVideoPositionTextView.setText(SimpleUtils.stringForTimeMills(video.getVideoPosition()) + "/");
        holder.histVideoDurationTextView.setText(SimpleUtils.stringForTimeMills((video.getVideoDuration())));
        holder.histVideoSizeTextView.setText(video.getVideoSize());
        holder.lastTimeTextView.setText("上次观看时间：" + video.getVideoHistTime());



        return view;
    }

    private class ViewHolder {
        ImageView histThumbImageView;
        ImageView histVipImageView ;
        TextView histVideoNameTextView;
        TextView histVideoPositionTextView;
        TextView histVideoDurationTextView;
        TextView histVideoSizeTextView;
        TextView lastTimeTextView;
    }
}