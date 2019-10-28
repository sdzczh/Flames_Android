package app.com.pgy.Fragments;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.List;

import app.com.pgy.Models.Beans.TradeCoinMarketBean;
import butterknife.BindView;
import app.com.pgy.Adapters.LastDealListAdapter;
import app.com.pgy.Constants.Constants;
import app.com.pgy.Fragments.Base.BaseFragment;
import app.com.pgy.Models.Beans.BuyOrSale;
import app.com.pgy.Models.Beans.EventBean.EventGoodsCoinChange;
import app.com.pgy.Models.Beans.KLineBean;
import app.com.pgy.Models.Beans.PushBean.RecordsBean;
import app.com.pgy.R;
import app.com.pgy.Receivers.GoodsListReceiver;
import app.com.pgy.Utils.LogUtils;

/**
 * 交易->币币->最新成交
 * @author xu
 */
public class TradeGoodsLastDealListFragment extends BaseFragment implements GoodsListReceiver.onListCallback{
    @BindView(R.id.fragment_baseList_list)
    EasyRecyclerView recyclerView;
    private LastDealListAdapter adapter;
    private int perCoin,tradeCoin;
    private GoodsListReceiver receiver;

    public static TradeGoodsLastDealListFragment newInstance(int perCoin,int tradeCoin) {
        TradeGoodsLastDealListFragment instance = new TradeGoodsLastDealListFragment();
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
    public void onResume() {
        super.onResume();
        if (receiver == null) {
            receiver = new GoodsListReceiver();
        }
        /*注册监听*/
        IntentFilter filters = new IntentFilter();
        filters.addAction(Constants.SOCKET_ACTION);
        getLocalBroadcastManager().registerReceiver(receiver, filters);
        receiver.setListCallback(this);
    }


    @Override
    public int getContentViewId() {
        return R.layout.fragment_baselist;
    }

    @Override
    public void onDestroy() {
         /*解除挖矿状态监听*/
        if (receiver != null) {
            getLocalBroadcastManager().unregisterReceiver(receiver);
        }
        /*解除顶部币对币监听*/
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    @Override
    protected void initData() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        if (adapter == null) {
            adapter = new LastDealListAdapter(mContext);
        }
    }

    /**
     * 币种状态监听
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventGoodsCoinChange goodsCoinChange) {
        perCoin = goodsCoinChange.getPerCoinType();
        tradeCoin = goodsCoinChange.getTradeCoinType();
        refreshHeader();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(adapter);
        refreshHeader();
    }

    private void refreshHeader() {
        adapter.removeAllHeader();
        if (adapter.getCount() > 0) {
            adapter.addHeader(new RecyclerArrayAdapter.ItemView() {
                @Override
                public View onCreateView(ViewGroup parent) {
                    return LayoutInflater.from(mContext).inflate(R.layout.layout_tradelastdeal_head, parent, false);
                }

                @Override
                public void onBindView(View headerView) {
                    TextView dealListHeaderPrice = headerView.findViewById(R.id.item_tradeLastDealHead_price);
                    TextView dealListHeaderAmount = headerView.findViewById(R.id.item_tradeLastDealHead_amount);
                    dealListHeaderPrice.setText("价格(" + getCoinName(perCoin) + ")");
                    dealListHeaderAmount.setText("数量(" + getCoinName(tradeCoin) + ")");
                }
            });
        }else{
            recyclerView.showEmpty();
        }
    }

    @Override
    public void onBuyListCallback(List<BuyOrSale.ListBean> buyList) {

    }

    @Override
    public void onSaleListCallback(List<BuyOrSale.ListBean> saleList) {

    }

    @Override
    public void onPriceCallback(Double price) {

    }

    @Override
    public void onLastDealListCallback(List<RecordsBean> lastDealList) {
        LogUtils.w(TAG,"列表长度："+lastDealList.size());
        adapter.clear();
        adapter.addAll(lastDealList);
        refreshHeader();
    }

    @Override
    public void onKLineListCallback(List<KLineBean.ListBean> kLineList) {

    }

    @Override
    public void onTradeCoinMarketCallback(List<TradeCoinMarketBean> marketBeanList) {

    }
}