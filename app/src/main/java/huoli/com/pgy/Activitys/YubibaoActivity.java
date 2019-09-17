package huoli.com.pgy.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import huoli.com.pgy.Activitys.Base.BaseListActivity;
import huoli.com.pgy.Activitys.Base.WebDetailActivity;
import huoli.com.pgy.Adapters.YubibaoFlowListAdapter;
import huoli.com.pgy.Constants.Preferences;
import huoli.com.pgy.Constants.StaticDatas;
import huoli.com.pgy.Interfaces.getBeanCallback;
import huoli.com.pgy.Interfaces.getStringCallback;
import huoli.com.pgy.Interfaces.spinnerSingleChooseListener;
import huoli.com.pgy.Models.Beans.Configuration;
import huoli.com.pgy.Models.Beans.YubibaoFlow;
import huoli.com.pgy.NetUtils.NetWorks;
import huoli.com.pgy.R;
import huoli.com.pgy.Utils.LogUtils;
import huoli.com.pgy.Utils.MathUtils;
import huoli.com.pgy.Utils.TimeUtils;
import huoli.com.pgy.Widgets.InputNumDialog;
import huoli.com.pgy.Widgets.YubibaoCoinspinner;

import static huoli.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * Created by YX on 2018/7/18.
 */

public class YubibaoActivity extends BaseListActivity {
    public static final int TRANSFER_SUCCESS = 0x0001;
    @BindView(R.id.tv_activity_yubibao_title)
    TextView tvActivityYubibaoTitle;
    @BindView(R.id.rl_activity_yubibao_title)
    RelativeLayout rlActivityYubibaoTitle;
    @BindView(R.id.tv_activity_yubibao_enName1)
    TextView tvActivityYubibaoEnName1;
    @BindView(R.id.tv_activity_yubibao_lastProfit)
    TextView tvActivityYubibaoLastProfit;
    @BindView(R.id.tv_activity_yubibao_availBalance)
    TextView tvActivityYubibaoAvailBalance;
    @BindView(R.id.tv_activity_yubibao_enName2)
    TextView tvActivityYubibaoEnName2;
    @BindView(R.id.tv_activity_yubibao_availBalanceCny)
    TextView tvActivityYubibaoAvailBalanceCny;
    @BindView(R.id.tv_activity_yubibao_enName3)
    TextView tvActivityYubibaoEnName3;
    @BindView(R.id.tv_activity_yubibao_totalProfit)
    TextView tvActivityYubibaoTotalProfit;
    @BindView(R.id.tv_activity_yubibao_enName4)
    TextView tvActivityYubibaoEnName4;
    @BindView(R.id.tv_activity_yubibao_forecastProfit)
    TextView tvActivityYubibaoForecastProfit;
    @BindView(R.id.tv_activity_yubibao_dayRate)
    TextView tvActivityYubibaoDayRate;
    @BindView(R.id.rv_activity_yubibao_flow)
    EasyRecyclerView rvActivityYubibaoFlow;
    @BindView(R.id.srl_activity_yubibao_refresh)
    SmartRefreshLayout srlActivityYubibaoRefresh;
    @BindView(R.id.iv_activity_yubibao_show)
    ImageView iv_show;
    private YubibaoFlowListAdapter adapter;
    private int coinType;
    private List<Integer> coinTypeList;
    private Configuration.CoinInfo coinInfo;
    private String allAmount,availBalanceOfCny;
    protected int pageIndex = StaticDatas.PAGE_START;
    private boolean isShow = true;

    private YubibaoCoinspinner coinspinner;
    @Override
    public int getContentViewId() {
        return R.layout.activity_yubibao;
    }

