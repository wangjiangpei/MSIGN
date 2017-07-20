package com.dscs.sign.handler;

import android.os.Handler;
import android.os.Message;

import com.dscs.sign.activity.IView;
import com.dscs.sign.bean.AppInfo;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 *
 */

public class MyHandler extends Handler {
    WeakReference<IView> weak;
    public MyHandler(IView mainActivity) {
        weak = new WeakReference<IView>(mainActivity);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (weak.get()!=null){
            if (msg.obj!=null){
                weak.get().upData((List<AppInfo>) msg.obj);
            }else{
                weak.get().showError("获取数据失败");
            }
        }
    }
}
