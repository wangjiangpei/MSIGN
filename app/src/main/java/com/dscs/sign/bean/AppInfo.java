package com.dscs.sign.bean;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

/**
 * Created by Administrator on 2017/7/19 0019.
 */

public class AppInfo implements Comparable<AppInfo> {
    public String appName; // 应用名
    public String packageName; // 包名
    public String versionName; // 版本名
    public int versionCode = 0; // 版本号
    public Drawable appIcon = null; // 应用图标
    public long firstInstallTime;


    @Override
    public String toString() {
        return appName + " , " + packageName + " ," + versionName + " ," + versionCode;
    }

    @Override
    public int compareTo(@NonNull AppInfo appInfo) {
        if (this.firstInstallTime > appInfo.firstInstallTime)
            return -1;
        else if (this.firstInstallTime == appInfo.firstInstallTime)
            return 0;
        else return 1;
    }
}