    @Override
    protected void initData() {
         /*获取余币宝币种列表*/
        coinTypeList = getYubiCoinTypeList();
//        getConfiguration().getDealDigCoinTypes()
        if (coinTypeList == null){
            coinTypeList = new ArrayList<>();
        }
        coinType = coinTypeList.size() > 0 ? coinTypeList.get(0) : -1;
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
        init(rvActivityYubibaoFlow,adapter);
        rvActivityYubibaoFlow.setRefreshing(false);
        //添加下划线
        rvActivityYubibaoFlow.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .size(MathUtils.dip2px(getApplicationContext(),1))
                .color(getResources().getColor(R.color.divider_line))
                .build());
        srlActivityYubibaoRefresh.setEnableLoadMore(false);
        srlActivityYubibaoRefresh.setRefreshHeader(new MaterialHeader(this));


        srlActivityYubibaoRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refresh();
            }
        });
        srlActivityYubibaoRefresh.autoRefresh();
        updateView();
    }

    private void updateView(){
        if (coinInfo == null){
            coinInfo = new Configuration.CoinInfo();
        }
        tvActivityYubibaoTitle.setText("节点挖矿-"+coinInfo.getCoinname());
        tvActivityYubibaoEnName1.setText(coinInfo.getCoinname());
        tvActivityYubibaoEnName2.setText("  "+coinInfo.getCoinname());
        tvActivityYubibaoEnName3.setText(coinInfo.getCoinname());
        tvActivityYubibaoEnName4.setText(coinInfo.getCoinname());
    }
    private void updateDatas(YubibaoFlow yubibaoFlow){
        if (yubibaoFlow == null){
            yubibaoFlow = new YubibaoFlow();
        }
        String lastProfit = yubibaoFlow.getLastProfit();
        tvActivityYubibaoLastProfit.setText(TextUtils.isEmpty(lastProfit)?"0.00":lastProfit);
        LogUtils.w(TAG,"lastProfit:"+lastProfit);
        allAmount = yubibaoFlow.getAvailBalance();
        tvActivityYubibaoAvailBalance.setText(TextUtils.isEmpty(allAmount)?"0.00":allAmount);
        LogUtils.w(TAG,"allAmount:"+allAmount);

        availBalanceOfCny = yubibaoFlow.getAvailBalanceOfCny();
        tvActivityYubibaoAvailBalanceCny.setText(TextUtils.isEmpty(availBalanceOfCny)?"0.00":availBalanceOfCny);
        LogUtils.w(TAG,"availBalanceOfCny:"+availBalanceOfCny);

        /*累计收益*/
        String totalProfit = yubibaoFlow.getTotalProfit();
        tvActivityYubibaoTotalProfit.setText(TextUtils.isEmpty(totalProfit)?"0.00":totalProfit);
        LogUtils.w(TAG,"totalProfit:"+totalProfit);
         /*万份收益*/
        String forecastProfit = yubibaoFlow.getForecastProfit();
        tvActivityYubibaoForecastProfit.setText(TextUtils.isEmpty(forecastProfit)?"0.0000":forecastProfit);
        LogUtils.w(TAG,"forecastProfit:"+forecastProfit);
        /*日利率*/
        String dayRate = yubibaoFlow.getAnnualRate();
        tvActivityYubibaoDayRate.setText(TextUtils.isEmpty(dayRate)?"0.00":dayRate);
        LogUtils.w(TAG,"dayRate:"+dayRate);
    }

    private void showSpinner(){
        if (coinTypeList == null || coinTypeList.size() <= 0){
            return;
        }
        if (coinspinner == null){
            coinspinner = new YubibaoCoinspinner(getApplicationContext(), coinTypeList, new spinnerSingleChooseListener() {
                @Override
                public void onItemClickListener(int position) {
                    coinspinner.dismiss();
                    if (coinTypeList.get(position) == coinType){
                        return;
                    }
                    coinType = coinTypeList.get(position);
                    coinInfo = getCoinInfo(coinType);
                    updateView();
                    srlActivityYubibaoRefresh.autoRefresh();
                }
            });
        }
        if (!coinspinner.isShowing()){
            coinspinner.showDown(rlActivityYubibaoTitle);
        }
    }

    @OnClick({R.id.iv_back, R.id.tv_activity_yubibao_title, R.id.iv_activity_yubibao_title_question,
            R.id.tv_activity_yubibao_out,R.id.tv_activity_yubibao_in,R.id.iv_activity_yubibao_show,
            R.id.ll_activity_yubibao_forecastProfit})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_activity_yubibao_title:
                showSpinner();
                break;
            case R.id.iv_activity_yubibao_title_question:
                intent = new Intent(mContext, WebDetailActivity.class);
                intent.putExtra("title", "节点账户帮助文档");
                intent.putExtra("url", getConfiguration().getYubibaoHelpUrl());
                break;
            case R.id.tv_activity_yubibao_out:
                intent = new Intent(mContext,YubibaoOutActivity.class);
                intent.putExtra("coinType",coinType);
                break;
            case R.id.tv_activity_yubibao_in:
                intent = new Intent(mContext,YubibaoInActivity.class);
                intent.putExtra("coinType",coinType);
                break;
            case R.id.iv_activity_yubibao_show:
                isShow = !isShow;
                changeShow();
                break;
            case R.id.ll_activity_yubibao_forecastProfit:
                InputNumDialog.Builder inputNumDialog = new InputNumDialog.Builder(mContext);
                inputNumDialog.setTitle("提取金额");
                inputNumDialog.setHint("请输入提取金额");
                inputNumDialog.setPositiveButton("提取");
                inputNumDialog.setNegativeButton("取消");
                inputNumDialog.setFinishListener(new getStringCallback() {
                    @Override
                    public void getString(final String amount) {
                        showPayDialog(new getStringCallback() {
                            @Override
                            public void getString(String pw) {
                                submitWithdrawFrozen(pw,amount);
                            }
                        });
                    }
                });
                inputNumDialog.create().show();
                break;
        }
        if (intent != null){
            startActivityForResult(intent,TRANSFER_SUCCESS);
        }
    }

    private void changeShow(){
        if (isShow){
            iv_show.setImageResource(R.mipmap.pw_show);
            tvActivityYubibaoAvailBalance.setText(""+allAmount);
            tvActivityYubibaoAvailBalanceCny.setText(""+availBalanceOfCny);
        }else {
            iv_show.setImageResource(R.mipmap.pw_unshow);
            tvActivityYubibaoAvailBalance.setText("****");
            tvActivityYubibaoAvailBalanceCny.setText("****");
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

    /**
     * 请求数据
     */
    private void requestData(int index) {
        Map<String, Object> map = new HashMap<>();
        map.put("coinType", coinType);
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
                srlActivityYubibaoRefresh.finishRefresh(true);
                /*去服务器获取，获取成功*/
                updateDatas(response);
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
                srlActivityYubibaoRefresh.finishRefresh(false);
                onFail(errorCode, reason);
                /*网络错误*/
                adapter.pauseMore();
                updateDatas(null);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TRANSFER_SUCCESS && resultCode == TRANSFER_SUCCESS){
            srlActivityYubibaoRefresh.autoRefresh();
        }
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


    /**
     * 去服务器转账
     */
    private void submitWithdrawFrozen(String pwd,String amount) {
        showLoading(null);
        Map<String, Object> map = new HashMap<>();
        map.put("coinType", coinType);
        map.put("accountType", 4);
        map.put("amount", amount);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", StaticDatas.SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        map.put("password", pwd);
        NetWorks.submitWithdrawFrozen(Preferences.getAccessToken(), map, new getBeanCallback() {
            @Override
            public void onSuccess(Object o) {
                showToast("提取申请成功");
                LogUtils.w(TAG, "提取申请成功");
                hideLoading();
                srlActivityYubibaoRefresh.autoRefresh();

            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
                hideLoading();
            }
        });
    }
}
