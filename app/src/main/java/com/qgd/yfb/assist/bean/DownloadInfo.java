package com.qgd.yfb.assist.bean;

import android.app.DownloadManager;
import android.graphics.drawable.Drawable;

public class DownloadInfo {
    private String packageName;
    private String filePath;
    private String fileName;
    private String downloadURL;
    private String version;
    private String md5;
    private Drawable icon;

    private long enqueueID;
    private int downloadProcess = 0;
    private int downloadStatus = DownloadManager.STATUS_PENDING;
    private int installStatus = 0;

    public DownloadInfo() {
    }



    public DownloadInfo(String downloadURL, String fileName,String packageName) {
        this.downloadURL = downloadURL;
        this.fileName = fileName;

        this.packageName = packageName;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public String getDownloadURL() {
        return downloadURL;
    }

    public String getVersion() {
        return version;
    }

    public String getMd5() {
        return md5;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public long getEnqueueID() {
        return enqueueID;
    }

    public void setEnqueueID(long enqueueID) {
        this.enqueueID = enqueueID;
    }

    public long getDownloadProcess() {
        return downloadProcess;
    }

    public void setDownloadProcess(int downloadProcess) {
        this.downloadProcess = downloadProcess;
    }

    public int getDownloadStatus() {
        return downloadStatus;
    }

    public void setDownloadStatus(int downloadStatus) {
        this.downloadStatus = downloadStatus;
    }

    public int getInstallStatus() {
        return installStatus;
    }

    public void setInstallStatus(int installStatus) {
        this.installStatus = installStatus;
    }
}
