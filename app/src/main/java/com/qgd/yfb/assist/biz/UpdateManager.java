package com.qgd.yfb.assist.biz;

import android.util.Log;

import com.qgd.yfb.assist.AssistApplication;

/**
 * Created by yangke on 2017/5/23.
 */

public class UpdateManager {
    public enum Status {IDLE, DOWNLOADING, DOWNLOAD_SUCCESS, INSTALLING};

    private static final String TAG = "UpdateManager";
    private UpdateTask currentTask;
    private Status status = Status.IDLE;
    private final Object lock = new Object();

    public UpdateManager() {
    }

    public UpdateTask getCurrentTask() {
        synchronized (lock) {
            return currentTask  = new AppUpdateTask(null, true);
        }
    }






    public void onInstallFinish() {
        Log.d(TAG, "onInstallFinish");
        synchronized(lock) {
            currentTask = null;
            status = Status.IDLE;
        }
    }

    private void onHomeResume() {
        synchronized(lock) {
            if (currentTask == null) {
                return;
            }

            if (status == Status.INSTALLING) {
                //安装过程意外中断，回到空闲状态
                Log.i(TAG, "reset status to IDLE");
                status = Status.IDLE;
                return;
            }

            if (status == Status.DOWNLOAD_SUCCESS) {
                if (currentTask.isAuto()) {
                    currentTask.showConfirmView(AssistApplication.getInstance());
                    status = Status.INSTALLING;
                } else {
                    Log.i(TAG, "illegal task, reset status to IDLE");
                    currentTask = null;
                    status = Status.IDLE;
                }
            }
        }
    }
}
