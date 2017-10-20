package com.qgd.yfb.assist.biz;

import android.content.Context;

import com.qgd.yfb.assist.bean.DownloadInfo;

import java.util.List;

/**
 * Created by yangke on 2017/5/23.
 */

public abstract class UpdateTask {
    private boolean auto;

    private boolean canceled;

    public UpdateTask(boolean auto) {
        this.auto = auto;
    }

    public boolean isAuto() {
        return auto;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public abstract List<DownloadInfo> getDownloadList();

    public abstract void showConfirmView(Context context);

    public void cancel() {
        canceled = true;
        //TODO 取消下载
    }
}
