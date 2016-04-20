package com.dlh.opensourcelib.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.alibaba.fastjson.JSON;
import com.dlh.opensourcelib.R;
import com.dlh.opensourcelib.adapter.AppBeanAdapter;
import com.dlh.opensourcelib.bean.AppBean;
import com.dlh.opensourcelib.fragment.MyMenuFragment;
import com.mxn.soul.flowingdrawer_core.FlowingView;
import com.mxn.soul.flowingdrawer_core.LeftDrawerLayout;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindCallback;

public class MainActivity extends AppCompatActivity {
    private GridView listGridview;

    private AppBeanAdapter mAppBeanAdapter;

    private LeftDrawerLayout mLeftDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolbar();
        mLeftDrawerLayout = (LeftDrawerLayout) findViewById(R.id.id_drawerlayout);
        FragmentManager fm = getSupportFragmentManager();
        Fragment mMenuFragment = fm.findFragmentById(R.id.id_container_menu);
        FlowingView mFlowingView = (FlowingView) findViewById(R.id.sv);
        if (mMenuFragment == null) {
            fm.beginTransaction().add(R.id.id_container_menu, mMenuFragment = new MyMenuFragment()).commit();
        }
        mLeftDrawerLayout.setFluidView(mFlowingView);
        mLeftDrawerLayout.setMenuFragment((MyMenuFragment) mMenuFragment);

        listGridview = (GridView) findViewById(R.id.list_gridview);
        mAppBeanAdapter = new AppBeanAdapter(this);
        listGridview.setAdapter(mAppBeanAdapter);
//        adapter = new Adapter<AppBean>(this, R.layout.adapter_item) {
//            @Override
//            protected void convert(AdapterHelper adapterHelper, AppBean appBean) {
//                adapterHelper.setText(R.id.tv, appBean.getTitle());
//                CubeImageView mImageView = (CubeImageView) adapterHelper.getItemView().findViewById(R.id.iv);
//                mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                LinearLayout.LayoutParams lyp = new LinearLayout.LayoutParams(sGirdImageSize, sGirdImageSize);
//                mImageView.setLayoutParams(lyp);
//                Glide.with(MainActivity.this).load(appBean.getThumbFile().getFileUrl(MainActivity.this)).placeholder(R.drawable.plugin_activity_loading).error(R.drawable.plugin_activity_loading).into(mImageView);
//            }
//        };
//        listGridview.setAdapter(adapter);
        listGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppBean appBean = (AppBean) parent.getAdapter().getItem(position);
                DetailsInfoActivity.toDetailsInfoActivity(MainActivity.this, appBean);
            }
        });
        queryData();
    }

    /**
     * 查询数据
     */
    public void queryData() {
        BmobQuery query = new BmobQuery(AppBean.table);
        query.findObjects(this, new FindCallback() {
            @Override
            public void onSuccess(JSONArray arg0) {
                //注意：查询的结果是JSONArray,需要自行解析
//                showToast("查询成功:" + arg0.length());
                Log.i("dlh", "查询成功:" + arg0.toString());

                List<AppBean> list = JSON.parseArray(arg0.toString(), AppBean.class);
                for (int i = 0; i < list.size(); i++) {
                    AppBean appBean = list.get(i);
                    Log.i("dlh", "appBean:" + appBean.getTitle());
                    Log.i("dlh", "appBean:" + appBean.getDesc());
                    Log.i("dlh", "appBean:" + appBean.getGitHub());
                    Log.i("dlh", "appBean:getPlun-->" + appBean.getPlun());
//                    Log.i("dlh", "appBean:getFilename()--->" + appBean.getPlun().getFilename());
                    Log.i("dlh", "appBean:getFileUrl--->" + appBean.getPlun().getFileUrl(MainActivity.this));
                    Log.i("dlh", "appBean:getThumbFile--->" + appBean.getThumbFile().getFileUrl(MainActivity.this));
                }
                mAppBeanAdapter.setList((ArrayList) list);
            }

            @Override
            public void onFailure(int arg0, String arg1) {
//                showToast("查询失败:" + arg1);
                Log.i("dlh", "查询失败:" + arg1);
            }
        });
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
