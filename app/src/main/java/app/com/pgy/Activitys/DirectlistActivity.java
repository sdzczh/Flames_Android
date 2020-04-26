package app.com.pgy.Activitys;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.scwang.smartrefresh.header.WaterDropHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.com.pgy.Activitys.Base.BaseActivity;
import app.com.pgy.Adapters.DirectlistAdapter;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Models.Beans.DirectlistBean2;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//import com.lzy.okgo.OkGo;
//import com.lzy.okgo.callback.StringCallback;
//import com.lzy.okgo.model.Response;

public class DirectlistActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_question)
    ImageView ivQuestion;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.view_line)
    View viewLine;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.smart)
    SmartRefreshLayout refreshLayout;
    private List<DirectlistBean2> mlist = new ArrayList<>();
    private DirectlistAdapter directlistAdapter;
    private int pageIndex = 1;
    private DirectlistBean2 directlistBean1;

    @Override
    public int getContentViewId() {
        return R.layout.activity_directlist;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

//        setContentView(R.layout.activity_directlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        directlistAdapter = new DirectlistAdapter(R.layout.item_direct, mlist);
        recyclerView.setAdapter(directlistAdapter);
        refreshLayout.setRefreshHeader(new WaterDropHeader(mContext));
        refreshLayout.setRefreshFooter(new ClassicsFooter(mContext));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                pageIndex = 1;
                mlist.clear();
                getData();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
                pageIndex++;
                getData();
            }
        });
        getData();

    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        tvTitle.setText("直推团队");
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }

    private void getData() {
        showLoading(null);
        Map<String, Object> map = new HashMap<>();
        map.put("page", pageIndex);
        map.put("rows", 20);
        NetWorks.directteamlist(Preferences.getAccessToken(), map, new getBeanCallback<List<DirectlistBean2>>() {
            @Override
            public void onSuccess(List<DirectlistBean2> directlistBean) {
                mlist.addAll(directlistBean);
                directlistAdapter.notifyDataSetChanged();
                hideLoading();
            }

            @Override
            public void onError(int errorCode, String reason) {
                hideLoading();
                onFail(errorCode, reason);
                finish();
            }
        });
    }
}
