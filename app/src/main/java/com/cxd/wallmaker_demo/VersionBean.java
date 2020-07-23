package com.cxd.wallmaker_demo;

import java.io.Serializable;
import java.util.Date;

public class VersionBean implements Serializable {
    private String versionCode ;
    private String versionName ;
    private String md5Code ;
    private String apkUrl ;
    private String versionNotice ;
    private Date uploadDate ;
    private String size ;

    @Override
    public String toString() {
        return "VersionBean{" +
                "versionCode='" + versionCode + '\'' +
                ", versionName='" + versionName + '\'' +
                ", md5Code='" + md5Code + '\'' +
                ", apkUrl='" + apkUrl + '\'' +
                ", versionNotice='" + versionNotice + '\'' +
                ", uploadDate=" + uploadDate +
                ", size='" + size + '\'' +
                '}';
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getMd5Code() {
        return md5Code;
    }

    public void setMd5Code(String md5Code) {
        this.md5Code = md5Code;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    public String getVersionNotice() {
        return versionNotice;
    }

    public void setVersionNotice(String versionNotice) {
        this.versionNotice = versionNotice;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
