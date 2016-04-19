package com.dlh.opensourcelib.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.dlh.opensourcelib.R;
import com.dlh.opensourcelib.bean.AppBean;
import com.dlh.opensourcelib.fragment.MyMenuFragment;
import com.dlh.opensourcelib.utils.HorizontalItemDecoration;
import com.dlh.opensourcelib.utils.VerticalItemDecoration;
import com.mxn.soul.flowingdrawer_core.FlowingView;
import com.mxn.soul.flowingdrawer_core.LeftDrawerLayout;
import com.pacific.adapter.Adapter;
import com.pacific.adapter.AdapterHelper;
import com.pacific.adapter.RecyclerAdapter;
import com.pacific.adapter.RecyclerAdapterHelper;

import org.json.JSONArray;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindCallback;

public class MainActivity extends AppCompatActivity {
    private RecyclerView lv;

//    private Adapter adapter;

    private RecyclerAdapter recyclerAdapter;
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

        lv = (RecyclerView) findViewById(R.id.lv);
        lv.setLayoutManager(new StaggeredGridLayoutManager(3,
                StaggeredGridLayoutManager.VERTICAL));
//        adapter = new Adapter<AppBean>(this, R.layout.adapter_item) {
//            @Override
//            protected void convert(AdapterHelper adapterHelper, AppBean appBean) {
//                adapterHelper.setText(R.id.tv, appBean.getTitle());
//                ImageView iv = (ImageView) adapterHelper.getItemView().findViewById(R.id.iv);
//                Glide.with(MainActivity.this).load(appBean.getThumbFile().getFileUrl(MainActivity.this)).placeholder(R.drawable.plugin_activity_loading).error(R.drawable.plugin_activity_loading).into(iv);
//            }
//        };
//        lv.setAdapter(adapter);
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                AppBean appBean = (AppBean) parent.getAdapter().getItem(position);
//                DetailsInfoActivity.toDetailsInfoActivity(MainActivity.this, appBean);
//            }
//        });

        recyclerAdapter = new RecyclerAdapter<AppBean>(this, R.layout.adapter_item) {
            @Override
            protected void convert(RecyclerAdapterHelper recyclerAdapterHelper, AppBean appBean) {
                recyclerAdapterHelper.setText(R.id.tv, appBean.getTitle());
                ImageView iv = (ImageView) recyclerAdapterHelper.getItemView().findViewById(R.id.iv);
                Glide.with(MainActivity.this).load(appBean.getThumbFile().getFileUrl(MainActivity.this)).placeholder(R.drawable.plugin_activity_loading).error(R.drawable.plugin_activity_loading).into(iv);
            }
        };

        lv.setAdapter(recyclerAdapter);


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
//                    Log.i("dlh", "appBean:getFileUrl--->" + appBean.getPlun().getFileUrl(MainActivity.this));
                }
                recyclerAdapter.addAll(list);
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
