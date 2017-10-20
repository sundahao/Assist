package com.qgd.yfb.assist.biz;

import android.content.Context;

import com.qgd.yfb.assist.bean.AppUpdateInfo2;
import com.qgd.yfb.assist.bean.DownloadInfo;
import com.qgd.yfb.assist.bean.UpdateInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangke on 2017/5/23.
 */

public class AppUpdateTask extends UpdateTask {
    private static final String TAG = "AppUpdateTask";
    private final UpdateInfo updateInfo;
    private List<DownloadInfo> downloadList;
    private final List<AppUpdateInfo2> list;

    public AppUpdateTask(UpdateInfo updateInfo, boolean auto) {
        super(auto);
        this.updateInfo = updateInfo;
        downloadList = new ArrayList<DownloadInfo>();
        list = new ArrayList<AppUpdateInfo2>();
        AppUpdateInfo2 appUpdateInfo1 = new AppUpdateInfo2();
        appUpdateInfo1.setDownloadUrl("http://download.adobe.com/pub/adobe/magic/photoshop/win/8.x/AdobePhotoshopCS.zip");
        appUpdateInfo1.setName("手游精灵app 5.1 安卓版");
        appUpdateInfo1.setPackageName("com.syjl.com");
        list.add(appUpdateInfo1);

        AppUpdateInfo2 appUpdateInfo3 = new AppUpdateInfo2();
        appUpdateInfo3.setDownloadUrl("http://pc.xzstatic.com/2016/10/QQ8.7.19113.zip");
        appUpdateInfo3.setName("QQ 最新版v8.7.19113");
        appUpdateInfo3.setPackageName("com.qq.com");
        list.add(appUpdateInfo3);

        AppUpdateInfo2 appUpdateInfo4 = new AppUpdateInfo2();
        appUpdateInfo4.setDownloadUrl("http://dldir1.qq.com/weixin/Windows/WeChatSetup.exe");
        appUpdateInfo4.setName("微信电脑版 2.5.5.26 官方版");
        appUpdateInfo4.setPackageName("com.weixin.com");
        list.add(appUpdateInfo4);

        AppUpdateInfo2 appUpdateInfo2 = new AppUpdateInfo2();
        appUpdateInfo2.setDownloadUrl("http://wxz.myapp.com/16891/E55F866B5D6435C09B343636B90E8B91.apk?fsname=tuoyan.com.xinghuo_daying_2.2.1_49.apk&hsr=4d5s");
        appUpdateInfo2.setName("迅雷9 9.1.42.926 官方正式版");
        appUpdateInfo2.setPackageName("com.xunlei.com");
        list.add(appUpdateInfo2);
        for(AppUpdateInfo2 inf : list) {
            downloadList.add(new DownloadInfo(inf.getDownloadUrl(), inf.getPackageName() + ".apk" ,inf.getPackageName()));
        }
    }

    public UpdateInfo getUpdateInfo() {
        return updateInfo;
    }

    @Override
    public List<DownloadInfo> getDownloadList() {
        return downloadList;
    }

    @Override
    public void showConfirmView(Context context) {

    }

    public List<AppUpdateInfo2> getTestData() {

        return list;
    }
}
