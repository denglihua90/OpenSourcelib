package com.dlh.opensourcelib.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dlh.opensourcelib.BuildConfig;
import com.dlh.opensourcelib.OpensourceLibApplication;
import com.dlh.opensourcelib.R;
import com.dlh.opensourcelib.bean.AppBean;
import com.dlh.opensourcelib.bean.FavoritesBean;
import com.dlh.opensourcelib.constants.Constants;
import com.dlh.opensourcelib.db.dao.FavoritesBeanDao;
import com.dlh.opensourcelib.utils.FileUtils;
import com.dlh.opensourcelib.utils.ShareQQorWeixinUtils;
import com.dlh.opensourcelib.view.CBProgressBar;
import com.dlh.opensourcelib.view.MMAlert;
import com.morgoo.droidplugin.pm.PluginManager;
import com.socks.library.KLog;
import com.umeng.analytics.MobclickAgent;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.ProgressCallback;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;

/**
 * @TODO:APP详情
 * @AUTHOR: dlh
 * @DATE: 2016/4/18
 */
public class DetailsInfoActivity extends AppCompatActivity implements ServiceConnection {

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

    private ShareQQorWeixinUtils shareQQorWeixinUtils = null;

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
        share_btn.setOnClickListener(onClickListener);
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
        shareQQorWeixinUtils = new ShareQQorWeixinUtils(this);
        setView();


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
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
                            startPlunActivity(filePath);
                        } else {
                            if (OpensourceLibApplication.isNetWork) {
                                //允许设置下载文件的存储路径，默认下载文件的目录为：context.getApplicationContext().getCacheDir()+"/bmob/"
//                        File saveFile = new File(Environment.getExternalStorageDirectory(), bmobFile.getFilename());
                                BmobFile bmobFile = new BmobFile(appBean.getTitle(), "", appBean.getPlun().getFileUrl());
//                                KLog.d("DetailsInfoActivity", "bmobFile.getFileUrl" + bmobFile.getFileUrl());
                                File saveFile = new File(OpensourceLibApplication.application.getCacheDir() + "/apk/", bmobFile.getFilename() + ".apk");
                                download(bmobFile, saveFile);
                            } else {
                                Toast.makeText(DetailsInfoActivity.this, DetailsInfoActivity.this.getResources().getString(R.string.net_hint), Toast.LENGTH_SHORT).show();
                            }

                        }
                    } else if (favoritesBean != null) {
                        String filePath = DetailsInfoActivity.this.getApplicationContext().getCacheDir() + "/apk/" + favoritesBean.getTitle() + ".apk";
                        if (FileUtils.fileIsExists(filePath)) {
                            startPlunActivity(filePath);
                        } else {
                            if (OpensourceLibApplication.isNetWork) {
                                File saveFile = new File(OpensourceLibApplication.application.getCacheDir() + "/apk/", favoritesBean.getTitle() + ".apk");
                                cbProgressBar.setVisibility(View.VISIBLE);
                                if (btn.getVisibility() == View.VISIBLE)
                                    btn.setVisibility(View.GONE);
                                BmobFile bmobfile = new BmobFile(favoritesBean.getTitle(), "", favoritesBean.getPlunURL());
                                download(bmobfile, saveFile);
                            } else {
                                Toast.makeText(DetailsInfoActivity.this, DetailsInfoActivity.this.getResources().getString(R.string.net_hint), Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                    break;
                case R.id.github_layout:
                    Uri uri = Uri.parse(github.getText().toString());
                    Intent it = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(it);

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
                            if (OpensourceLibApplication.isNetWork) {
                                favoritesBean.setPlunURL(appBean.getPlun().getFileUrl());
                                favoritesBean.setThumbFileURL(appBean.getThumbFile().getFileUrl());
                            } else {
                                favoritesBean.setPlunURL(appBean.getPlunURL());
                                favoritesBean.setThumbFileURL(appBean.getThumbFileURL());
                            }
                            FavoritesBeanDao.getDao().save(favoritesBean);
                        }


                    } else if (favoritesBean != null) {
                        FavoritesBeanDao.getDao().deleteByID(favoritesBean.getProID());
                        favor_btn.setSelected(false);
                    }
                    break;

                case R.id.share_btn:
                    MMAlert.showAlert(DetailsInfoActivity.this, DetailsInfoActivity.this.getString(R.string.share_URL), DetailsInfoActivity.this.getResources().getStringArray(R.array.sharearray2), null, new MMAlert.OnAlertSelectId() {
                        @Override
                        public void onClick(int whichButton) {
                            KLog.i("dlh", "whichButton---->" + whichButton);
                            switch (whichButton) {
                                case 0:
                                    if (shareQQorWeixinUtils != null) {
                                        shareQQorWeixinUtils.shareTextToQQ(github.getText().toString());
                                    }
                                    break;
                                case 1:
                                    if (shareQQorWeixinUtils != null) {
                                        shareQQorWeixinUtils.shareTextToWeixin(github.getText().toString());
                                    }
                                    break;
                            }


                        }
                    });
                    break;

            }


        }
    };

    private void download(BmobFile bmobfile, File saveFile) {
        bmobfile.downloadObservable(saveFile, new ProgressCallback() {
            @Override
            public void onProgress(Integer integer, long l) {
                cbProgressBar.setVisibility(View.VISIBLE);
                if (btn.getVisibility() == View.VISIBLE)
                    btn.setVisibility(View.GONE);
                KLog.d("dlh", "integer--->" + integer + "---->:" + l);
                cbProgressBar.updateProgress(integer);
            }
        }).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                cbProgressBar.setVisibility(View.GONE);
                if (btn.getVisibility() == View.GONE)
                    btn.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(Throwable e) {
                cbProgressBar.setVisibility(View.GONE);
                if (btn.getVisibility() == View.GONE)
                    btn.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNext(String s) {
                if (!TextUtils.isEmpty(s)) {
                    startPlunActivity(s);
                }
                KLog.d("dlh", "integer--->" + s);
            }
        });
