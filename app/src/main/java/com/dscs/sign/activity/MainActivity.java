package com.dscs.sign.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dscs.sign.R;
import com.dscs.sign.activity.presenter.PackPresenter;
import com.dscs.sign.adapter.AppInfoAdapter;
import com.dscs.sign.bean.AppInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements IView {

    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private List<AppInfo> appList = new ArrayList<>();
    private AppInfoAdapter adapter;
    private ProgressDialog progressDialog;
    PackPresenter packPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolBar);
        packPresenter = new PackPresenter(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("加载中...");
        initListView();
        packPresenter.loadData(3);
    }
    @Override
    public void showError(String msg) {
        Toast.makeText(this, "error:"+msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(this, "message:"+msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showDialog(String msg) {

    }

    private void initListView() {
        adapter = new AppInfoAdapter(this, appList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String str = packPresenter.getSignInfo(appList.get(i).packageName);
                new AlertDialog.Builder(MainActivity.this).setTitle("SIGN")
                        .setMessage(str)
                        .setNegativeButton("確定", null)
                        .setPositiveButton("分享", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setType("image/*");
                                intent.putExtra(Intent.EXTRA_SUBJECT, "发送到");
                                intent.putExtra(Intent.EXTRA_TEXT, str);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(Intent.createChooser(intent, getTitle()));
                            }
                        })
                        .show();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.appInfo_all:
                packPresenter.loadData(1);
                break;
            case R.id.appInfo_system:
                packPresenter.loadData(2);
                break;
            case R.id.appInfo_nonsystem:
                packPresenter.loadData(3);
                break;
        }
        return false;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void upData(Object obj) {
        progressDialog.dismiss();
        appList.clear();
        appList.addAll((List<AppInfo>) obj);
        adapter.notifyDataSetInvalidated();
    }
}