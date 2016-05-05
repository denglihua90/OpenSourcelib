package com.dlh.opensourcelib;

import cn.bmob.v3.Bmob;
import com.dlh.opensourcelib.constants.Constants;
import com.morgoo.droidplugin.PluginApplication;

/**
 * @TODO:
 * @AUTHOR: dlh
 * @DATE: 2016/4/19
 */
public class OpensourceLibApplication extends PluginApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this, Constants.BMOB_APP_ID);
    }
}
