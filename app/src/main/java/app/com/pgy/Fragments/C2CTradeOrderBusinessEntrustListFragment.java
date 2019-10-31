package app.com.pgy.Fragments;

import android.os.Bundle;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.decoration.DividerDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.com.pgy.Adapters.C2CTradeEntrustListNewAdapter;
import app.com.pgy.Models.Beans.EventBean.EventC2cTradeCoin;
import butterknife.BindView;
import app.com.pgy.Adapters.C2CTradeEntrustListAdapter;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Constants.StaticDatas;
import app.com.pgy.Fragments.Base.BaseListFragment;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Interfaces.getPositionCallback;
import app.com.pgy.Interfaces.getStringCallback;
import app.com.pgy.Models.Beans.C2cBusinessEntrust;
import app.com.pgy.Models.Beans.EventBean.EventC2cEntrustCondition;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Utils.MathUtils;
import app.com.pgy.Utils.TimeUtils;

import static app.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * 创建日期：2018/04/18 0022 on 上午 11:23
 * 描述: 法币(C2C)交易->顶部导航栏->订单(商家)->委托列表
 *
 * @author 徐庆重
 */

public class C2CTradeOrderBusinessEntrustListFragment extends BaseListFragment {
    @BindView(R.id.activity_baseListMargin_list)
    EasyRecyclerView recyclerView;
    private C2CTradeEntrustListNewAdapter adapter;
    private int tradeType = -1, stateType = -1;
    private int coinType;

    public static C2CTradeOrderBusinessEntrustListFragment newInstance(int coinType) {
        C2CTradeOrderBusinessEntrustListFragment fragment = new C2CTradeOrderBusinessEntrustListFragment();
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
        /*解除顶部币对币监听*/
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroyView();
    }

    @Override
    protected void initData() {
        coinType = Preferences.getC2CCoin();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        if (adapter == null) {
            adapter = new C2CTradeEntrustListNewAdapter(mContext);
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        init(recyclerView,adapter);
        recyclerView.addItemDecoration(new DividerDecoration(getResources().getColor(R.color.divider_line), MathUtils.dip2px(mContext,1)));
        /*撤掉订单*/
        adapter.setUndoOrderCallback(new getPositionCallback() {
            @Override
            public void getPosition(int pos) {
                C2cBusinessEntrust.ListBean item = adapter.getItem(pos);
                final int id = item.getId();
                /*弹出输入交易密码对话框*/
                showPayDialog(new getStringCallback() {
                    @Override
                    public void getString(String string) {
                        cancelSelectEntrust(id,string);
                    }
                });
            }
        });
        /*取消订单*/
        adapter.setCancelOrderCallback(new getPositionCallback() {
            @Override
            public void getPosition(int pos) {
                C2cBusinessEntrust.ListBean item = adapter.getItem(pos);
                receiptOneEntrust(item.getId());
            }
        });
        onRefresh();
    }

    /**
     * 筛选条件回调
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventC2cEntrustCondition entrustCondition) {
        tradeType = entrustCondition.getTradeType();
        stateType = entrustCondition.getStateType();
        /*商家用户订单委托列表条件筛选*/
        onRefresh();
    }
    /**
     * 币种状态监听
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventC2cTradeCoin c2cCoinChange) {
        LogUtils.e("C2CTradeOrderBusinessEntrustListFragment",tradeType+"收到广播");
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
        map.put("page", index);
        map.put("rows", StaticDatas.PAGE_SIZE);
        map.put("coinType",coinType);
        map.put("orderType",tradeType);
        map.put("state", stateType);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.getC2CBusinessEntrustList(Preferences.getAccessToken(), map, new getBeanCallback<C2cBusinessEntrust>() {
            @Override
            public void onSuccess(C2cBusinessEntrust entrust) {
                List<C2cBusinessEntrust.ListBean> list = entrust.getList();
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
                /*List<C2cBusinessEntrust.ListBean> c2cEntrustList = DefaultData.getC2cEntrustList(tradeType, stateType);
                adapter.addAll(c2cEntrustList);*/
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

    }

    /**
     * 撤销选中的委托
     */
    private void cancelSelectEntrust(int id, String pwd) {
        showLoading(null);
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", id);
        map.put("password", pwd);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.cancelC2CBusinessEntrust(Preferences.getAccessToken(), map, new getBeanCallback() {
            @Override
            public void onSuccess(Object o) {
                showToast("撤销订单成功");
                LogUtils.w(TAG, "撤销订单成功");
                onRefresh();
                hideLoading();
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
                hideLoading();
            }
        });
    }

    /**
     * 开始接单或取消接单
     */
    private void receiptOneEntrust(int id) {
        showLoading(null);
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", id);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.receiptC2CBusinessEntrust(Preferences.getAccessToken(), map, new getBeanCallback() {
            @Override
            public void onSuccess(Object o) {
                showToast("成功");
                LogUtils.w(TAG, "成功");
                onRefresh();
                hideLoading();
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
                hideLoading();
            }
        });
    }

}
