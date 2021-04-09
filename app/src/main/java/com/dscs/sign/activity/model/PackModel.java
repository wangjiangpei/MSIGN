package com.dscs.sign.activity.model;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

import com.dscs.sign.activity.IView;
import com.dscs.sign.activity.presenter.PackPresenter;
import com.dscs.sign.bean.AppInfo;
import com.dscs.sign.utils.ApplicationInfoUtil;
import com.dscs.sign.utils.LogUtils;

import java.io.ByteArrayInputStream;
import java.security.MessageDigest;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.security.auth.x500.X500Principal;

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
        return signatureSHA1(arrayOfSignature);
    }
    // 如需要小写则把ABCDEF改成小写
    private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
    /**
     * 检测应用程序是否是用"CN=Android Debug,O=Android,C=US"的debug信息来签名的
     * 判断签名是debug签名还是release签名
     */
    private final static X500Principal DEBUG_DN = new X500Principal(
            "CN=Android Debug,O=Android,C=US");
    /**
     * 进行转换
     */
    public static String toHexString(byte[] bData) {
        StringBuilder sb = new StringBuilder(bData.length * 2);
        for (int i = 0; i < bData.length; i++) {
            sb.append(HEX_DIGITS[(bData[i] & 0xf0) >>> 4]);
            sb.append(HEX_DIGITS[bData[i] & 0x0f]);
        }
        return sb.toString();
    }
    /**
     * SHA1
     */
    public static String signatureSHA1(Signature[] signatures) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            if (signatures != null) {
                for (Signature s : signatures)
                    digest.update(s.toByteArray());
            }
            return toHexString(digest.digest());
        } catch (Exception e) {
            return "";
        }
    }
    /**
     * 判断签名是debug签名还是release签名
     * @return true = 开发(debug.keystore)，false = 上线发布（非.android默认debug.keystore）
     */
    public  boolean isDebuggable(Signature[] signatures) {
        // 判断是否默认key(默认是)
        boolean debuggable = true;
        try {
            for (int i = 0, c = signatures.length; i < c; i++) {
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                ByteArrayInputStream stream = new ByteArrayInputStream(signatures[i].toByteArray());
                X509Certificate cert = (X509Certificate) cf.generateCertificate(stream);
                debuggable = cert.getSubjectX500Principal().equals(DEBUG_DN);
                if (debuggable) {
                    break;
                }
            }
        } catch (Exception e) {
        }
        return debuggable;
    }
    public  Signature[] getRawSignature(Context paramContext, String paramString) {
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
