package com.example.alan_lin.smart_locker;

public class RequestClass {

    String Bucket;
    String Filename;
    String Nickname;


    public String getBucketName() {
        return Bucket;
    }

    public void setBucketName(String Bucket) {
        this.Bucket = Bucket;
    }

    public String getFilenameFileName() {
        return Filename;
    }

    public void setFilenameName(String Filename) {
        this.Filename = Filename;
    }

    public String getNicknameFileName() {
        return Nickname;
    }

    public void setNicknameName(String Nickname) {
        this.Nickname = Nickname;
    }

    public RequestClass(String Bucket, String Filename, String Nickname) {
        this.Bucket = Bucket;
        this.Filename = Filename;
        this.Nickname = Nickname;
    }

    public RequestClass() {
    }
}

