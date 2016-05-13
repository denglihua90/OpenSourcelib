package com.dlh.opensourcelib.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DownloadFileListener;

import com.dlh.opensourcelib.OpensourceLibApplication;
import com.dlh.opensourcelib.R;
import com.dlh.opensourcelib.bean.AppBean;
import com.dlh.opensourcelib.bean.FavoritesBean;
import com.dlh.opensourcelib.constants.Constants;
import com.dlh.opensourcelib.db.dao.FavoritesBeanDao;
import com.dlh.opensourcelib.utils.FileUtils;
import com.dlh.opensourcelib.view.CBProgressBar;
import com.morgoo.droidplugin.pm.PluginManager;
import com.socks.library.KLog;

import java.io.File;

/**
 * @TODO:APP详情
 * @AUTHOR: dlh
 * @DATE: 2016/4/18
 */
public class DetailsInfoActivity extends AppCompatActivity {

    private android.widget.Button btn, share_btn, favor_btn;
    private AppBean appBean;
    private FavoritesBean favoritesBean;
    private LinearLayout titleLayout;
    private TextView title;
    private LinearLayout githubLayout;
    private TextView github;
    private LinearLayout descLayout;
    private TextView desc;
    private CBProgressBar cbProgressBar;

    public static void toDetailsInfoActivity(Context context, AppBean appBean) {
        Intent intent = new Intent(context, DetailsInfoActivity.class);
        intent.putExtra(Constants.APP_BEAN_KEY, appBean);
        context.startActivity(intent);
    }

