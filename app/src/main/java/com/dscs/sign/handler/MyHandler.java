package com.dscs.sign.handler;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.dscs.sign.MainActivity;
import com.dscs.sign.bean.AppInfo;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 *
 */

public class MyHandler extends Handler {
    WeakReference<MainActivity> weak;
    public MyHandler(MainActivity mainActivity) {
        weak = new WeakReference<MainActivity>(mainActivity);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (weak.get()!=null){
            if (msg.obj!=null){
                weak.get().upData((List<AppInfo>) msg.obj);
            }else{
                Toast.makeText(weak.get(), "获取数据失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
