package huoli.com.pgy.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import com.jude.easyrecyclerview.EasyRecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import huoli.com.pgy.Activitys.C2CEntrustDetailsActivity;
import huoli.com.pgy.Activitys.C2CPersonalBusinessActivity;
import huoli.com.pgy.Adapters.C2CNormalBuyAdapter;
import huoli.com.pgy.Constants.Preferences;
import huoli.com.pgy.Constants.StaticDatas;
import huoli.com.pgy.Fragments.Base.BaseListFragment;
import huoli.com.pgy.Interfaces.clickHeadCallback;
import huoli.com.pgy.Interfaces.getBeanCallback;
import huoli.com.pgy.Interfaces.getStringCallback;
import huoli.com.pgy.Models.Beans.C2cNormalBusiness;
import huoli.com.pgy.Models.Beans.CoinAvailbalance;
import huoli.com.pgy.Models.Beans.OrderIdBean;
import huoli.com.pgy.NetUtils.NetWorks;
import huoli.com.pgy.R;
import huoli.com.pgy.Utils.LogUtils;
import huoli.com.pgy.Utils.TimeUtils;
import huoli.com.pgy.Widgets.BottomTradeOrderFrame;

import static huoli.com.pgy.Constants.StaticDatas.ACCOUNT_C2C;
import static huoli.com.pgy.Constants.StaticDatas.BUY;
import static huoli.com.pgy.Constants.StaticDatas.NORMAL;
import static huoli.com.pgy.Constants.StaticDatas.SALE;
import static huoli.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * 创建日期：2018/7/9 0009 on 下午 7:08
 * 描述:  C2C法币交易->购买->普通用户
 *
 * @author xu
 */

public class C2cTradeBuyOrSaleNormalFragment extends BaseListFragment {
    private static final String TAG = "C2cTradeBuyNormalFragment";
    @BindView(R.id.fragment_baseList_list)
    EasyRecyclerView recyclerView;
    private C2CNormalBuyAdapter adapter;
    /**
     * 当前币种
     */
    private int coinType;
    private boolean isBuy;
    private BottomTradeOrderFrame tradeOrder;
    private String availBalance;

    public C2cTradeBuyOrSaleNormalFragment() {
    }

    public static C2cTradeBuyOrSaleNormalFragment newInstance(int coinType, boolean isBuy) {
        C2cTradeBuyOrSaleNormalFragment fragment = new C2cTradeBuyOrSaleNormalFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("coinType", coinType);
        bundle.putBoolean("isBuy", isBuy);
        LogUtils.w(TAG,"newInstance"+isBuy);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_baselist;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        coinType = getArguments().getInt("coinType");
        isBuy = getArguments().getBoolean("isBuy");
        LogUtils.w(TAG,"onCreate"+isBuy);
    }

