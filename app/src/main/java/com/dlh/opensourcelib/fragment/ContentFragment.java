package com.dlh.opensourcelib.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dlh.opensourcelib.OpensourceLibApplication;
import com.dlh.opensourcelib.R;
import com.dlh.opensourcelib.activity.DetailsInfoActivity;
import com.dlh.opensourcelib.bean.AppBean;
import com.dlh.opensourcelib.db.dao.AppBeanDao;
import com.dlh.opensourcelib.utils.NetWorkUtil;
import com.pacific.adapter.RecyclerAdapter;
import com.pacific.adapter.RecyclerAdapterHelper;
import com.socks.library.KLog;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindCallback;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContentFragment extends Fragment implements OnRefreshListener, OnLoadMoreListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
    private String Tag = ContentFragment.class.getSimpleName();

    private RecyclerView listGridView;

    private RecyclerAdapter recyclerAdapter;
    private SwipeToLoadLayout swipeToLoadLayout;
    private int pageSize = 9;
    private int curPage = 0;

    private String lastTime = "";
    private final static int REFRESH = 101;
    private final static int LOAD_MORE = 102;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContentFragment newInstance(String param1, String param2) {
        ContentFragment fragment = new ContentFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    public ContentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_content, container, false);
        swipeToLoadLayout = (SwipeToLoadLayout) view.findViewById(R.id.swipeToLoadLayout);
        swipeToLoadLayout.setSwipeStyle(SwipeToLoadLayout.STYLE.SCALE);
        listGridView = (RecyclerView) view.findViewById(R.id.swipe_target);
        listGridView.setHasFixedSize(true);
        listGridView.setLayoutManager(new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL,
                false));
        recyclerAdapter = new RecyclerAdapter<AppBean>(getActivity(), R.layout.adapter_item) {
            @Override
            protected void convert(RecyclerAdapterHelper recyclerAdapterHelper, final AppBean appBean) {
                recyclerAdapterHelper.setText(R.id.tv, appBean.getTitle());
                ImageView iv = (ImageView) recyclerAdapterHelper.getItemView().findViewById(R.id.iv);
                Glide.with(getActivity()).load(appBean.getThumbFileURL()).placeholder(R.drawable.plugin_activity_loading).error(R.drawable.img_circle_placeholder).diskCacheStrategy(DiskCacheStrategy.ALL).into(iv);
                recyclerAdapterHelper.getItemView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DetailsInfoActivity.toDetailsInfoActivity(getActivity(), appBean);
                    }
                });

            }
        };
        listGridView.setAdapter(recyclerAdapter);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        listGridView.setAdapter(recyclerAdapter);
        listGridView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!ViewCompat.canScrollVertically(recyclerView, 1)) {
                        swipeToLoadLayout.setLoadingMore(true);
                    }
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        swipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setRefreshing(true);
            }
        });
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(Tag); //统计页面，"MainScreen"为页面名称，可自定义
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(Tag);
        if (swipeToLoadLayout.isRefreshing()) {
            swipeToLoadLayout.setRefreshing(false);
        }
        if (swipeToLoadLayout.isLoadingMore()) {
            swipeToLoadLayout.setLoadingMore(false);
        }
    }

    @Override
    public void onLoadMore() {
        if (OpensourceLibApplication.isNetWork) {
            queryData(curPage, LOAD_MORE);
        } else {
            if (swipeToLoadLayout.isLoadingMore()) {
                swipeToLoadLayout.setLoadingMore(false);
            }
        }
    }

    @Override
    public void onRefresh() {
        if (OpensourceLibApplication.isNetWork) {
            queryData(0, REFRESH);
        } else {
            if (recyclerAdapter.getSize() > 0) {
                recyclerAdapter.clear();
            }
            List<AppBean> list = AppBeanDao.getDao().queryAll();
            recyclerAdapter.addAll(list);
            if (swipeToLoadLayout.isRefreshing()) {
                swipeToLoadLayout.setRefreshing(false);

            }
        }
    }

    /**
     * 查询数据
     */
    public void queryData(int page, final int type) {
        BmobQuery query = new BmobQuery(AppBean.table);
        query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        // 按时间降序查询
        query.order("-createdAt");
        // 如果是加载更多
        if (type == LOAD_MORE) {
            // 处理时间查询
            Date date = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                if (!TextUtils.isEmpty(lastTime)) {
                    date = sdf.parse(lastTime);
                    // 只查询小于等于最后一个item发表时间的数据
                    query.addWhereLessThanOrEqualTo("createdAt", new BmobDate(date));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // 跳过之前页数并去掉重复数据
            if (page != 0) {
                query.setSkip(pageSize * page);
            }
        } else {
            page = 0;
            query.setSkip(page);
        }
        // 设置每页数据个数
        query.setLimit(pageSize);
        query.findObjects(getActivity(), new FindCallback() {
                    @Override
                    public void onSuccess(JSONArray arg0) {
                        //注意：查询的结果是JSONArray,需要自行解析
                        KLog.i("dlh", "查询成功:" + arg0.toString());
                        if (arg0 != null) {
                            List<AppBean> list = JSON.parseArray(arg0.toString(), AppBean.class);
                            if (list != null && list.size() > 0) {
                                List<AppBean> tempList = new ArrayList<AppBean>();
                                for (AppBean appBean : list) {
                                    String imageUrl = appBean.getThumbFile().getFileUrl(OpensourceLibApplication.application);
                                    String plunUrl = appBean.getPlun().getFileUrl(OpensourceLibApplication.application);
                                    appBean.setThumbFileURL(imageUrl);
                                    appBean.setPlunURL(plunUrl);
                                    tempList.add(appBean);
                                }

                                if (type == REFRESH) {
                                    swipeToLoadLayout.setRefreshing(false);
                                    curPage = 0;
                                    recyclerAdapter.clear();
                                    // 获取最后时间
                                    lastTime = list.get(list.size() - 1).getCreatedAt();
                                    recyclerAdapter.addAll(tempList);
                                } else {
                                    // 获取最后时间
                                    lastTime = list.get(list.size() - 1).getCreatedAt();
                                    recyclerAdapter.addAll(tempList);
                                    swipeToLoadLayout.setLoadingMore(false);

                                }
                                if (tempList.size() > 0) {
                                    for (AppBean appBean : tempList) {
                                        AppBeanDao.getDao().save(appBean);
                                    }

                                }
                                curPage++;
                            } else {
                                if (type == REFRESH) {
                                    swipeToLoadLayout.setRefreshing(false);
                                } else {
                                    swipeToLoadLayout.setLoadingMore(false);
                                }
                            }
                        } else {
                            if (type == REFRESH) {
                                swipeToLoadLayout.setRefreshing(false);
                            } else {
                                swipeToLoadLayout.setLoadingMore(false);
                            }

                        }
                    }

                    @Override
                    public void onFailure(int arg0, String arg1) {
                        KLog.i("dlh", "查询失败:" + arg1);
                        if (type == REFRESH) {
                            swipeToLoadLayout.setRefreshing(false);
                        } else {
                            swipeToLoadLayout.setLoadingMore(false);
                        }
                    }
                }

        );
    }


}
