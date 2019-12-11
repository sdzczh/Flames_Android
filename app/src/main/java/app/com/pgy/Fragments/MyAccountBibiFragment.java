package app.com.pgy.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import app.com.pgy.Activitys.MyWalletCoinInfoActivity;
import app.com.pgy.Activitys.MyWalletRechargeActivity;
import app.com.pgy.Activitys.MyWalletTransferActivity;
import app.com.pgy.Activitys.MyWalletWithdrawActivity;
import app.com.pgy.Activitys.SecuritycenterActivity;
import app.com.pgy.Activitys.TradeGoodsEntrustListActivity;
import app.com.pgy.Adapters.MyAccountListAdapter;
import app.com.pgy.Adapters.MyWalletListAdapter;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Fragments.Base.BaseFragment;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Interfaces.getPositionCallback;
import app.com.pgy.Models.Beans.EventBean.EventAssetsChange;
import app.com.pgy.Models.Beans.EventBean.EventGoodsToTrade;
import app.com.pgy.Models.Beans.EventBean.EventMainChangeState;
import app.com.pgy.Models.Beans.MyWallet;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Utils.TimeUtils;
import butterknife.BindView;
import butterknife.OnClick;

import static app.com.pgy.Constants.StaticDatas.ACCOUNT_C2C;
import static app.com.pgy.Constants.StaticDatas.ACCOUNT_GOODS;
import static app.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;
import static app.com.pgy.Models.Beans.EventBean.EventMainChangeState.CHANGE_TO_C2C;
import static app.com.pgy.Models.Beans.EventBean.EventMainChangeState.CHANGE_TO_GOODS;

/**
 * Create by Android on 2019/10/14 0014
 */
public class MyAccountBibiFragment extends BaseFragment implements getPositionCallback {
    @BindView(R.id.iv_fragment_account_trade_show)
    ImageView ivFtagmentAccountShow;
    @BindView(R.id.tv_fragment_account_trade_total)
    TextView tvFtagmentAccountAmount;
    @BindView(R.id.tv_fragment_account_trade_totalcny)
    TextView tvFtagmentAccountAmountCny;

    @BindView(R.id.rv_fragment_account_trade__list)
    RecyclerView rvList;

    @BindView(R.id.ll_error_content)
    LinearLayout llErrorContent;
    @BindView(R.id.btn_error_reload)
    Button btnReload;
    @BindView(R.id.tv_error_content)
    TextView  tvError;

    private boolean isShow = false;
    private MyWallet myWallet;
    private MyAccountListAdapter mAdapter;
    private int currentAccount = ACCOUNT_GOODS;
    @Override
    public int getContentViewId() {
        return R.layout.fragment_account_trade_asset;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (mAdapter == null) {
            mAdapter = new MyAccountListAdapter(mContext);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        /*添加Layout*/
        rvList.setLayoutManager(layoutManager);
        /*添加加载进度条*/
        rvList.setAdapter(mAdapter);
        mAdapter.setCallback(this);
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
//        getMyWalletFromNet();
    }

    @OnClick({R.id.iv_fragment_account_trade_show, R.id.ll_fragment_account_trade_recharge,
            R.id.ll_fragment_account_trade_withdraw, R.id.ll_fragment_account_trade_trust,
            R.id.tv_fragment_account_trade_recharge, R.id.tv_fragment_account_trade_withdraw,
            R.id.tv_fragment_account_trade_trust,R.id.btn_error_reload})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.iv_fragment_account_trade_show:
                isShow = !isShow;
                if (isShow){
                    tvFtagmentAccountAmount.setText("****");
                    tvFtagmentAccountAmountCny.setText("****");

                }else {
                    tvFtagmentAccountAmount.setText(myWallet.getTotalSum());
                    tvFtagmentAccountAmountCny.setText(myWallet.getTotalSumOfCny());
                }
                break;
            case R.id.tv_fragment_account_trade_withdraw:
                intent = new Intent(mContext, MyWalletWithdrawActivity.class);
                break;
            case R.id.ll_fragment_account_trade_withdraw://order
                intent = new Intent(mContext, TradeGoodsEntrustListActivity.class);
                break;
            case R.id.tv_fragment_account_trade_recharge:
                intent = new Intent(mContext, MyWalletRechargeActivity.class);
                break;
            case R.id.ll_fragment_account_trade_recharge://收款设置
                intent = new Intent(mContext, SecuritycenterActivity.class);
                break;
            case R.id.tv_fragment_account_trade_trust:
            case R.id.ll_fragment_account_trade_trust:
                intent = new Intent(mContext, MyWalletTransferActivity.class);
                break;
            case R.id.btn_error_reload:
                // 重新加载
                getMyWalletFromNet();
                break;
        }

