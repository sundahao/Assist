package com.qgd.yfb.assist.biz;

import android.content.Context;
import android.os.RecoverySystem;
import android.util.Log;

import com.qgd.yfb.assist.AssistApplication;
import com.qgd.yfb.assist.bean.DownloadInfo;
import com.qgd.yfb.assist.bean.RomUpdateInfo;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * Created by yangke on 2017/5/23.
 */

public class RomUpdateTask extends UpdateTask {
    private static final String TAG = "AppUpdateTask";
    private final RomUpdateInfo updateInfo;
    private List<DownloadInfo> downloadList;

    public RomUpdateTask(RomUpdateInfo romUpdateInfo, boolean auto) {
        super(auto);
        this.updateInfo = romUpdateInfo;
        downloadList = Collections.singletonList(new DownloadInfo(updateInfo.getDownloadUrl(), "rom_update.zip", updateInfo.getMd5()));
    }

    public RomUpdateInfo getUpdateInfo() {
        return updateInfo;
    }

    @Override
    public List<DownloadInfo> getDownloadList() {
        return downloadList;
    }

    @Override
    public void showConfirmView(Context context) {

    }

    public void install() {
        Log.i(TAG, "installRom: " + updateInfo.getVersion());
        try {
            File romFile = DownloadHelper.getDownloadFile("rom_update.zip");
            RecoverySystem.verifyPackage(romFile, null, null);
            Log.i(TAG, "verifyPackage success");

            RecoverySystem.installPackage(AssistApplication.getInstance(), romFile);
            Log.i(TAG, "installPackage end");
        } catch (Exception e) {
            Log.e(TAG, "installRom fail", e);
        }
    }
}
