package com.foxmo.bilibili.domain;

import java.util.Date;

public class File {
    private Long id;
    private Long userId;
    private String fileName;
    private String bucket;
    private String url;
    private String type;
    private String md5;
    private Date createTime;

    public File() {
    }

    public File(Long id, Long userId, String fileName, String bucket, String url, String type, String md5, Date createTime) {
        this.id = id;
        this.userId = userId;
        this.fileName = fileName;
        this.bucket = bucket;
        this.url = url;
        this.type = type;
        this.md5 = md5;
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
