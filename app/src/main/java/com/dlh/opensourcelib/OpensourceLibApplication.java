package com.dlh.opensourcelib;

import cn.bmob.v3.Bmob;

import com.dlh.opensourcelib.constants.Constants;
import com.morgoo.droidplugin.PluginApplication;
import com.socks.library.KLog;

import net.sqlcipher.database.SQLiteDatabase;

/**
 * @TODO:
 * @AUTHOR: dlh
 * @DATE: 2016/4/19
 */
public class OpensourceLibApplication extends PluginApplication {
    public static OpensourceLibApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        Bmob.initialize(application, Constants.BMOB_APP_ID);
        SQLiteDatabase.loadLibs(application);
        KLog.init(true);

    }
}
