package com.dscs.sign.activity.presenter;

import android.content.Context;

import com.dscs.sign.activity.IView;
import com.dscs.sign.activity.model.PackModel;
import com.dscs.sign.handler.MyHandler;
import com.dscs.sign.utils.MTextUrils;

import static com.dscs.sign.utils.MTextUrils.exChange;

/**
 *
 */

public class PackPresenter {
    private IView iView;
    private PackModel iModel;
    public MyHandler handler;

    public PackPresenter(IView iView) {
        this.iView = iView;
        handler = new MyHandler(iView);
        iModel = new PackModel(this);
    }

    public void loadData(final int tag) {
        iModel.loadData(tag, iView);
    }

    public String getSignInfo(String packageName) {
        //小写
        String sign = iModel.getSign(packageName, iView);
        //大写
        String sign1 = exChange(sign);
        //带冒号小写
        String sign2 = MTextUrils.maohao(sign);
        //带冒号大写
        String sign3 = MTextUrils.maohao(exChange(sign));
        final StringBuilder sb = new StringBuilder();

        sb.append(sign).append("\n")
                .append(sign1).append("\n")
                .append(sign2).append("\n")
                .append(sign3);
        return sb.toString();
    }

    public void showError(String s) {
        iView.showError(s);
    }

    public boolean isDebug(String packageName) {
        return iModel.isDebuggable(iModel.getRawSignature((Context) iView, packageName));
    }
}
