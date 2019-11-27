package app.com.pgy.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scwang.smartrefresh.header.WaterDropHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.com.pgy.Activitys.Base.BaseActivity;
import app.com.pgy.Activitys.Base.WebDetailActivity;
import app.com.pgy.Adapters.DiglistAdapter;
import app.com.pgy.Adapters.MyteamAdapter;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Interfaces.spinnerSingleChooseListener;
import app.com.pgy.Models.Beans.EventBean.EventC2cTradeCoin;
import app.com.pgy.Models.Beans.MortgageBean;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Widgets.CoinTypeListPopupwindow;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MortgageActivity extends BaseActivity {

    @BindView(R.id.ll_back)
    ImageView llBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_question)
    ImageView ivQuestion;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.tv_yesterday_info)
    TextView tvYesterdayInfo;
    @BindView(R.id.tv_yesterday)
    TextView tvYesterday;
    @BindView(R.id.tv_all)
    TextView tvAll;
    @BindView(R.id.tv_bizhong)
    TextView tvBizhong;
    @BindView(R.id.tv_rate)
    TextView tvRate;
    @BindView(R.id.ll_diya_next)
    LinearLayout llDiyaNext;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.smart)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.view_line)
    View viewLine;
    private Integer coinType = 8;
    private int pageIndex = 1;
    private String digDoc = "";
    List<MortgageBean.DigListBean> mlist = new ArrayList<>();
    private DiglistAdapter diglistAdapter;

    public MortgageActivity() {
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_mortgage;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        Preferences.setDiyaCoin(coinType);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        diglistAdapter = new DiglistAdapter(R.layout.item_diglist, mlist);

        recyclerView.setAdapter(diglistAdapter);


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

    @OnClick({R.id.ll_back, R.id.tv_title, R.id.iv_question, R.id.ll_diya_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_title:
                showSpinner();
                break;
            case R.id.iv_question:
                Intent intent = new Intent(this, WebDetailActivity.class);
                intent.putExtra("title", "抵押挖矿帮助");
                intent.putExtra("url", digDoc);
                startActivity(intent);
                break;
            case R.id.ll_diya_next:
                Intent intent2 = new Intent(this, MortgagecommitActivity.class);

//                Bundle bundle = new Bundle();
//                bundle.putInt("coinType", coinType);
//                intent2.putExtras(bundle);

                startActivity(intent2);
                break;
        }
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
                    tvTitle.setText("抵押挖矿(" + getCoinName(coinType) + ")");
                    tvBizhong.setText(getCoinName(coinType));
                    tvYesterdayInfo.setText("昨日收益(" + getCoinName(coinType) + ")");
//                    EventBus.getDefault().post(new EventC2cTradeCoin(coinType));
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

    private void getData() {
        showLoading(null);
        Map<String, Object> map = new HashMap<>();
        map.put("coinType", coinType);
        map.put("page", pageIndex);
        map.put("rows", 20);
        NetWorks.initmortgage(Preferences.getAccessToken(), map, new getBeanCallback<MortgageBean>() {
            @Override
            public void onSuccess(MortgageBean mortgageBean) {

//                String json = "{\"data\":{\"digDoc\":\"https://www.eolinker.com\",\"digList\":[{\"amount\":30.00000000,\"cointype\":8,\"createtime\":1574705256000,\"id\":4,\"updatetime\":1574705256000,\"userid\":2,\"isTeam\":1},{\"amount\":40.00000000,\"cointype\":8,\"createtime\":1574705255000,\"id\":3,\"updatetime\":1574705255000,\"userid\":2,\"isTeam\":0},{\"amount\":20.00000000,\"cointype\":8,\"createtime\":1574705253000,\"id\":2,\"updatetime\":1574705253000,\"userid\":2,\"isTeam\":0},{\"amount\":10.00000000,\"cointype\":8,\"createtime\":1574618851000,\"id\":1,\"updatetime\":1574705251000,\"userid\":2,\"isTeam\":0}],\"mortgageCoinList\":[8],\"totalProfit\":100.00,\"rate\":0.05,\"yesTodayProfit\":60.00},\"msg\":\"成功\",\"code\":10000}";
//                Gson gson = new Gson();

                digDoc = mortgageBean.getDigDoc();
                ivQuestion.setVisibility(View.VISIBLE);
                List<MortgageBean.DigListBean> digList = mortgageBean.getDigList();
                DecimalFormat dfs = new DecimalFormat("0.00");
                DecimalFormat dfs2 = new DecimalFormat("#.##");

                tvYesterday.setText(dfs.format(Double.valueOf(mortgageBean.getYesTodayProfit())));
                tvAll.setText(dfs.format(Double.valueOf(mortgageBean.getTotalProfit())));
                tvRate.setText(mortgageBean.getRate() * 100 + "%");
                mlist.addAll(digList);
                diglistAdapter.notifyDataSetChanged();
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
