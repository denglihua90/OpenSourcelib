package com.dlh.opensourcelib.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.dlh.opensourcelib.R;
import com.dlh.opensourcelib.bean.AppBean;
import com.dlh.opensourcelib.utils.DlhDisplay;

public class AppBeanAdapter extends ArrayListAdapter<AppBean> {
    private static final int sGirdImageSize = (DlhDisplay.SCREEN_WIDTH_PIXELS - DlhDisplay.dp2px(300)) / 2;
    private Context mContext;

    public AppBeanAdapter(Activity context) {
        super(context);
        mContext = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.adapter_item, null);
            holder.iv = (ImageView) convertView.findViewById(R.id.iv);
//            LinearLayout.LayoutParams lyp = new LinearLayout.LayoutParams(sGirdImageSize, sGirdImageSize);
//            holder.iv.setLayoutParams(lyp);
            holder.tv = (TextView) convertView.findViewById(R.id.tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        AppBean appBean = mList.get(position);
        if (appBean != null) {
            if (appBean.getThumbFile() != null) {

                Glide.with(mContext).load(appBean.getThumbFile().getFileUrl(mContext)).placeholder(R.drawable.plugin_activity_loading).error(R.drawable.plugin_activity_loading).into(holder.iv);
            }
            if (!TextUtils.isEmpty(appBean.getTitle())) {
                holder.tv.setText(appBean.getTitle());
            }
        }
        return convertView;
    }

    static class ViewHolder {
        public ImageView iv;
        public TextView tv;

    }

}