//        bmobfile.download(saveFile, new DownloadFileListener() {
//            @Override
//            public void onStart() {
//                cbProgressBar.setVisibility(View.VISIBLE);
//                if (btn.getVisibility() == View.VISIBLE)
//                    btn.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void done(String s, BmobException e) {
//                if (e == null) {
//                    startPlunActivity(s, appBean.getPackageInfo());
//                    cbProgressBar.setVisibility(View.GONE);
//                    if (btn.getVisibility() == View.GONE)
//                        btn.setVisibility(View.VISIBLE);
//
//                } else {
//                    cbProgressBar.setVisibility(View.GONE);
//                    if (btn.getVisibility() == View.GONE)
//                        btn.setVisibility(View.VISIBLE);
//                }
//            }
//            @Override
//            public void onProgress(Integer value, long newworkSpeed) {
//                cbProgressBar.updateProgress(value);
//            }
//        });
    }

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

    public void startPlunActivity(String filepath) {
        if (PluginManager.getInstance().isConnected()) {
            startLoad(filepath);
        } else {
            PluginManager.getInstance().addServiceConnection(this);
        }
    }

    private void startLoad(String filepath) {
//        if (ActivityCompat.checkSelfPermission(DetailsInfoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
        PackageManager pm = DetailsInfoActivity.this.getPackageManager();
        final PackageInfo info = pm.getPackageArchiveInfo(filepath, 0);
        try {
            if (PluginManager.getInstance().getPackageInfo(info.packageName, 0) == null) {
                int re = PluginManager.getInstance().installPackage(filepath, 0);
                if (re == PluginManager.INSTALL_FAILED_NO_REQUESTEDPERMISSION) {
                    Toast.makeText(DetailsInfoActivity.this, "启动异常，请重试", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            Intent intent = pm.getLaunchIntentForPackage(info.packageName);
//                Intent.FLAG_ACTIVITY_NEW_TASK= 0x10000000
//                Intent.FLAG_ACTIVITY_CLEAR_TOP = 0x04000000;
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
//        } else {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0x1);
//            }
//        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == 0x1) {
//            if (permissions != null && permissions.length > 0) {
//                for (int i = 0; i < permissions.length; i++) {
//                    String permisson = permissions[i];
//                    int grantResult = grantResults[i];
//                    if (Manifest.permission.READ_EXTERNAL_STORAGE.equals(permisson)) {
//                        if (grantResult == PackageManager.PERMISSION_GRANTED) {
////                            startLoadInner();
//                        } else {
//                            Toast.makeText(DetailsInfoActivity.this, "没有授权，无法使用", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }
//                for (String permisson : permissions) {
//
//                }
//            }
//        }
//    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    @Override
    public void onDestroy() {
        PluginManager.getInstance().removeServiceConnection(this);
        super.onDestroy();
    }


}
