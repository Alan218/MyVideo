package com.practice.alan.myvideo.bean;

import android.graphics.Bitmap;

/**
 * Created by Alan on 2017/9/21.
 */

public class VideoBean {
    private int videoId;
    private String videoName;
    private String videoDuration;
    private String videoSize;
    private String videoPath;
    private String videoLevel;
    private Bitmap videoThumbnail;
    private String videoPosition;
    private String videoHistTime;
    public VideoBean() {
        super();
    }

    public VideoBean(int videoId, String videoName, String videoDuration, String videoSize,
                     String videoPath, String videoLevel, Bitmap videoThumbnail) {
        this.videoId = videoId;
        this.videoName = videoName;
        this.videoDuration = videoDuration;
        this.videoSize = videoSize;
        this.videoPath = videoPath;
        this.videoLevel = videoLevel;
        this.videoThumbnail = videoThumbnail;
    }


    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoDuration() {
        return videoDuration;
    }

    public void setVideoDuration(String videoDuration) {
        this.videoDuration = videoDuration;
    }

    public String getVideoSize() {
        return videoSize;
    }

    public void setVideoSize(String videoSize) {
        this.videoSize = videoSize;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getVideoLevel() {
        return videoLevel;
    }

    public void setVideoLevel(String videoLevel) {
        this.videoLevel = videoLevel;
    }

    public Bitmap getVideoThumbnail() {
        return videoThumbnail;
    }

    public void setVideoThumbnail(Bitmap videoThumbnail) {
        this.videoThumbnail = videoThumbnail;
    }


    public String  getVideoPosition() {
        return videoPosition;
    }

    public void setVideoPosition(String videoPosition) {
        this.videoPosition = videoPosition;
    }

    public String getVideoHistTime() {
        return videoHistTime;
    }

    public void setVideoHistTime(String videoHistTime) {
        this.videoHistTime = videoHistTime;
    }
}
