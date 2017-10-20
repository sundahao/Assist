package com.qgd.yfb.assist;

import android.app.Application;

import com.qgd.yfb.assist.biz.UpdateManager;

/**
 * Created by Administrator on 2017/10/18.
 */

public class AssistApplication extends Application {

    private static AssistApplication instance;
    private UpdateManager updateManager;

    @Override
    public void onCreate() {
        super.onCreate();
        instance =this;
        updateManager = new UpdateManager();
    }

    public static AssistApplication getInstance() {
        return instance;
    }

    public UpdateManager getUpdateManager() {
        return updateManager;
    }
}
