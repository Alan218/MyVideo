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

/**
 * Created by Alan on 2017/9/21.
 */

public class VideoListAdapter extends BaseAdapter {
    private List<VideoBean> videoList;
    private LayoutInflater mInflater;

    public VideoListAdapter(Context context, List<VideoBean> videoList  ) {
        this.videoList = videoList;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return videoList.size();
    }

    @Override
    public Object getItem(int i) {
        return videoList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view=null;
        ViewHolder holder = new ViewHolder();
        if (view == null) {
            view = mInflater.inflate(R.layout.item_video_list, viewGroup, false);

            holder.videoName= (TextView) view.findViewById(R.id.videoNameTextView);
            holder.videoDuration= (TextView) view.findViewById(R.id.videoDurationTextView);
            holder.videoSize= (TextView) view.findViewById(R.id.videoSizeTextView);
            holder.videoThumbnail= (ImageView) view.findViewById(R.id.thumbnailImageView);
            holder.vipImageView= (ImageView) view.findViewById(R.id.vipImageView);

            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        VideoBean videoBean = videoList.get(i);

        holder.videoName.setText(videoBean.getVideoName());
        holder.videoSize.setText(videoBean.getVideoSize());
        holder.videoDuration.setText(SimpleUtils.stringForTimeMills(videoBean.getVideoDuration()));
        holder.videoThumbnail.setImageBitmap(videoBean.getVideoThumbnail());


        if ((MyDatas.VideoLeverValues.VIPLEVEL.equals(videoBean.getVideoLevel()))) {
            holder.vipImageView.setVisibility(View.VISIBLE);
        }
        return view;
    }
    class ViewHolder {
        TextView videoName;
        TextView videoDuration;
        TextView videoSize;
        ImageView videoThumbnail;
        ImageView vipImageView;
    }

}
