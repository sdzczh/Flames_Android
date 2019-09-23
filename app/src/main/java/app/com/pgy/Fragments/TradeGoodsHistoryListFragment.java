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
import butterknife.BindView;
import app.com.pgy.Activitys.GoodsEntrustDetailsActivity;
import app.com.pgy.Adapters.GoodsEntrustListAdapter;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Constants.StaticDatas;
import app.com.pgy.Fragments.Base.BaseListFragment;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Models.Beans.Entrust;
import app.com.pgy.Models.Beans.EventBean.EventGoodsCoinChange;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Utils.MathUtils;
import app.com.pgy.Utils.TimeUtils;

import static app.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * 交易->币币->历史记录
 * @author xu
 */
public class TradeGoodsHistoryListFragment extends BaseListFragment{
    @BindView(R.id.fragment_baseList_list)
    EasyRecyclerView recyclerView;
    private GoodsEntrustListAdapter adapter;
    private int perCoin,tradeCoin;

    public static TradeGoodsHistoryListFragment newInstance(int perCoin,int tradeCoin) {
        TradeGoodsHistoryListFragment instance = new TradeGoodsHistoryListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("perCoin", perCoin);
        bundle.putInt("tradeCoin", tradeCoin);
        instance.setArguments(bundle);
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        perCoin = getArguments().getInt("perCoin");
        tradeCoin = getArguments().getInt("tradeCoin");
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_baselist;
    }

    @Override
    protected void initData() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        if (adapter == null) {
            adapter = new GoodsEntrustListAdapter(mContext,false);
        }
    }


    @Override
    public void onDestroy() {
        /*解除顶部币对币监听*/
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        init(recyclerView,adapter);
        int sp = MathUtils.dip2px(mContext, 15);
        recyclerView.addItemDecoration(new DividerDecoration(getResources().getColor(R.color.divider_line), MathUtils.dip2px(mContext,1),sp,sp));
        onRefresh();
    }

    /**
     * 币种状态监听
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventGoodsCoinChange goodsCoinChange) {
        perCoin = goodsCoinChange.getPerCoinType();
        tradeCoin = goodsCoinChange.getTradeCoinType();
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
        map.put("orderCoin", tradeCoin);
        map.put("unitCoin", perCoin);
        map.put("type",-1);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.getMyEntrustList(Preferences.getAccessToken(), map, new getBeanCallback<Entrust>() {
            @Override
            public void onSuccess(Entrust entrust) {
                List<Entrust.ListBean> list = entrust.getList();
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
                /*Entrust currentEntrustList = DefaultData.getTradeGoodsCurrentEntrustList();
                List<Entrust.ListBean> list = currentEntrustList.getList();
                adapter.addAll(list);*/
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
        Entrust.ListBean item = adapter.getItem(position);
        if (item == null){
            return;
        }
        Intent intent = new Intent(mContext, GoodsEntrustDetailsActivity.class);
        intent.putExtra("entrustId", item.getId());
        startActivity(intent);
    }

}