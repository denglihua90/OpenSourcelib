package com.dlh.opensourcelib.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dlh.opensourcelib.OpensourceLibApplication;
import com.dlh.opensourcelib.R;
import com.dlh.opensourcelib.activity.AboutActivity;
import com.dlh.opensourcelib.activity.FavoritesActivity;
import com.dlh.opensourcelib.utils.GlideCircleTransform;
import com.dlh.opensourcelib.utils.ShareQQorWeixinUtils;
import com.dlh.opensourcelib.view.MMAlert;
import com.mxn.soul.flowingdrawer_core.MenuFragment;
import com.socks.library.KLog;
import com.umeng.analytics.MobclickAgent;

import cn.bmob.v3.update.BmobUpdateAgent;


public class MyMenuFragment extends MenuFragment {

    private ImageView ivMenuUserProfilePhoto;
    private NavigationView vNavigation;
    private String Tag = MyMenuFragment.class.getSimpleName();

    private ShareQQorWeixinUtils shareQQorWeixinUtils = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shareQQorWeixinUtils = new ShareQQorWeixinUtils(getActivity());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container,
                false);
        vNavigation = (NavigationView) view.findViewById(R.id.vNavigation);
        ivMenuUserProfilePhoto = (ImageView) vNavigation.getHeaderView(0).findViewById(R.id.ivMenuUserProfilePhoto);
        vNavigation.setNavigationItemSelectedListener(onNavigationItemSelectedListener);
        setupHeader();
        return setupReveal(view);
    }

    private void setupHeader() {
        int avatarSize = getResources().getDimensionPixelSize(R.dimen.global_menu_avatar_size);
//        String profilePhoto = getResources().getString(R.string.user_profile_photo);
        Glide.with(getActivity()).load(R.mipmap.ic_launcher).placeholder(R.drawable.img_circle_placeholder).override(avatarSize, avatarSize).centerCrop().transform(new GlideCircleTransform(getActivity()))
                .into(ivMenuUserProfilePhoto);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(Tag); //统计页面，"MainScreen"为页面名称，可自定义
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(Tag);
    }

    private NavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_favorites:
                    FavoritesActivity.toFavoritesActivity(getActivity());
                    break;
                case R.id.menu_update:
                    BmobUpdateAgent.forceUpdate(getActivity());
                    break;
                case R.id.menu_about:
                    AboutActivity.toAboutActivity(getActivity());

                    break;
                case R.id.menu_share:
                    MMAlert.showAlert(getActivity(), getActivity().getString(R.string.share), getActivity().getResources().getStringArray(R.array.sharearray), null, new MMAlert.OnAlertSelectId() {
                        @Override
                        public void onClick(int whichButton) {
                            KLog.i("dlh", "whichButton---->" + whichButton);
                            switch (whichButton) {
                                case 0:
                                    if (shareQQorWeixinUtils != null) {
                                        shareQQorWeixinUtils.shareAppToQQ();
                                    }
                                    break;
                                case 1:
                                    if (shareQQorWeixinUtils != null) {
                                        shareQQorWeixinUtils.shareAppToQZone();
                                    }
                                    break;
                                case 2:
                                    if (shareQQorWeixinUtils != null) {
                                        shareQQorWeixinUtils.shareAppToWeixin(true);
                                    }
                                    break;
                                case 3:
                                    if (shareQQorWeixinUtils != null) {
                                        shareQQorWeixinUtils.shareAppToWeixin(false);
                                    }
                                    break;


                            }


                        }
                    });
                    break;
                case R.id.menu_app_star:
                    Uri uri = Uri.parse("market://details?id=" + OpensourceLibApplication.application.getPackageName());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    break;
            }
            return true;
        }
    };

    public void onOpenMenu() {
//        Toast.makeText(getActivity(), "onOpenMenu", Toast.LENGTH_SHORT).show();
    }

    public void onCloseMenu() {
//        Toast.makeText(getActivity(), "onCloseMenu", Toast.LENGTH_SHORT).show();
    }
}