    public static void toDetailsInfoActivity(Context context, FavoritesBean favoritesBean) {
        Intent intent = new Intent(context, DetailsInfoActivity.class);
        intent.putExtra(Constants.FAVORITES_BEAN_KEY, favoritesBean);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_iinfo_activity);
        setupToolbar();
        appBean = (AppBean) getIntent().getSerializableExtra(Constants.APP_BEAN_KEY);
        favoritesBean = (FavoritesBean) getIntent().getSerializableExtra(Constants.FAVORITES_BEAN_KEY);
        btn = (Button) findViewById(R.id.btn);
        share_btn = (Button) findViewById(R.id.share_btn);
        favor_btn = (Button) findViewById(R.id.favor_btn);
        favor_btn.setOnClickListener(onClickListener);
        titleLayout = (LinearLayout) findViewById(R.id.title_layout);
        title = (TextView) findViewById(R.id.title);
        githubLayout = (LinearLayout) findViewById(R.id.github_layout);
        githubLayout.setOnClickListener(onClickListener);
        github = (TextView) findViewById(R.id.github);
        descLayout = (LinearLayout) findViewById(R.id.desc_layout);
        desc = (TextView) findViewById(R.id.desc);
        btn.setOnClickListener(onClickListener);
        cbProgressBar = (CBProgressBar) findViewById(R.id.my_progress);
        cbProgressBar.setVisibility(View.GONE);
        btn.setVisibility(View.VISIBLE);
        setView();
    }

    private void setView() {
        if (appBean != null) {
            if (!TextUtils.isEmpty(appBean.getTitle())) {
                title.setText(appBean.getTitle());
            }
            if (!TextUtils.isEmpty(appBean.getDesc())) {
                desc.setText(appBean.getDesc());
            }

            if (!TextUtils.isEmpty(appBean.getGitHub())) {
                github.setText(appBean.getGitHub());
            }
            boolean select = FavoritesBeanDao.getDao().getFavoritesBeanByID(appBean.getProID());
            favor_btn.setSelected(select);

        }
        if (favoritesBean != null) {
            if (!TextUtils.isEmpty(favoritesBean.getTitle())) {
                title.setText(favoritesBean.getTitle());
            }
            if (!TextUtils.isEmpty(favoritesBean.getDesc())) {
                desc.setText(favoritesBean.getDesc());
            }


            if (!TextUtils.isEmpty(favoritesBean.getGitHub())) {
                github.setText(favoritesBean.getGitHub());
            }
            boolean select = FavoritesBeanDao.getDao().getFavoritesBeanByID(favoritesBean.getProID());
            favor_btn.setSelected(select);
        }

    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn:
                    if (appBean != null) {
                        String filePath = DetailsInfoActivity.this.getApplicationContext().getCacheDir() + "/apk/" + appBean.getTitle() + ".apk";
                        if (FileUtils.fileIsExists(filePath)) {
                            startPlunActivity(filePath, appBean.getPackageInfo());
                        } else {
                            //允许设置下载文件的存储路径，默认下载文件的目录为：context.getApplicationContext().getCacheDir()+"/bmob/"
//                        File saveFile = new File(Environment.getExternalStorageDirectory(), bmobFile.getFilename());
                            cbProgressBar.setVisibility(View.VISIBLE);
                            if (btn.getVisibility() == View.VISIBLE)
                                btn.setVisibility(View.GONE);
                            File saveFile = new File(OpensourceLibApplication.application.getCacheDir() + "/apk/", appBean.getTitle() + ".apk");
                            BmobFile bmobFile = appBean.getPlun();
                            bmobFile.download(DetailsInfoActivity.this, saveFile, new DownloadFileListener() {
                                @Override
                                public void onProgress(Integer progress, long total) {
                                    cbProgressBar.updateProgress(progress);
                                    super.onProgress(progress, total);
                                }

                                @Override
                                public void onSuccess(String s) {
                                    KLog.i("dlh", s);
                                    startPlunActivity(s, appBean.getPackageInfo());
                                    cbProgressBar.setVisibility(View.GONE);
                                    if (btn.getVisibility() == View.GONE)
                                        btn.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onFailure(int i, String s) {
                                    KLog.i("dlh", s);
                                    cbProgressBar.setVisibility(View.GONE);
                                    if (btn.getVisibility() == View.GONE)
                                        btn.setVisibility(View.VISIBLE);
                                }

                            });
                        }
                    } else if (favoritesBean != null) {
                        String filePath = DetailsInfoActivity.this.getApplicationContext().getCacheDir() + "/apk/" + favoritesBean.getTitle() + ".apk";
                        if (FileUtils.fileIsExists(filePath)) {
                            startPlunActivity(filePath, favoritesBean.getPackageInfo());
                        } else {
                            File saveFile = new File(OpensourceLibApplication.application.getCacheDir() + "/apk/", favoritesBean.getTitle() + ".apk");
                            cbProgressBar.setVisibility(View.VISIBLE);
                            if (btn.getVisibility() == View.VISIBLE)
                                btn.setVisibility(View.GONE);
                            BmobFile bmobfile = new BmobFile(favoritesBean.getTitle(), "", favoritesBean.getPlunURL());
                            bmobfile.download(DetailsInfoActivity.this, saveFile, new DownloadFileListener() {
                                @Override
                                public void onProgress(Integer progress, long total) {
                                    cbProgressBar.updateProgress(progress);
                                    super.onProgress(progress, total);
                                }

                                @Override
                                public void onSuccess(String s) {
                                    KLog.i("dlh", s);
                                    startPlunActivity(s, favoritesBean.getPackageInfo());
                                    cbProgressBar.setVisibility(View.GONE);
                                    if (btn.getVisibility() == View.GONE)
                                        btn.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onFailure(int i, String s) {
                                    KLog.i("dlh", s);
                                    cbProgressBar.setVisibility(View.GONE);
                                    if (btn.getVisibility() == View.GONE)
                                        btn.setVisibility(View.VISIBLE);
                                }

                            });
                        }

                    }
                    break;
                case R.id.github_layout:


                    break;
                case R.id.favor_btn:
                    if (appBean != null) {
                        boolean is = FavoritesBeanDao.getDao().getFavoritesBeanByID(appBean.getProID());
                        if (is) {
                            FavoritesBeanDao.getDao().deleteByID(appBean.getProID());
                            favor_btn.setSelected(false);
                        } else {
                            favor_btn.setSelected(true);
                            FavoritesBean favoritesBean = new FavoritesBean();
                            favoritesBean.setProID(appBean.getProID());
                            favoritesBean.setDesc(appBean.getDesc());
                            favoritesBean.setGitHub(appBean.getGitHub());
                            favoritesBean.setPackageInfo(appBean.getPackageInfo());
                            favoritesBean.setTitle(appBean.getTitle());
                            favoritesBean.setType(appBean.getType());
                            favoritesBean.setPlunURL(appBean.getPlun().getFileUrl(OpensourceLibApplication.application));
                            favoritesBean.setThumbFileURL(appBean.getThumbFile().getFileUrl(OpensourceLibApplication.application));
                            FavoritesBeanDao.getDao().save(favoritesBean);
//                        List<FavoritesBean> list = FavoritesBeanDao.getDao().queryAll();
//                        for (int i = 0; i < list.size(); i++) {
//                            Log.i("dlh", list.get(i).getTitle().toString());
//                        }
                        }


                    } else if (favoritesBean != null) {
                        FavoritesBeanDao.getDao().deleteByID(favoritesBean.getProID());
                        favor_btn.setSelected(false);
                    }
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
