package com.dlh.opensourcelib.utils;

import android.content.Context;

import com.dlh.opensourcelib.constants.Constants;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;

/**
 * Created by dlh on 16/5/19.
 */
public class ShareQQorWeixinUtils {
    private Context mContext;
    private IWXAPI api;
    public static Tencent mTencent;

    public ShareQQorWeixinUtils(Context context) {
        mContext = context;
        api = WXAPIFactory.createWXAPI(mContext, Constants.WENXIN_APP_ID, false);
        api.registerApp(Constants.WENXIN_APP_ID);
        if (mTencent == null) {
            mTencent = Tencent.createInstance(Constants.QQ_APP_ID, mContext);
        }
    }
}
