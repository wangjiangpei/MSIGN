package com.dscs.sign.activity;

import android.content.Context;

/**
 *
 */

public interface IView {

    public Context getContext();

    public void upData(Object obj);

    public void showError(String msg);

    public void showMessage(String msg);

    public void showDialog(String msg);
}
