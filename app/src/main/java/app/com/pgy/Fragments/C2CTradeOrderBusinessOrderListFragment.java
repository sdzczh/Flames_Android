package app.com.pgy.Fragments;

import android.content.Intent;
import android.os.Bundle;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.decoration.DividerDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.com.pgy.Activitys.C2CEntrustDetailsNewActivity;
import app.com.pgy.Adapters.C2CTradeOrderListNewAdapter;
import app.com.pgy.Models.Beans.EventBean.EventC2cTradeCoin;
import butterknife.BindView;
import app.com.pgy.Activitys.C2CEntrustDetailsActivity;
import app.com.pgy.Adapters.C2CTradeOrderListAdapter;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Constants.StaticDatas;
import app.com.pgy.Fragments.Base.BaseListFragment;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Models.Beans.C2cNormalEntrust;
import app.com.pgy.Models.Beans.EventBean.EventC2cEntrustList;
import app.com.pgy.Models.Beans.EventBean.EventC2cOrderCondition;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Utils.MathUtils;
import app.com.pgy.Utils.TimeUtils;

import static app.com.pgy.Constants.StaticDatas.BUSINESS;
import static app.com.pgy.Constants.StaticDatas.NORMAL;
import static app.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * 创建日期：2018/04/18 0022 on 上午 11:23
 * 描述: 法币(C2C)交易->顶部导航栏->订单(商家)->订单列表
 *
 * @author 徐庆重
 */

public class C2CTradeOrderBusinessOrderListFragment extends BaseListFragment {
    @BindView(R.id.activity_baseListMargin_list)
    EasyRecyclerView recyclerView;
    private C2CTradeOrderListNewAdapter adapter;
    private int tradeType = -1,stateType = -1;
    private int coinType;

    public C2CTradeOrderBusinessOrderListFragment() {
    }

    public static C2CTradeOrderBusinessOrderListFragment newInstance(int coinType) {
        C2CTradeOrderBusinessOrderListFragment fragment = new C2CTradeOrderBusinessOrderListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("coinType", coinType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_baselist_margin;
    }

    @Override
    public void onDestroyView() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
            LogUtils.w("EventC2cEntrustList","unregister");
        }
        super.onDestroyView();
    }

    @Override
    protected void initData() {
//        coinType = getArguments().getInt("coinType");
        coinType = Preferences.getC2CCoin();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
            LogUtils.w("EventC2cEntrustList","register");
        }
        if (adapter == null) {
            adapter = new C2CTradeOrderListNewAdapter(mContext);
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        init(recyclerView,adapter);
        recyclerView.addItemDecoration(new DividerDecoration(getResources().getColor(R.color.divider_line), MathUtils.dip2px(mContext,1)));
        onRefresh();
    }

    /**
     * 筛选条件回调
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventC2cOrderCondition orderCondition) {
        tradeType = orderCondition.getTradeType();
        stateType = orderCondition.getStateType();
        onRefresh();
    }

    /**
     * 订单详情状态改变，刷新列表
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event1(EventC2cEntrustList entrustList) {
        LogUtils.w("EventC2cEntrustList","receiver");
        if (entrustList.isRefresh()) {
            LogUtils.w("EventC2cEntrustList","refresh");
            onRefresh();
        }
    }
    /**
     * 币种状态监听
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventC2cTradeCoin c2cCoinChange) {
        LogUtils.e("C2CTradeOrderBusinessOrderListFragment",tradeType+"收到广播");
        coinType = c2cCoinChange.getCoinType();
        onRefresh();
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

    private void requestData(int index) {
        Map<String, Object> map = new HashMap<>();
        map.put("coinType", coinType);
        map.put("userRole", BUSINESS);
        map.put("orderType", tradeType);
        map.put("state", stateType);
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
}
