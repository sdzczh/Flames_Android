package app.com.pgy.Activitys;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import app.com.pgy.Activitys.Base.BaseListActivity;
import app.com.pgy.Activitys.Base.WebDetailActivity;
import app.com.pgy.Adapters.BlockTradeListAdapter;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Constants.StaticDatas;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Interfaces.spinnerSingleChooseListener;
import app.com.pgy.Models.Beans.BlockTradeInfo;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Utils.MathUtils;
import app.com.pgy.Utils.TimeUtils;
import app.com.pgy.Widgets.YubibaoCoinspinner;

import static app.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * Created by YX on 2018/7/19.
 * 交易挖矿
 */

public class BlockTradeActviity extends BaseListActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_question)
    ImageView ivQuestion;
    @BindView(R.id.view_line)
    View viewLine;
    @BindView(R.id.tv_activity_block_trade_enName3)
    TextView tvActivityBlockTradeEnName3;
    @BindView(R.id.tv_activity_block_trade_todayProfit)
    TextView tvActivityBlockTradeTodayProfit;
    @BindView(R.id.tv_activity_block_trade_enName2)
    TextView tvActivityBlockTradeEnName2;
    @BindView(R.id.tv_activity_block_trade_enName1)
    TextView tvActivityBlockTradeEnName1;
    @BindView(R.id.tv_activity_block_trade_lastProfit)
    TextView tvActivityBlockTradeLastProfit;
    @BindView(R.id.tv_activity_block_trade_totalProfit)
    TextView tvActivityBlockTradeTotalProfit;
    @BindView(R.id.rv_activity_block_trade_flows)
    EasyRecyclerView rvActivityBlockTradeFlows;
    @BindView(R.id.srl_activity_block_trade_refresh)
    SmartRefreshLayout srlActivityBlockTradeRefresh;

    private BlockTradeListAdapter adapter;
    private int coinType;
    private YubibaoCoinspinner coinspinner;
    /**
     * 请求数据的页码，从0开始即第一页，每页的数据由后台定
     */
    protected int pageIndex = StaticDatas.PAGE_START;
    @Override
    public int getContentViewId() {
        return R.layout.activity_block_trade;
    }

    @Override
    protected void initData() {
        coinType = (getConfiguration().getDealDigCoinTypes() == null || getConfiguration().getDealDigCoinTypes().size() < 1)?-1:getConfiguration().getDealDigCoinTypes().get(0);
        adapter = new BlockTradeListAdapter(this,coinType);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ivQuestion.setVisibility(View.VISIBLE);
        if (coinType == -1){
            showToast("币种信息为空");
            finish();
            return;
        }
        tvTitle.setText("交易挖矿-"+getCoinName(coinType));
        tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSpinner();
            }
        });
        Drawable dwRight = getResources().getDrawable(R.mipmap.arrow_down);
        dwRight.setBounds(0, 0, dwRight.getMinimumWidth(), dwRight.getMinimumHeight());
        tvTitle.setCompoundDrawables(null, null, dwRight, null);
        init(rvActivityBlockTradeFlows,adapter);
        rvActivityBlockTradeFlows.setRefreshing(false);
        //添加下划线
        rvActivityBlockTradeFlows.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .size(MathUtils.dip2px(getApplicationContext(),1))
                .color(getResources().getColor(R.color.divider_line))
                .build());
        srlActivityBlockTradeRefresh.setEnableLoadMore(false);
        srlActivityBlockTradeRefresh.setRefreshHeader(new MaterialHeader(this));


        srlActivityBlockTradeRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refresh();
            }
        });
        srlActivityBlockTradeRefresh.autoRefresh();
        tvActivityBlockTradeEnName1.setText(getCoinName(coinType));
        tvActivityBlockTradeEnName2.setText(getCoinName(coinType));
        tvActivityBlockTradeEnName3.setText(getCoinName(coinType));
    }

    private void updateView(){
        tvTitle.setText("交易挖矿-"+getCoinName(coinType));
        tvActivityBlockTradeEnName1.setText(getCoinName(coinType));
        tvActivityBlockTradeEnName2.setText(getCoinName(coinType));
        tvActivityBlockTradeEnName3.setText(getCoinName(coinType));
    }
    @OnClick({R.id.iv_back, R.id.iv_question})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_question:
                //  2018/7/19 交易挖矿帮助
                Intent intent = new Intent(this, WebDetailActivity.class);
                intent.putExtra("title","交易挖矿帮助");
                intent.putExtra("url",getConfiguration().getDealDigDocUrl());
                startActivity(intent);
                break;
        }
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

    private void showSpinner(){
        if (getConfiguration().getDealDigCoinTypes() == null || getConfiguration().getDealDigCoinTypes().size() <= 0){
            return;
        }
        if (coinspinner == null){
            coinspinner = new YubibaoCoinspinner(getApplicationContext(), getConfiguration().getDealDigCoinTypes(), new spinnerSingleChooseListener() {
                @Override
                public void onItemClickListener(int position) {
                    coinspinner.dismiss();
                    if (getConfiguration().getDealDigCoinTypes().get(position) == coinType){
                        return;
                    }
                    coinType = getConfiguration().getDealDigCoinTypes().get(position);
                    updateView();
                    adapter.setCoinType(coinType);
                    srlActivityBlockTradeRefresh.autoRefresh();
                }
            });
        }
        if (!coinspinner.isShowing()){
            coinspinner.showDown(viewLine);
        }
    }

    /**
     * 请求数据
     */
    private void requestData(final int index) {
        Map<String, Object> map = new HashMap<>();
        map.put("page", index);
        map.put("coinType", coinType);
        map.put("rows", StaticDatas.PAGE_SIZE);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.getBlockTrade(Preferences.getAccessToken(), map, new getBeanCallback<BlockTradeInfo>() {
            @Override
            public void onSuccess(BlockTradeInfo response) {
                if (isFinishing()){
                    return;
                }
                srlActivityBlockTradeRefresh.finishRefresh(true);
                /*去服务器获取，获取成功*/
                if (response == null){
                    response = new BlockTradeInfo();
                }
                initFrame(response);
                List<BlockTradeInfo.ListBean> list = response.getList();
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
                srlActivityBlockTradeRefresh.finishRefresh(false);
                onFail(errorCode, reason);
                /*网络错误*/
                adapter.pauseMore();
                initFrame(null);
            }
        });
    }


    private void initFrame(BlockTradeInfo blockTradeInfo) {
        if (blockTradeInfo == null) {
            blockTradeInfo = new BlockTradeInfo();
        }
         /*今日收益*/
        String todayProfit = blockTradeInfo.getToday();
        tvActivityBlockTradeTodayProfit.setText(TextUtils.isEmpty(todayProfit)?"暂无收益":todayProfit);
        LogUtils.w(TAG,"todayProfit:"+todayProfit);
        /*昨日收益*/
        String lastProfit = blockTradeInfo.getYesterday();
        tvActivityBlockTradeLastProfit.setText(TextUtils.isEmpty(lastProfit)?"暂无收益":lastProfit);
        LogUtils.w(TAG,"lastProfit:"+lastProfit);
        /*累计收益*/
        String totalProfit = blockTradeInfo.getTotal();
        tvActivityBlockTradeTotalProfit.setText(TextUtils.isEmpty(totalProfit)?"暂无收益":totalProfit);
        LogUtils.w(TAG,"totalProfit:"+totalProfit);
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
