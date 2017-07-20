package com.dscs.sign.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dscs.sign.R;
import com.dscs.sign.bean.AppInfo;
import com.dscs.sign.utils.DateUtil;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */

public class AppInfoAdapter extends BaseAdapter {
    List<AppInfo> appList;
    Context context;
    public AppInfoAdapter(Context context, List<AppInfo> appList) {
        this.context = context.getApplicationContext();
        this.appList = appList;
    }

    @Override
    public int getCount() {
        return appList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder vh;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_appinfo, null);
            vh = new ViewHolder(view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        vh.appIcon.setImageDrawable(appList.get(i).appIcon);
        vh.appName.setText(appList.get(i).appName);
        vh.appPackname.setText(appList.get(i).packageName);
        vh.appTime.setText(
                DateUtil.formateDate(
                        DateUtil.TIME_DATA,new Date(appList.get(i).firstInstallTime)
                )
        );
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.app_icon)
        ImageView appIcon;
        @BindView(R.id.app_name)
        TextView appName;
        @BindView(R.id.app_packname)
        TextView appPackname;
        @BindView(R.id.app_time)
        TextView appTime;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
