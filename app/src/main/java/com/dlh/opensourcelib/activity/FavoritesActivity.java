package com.dlh.opensourcelib.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dlh.opensourcelib.R;
import com.dlh.opensourcelib.bean.AppBean;
import com.dlh.opensourcelib.bean.FavoritesBean;
import com.dlh.opensourcelib.constants.Constants;
import com.dlh.opensourcelib.db.dao.FavoritesBeanDao;
import com.pacific.adapter.RecyclerAdapter;
import com.pacific.adapter.RecyclerAdapterHelper;

/**
 * @TODO:
 * @AUTHOR: dlh
 * @DATE: 2016/5/10
 */
public class FavoritesActivity extends AppCompatActivity {

    public static void toFavoritesActivity(Context context) {
        Intent intent = new Intent(context, FavoritesActivity.class);
        context.startActivity(intent);
    }

    private RecyclerView listGridView;
    private RecyclerAdapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorites_activity_layout);
        setupToolbar();
        listGridView = (RecyclerView) findViewById(R.id.favor_recyckerView);
        listGridView.setHasFixedSize(true);
        listGridView.setLayoutManager(new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL,
                false));
        recyclerAdapter = new RecyclerAdapter<FavoritesBean>(this, R.layout.adapter_item) {
            @Override
            protected void convert(RecyclerAdapterHelper recyclerAdapterHelper, final FavoritesBean favoritesBean) {
                recyclerAdapterHelper.setText(R.id.tv, favoritesBean.getTitle());
                ImageView iv = (ImageView) recyclerAdapterHelper.getItemView().findViewById(R.id.iv);
                Glide.with(FavoritesActivity.this).load(favoritesBean.getThumbFileURL()).placeholder(R.drawable.plugin_activity_loading).error(R.drawable.img_circle_placeholder).diskCacheStrategy(DiskCacheStrategy.ALL).into(iv);
                recyclerAdapterHelper.getItemView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DetailsInfoActivity.toDetailsInfoActivity(FavoritesActivity.this, favoritesBean);
                    }
                });

            }
        };
        listGridView.setAdapter(recyclerAdapter);
        recyclerAdapter.addAll(FavoritesBeanDao.getDao().queryAll());

    }

    protected void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.page_title_back_btn_bg);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
    }

}
