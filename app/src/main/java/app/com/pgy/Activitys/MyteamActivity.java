package app.com.pgy.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
//import com.lzy.okgo.OkGo;
//import com.lzy.okgo.callback.StringCallback;
//import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.header.WaterDropHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.com.pgy.Activitys.Base.BaseActivity;
import app.com.pgy.Adapters.MyteamAdapter;
import app.com.pgy.Constants.Constants;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Models.Beans.MyteamBean;
import app.com.pgy.Models.Beans.MyteamBean2;
import app.com.pgy.Models.Beans.TradeMessage;
import app.com.pgy.Models.ResultBean;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Utils.ImageLoaderUtils;
import app.com.pgy.Utils.TimeUtils;
import app.com.pgy.Utils.ToolsUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static app.com.pgy.Constants.Constants.HTTP_URL;
import static app.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

public class MyteamActivity extends BaseActivity {

    @BindView(R.id.ll_back)
    ImageView llBack;
    @BindView(R.id.tv_myteam_myearnings)
    TextView tvMyteamMyearnings;
    @BindView(R.id.tv_myteam_directnum)
    TextView tvMyteamDirectnum;
    @BindView(R.id.ll_myteam_direct)
    LinearLayout llMyteamDirect;
    @BindView(R.id.tv_myteam_teamearnings)
    TextView tvMyteamTeamearnings;
    @BindView(R.id.tv_myteam_indirectnum)
    TextView tvMyteamIndirectnum;
    @BindView(R.id.ll_myteam_indirect)
    LinearLayout llMyteamIndirect;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.smart)
    SmartRefreshLayout refreshLayout;
    private List<MyteamBean2.RecordsBean> mlist = new ArrayList<>();
    private MyteamAdapter myteamAdapter;
    private int pageIndex = 1;
    private int pageTotal = 0;

    @Override
    public int getContentViewId() {
        return R.layout.activity_myteam;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_myteam);
        ButterKnife.bind(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myteamAdapter = new MyteamAdapter(R.layout.item_myteam, mlist);

        recyclerView.setAdapter(myteamAdapter);


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

    }

    @OnClick({R.id.ll_back, R.id.ll_myteam_direct, R.id.ll_myteam_indirect})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.ll_myteam_direct:
                startActivity(new Intent(MyteamActivity.this, DirectlistActivity.class));

                break;
            case R.id.ll_myteam_indirect:
                break;
        }
    }

    private void getData() {
        showLoading(null);
        Map<String, Object> map = new HashMap<>();
        map.put("page", pageIndex);
        map.put("rows", 5);
        NetWorks.initmyteam(Preferences.getAccessToken(), map, new getBeanCallback<MyteamBean2>() {
            @Override
            public void onSuccess(MyteamBean2 myteamBean) {
                List<MyteamBean2.RecordsBean> records = myteamBean.getRecords();
                DecimalFormat dfs = new DecimalFormat("0.00");
                tvMyteamMyearnings.setText(dfs.format(Double.valueOf(myteamBean.getPersonDigProfit())) + " ANT");
                tvMyteamTeamearnings.setText(dfs.format(Double.valueOf(myteamBean.getTeamDigProfit())) + " ANT");
                tvMyteamDirectnum.setText("直推人数：" + myteamBean.getDirectCount());
                tvMyteamIndirectnum.setText("团队人数：" + myteamBean.getChildCount());
                mlist.addAll(records);
                myteamAdapter.notifyDataSetChanged();
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
