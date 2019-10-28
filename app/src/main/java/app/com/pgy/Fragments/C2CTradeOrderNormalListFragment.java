package app.com.pgy.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.EasyRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.com.pgy.Activitys.C2CEntrustDetailsActivity;
import app.com.pgy.Activitys.C2CEntrustDetailsNewActivity;
import app.com.pgy.Adapters.C2CTradeOrderListAdapter;
import app.com.pgy.Adapters.C2CTradeOrderListNewAdapter;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Constants.StaticDatas;
import app.com.pgy.Fragments.Base.BaseFragment;
import app.com.pgy.Fragments.Base.BaseListFragment;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Models.Beans.C2cNormalEntrust;
import app.com.pgy.Models.Beans.EventBean.EventC2cCoinChange;
import app.com.pgy.Models.Beans.EventBean.EventC2cTradeCoin;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Utils.TimeUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static app.com.pgy.Constants.StaticDatas.NORMAL;
import static app.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * Create by Android on 2019/10/15 0015
 */
public class C2CTradeOrderNormalListFragment extends BaseListFragment {
    @BindView(R.id.fragment_baseList_list)
    EasyRecyclerView recyclerView;

    private C2CTradeOrderListNewAdapter adapter;
    private int tradeType = 0;
    private int coinType;


    public static C2CTradeOrderNormalListFragment newInstance(int coinType, boolean isBuy) {
        C2CTradeOrderNormalListFragment fragment = new C2CTradeOrderNormalListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("coinType", coinType);
        if (isBuy){
            bundle.putInt("tradeType", 0);
        }else {
            bundle.putInt("tradeType", 1);
        }
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_base;
    }

    @Override
    protected void initData() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        if (adapter == null) {
            adapter = new C2CTradeOrderListNewAdapter(mContext);
        }

        coinType =  getArguments().getInt("coinType");
        tradeType = getArguments().getInt("tradeType",0);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        init(recyclerView,adapter);
        onRefresh();
    }

    @Override
    public void onDestroyView() {
        /*解除顶部币对币监听*/
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
            LogUtils.w("EventC2cEntrustList","unregister");
        }
        super.onDestroyView();
    }
    /**
     * 币种状态监听
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventC2cTradeCoin c2cCoinChange) {
        coinType = c2cCoinChange.getCoinType();
        onRefresh();
    }

    @Override
    public void onListItemClick(int position) {
        /*跳转详情*/
        C2cNormalEntrust.ListBean item = adapter.getItem(position);
        if (item == null){
            item = new C2cNormalEntrust.ListBean();
        }
        Intent intent = new Intent(mContext, C2CEntrustDetailsNewActivity.class);
        intent.putExtra("normalOrBusiness",NORMAL);
        intent.putExtra("entrustId",item.getId());
        startActivity(intent);
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


    private void requestData(int index) {
        Map<String, Object> map = new HashMap<>();
        map.put("coinType", coinType);
        map.put("userRole", NORMAL);
        map.put("orderType", tradeType);
        if (tradeType == 0){
            map.put("state", "0,1,2");
        }else {
            map.put("state", "3,4,5");
        }
        map.put("page", index);
        map.put("rows", StaticDatas.PAGE_SIZE);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.getC2CEntrustList(Preferences.getAccessToken(), map, new getBeanCallback<C2cNormalEntrust>() {
            @Override
            public void onSuccess(C2cNormalEntrust entrust) {
                List<C2cNormalEntrust.ListBean> list = entrust.getList();
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
                /*List<C2cNormalEntrust.ListBean> c2CTradeOrder = DefaultData.getC2CTradeOrder(tradeType, stateType);
                adapter.addAll(c2CTradeOrder);*/
            }
        });
    }
}
