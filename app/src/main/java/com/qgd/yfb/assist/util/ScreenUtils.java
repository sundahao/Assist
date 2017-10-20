package com.qgd.yfb.assist.util;

import android.content.Context;

/**
 * Created by Administrator on 2017/5/27.
 */

public class ScreenUtils {

    public static int dip2px(Context context, float dpValue) {
        try {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dpValue * scale + 0.5f);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (int) dpValue;
    }

    public static int px2dip(Context context, float pxValue) {
        try {
            /**获得屏幕分辨率**/
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (pxValue / scale + 0.5f);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (int) pxValue;
    }
}
