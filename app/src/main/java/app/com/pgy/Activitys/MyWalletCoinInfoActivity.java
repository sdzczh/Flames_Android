package app.com.pgy.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

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
import app.com.pgy.Adapters.MyWalletCoinInfoAdapter;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Constants.StaticDatas;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Models.Beans.CoinFlowDetail;
import app.com.pgy.Models.Beans.Configuration;
import app.com.pgy.Models.Beans.EventBean.EventAssetsChange;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Utils.ImageLoaderUtils;
import app.com.pgy.Utils.MathUtils;
import app.com.pgy.Utils.TimeUtils;

import static app.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * Created by YX on 2018/7/17.
 */

public class MyWalletCoinInfoActivity extends BaseListActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.riv_activity_mywallet_coininfo_icon)
    RoundedImageView rivActivityMywalletCoininfoIcon;
    @BindView(R.id.tv_activity_mywallet_coininfo_chName)
    TextView tvActivityMywalletCoininfoChName;
    @BindView(R.id.ll_activity_mywallet_coininfo_transfer)
    LinearLayout llActivityMywalletCoininfoTransfer;
    @BindView(R.id.tv_activity_mywallet_coininfo_enName1)
    TextView tvActivityMywalletCoininfoEnName1;
    @BindView(R.id.tv_activity_mywallet_coininfo_availTotal)
    TextView tvActivityMywalletCoininfoAvailTotal;
    @BindView(R.id.tv_activity_mywallet_coininfo_availTotalCny)
    TextView tvActivityMywalletCoininfoAvailTotalCny;
    @BindView(R.id.ll_activity_mywallet_coininfo_avail)
    LinearLayout llActivityMywalletCoininfoAvail;
    @BindView(R.id.tv_activity_mywallet_coininfo_enName2)
    TextView tvActivityMywalletCoininfoEnName2;
    @BindView(R.id.tv_activity_mywallet_coininfo_frozenTotal)
    TextView tvActivityMywalletCoininfoFrozenTotal;
    @BindView(R.id.tv_activity_mywallet_coininfo_frozenTotalCny)
    TextView tvActivityMywalletCoininfoFrozenTotalCny;
    @BindView(R.id.ll_activity_mywallet_coininfo_frozen)
    LinearLayout llActivityMywalletCoininfoFrozen;
    @BindView(R.id.rv_activity_mywallet_coininfo_records)
    EasyRecyclerView rvActivityMywalletCoininfoRecords;
    @BindView(R.id.srl_activity_my_wallet_coininfo_refresh)
    SmartRefreshLayout srlActivityMyWalletCoininfoRefresh;
    @BindView(R.id.tv_activity_mywallet_coininfo_recharge)
    TextView tvActivityMywalletCoininfoRecharge;
    @BindView(R.id.tv_activity_mywallet_coininfo_withdraw)
    TextView tvActivityMywalletCoininfoWithdraw;
    @BindView(R.id.line)
    View line;

    private MyWalletCoinInfoAdapter adapter;
    private int coinType;
    private int accountType = StaticDatas.ACCOUNT_GOODS;
    private Configuration.CoinInfo coinInfo;
    /**
     * 请求数据的页码，从0开始即第一页，每页的数据由后台定
     */
    protected int pageIndex = StaticDatas.PAGE_START;
    @Override
    public int getContentViewId() {
        return R.layout.activity_my_wallet_coininfo;
    }

    @Override
    protected void initData() {
        coinType = getIntent().getIntExtra("coinType",-1);
        coinInfo = getCoinInfo(coinType);
        accountType = getIntent().getIntExtra("accountType", StaticDatas.ACCOUNT_GOODS);
        adapter = new MyWalletCoinInfoAdapter(this);
        adapter.setCoinType(coinType);
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
        init(rvActivityMywalletCoininfoRecords,adapter);
        rvActivityMywalletCoininfoRecords.setRefreshing(false);

        //添加下划线
        rvActivityMywalletCoininfoRecords.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
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
        tvActivityMywalletCoininfoEnName1.setText(coinInfo.getCoinname());
        tvActivityMywalletCoininfoEnName2.setText(coinInfo.getCoinname());
        tvActivityMywalletCoininfoChName.setText(coinInfo.getCnname());
        ImageLoaderUtils.displayCircle(getApplicationContext(),rivActivityMywalletCoininfoIcon,coinInfo.getImgurl());
        if (accountType == StaticDatas.ACCOUNT_GOODS){
            tvActivityMywalletCoininfoRecharge.setVisibility(View.VISIBLE);
            tvActivityMywalletCoininfoWithdraw.setVisibility(View.VISIBLE);
            line.setVisibility(View.VISIBLE);
        }else {
            tvActivityMywalletCoininfoRecharge.setVisibility(View.GONE);
            tvActivityMywalletCoininfoWithdraw.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
        }
        updateView(null);
        srlActivityMyWalletCoininfoRefresh.autoRefresh();
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }
    private void updateView(CoinFlowDetail detail){
        if (detail == null){
            detail = new CoinFlowDetail();
        }

        tvActivityMywalletCoininfoAvailTotal.setText(TextUtils.isEmpty(detail.getAvailBalance())?"0.00":detail.getAvailBalance());
        tvActivityMywalletCoininfoAvailTotalCny.setText(TextUtils.isEmpty(detail.getAvailBalanceCny())?"0.00":detail.getAvailBalanceCny());
        tvActivityMywalletCoininfoFrozenTotal.setText(TextUtils.isEmpty(detail.getFrozenBlance())?"0.00":detail.getFrozenBlance());
        tvActivityMywalletCoininfoFrozenTotalCny.setText(TextUtils.isEmpty(detail.getFrozenBlanceCny())?"0.00":detail.getFrozenBlanceCny());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void AssetsChangeEvent(EventAssetsChange event){
        if (event != null && srlActivityMyWalletCoininfoRefresh != null){
            srlActivityMyWalletCoininfoRefresh.autoRefresh();
        }
    }

    @OnClick({R.id.iv_back,R.id.ll_activity_mywallet_coininfo_transfer,R.id.tv_activity_mywallet_coininfo_recharge,
                R.id.tv_activity_mywallet_coininfo_withdraw})
    public void onViewClick(View view){
        Intent intent = null;
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_activity_mywallet_coininfo_transfer:
                intent = new Intent(mContext,MyWalletTransferActivity.class);
                intent.putExtra("coinType",coinType);
                intent.putExtra("accountType",accountType);
                break;
            case R.id.tv_activity_mywallet_coininfo_recharge:
//                showToast("功能暂不可用");
                intent = new Intent(mContext,MyWalletRechargeActivity.class);
                intent.putExtra("coinType",coinType);
                intent.putExtra("accountType",accountType);
                break;
            case R.id.tv_activity_mywallet_coininfo_withdraw:
//                showToast("功能暂不可用");
                intent = new Intent(mContext,MyWalletWithdrawActivity.class);
                intent.putExtra("coinType",coinType);
                intent.putExtra("accountType",accountType);
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

    public void refresh() {
        adapter.clear();
        if (!isLogin()){
            return;
        }
        pageIndex = StaticDatas.PAGE_START;
        if (!checkNetworkState()){
            showToast(R.string.notHaveNet);
            return;
        }
        requestData(pageIndex);
    }

    public void loadMore() {
        if (!isLogin()){
            return;
        }
        if (!checkNetworkState()){
            showToast(R.string.notHaveNet);
            return;
        }
        pageIndex++;
        requestData(pageIndex);
    }
    /**请求数据*/
    private void requestData(int index) {
        Map<String, Object> map = new HashMap<>();
        map.put("page", index);
        map.put("rows", StaticDatas.PAGE_SIZE);
        map.put("coinType", coinType);
        map.put("accountType", accountType);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.getCoinDetail(Preferences.getAccessToken(),map, new getBeanCallback<CoinFlowDetail>() {
            @Override
            public void onSuccess(CoinFlowDetail coinFlowDetail) {
                if (isFinishing()){
                    return;
                }
                srlActivityMyWalletCoininfoRefresh.finishRefresh(true);
                updateView(coinFlowDetail);
                List<CoinFlowDetail.ListBean> list = coinFlowDetail.getFlowList();
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
                onFail(errorCode, reason);
                srlActivityMyWalletCoininfoRefresh.finishRefresh(false);
                /*网络错误*/
                adapter.pauseMore();
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
