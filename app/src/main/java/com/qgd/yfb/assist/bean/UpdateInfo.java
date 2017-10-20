package com.qgd.yfb.assist.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：ethan on 2016/2/25 17:27
 * 邮箱：ethan.chen@fm2020.com
 *
 * 修改：使其实现序列化,包括其内部成员也要实现序列化
 */
public class UpdateInfo implements Serializable {
    private List<AppUpdateInfo2> newApps;
    private RomUpdateInfo newRom;

    public List<AppUpdateInfo2> getNewApps() {
        return newApps;
    }

    public void setNewApps(List<AppUpdateInfo2> newApps) {
        this.newApps = newApps;
    }

    public RomUpdateInfo getNewRom() {
        return newRom;
    }

    public void setNewRom(RomUpdateInfo newRom) {
        this.newRom = newRom;
    }
}
