package app.com.pgy.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import app.com.pgy.Activitys.Base.BaseListActivity;
import app.com.pgy.Adapters.YubibaoFlowListAdapter;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Constants.StaticDatas;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Models.Beans.Configuration;
import app.com.pgy.Models.Beans.EventBean.EventAssetsChange;
import app.com.pgy.Models.Beans.YubibaoFlow;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Utils.ImageLoaderUtils;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Utils.MathUtils;
import app.com.pgy.Utils.TimeUtils;

import static app.com.pgy.Constants.StaticDatas.ACCOUNT_DIG;
import static app.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * Created by YX on 2018/7/18.
 */

public class MyWalletYbbCoinInfoActivity extends BaseListActivity {
    @BindView(R.id.riv_activity_mywallet_ybb_coininfo_icon)
    RoundedImageView rivActivityMywalletYbbCoininfoIcon;
    @BindView(R.id.tv_activity_mywallet_ybb_coininfo_chName)
    TextView tvActivityMywalletYbbCoininfoChName;
    @BindView(R.id.tv_activity_mywallet_ybb_coininfo_enName1)
    TextView tvActivityMywalletYbbCoininfoEnName1;
    @BindView(R.id.tv_activity_mywallet_ybb_coininfo_availTotal)
    TextView tvActivityMywalletYbbCoininfoAvailTotal;
    @BindView(R.id.tv_activity_mywallet_ybb_coininfo_availTotalCny)
    TextView tvActivityMywalletYbbCoininfoAvailTotalCny;
    @BindView(R.id.tv_activity_mywallet_ybb_coininfo_enName2)
    TextView tvActivityMywalletYbbCoininfoEnName2;
    @BindView(R.id.tv_activity_mywallet_ybb_coininfo_frozenTotal)
    TextView tvActivityMywalletYbbCoininfoFrozenTotal;
    @BindView(R.id.tv_activity_mywallet_ybb_coininfo_enName3)
    TextView tvActivityMywalletYbbCoininfoEnName3;
    @BindView(R.id.tv_activity_mywallet_ybb_coininfo_income)
    TextView tvActivityMywalletYbbCoininfoIncome;
    @BindView(R.id.tv_activity_mywallet_ybb_coininfo_fee)
    TextView tvActivityMywalletYbbCoininfoFee;
    @BindView(R.id.rv_activity_mywallet_ybb_coininfo_records)
    EasyRecyclerView rvActivityMywalletYbbCoininfoRecords;
    @BindView(R.id.srl_activity_my_wallet_coininfo_refresh)
    SmartRefreshLayout srlActivityMyWalletCoininfoRefresh;

    private YubibaoFlowListAdapter adapter;
    private int coinType;
    private Configuration.CoinInfo coinInfo;
    private String allAmount,availBalanceOfCny;
    protected int pageIndex = StaticDatas.PAGE_START;
    @Override
    public int getContentViewId() {
        return R.layout.activity_mywallet_ybb_coininfo;
    }

    @Override
    protected void initData() {
        coinType = getIntent().getIntExtra("coinType",-1);
        coinInfo = getCoinInfo(coinType);
        adapter = new YubibaoFlowListAdapter(this,coinType);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (coinType == -1){
            showToast("币种信息为空");
            finish();
            return;
        }
        if (coinInfo == null){
            showToast("获取币种信息为空");
            finish();
            return;
        }
        init(rvActivityMywalletYbbCoininfoRecords,adapter);
        rvActivityMywalletYbbCoininfoRecords.setRefreshing(false);
        //添加下划线
        rvActivityMywalletYbbCoininfoRecords.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .size(MathUtils.dip2px(getApplicationContext(),1))
                .color(getResources().getColor(R.color.divider_line))
                .build());
        srlActivityMyWalletCoininfoRefresh.setEnableLoadMore(false);
        srlActivityMyWalletCoininfoRefresh.setRefreshHeader(new MaterialHeader(this));


        srlActivityMyWalletCoininfoRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refresh();
            }
        });
        srlActivityMyWalletCoininfoRefresh.autoRefresh();
        ImageLoaderUtils.displayCircle(mContext,rivActivityMywalletYbbCoininfoIcon,coinInfo.getImgurl());
        tvActivityMywalletYbbCoininfoEnName1.setText(coinInfo.getCoinname());
        tvActivityMywalletYbbCoininfoEnName2.setText(coinInfo.getCoinname());
        tvActivityMywalletYbbCoininfoEnName3.setText(coinInfo.getCoinname());
        tvActivityMywalletYbbCoininfoChName.setText(coinInfo.getCnname());
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void AssetsChangeEvent(EventAssetsChange event){
        if (event != null && srlActivityMyWalletCoininfoRefresh != null){
            srlActivityMyWalletCoininfoRefresh.autoRefresh();
        }
    }

    @OnClick({R.id.iv_back, R.id.tv_activity_mywallet_ybb_coininfo_out,
            R.id.tv_activity_mywallet_ybb_coininfo_in})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_activity_mywallet_ybb_coininfo_in:
                intent = new Intent(mContext,YubibaoInActivity.class);
                intent.putExtra("coinType",coinType);
                break;
            case R.id.tv_activity_mywallet_ybb_coininfo_out:
                intent = new Intent(mContext,YubibaoOutActivity.class);
                intent.putExtra("coinType",coinType);
                break;
        }
        if (intent != null){
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    private void initFrame(YubibaoFlow yubibaoFlow) {
        if (yubibaoFlow == null) {
            yubibaoFlow = new YubibaoFlow();
        }
        /*昨日收益*/
        String lastProfit = yubibaoFlow.getLastProfit();
        tvActivityMywalletYbbCoininfoFrozenTotal.setText(TextUtils.isEmpty(lastProfit)?"0.00":lastProfit);
        LogUtils.w(TAG,"lastProfit:"+lastProfit);
        /*总金额*/
        allAmount = yubibaoFlow.getAvailBalance();
        tvActivityMywalletYbbCoininfoAvailTotal.setText(TextUtils.isEmpty(allAmount)?"0.00":allAmount);
        LogUtils.w(TAG,"allAmount:"+allAmount);
        /*折扣cny*/
        availBalanceOfCny = yubibaoFlow.getAvailBalanceOfCny();
        tvActivityMywalletYbbCoininfoAvailTotalCny.setText(TextUtils.isEmpty(availBalanceOfCny)?"0.00":availBalanceOfCny);
        LogUtils.w(TAG,"availBalanceOfCny:"+availBalanceOfCny);

        /*累计收益*/
        String totalProfit = yubibaoFlow.getTotalProfit();
        tvActivityMywalletYbbCoininfoIncome.setText(TextUtils.isEmpty(totalProfit)?"0.00":totalProfit);
        LogUtils.w(TAG,"totalProfit:"+totalProfit);
        /*日利率*/
        String dayRate = yubibaoFlow.getAnnualRate();
        tvActivityMywalletYbbCoininfoFee.setText(TextUtils.isEmpty(dayRate)?"0.00":dayRate);
        LogUtils.w(TAG,"dayRate:"+dayRate);
    }



    public void refresh() {
        adapter.clear();
        if (!isLogin()) {
            return;
        }
        pageIndex = StaticDatas.PAGE_START;
        if (!checkNetworkState()) {
            showToast(R.string.notHaveNet);
            return;
        }
        requestData(pageIndex);
    }
    public void loadMore() {
        if (!isLogin()) {
            return;
        }
        if (!checkNetworkState()) {
            showToast(R.string.notHaveNet);
            return;
        }
        pageIndex++;
        requestData(pageIndex);
    }

    /**
     * 请求数据
     */
    private void requestData(int index) {
//        showLoading(null);
        Map<String, Object> map = new HashMap<>();
        map.put("coinType", coinType);
        map.put("type", ACCOUNT_DIG);
        map.put("page", index);
        map.put("rows", StaticDatas.PAGE_SIZE);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.getYubiBaoFlows(Preferences.getAccessToken(), map, new getBeanCallback<YubibaoFlow>() {
            @Override
            public void onSuccess(YubibaoFlow response) {
                if (isFinishing()){
                    return;
                }
                srlActivityMyWalletCoininfoRefresh.finishRefresh(true);
//                hideLoading();
                /*去服务器获取，获取成功*/
                initFrame(response);
                List<YubibaoFlow.ListBean> list = response.getList();
                if (list == null || list.size() <= 0) {
                    /*再无更多数据*/
                    adapter.stopMore();
                    return;
                }
                adapter.addAll(list);
            }

            @Override
            public void onError(int errorCode, String reason) {
                if (isFinishing()){
                    return;
                }
                srlActivityMyWalletCoininfoRefresh.finishRefresh(false);
//                hideLoading();
                onFail(errorCode, reason);
                /*网络错误*/
                adapter.pauseMore();
                initFrame(null);
            }
        });
    }

    @Override
    protected void onListItemClick(int position) {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
        loadMore();
    }
}
