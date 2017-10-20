package com.qgd.yfb.assist.bean;

import java.io.Serializable;

/**
 * 作者：ethan on 2016/2/25 16:43
 * 邮箱：ethan.chen@fm2020.com
 *
 * 修改：实现序列化
 */
public class RomUpdateInfo implements Serializable {
    private String version;
    private int    forceUpdate;
    private String downloadUrl;
    private String md5;

    public RomUpdateInfo() {
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(int forceUpdate) {
        this.forceUpdate = forceUpdate;
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
