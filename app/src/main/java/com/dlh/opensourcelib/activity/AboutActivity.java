package com.dlh.opensourcelib.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.dlh.opensourcelib.R;
import com.dlh.opensourcelib.utils.ShareQQorWeixinUtils;

/**
 * @TODO:
 * @AUTHOR: dlh
 * @DATE: 2016/5/17
 */
public class AboutActivity extends AppCompatActivity {
    private TextView androidLearn;
    private TextView android_news1;
    private TextView android_news3;

    private ShareQQorWeixinUtils shareQQorWeixinUtils = null;

    public static void toAboutActivity(Context context) {
        Intent intent = new Intent(context, AboutActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity_layout);
        shareQQorWeixinUtils = new ShareQQorWeixinUtils(this);
        androidLearn = (TextView) findViewById(R.id.androidLearn);
        androidLearn.setOnClickListener(onClickListener);

        android_news1 = (TextView) findViewById(R.id.android_news1);
        android_news1.setOnClickListener(onClickListener);

        android_news3 = (TextView) findViewById(R.id.android_news3);
        android_news3.setOnClickListener(onClickListener);
        setupToolbar();
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.androidLearn:
                    shareQQorWeixinUtils.joinQQGroup(AboutActivity.this.getResources().getString(R.string.androidLearn_key));
                    break;
                case R.id.android_news1:
                    shareQQorWeixinUtils.joinQQGroup(AboutActivity.this.getResources().getString(R.string.android_news1_key));
                    break;
                case R.id.android_news3:
                    shareQQorWeixinUtils.joinQQGroup(AboutActivity.this.getResources().getString(R.string.android_news3_key));
                    break;
            }

        }
    };

    protected void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.page_title_back_btn_bg);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
