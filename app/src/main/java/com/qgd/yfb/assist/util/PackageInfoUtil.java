package com.qgd.yfb.assist.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.qgd.yfb.assist.R;

/**
 * Created by Administrator on 2017/5/27.
 */

public class PackageInfoUtil {
    public static final String TAG = "PackageInfoUtil";

    public static Drawable getAppIcon(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        ApplicationInfo applicationInfo = null;

        try {
            applicationInfo = packageManager.getPackageInfo(packageName, 0).applicationInfo;
            Log.e(TAG,applicationInfo.toString());
            Drawable icon = applicationInfo.loadIcon(packageManager);
            if (icon==null){
                //没有应用图标
                return context.getResources().getDrawable(R.mipmap.ic_launcher);
            }else{
                return icon;
            }
        } catch (PackageManager.NameNotFoundException e) {
            //没有本地应用
            e.printStackTrace();
            Log.e(TAG,"no loacl apk");
            return context.getResources().getDrawable(R.mipmap.ic_launcher);
        }

    }
}
