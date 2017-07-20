package com.dscs.sign;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
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

import com.dscs.sign.adapter.AppInfoAdapter;
import com.dscs.sign.bean.AppInfo;
import com.dscs.sign.handler.MyHandler;
import com.dscs.sign.tools.Md5;
import com.dscs.sign.utils.ApplicationInfoUtil;
import com.dscs.sign.utils.LogUtils;
import com.dscs.sign.utils.MTextUrils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dscs.sign.utils.MTextUrils.exChange;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private List<AppInfo> appList = new ArrayList<>();
    private AppInfoAdapter adapter;
    private MyHandler handler;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolBar);
        handler = new MyHandler(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("加载中...");
        initListView();

        initDate(3);
    }

    private void initDate(final int tag) {
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                appList.clear();
                switch (tag) {
                    case 1:
                        appList.addAll(ApplicationInfoUtil.getAllProgramInfo(appList, MainActivity.this));
                        break;
                    case 2:
                        appList.addAll(ApplicationInfoUtil.getAllSystemProgramInfo(MainActivity.this));
                        break;
                    case 3:
                        appList.addAll(ApplicationInfoUtil.getAllNonsystemProgramInfo(MainActivity.this));
                        break;
                }
                handler.sendMessage(handler.obtainMessage(1, appList));
            }
        }).start();
    }

    public void upData(List<AppInfo> appList) {
        progressDialog.dismiss();
        Collections.sort(appList);
        adapter.notifyDataSetInvalidated();
    }

    private void initListView() {
        adapter = new AppInfoAdapter(this, appList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                show(i);
            }
        });
    }

    ;

    private void show(int i) {
        //小写
        String sign = getSign(appList.get(i).packageName);
        //大写
        String sign1 = MTextUrils.exChange(sign);
        //带冒号小写
        String sign2 = MTextUrils.maohao(sign);
        //带冒号大写
        String sign3 = MTextUrils.maohao(exChange(sign));
        final StringBuilder sb = new StringBuilder();
        sb.append(sign).append("\n")
                .append(sign1).append("\n")
                .append(sign2).append("\n")
                .append(sign3);
        new AlertDialog.Builder(MainActivity.this).setTitle("SIGN")
                .setMessage(sb.toString())
                .setNegativeButton("確定", null)
                .setPositiveButton("分享", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("image/*");
                        intent.putExtra(Intent.EXTRA_SUBJECT, "发送到");
                        intent.putExtra(Intent.EXTRA_TEXT, sb.toString());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(Intent.createChooser(intent, getTitle()));
                    }
                })
                .show();
    }

    private String getSign(String packageName) {
        Signature[] arrayOfSignature = getRawSignature(this, packageName);
        if ((arrayOfSignature == null) || (arrayOfSignature.length == 0)) {
            LogUtils.i("signs is null");
            return "";
        }
        return Md5.getMessageDigest(arrayOfSignature[0].toByteArray());
    }

    private Signature[] getRawSignature(Context paramContext, String paramString) {
        if ((paramString == null) || (paramString.length() == 0)) {
            LogUtils.i("获取签名失败，包名为 null");
            return null;
        }
        PackageManager localPackageManager = paramContext.getPackageManager();
        PackageInfo localPackageInfo;
        try {
            localPackageInfo = localPackageManager.getPackageInfo(paramString, PackageManager.GET_SIGNATURES);
            if (localPackageInfo == null) {
                LogUtils.i("信息为 null, 包名 = " + paramString);
                return null;
            }
        } catch (PackageManager.NameNotFoundException localNameNotFoundException) {
            LogUtils.i("包名没有找到...");
            return null;
        }
        return localPackageInfo.signatures;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.appInfo_all:
                initDate(1);
                break;
            case R.id.appInfo_system:
                initDate(2);
                break;
            case R.id.appInfo_nonsystem:
                initDate(3);
                break;
        }
        return false;
    }
}
