package com.mastervidya.mastervidya.video;


public class VideoModel {

    private String videoId;
    private String videoName;
    private String videoUrl;
    private long videoDuration;
    private String downloadkey;
    String classname,chaptername,subjectname;

    public String getDownloadkey() {
        return downloadkey;
    }

    public void setDownloadkey(String downloadkey) {
        this.downloadkey = downloadkey;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getChaptername() {
        return chaptername;
    }

    public void setChaptername(String chaptername) {
        this.chaptername = chaptername;
    }

    public String getSubjectname() {
        return subjectname;
    }

    public void setSubjectname(String subjectname) {
        this.subjectname = subjectname;
    }

//    public VideoModel(String videoId, String videoName, String videoUrl, long videoDuration, String classname, String chaptername, String subjectname) {
//        this.videoId = videoId;
//        this.videoName = videoName;
//        this.videoUrl = videoUrl;
//        this.videoDuration = videoDuration;
//        this.classname = classname;
//        this.chaptername = chaptername;
//        this.subjectname = subjectname;
//    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public long getVideoDuration() {
        return videoDuration;
    }

    public void setVideoDuration(long videoDuration) {
        this.videoDuration = videoDuration;
    }
}