        if (intent != null) {
            startActivity(intent);
        }
    }

    /**
     * 从服务器获取我的钱包
     */
    private void getMyWalletFromNet() {
        showLoading(null);
        Map<String, Object> map = new HashMap<>();
        map.put("accountType", currentAccount);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.getMyWalletList(Preferences.getAccessToken(), map, new getBeanCallback<MyWallet>() {
            @Override
            public void onSuccess(MyWallet myWalletFromNet) {
                if (myWalletFromNet == null) {
                    myWalletFromNet = new MyWallet();
                }
                updateAccount(myWalletFromNet);
                hideLoading();
            }

            @Override
            public void onError(int errorCode, String reason) {
                updateAccount(new MyWallet());
                onFail(errorCode, reason);
                /*网络错误*/
                hideLoading();

            }
        });
    }
    private boolean isFirstVisible = true;
    private boolean isFragmentVisible;
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isFragmentVisible = isVisibleToUser;

        //当 View 创建完成切 用户可见的时候请求 且仅当是第一次对用户可见的时候请求自动数据
        if (isVisibleToUser && isViewCreated && isFirstVisible) {
            getMyWalletFromNet();
            isFirstVisible = false;

        }

        // 由于每次可见都需要刷新所以我们只需要判断  Fragment 展示在用户面面前了，view 初始化完成了 然后即可以请求数据了
//        if (isVisibleToUser && isViewCreated) {
//            // Log.e(TAG, "每次都可见数据  requestDataAutoRefresh");
//            requestDataAutoRefresh();
//        }
//
//        if (!isVisibleToUser && isViewCreated) {
//            stopRefresh();
//        }
    }

    private void updateAccount(MyWallet wallets){
        mAdapter.clear();
        myWallet = wallets;

        if (myWallet != null){
            if (TextUtils.isEmpty(myWallet.getTotalSumOfCny())){
                tvFtagmentAccountAmountCny.setText("0.00");
                tvFtagmentAccountAmount.setText("0.00");

            }else {
                tvFtagmentAccountAmountCny.setText(""+ myWallet.getTotalSumOfCny());
                tvFtagmentAccountAmount.setText(""+ myWallet.getTotalSum());
            }
        }else {
            tvFtagmentAccountAmountCny.setText("0.00");
            tvFtagmentAccountAmount.setText("0.00");
        }
        //刷新列表
        if (myWallet != null && myWallet.getList() != null && myWallet.getList().size() > 0){
            llErrorContent.setVisibility(View.GONE);
            mAdapter.addAll(myWallet.getList());
        }else if (myWallet != null){
            llErrorContent.setVisibility(View.VISIBLE);
            btnReload.setVisibility(View.GONE);
            tvError.setText("账户还没有币种");
        }else {
            llErrorContent.setVisibility(View.VISIBLE);
            btnReload.setVisibility(View.VISIBLE);
            tvError.setText("加载失败");
        }
    }

    @Override
    public void getPosition(int pos) {
        MyWallet.ListBean info = mAdapter.getItem(pos);
        Intent intent = new Intent(mContext, MyWalletCoinInfoActivity.class);
        intent.putExtra("accountType",currentAccount);
        intent.putExtra("coinType",info.getCoinType());
        startActivity(intent);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void AssetsChangeEvent(EventAssetsChange event){
        if (event != null){
            getMyWalletFromNet();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void EventGoodsToTrade(EventGoodsToTrade event){
        getActivity().finish();
    }
    @Override
    public void onDestroyView() {
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
        super.onDestroyView();
    }

}
