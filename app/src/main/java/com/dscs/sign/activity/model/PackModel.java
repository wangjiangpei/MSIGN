package com.dscs.sign.activity.model;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

import com.dscs.sign.activity.IView;
import com.dscs.sign.activity.presenter.PackPresenter;
import com.dscs.sign.bean.AppInfo;
import com.dscs.sign.tools.Md5;
import com.dscs.sign.utils.ApplicationInfoUtil;
import com.dscs.sign.utils.LogUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 */

public class PackModel implements IModel {
    private List<AppInfo> appList = new ArrayList<>();
    PackPresenter packPresenter;

    public PackModel(PackPresenter packPresenter) {
        this.packPresenter = packPresenter;
    }

    public void loadData(final int tag, final IView iView) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                appList.clear();
                switch (tag) {
                    case 1:
                        appList.addAll(ApplicationInfoUtil.getAllProgramInfo(iView.getContext()));
                        break;
                    case 2:
                        appList.addAll(ApplicationInfoUtil.getAllSystemProgramInfo(iView.getContext()));
                        break;
                    case 3:
                        appList.addAll(ApplicationInfoUtil.getAllNonsystemProgramInfo(iView.getContext()));
                        break;
                }
                LogUtils.i(appList.size()+"條");
                Collections.sort(appList);
                packPresenter.handler.sendMessage(packPresenter.handler.obtainMessage(1, appList));
            }
        }).start();
    }

    public String getSign(String packageName, IView iView) {
        Signature[] arrayOfSignature = getRawSignature(iView.getContext(), packageName);
        if ((arrayOfSignature == null) || (arrayOfSignature.length == 0)) {
            packPresenter.showError("signs is null");
            return "";
        }
        return Md5.getMessageDigest(arrayOfSignature[0].toByteArray());
    }

    private Signature[] getRawSignature(Context paramContext, String paramString) {
        if ((paramString == null) || (paramString.length() == 0)) {
            packPresenter.showError("获取签名失败，包名为 null");
            return null;
        }
        PackageManager localPackageManager = paramContext.getPackageManager();
        PackageInfo localPackageInfo;
        try {
            localPackageInfo = localPackageManager.getPackageInfo(paramString, PackageManager.GET_SIGNATURES);
            if (localPackageInfo == null) {
                packPresenter.showError("信息为 null, 包名 = " + paramString);
                return null;
            }
        } catch (PackageManager.NameNotFoundException localNameNotFoundException) {
            packPresenter.showError("包名没有找到...");
            return null;
        }
        return localPackageInfo.signatures;
    }
}
