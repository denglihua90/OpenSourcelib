package com.dlh.opensourcelib.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dlh.opensourcelib.BuildConfig;
import com.dlh.opensourcelib.R;
import com.dlh.opensourcelib.fragment.ContentFragment;
import com.dlh.opensourcelib.fragment.MyMenuFragment;
import com.mxn.soul.flowingdrawer_core.FlowingView;
import com.mxn.soul.flowingdrawer_core.LeftDrawerLayout;
import com.umeng.analytics.MobclickAgent;

import cn.bmob.v3.update.BmobUpdateAgent;

public class MainActivity extends AppCompatActivity {
    private LeftDrawerLayout mLeftDrawerLayout;
    private String Tag = MainActivity.class.getSimpleName();

    private Fragment mContentFragment = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolbar();
//        BmobUpdateAgent.initAppVersion(this);
        BmobUpdateAgent.setUpdateOnlyWifi(false);
        BmobUpdateAgent.update(this);
        mLeftDrawerLayout = (LeftDrawerLayout) findViewById(R.id.id_drawerlayout);
        FragmentManager fm = getSupportFragmentManager();
        Fragment mMenuFragment = fm.findFragmentById(R.id.id_container_menu);
        FlowingView mFlowingView = (FlowingView) findViewById(R.id.sv);
        if (mMenuFragment == null) {
            fm.beginTransaction().add(R.id.id_container_menu, mMenuFragment = new MyMenuFragment()).commit();
        }
        mLeftDrawerLayout.setFluidView(mFlowingView);
        mLeftDrawerLayout.setMenuFragment((MyMenuFragment) mMenuFragment);

        mContentFragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (mContentFragment == null) {
            mContentFragment = new ContentFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.content_frame, mContentFragment).commit();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(Tag);
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(Tag);
        MobclickAgent.onPause(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    protected void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_white);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLeftDrawerLayout.toggle();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ab_search:
                        if (mContentFragment != null) {
                            ((ContentFragment) mContentFragment).showSweetSeet();
                        }
//                        Toast.makeText(MainActivity.this, "点击搜索", Toast.LENGTH_SHORT).show();
                        Log.d("MainActivity", "点击搜索");
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mLeftDrawerLayout.isShownMenu()) {
            mLeftDrawerLayout.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }


}
