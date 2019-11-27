package app.com.pgy.Activitys;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import app.com.pgy.Adapters.MortgageorderAdapter;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Interfaces.spinnerSingleChooseListener;
import app.com.pgy.Models.Beans.MortgageorderBean;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Widgets.CoinTypeListPopupwindow;
import butterknife.BindView;
import butterknife.OnClick;

public class MortgageorderActivity extends BaseActivity {

    @BindView(R.id.ll_back)
    ImageView llBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.smart)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.view_line)
    View viewLine;
    private List<MortgageorderBean> mlist = new ArrayList<>();
    private int pageIndex = 1;
    private MortgageorderAdapter mortgageorderAdapter;
    private int coinType;

    @Override
    public int getContentViewId() {
        return R.layout.activity_mortgageorder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Bundle bundle = getIntent().getExtras();
//        coinType = bundle.getInt("coinType");
        coinType = Preferences.getDiyaCoin();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mortgageorderAdapter = new MortgageorderAdapter(R.layout.item_mortgageorder, mlist);
        recyclerView.setAdapter(mortgageorderAdapter);
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

    @OnClick({R.id.ll_back, R.id.tv_title, R.id.tv_title_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_title:
                break;
            case R.id.tv_title_right:
                showSpinner();
                break;
        }
    }

    private void getData() {
        showLoading(null);
        Map<String, Object> map = new HashMap<>();
        map.put("coinType", coinType);
        map.put("page", pageIndex);
        map.put("rows", 15);
        NetWorks.mortgagelist(Preferences.getAccessToken(), map, new getBeanCallback<List<MortgageorderBean>>() {
            @Override
            public void onSuccess(List<MortgageorderBean> mortgageorderBean) {
//                String json = "{\"data\":{\"date\":\"2019-11-28\",\"amount\":1246.04832560,\"agreement\":\"https://pgy.zendesk.com/hc/zh-cn/articles/360036754292-抵押挖矿协议\",\"rate\":{\"30\":0.02,\"60\":0.022,\"90\":0.025},\"cycle\":[30,60,90]},\"msg\":\"成功\",\"code\":10000}";
                mlist.addAll(mortgageorderBean);
                mortgageorderAdapter.notifyDataSetChanged();

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

    private CoinTypeListPopupwindow coinspinner;

    private void showSpinner() {
        if (getMortgageList() == null || getMortgageList().size() <= 0) {
            return;
        }
        if (coinspinner == null) {
            coinspinner = new CoinTypeListPopupwindow(mContext, getMortgageList(), new spinnerSingleChooseListener() {
                @Override
                public void onItemClickListener(int position) {
                    coinspinner.dismiss();
                    if (getMortgageList().get(position) == coinType) {
                        return;
                    }
                    coinType = getMortgageList().get(position);
                    Preferences.setDiyaCoin(coinType);
                    tvTitleRight.setText(getCoinName(coinType));

                    mlist.clear();
                    getData();
                }
            });
        }
        coinspinner.setSelectCoin(coinType);
        if (!coinspinner.isShowing()) {
            coinspinner.showDown(viewLine);
        }
    }
}
