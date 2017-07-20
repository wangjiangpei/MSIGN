package com.dscs.sign.utils;

import android.text.TextUtils;
import android.util.Log;


public class LogUtils {
    private static String TAG = "T_LOG";
    private static Boolean LOG_SHOW = true;

    private static String generateTag() {
        StackTraceElement caller = new Throwable().getStackTrace()[2];
        String tag = "%s.%s(Lins:%d)";
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        tag = String.format(tag, callerClazzName, caller.getMethodName(), caller.getLineNumber());
        tag = TextUtils.isEmpty(TAG) ? tag : TAG + ":" + tag;
        return tag;
    }

    public static void d(String content) {
        if (!LOG_SHOW) return;
        Log.d(generateTag(), content);
    }


    public static void i(String content) {
        if (!LOG_SHOW) return;
        Log.i(generateTag(), content);
    }

    public static void e(String context) {
        if (!LOG_SHOW) return;
        Log.e(generateTag(), context);
    }

    public static void e(Throwable e) {
        if (!LOG_SHOW) return;
        Log.e(generateTag(), e.toString());
    }
}