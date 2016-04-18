package com.dlh.opensourcelib.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.dlh.opensourcelib.R;
import com.dlh.opensourcelib.bean.AppBean;
import com.pacific.adapter.Adapter;
import com.pacific.adapter.AdapterHelper;

import org.json.JSONArray;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindCallback;

public class MainActivity extends AppCompatActivity {
    private ListView lv;

    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bmob.initialize(this, "011f2d901a982ed3b5ce8f76e6a1cba1");
        lv = (ListView) findViewById(R.id.lv);

        adapter = new Adapter<AppBean>(this, R.layout.adapter_item) {
            @Override
            protected void convert(AdapterHelper adapterHelper, AppBean appBean) {
                adapterHelper.setText(R.id.tv, appBean.getTitle());
                ImageView iv = (ImageView) adapterHelper.getItemView().findViewById(R.id.iv);
                Glide.with(MainActivity.this).load(appBean.getThumbFile().getFileUrl(MainActivity.this)).placeholder(R.drawable.plugin_activity_loading).error(R.drawable.plugin_activity_loading).into(iv);
            }
        };
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                }
                adapter.addAll(list);
            }

            @Override
            public void onFailure(int arg0, String arg1) {
//                showToast("查询失败:" + arg1);
                Log.i("dlh", "查询失败:" + arg1);
            }
        });
    }

}
