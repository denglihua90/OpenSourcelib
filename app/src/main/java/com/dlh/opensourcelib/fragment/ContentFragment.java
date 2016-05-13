package com.dlh.opensourcelib.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindCallback;
import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dlh.opensourcelib.R;
import com.dlh.opensourcelib.activity.DetailsInfoActivity;
import com.dlh.opensourcelib.bean.AppBean;
import com.pacific.adapter.RecyclerAdapter;
import com.pacific.adapter.RecyclerAdapterHelper;
import org.json.JSONArray;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContentFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

    private RecyclerView listGridView;

    private RecyclerAdapter recyclerAdapter;

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
        listGridView = (RecyclerView) view.findViewById(R.id.list_grid_view);
        listGridView.setHasFixedSize(true);
        listGridView.setLayoutManager(new StaggeredGridLayoutManager(3,
                StaggeredGridLayoutManager.VERTICAL));
        recyclerAdapter = new RecyclerAdapter<AppBean>(getActivity(), R.layout.adapter_item) {
            @Override
            protected void convert(RecyclerAdapterHelper recyclerAdapterHelper, final AppBean appBean) {
                recyclerAdapterHelper.setText(R.id.tv, appBean.getTitle());
                ImageView iv = (ImageView) recyclerAdapterHelper.getItemView().findViewById(R.id.iv);
                Glide.with(getActivity()).load(appBean.getThumbFile().getFileUrl(getActivity())).placeholder(R.drawable.plugin_activity_loading).error(R.drawable.img_circle_placeholder).diskCacheStrategy(DiskCacheStrategy.ALL).into(iv);
                recyclerAdapterHelper.getItemView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DetailsInfoActivity.toDetailsInfoActivity(getActivity(), appBean);
                    }
                });

            }
        };
        listGridView.setAdapter(recyclerAdapter);
        queryData();
        return view;
    }

    /**
     * 查询数据
     */
    public void queryData() {
        BmobQuery query = new BmobQuery(AppBean.table);
        query.findObjects(getActivity(), new FindCallback() {
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


}
