package com.dscs.sign;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dscs.sign.activity.IView;
import com.dscs.sign.activity.MainActivity;
import com.dscs.sign.activity.presenter.PackPresenter;

public class WelComeActivity extends AppCompatActivity implements IView{

    private PackPresenter packPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wel_come);
        packPresenter = new PackPresenter(this);
        Intent intext = new Intent(this, MainActivity.class);
        startActivity(intext);
//        packPresenter.loadData(3);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void upData(Object obj) {
       Intent intext = new Intent(this, MainActivity.class);
        startActivity(intext);
    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void showMessage(String msg) {

    }

    @Override
    public void showDialog(String msg) {

    }
}
