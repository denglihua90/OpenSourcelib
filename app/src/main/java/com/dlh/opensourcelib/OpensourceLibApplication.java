package com.dlh.opensourcelib;

import android.app.Application;
import android.content.Context;

import com.dlh.opensourcelib.constants.Constants;
import com.dlh.opensourcelib.utils.NetWorkUtil;
import com.morgoo.droidplugin.PluginApplication;
import com.morgoo.droidplugin.PluginHelper;
import com.socks.library.KLog;
import com.umeng.analytics.MobclickAgent;

import net.sqlcipher.database.SQLiteDatabase;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.update.BmobUpdateAgent;

/**
 * @TODO:
 * @@ -10,9 +14,15 @@ import com.morgoo.droidplugin.PluginApplication;
 * @DATE: 2016/4/19
 */
public class OpensourceLibApplication extends Application {
    public static OpensourceLibApplication application;
    public static boolean isNetWork = true;

    @Override
    public void onCreate() {
        super.onCreate();
        PluginHelper.getInstance().applicationOnCreate(getBaseContext());
        application = this;
        Bmob.initialize(application, Constants.BMOB_APP_ID);
//        BmobUpdateAgent.initAppVersion(this);
        SQLiteDatabase.loadLibs(application);
        KLog.init(BuildConfig.LOG_DEBUG);
        if (NetWorkUtil.getNetType(application) == NetWorkUtil.NetType.NONET) {
            isNetWork = false;
        } else {
            isNetWork = NetWorkUtil.isNetWorkAvailable(application);
        }

        MobclickAgent.setDebugMode(true);
        // SDK在统计Fragment时，需要关闭Activity自带的页面统计，
        // 然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
        MobclickAgent.openActivityDurationTrack(false);
        // MobclickAgent.setAutoLocation(true);
        // MobclickAgent.setSessionContinueMillis(1000);
        // MobclickAgent.startWithConfigure(
        // new UMAnalyticsConfig(mContext, "4f83c5d852701564c0000011", "Umeng", EScenarioType.E_UM_NORMAL));
        MobclickAgent.setScenarioType(application, MobclickAgent.EScenarioType.E_UM_NORMAL);
    }

    @Override
    protected void attachBaseContext(Context base) {
        PluginHelper.getInstance().applicationAttachBaseContext(base);
        super.attachBaseContext(base);
    }
}