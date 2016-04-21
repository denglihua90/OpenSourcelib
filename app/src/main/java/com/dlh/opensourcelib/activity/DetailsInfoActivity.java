package com.dlh.opensourcelib.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.dlh.opensourcelib.R;
import com.dlh.opensourcelib.bean.AppBean;
import com.dlh.opensourcelib.constants.Constants;
import com.dlh.opensourcelib.utils.FileUtils;
import com.dlh.opensourcelib.view.TextLableContentView;
import com.morgoo.droidplugin.pm.PluginManager;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DownloadFileListener;

/**
 * @TODO:
 * @AUTHOR: dlh
 * @DATE: 2016/4/18
 */
public class DetailsInfoActivity extends AppCompatActivity {

    private android.widget.Button btn;
    private com.dlh.opensourcelib.view.TextLableContentView title;
    private com.dlh.opensourcelib.view.TextLableContentView github;
    private com.dlh.opensourcelib.view.TextLableContentView desc;
    private AppBean appBean;

    public static void toDetailsInfoActivity(Context context, AppBean appBean) {
        Intent intent = new Intent(context, DetailsInfoActivity.class);
        intent.putExtra(Constants.APP_BEAN_KEY, appBean);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_iinfo_activity);
        setupToolbar();

        this.desc = (TextLableContentView) findViewById(R.id.desc);
        this.github = (TextLableContentView) findViewById(R.id.github);
        this.title = (TextLableContentView) findViewById(R.id.title);
        appBean = (AppBean) getIntent().getSerializableExtra(Constants.APP_BEAN_KEY);
        this.btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(onClickListener);
        setView();
    }

    private void setView() {
        title.setLeftText(R.string.info_title);
        title.setRightTvText(appBean.getTitle());
        github.setLeftText(R.string.info_github);
        github.setRightTvText(appBean.getGitHub());
        desc.setLeftText(R.string.info_desc);
        desc.getLeftTv().setPadding(6, 6, 6, 6);
//        desc.getLinearLayout().setOrientation(LinearLayout.VERTICAL);
        desc.setRightTvText(appBean.getDesc());
    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String filePath = DetailsInfoActivity.this.getApplicationContext().getCacheDir() + "/apk/" + appBean.getTitle() + ".apk";
            if (FileUtils.fileIsExists(filePath)) {
                startPlunActivity(filePath, appBean.getPackageInfo());
            } else {
                //允许设置下载文件的存储路径，默认下载文件的目录为：context.getApplicationContext().getCacheDir()+"/bmob/"
//                File saveFile = new File(Environment.getExternalStorageDirectory(), bmobFile.getFilename());
                File saveFile = new File(DetailsInfoActivity.this.getApplicationContext().getCacheDir() + "/apk/", appBean.getTitle() + ".apk");
                BmobFile bmobFile = appBean.getPlun();
                bmobFile.download(DetailsInfoActivity.this, saveFile, new DownloadFileListener() {
                    @Override
                    public void onSuccess(String s) {
                        Log.i("dlh", s);
                        startPlunActivity(s, appBean.getPackageInfo());
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Log.i("dlh", s);
                    }
                });
            }


        }
    };

    protected void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void startPlunActivity(String filepath, String packageInfo) {

        try {
            int i = PluginManager.getInstance().installPackage(filepath, 0);
            if (i != PluginManager.INSTALL_FAILED_NO_REQUESTEDPERMISSION) {
                PackageManager pm = DetailsInfoActivity.this.getPackageManager();
//                "com.bigkoo.convenientbannerdemo"
                Intent intent = pm.getLaunchIntentForPackage(packageInfo);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {

            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


}
