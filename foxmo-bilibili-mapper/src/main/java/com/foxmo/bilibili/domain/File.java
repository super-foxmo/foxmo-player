package com.foxmo.bilibili.domain;

import java.util.Date;

public class File {
    private Integer id;
    private String fileName;
    private String bucket;
    private String type;
    private String md5;
    private Date createTime;

    public File() {
    }

    public File(Integer id, String fileName, String bucket, String type, String md5, Date createTime) {
        this.id = id;
        this.fileName = fileName;
        this.bucket = bucket;
        this.type = type;
        this.md5 = md5;
        this.createTime = createTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
