package com.qgd.yfb.assist.bean;

import java.io.Serializable;

/**
 * 作者：ethan on 2016/2/25 16:41
 * 邮箱：ethan.chen@fm2020.com
 *
 * 修改：实现序列化
 */
public class AppUpdateInfo2 implements Serializable {
    private String packageName;
    private String version;
    private String name;
    private String cover;
    private int    forceUpate;
    private String downloadUrl;
    private String md5;

    public AppUpdateInfo2() {
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getForceUpate() {
        return forceUpate;
    }

    public void setForceUpate(int forceUpate) {
        this.forceUpate = forceUpate;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
}