    @Override
    protected void initData() {
        if (adapter == null){
            adapter = new C2CNormalBuyAdapter(mContext,isBuy,coinType);
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        init(recyclerView,adapter);
        adapter.setPositionCallback(new clickHeadCallback() {
            @Override
            public void clickHead(int pos) {
                if (!isLogin()){
                    showToast(R.string.unlogin);
                    return;
                }
                C2cNormalBusiness.ListBean item = adapter.getItem(pos);
                if (item == null){
                    return;
                }
                String userPhone = item.getPhone();
                Intent intent2Business = new Intent(mContext,C2CPersonalBusinessActivity.class);
                intent2Business.putExtra("phone",userPhone);
                startActivity(intent2Business);
            }

            @Override
            public void clickItem(int pos) {
                if (!isLogin()){
                    showToast(R.string.unlogin);
                    return;
                }
                C2cNormalBusiness.ListBean item = adapter.getItem(pos);
                if (item == null){
                    return;
                }
                if (!isBuy) {
                    getCoinAvailBalance(item);
                }else{
                    showOrderChooseDialog(item);
                }
            }
        });
        onRefresh();
    }

    /**从服务器获取该币种的可用余额
     * @param item*/
    private void getCoinAvailBalance(final C2cNormalBusiness.ListBean item) {
        showLoading(null);
        Map<String, Object> map = new HashMap<>();
        map.put("accountType",ACCOUNT_C2C);
        map.put("coinType",coinType);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", StaticDatas.SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.getCoinAvailbalance(Preferences.getAccessToken(), map, new getBeanCallback<CoinAvailbalance>() {
            @Override
            public void onSuccess(CoinAvailbalance availbalance) {
                availBalance = TextUtils.isEmpty(availbalance.getAvailBalance())?"0.0":availbalance.getAvailBalance();
                showOrderChooseDialog(item);
                hideLoading();
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
                /*网络错误*/
                hideLoading();
            }
        });
    }

    /**弹出下单对话框*/
    private void showOrderChooseDialog(final C2cNormalBusiness.ListBean item) {
        tradeOrder = new BottomTradeOrderFrame(mContext,item,isBuy,coinType,availBalance);
        tradeOrder.setGetTradeOrderCallback(new BottomTradeOrderFrame.GetTradeOrderCallback() {
            @Override
            public void getInputDoubleAmount(final Double amount) {
                if (isBuy) {
                    ccNormalBuy(item,amount);
                }else{
                 /*弹出输入交易密码对话框*/
                        showPayDialog(new getStringCallback() {
                            @Override
                            public void getString(String string) {
                                ccNormalSale(string,item,amount);
                            }
                        });
                }
            }
        });
        tradeOrder.showUp(recyclerView);
    }

    private void ccNormalBuy(C2cNormalBusiness.ListBean item, Double amount) {
        /*获取用户输入委托价格、委托数量*/
        showLoading(null);
        Map<String, Object> map = new HashMap<>();
        map.put("coinType", coinType);
        map.put("amount", amount);
        map.put("orderId", item.getId());
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        NetWorks.c2cNormalBuy(Preferences.getAccessToken(), map, new getBeanCallback<OrderIdBean>() {
            @Override
            public void onSuccess(OrderIdBean info) {
                hideLoading();
                showToast("购买成功");
                LogUtils.w(TAG, "购买成功");
                if (info != null){
                    Intent intent = new Intent(mContext, C2CEntrustDetailsActivity.class);
                    intent.putExtra("normalOrBusiness",NORMAL);
                    intent.putExtra("entrustId",info.getId());
                    startActivity(intent);
                }
            }

            @Override
            public void onError(int errorCode, String reason) {
                hideLoading();
                onFail(errorCode, reason);
            }
        });
    }

    private void ccNormalSale(String pwd, C2cNormalBusiness.ListBean item, Double amount) {
        /*获取用户输入委托价格、委托数量*/
        showLoading(null);
        Map<String, Object> map = new HashMap<>();
        map.put("coinType", coinType);
        map.put("amount", amount);
        map.put("password", pwd);
        map.put("orderId", item.getId());
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        NetWorks.c2cNormalSale(Preferences.getAccessToken(), map, new getBeanCallback<OrderIdBean>() {
            @Override
            public void onSuccess(OrderIdBean info) {
                hideLoading();
                showToast("出售成功");
                LogUtils.w(TAG, "出售成功");
                if (info != null){
                    Intent intent = new Intent(mContext, C2CEntrustDetailsActivity.class);
                    intent.putExtra("normalOrBusiness",NORMAL);
                    intent.putExtra("entrustId",info.getId());
                    startActivity(intent);
                }
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
                hideLoading();
            }
        });
    }

    private void requestData(int index) {
        Map<String, Object> map = new HashMap<>();
        map.put("page", index);
        map.put("rows", StaticDatas.PAGE_SIZE);
        map.put("coinType", coinType);
        map.put("orderType", !isBuy?BUY:SALE);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", StaticDatas.SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.getC2CNormalBusinessEntrustList(map, new getBeanCallback<C2cNormalBusiness>() {
            @Override
            public void onSuccess(C2cNormalBusiness business) {
                List<C2cNormalBusiness.ListBean> list = business.getList();
                if (list == null || list.size() <= 0) {
                    /*再无更多数据*/
                    adapter.stopMore();
                    return;
                }
                adapter.addAll(list);
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
                /*网络错误*/
                adapter.pauseMore();
                //加载假数据
                /*C2cNormalBusiness currentEntrustList = DefaultData.getTradeC2cBuyNormalList();
                List<C2cNormalBusiness.ListBean> list = currentEntrustList.getList();
                adapter.addAll(list);*/
            }
        });
    }

    @Override
    public void onListItemClick(int position) {

    }

    @Override
    public void onRefresh() {
        adapter.clear();
        pageIndex = StaticDatas.PAGE_START;
        if (!checkNetworkState()) {
            showToast(R.string.notHaveNet);
            recyclerView.setRefreshing(false);
            return;
        }
        requestData(pageIndex);
    }

    @Override
    public void onLoadMore() {
        if (!checkNetworkState()) {
            showToast(R.string.notHaveNet);
            recyclerView.setRefreshing(false);
            return;
        }
        pageIndex++;
        requestData(pageIndex);
    }
}
