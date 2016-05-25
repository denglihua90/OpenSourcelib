package com.dlh.opensourcelib.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import com.dlh.opensourcelib.R;
import com.dlh.opensourcelib.constants.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXTextObject;
import com.tencent.mm.sdk.openapi.WXWebpageObject;
import com.tencent.mm.sdk.platformtools.Util;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.ArrayList;

/**
 * Created by dlh on 16/5/19.
 */
public class ShareQQorWeixinUtils {
    private Context mContext;
    private IWXAPI api;
    public static Tencent mTencent;

    public ShareQQorWeixinUtils(Context context) {
        mContext = context;
        api = WXAPIFactory.createWXAPI(mContext, Constants.WENXIN_APP_ID, true);
        api.registerApp(Constants.WENXIN_APP_ID);
        mTencent = Tencent.createInstance(Constants.QQ_APP_ID, mContext);
    }

    /**
     * 分享消息到QQ（无需QQ登录）
     */
    public void shareAppToQQ() {
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, mContext.getResources().getString(R.string.app_name));
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, mContext.getResources().getString(R.string.app_summary));
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "http://androidlearn.bmob.cn/");
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://www.bmob.cn/uploads/attached/app/logo/20160515/4350e1fd-1d21-85e7-9e0b-d98eb29e2807.png");
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, mContext.getResources().getString(R.string.app_name));
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, 0x00);
        mTencent.shareToQQ((Activity) mContext, params, new IUiListener() {
            @Override
            public void onComplete(Object o) {

            }

            @Override
            public void onError(UiError uiError) {

            }

            @Override
            public void onCancel() {

            }
        });
    }


    /**
     * 分享文本消息到QQ（无需QQ登录）
     */
    public void shareTextToQQ(String text) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        sendIntent.setPackage("com.tencent.mobileqq");
        // List<ResolveInfo> list= getShareTargets(mContext);
        try {
            sendIntent.setClassName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity");
            Intent chooserIntent = Intent.createChooser(sendIntent, "选择分享途径");
            if (chooserIntent == null) {
                return;
            }
            mContext.startActivity(chooserIntent);
        } catch (Exception e) {
            mContext.startActivity(sendIntent);
        }
    }

    /**
     * 分享消息到QZone（无需QQ登录）
     */
    public void shareAppToQZone() {
        ArrayList<String> imageUrls = new ArrayList<String>();
        imageUrls.add("http://www.bmob.cn/uploads/attached/app/logo/20160515/4350e1fd-1d21-85e7-9e0b-d98eb29e2807.png");
        final Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, mContext.getResources().getString(R.string.app_name));
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, mContext.getResources().getString(R.string.app_summary));
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, "http://androidlearn.bmob.cn/");
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
        params.putString(QzoneShare.SHARE_TO_QQ_APP_NAME, mContext.getResources().getString(R.string.app_name));
//        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, 0x00);
        mTencent.shareToQzone((Activity) mContext, params, new IUiListener() {
            @Override
            public void onComplete(Object o) {

            }

            @Override
            public void onError(UiError uiError) {

            }

            @Override
            public void onCancel() {

            }
        });
    }


    /**
     * 分享APP消息到微信
     */
    public void shareAppToWeixin(boolean scene) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = "http://androidlearn.bmob.cn/";
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = mContext.getResources().getString(R.string.app_name);
        msg.description = mContext.getResources().getString(R.string.app_summary);
        Bitmap thumb = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);
        msg.thumbData = Util.bmpToByteArray(thumb, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        //好友列表 或者朋友圈
        req.scene = scene ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);
    }

    /**
     * 分享文本到微信好友
     *
     * @param text
     */
    public void shareTextToWeixin(String text) {
        // 初始化一个WXTextObject对象
        WXTextObject textObj = new WXTextObject();
//        mContext.getResources().getString(R.string.app_name)
        textObj.text = text;
        // 用WXTextObject对象初始化一个WXMediaMessage对象
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        // 发送文本类型的消息时，title字段不起作用
        // msg.title = "Will be ignored";
        msg.description = text;

        // 构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("text"); // transaction字段用于唯一标识一个请求
        req.message = msg;
        //好友列表
        req.scene = SendMessageToWX.Req.WXSceneSession;

        // 调用api接口发送数据到微信
        api.sendReq(req);
    }


    /****************
     * 发起添加群流程。群号：androidLearn(454042538) 的 key 为： mho283hv9w_6RVaQwS8tOWYA3ORfV2U-
     * 调用 joinQQGroup(mho283hv9w_6RVaQwS8tOWYA3ORfV2U-) 即可发起手Q客户端申请加群 androidLearn(454042538)
     * 调用 joinQQGroup(u_7RIf3m7b6vbJyaDIx2iUODrNgxbloF) 即可发起手Q客户端申请加群 android新手③(294927468)
     * 调用 joinQQGroup(F9_Xo0k9vZLG94A2Us3zT9IHIbKNGF9F) 即可发起手Q客户端申请加群 android新手①(474900184)
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
     ******************/
    public boolean joinQQGroup(String key) {
        return mTencent.joinQQGroup((Activity) mContext, key);
    }


    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
