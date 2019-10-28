package app.com.pgy.Fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import app.com.pgy.Activitys.KLineActivity;
import app.com.pgy.Activitys.MainActivity;
import app.com.pgy.Adapters.MarketListAdapter;
import app.com.pgy.Constants.Constants;
import app.com.pgy.Fragments.Base.BaseFragment;
import app.com.pgy.Interfaces.getPositionCallback;
import app.com.pgy.Models.Beans.EventBean.EventMarketScene;
import app.com.pgy.Models.Beans.PushBean.PushData;
import app.com.pgy.Models.Beans.PushMarketBean;
import app.com.pgy.R;
import app.com.pgy.Receivers.MarketListReceiver;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Utils.MathUtils;

import static app.com.pgy.Constants.StaticDatas.MARKET_MAIN;
import static app.com.pgy.Constants.StaticDatas.MARKET_ONECOIN;
import static app.com.pgy.Fragments.MarketFragment.TYPE_ONCOIN;
import static app.com.pgy.Fragments.MarketFragment.TYPE_WORLD;

/**
 * Created by YX on 2018/7/13.
 */

public class MarketListFragment extends BaseFragment implements getPositionCallback,MarketListReceiver.onListCallback {
    public static final int COIN_TYPE_AUTO = 0;//自选
    public static final int COIN_TYPE_KN = 1;//kn计价币
    public static final int COIN_TYPE_DK = 2;//dk计价币

    public static final String SCENE_YIBI_KN = "3511";//COIN行情，kn为计价币
    public static final String SCENE_YIBI_AUTO = "3511";//COIN行情，自选
    public static final String SCENE_YIBI_DK = "3511";//COIN行情，DK为计价币
    public static final String SCENE_WORLD_KN = "3512";//全球行情，kn为计价币
    public static final String SCENE_WORLD_DK = "3512";//全球行情，自选
    public static final String SCENE_WORLD_AUTO = "3512";//全球行情，DK为计价币

    @BindView(R.id.iv_fragment_marketList_tradeVolume)
    ImageView iv_volume;
    @BindView(R.id.iv_fragment_marketList_tradePrice)
    ImageView iv_price;
    @BindView(R.id.iv_fragment_marketList_riseFallRate)
    ImageView iv_rise;
    @BindView(R.id.rv_fragment_marketList_list)
    RecyclerView rv_list;

    /**顶部排序*/
    private boolean isVolumeUp,isPriceUp,isRateUp;
    private MarketListReceiver receiver;
    private MarketListAdapter adapter;

    private int coinType = 0;
    private int marketType = 0;
    private String scene = "3511";

    public static Fragment getInstance(int cointype,int markettype){
        MarketListFragment fragment = new MarketListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("cointype",cointype);
        bundle.putInt("markettype",markettype);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public int getContentViewId() {
        return R.layout.fragment_market_content_list;
    }

    @Override
    protected void initData() {
        if (getArguments() != null){
            coinType = getArguments().getInt("cointype",0);
            marketType = getArguments().getInt("markettype",0);
        }
        if (adapter == null){
            adapter = new MarketListAdapter(mContext);
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        rv_list.setLayoutManager(new LinearLayoutManager(mContext));
        rv_list.setAdapter(adapter);
        adapter.setCallback(this);

        if (marketType == TYPE_ONCOIN){
            if (coinType == COIN_TYPE_AUTO){
                scene = SCENE_YIBI_AUTO;
            }else if (coinType == COIN_TYPE_DK){
                scene = SCENE_YIBI_DK;
            }else {
                scene = SCENE_YIBI_KN;
            }
        }else if (marketType == TYPE_WORLD){
            adapter.setWord(true);
            if (coinType == COIN_TYPE_AUTO){
                scene = SCENE_WORLD_AUTO;
            }else if (coinType == COIN_TYPE_DK){
                scene = SCENE_WORLD_DK;
            }else {
                scene = SCENE_WORLD_KN;
            }
        }
        LogUtils.w("switch", "resume_marketOneListVisible");
        if (!getUserVisibleHint() || !isViewCreated) {
            LogUtils.w("switch", "goodsSale界面不显示，不予切换场景");
        }else {
            switchScene(new PushData(scene));
        }
//        initMarketData();
        if (receiver == null) {
            receiver = new MarketListReceiver();
        }
        /*注册监听*/
        IntentFilter filters = new IntentFilter();
        filters.addAction(Constants.SOCKET_ACTION);
        getLocalBroadcastManager().registerReceiver(receiver, filters);
        receiver.setListCallback(this);
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    @Subscribe (threadMode = ThreadMode.MAIN)
    public void SwichSceneEvent(EventMarketScene event){
        if (event == null){
            return;
        }
        LogUtils.w("switch","MarketListFragment接收marketOne----event:"+getUserVisibleHint());
        if (event.getType() == marketType && getUserVisibleHint()){
            switchScene(new PushData(scene));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
        /*解除挖矿状态监听*/
        if (receiver != null) {
            getLocalBroadcastManager().unregisterReceiver(receiver);
        }
    }
    /**
     * ViewPager中fragment的声明周期，切换显示不显示，刷新界面和数据接收
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (!isViewCreated) {
            return;
        }
        LogUtils.w("switch","marketOne----setUserVisibleHint:"+isVisibleToUser+isViewCreated);
        /*切换COIN和主流界面时*/
        if (isVisibleToUser) {
            LogUtils.w("switch", "marketOneListVisible");
            switchScene(new PushData(scene));
        }
//        else {
//            switchScene(null);
//        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @OnClick({R.id.tv_fragment_marketList_tradeVolume, R.id.tv_fragment_marketList_tradePrice, R.id.tv_fragment_marketList_riseFallRate,
            R.id.iv_fragment_marketList_tradeVolume, R.id.iv_fragment_marketList_tradePrice, R.id.iv_fragment_marketList_riseFallRate})
    public void onViewClicked(View view) {
        List<PushMarketBean.ListBean> list = adapter.getAllData();
        if (list == null || list.size() < 2){
            return;
        }
        switch (view.getId()) {
            /*24小时交易量*/
            case R.id.tv_fragment_marketList_tradeVolume:
            case R.id.iv_fragment_marketList_tradeVolume:
                isVolumeUp = !isVolumeUp;
                changeSortImg(iv_volume,isVolumeUp);
                Collections.sort(list, new Comparator<PushMarketBean.ListBean>() {
                    @Override
                    public int compare(PushMarketBean.ListBean arg0, PushMarketBean.ListBean arg1) {
                        double quantity1 = MathUtils.string2Double(arg0.getSumAmount());
                        double quantity2 = MathUtils.string2Double(arg1.getSumAmount());
                        if (quantity2 > quantity1) {
                            return !isVolumeUp?1:-1;
                        } else if (quantity2 == quantity1) {
                            return 0;
                        } else {
                            return !isVolumeUp?-1:1;
                        }
                    }
                });
                adapter.clear();
                adapter.addAll(list);
                break;
            /*最新价*/
            case R.id.tv_fragment_marketList_tradePrice:
            case R.id.iv_fragment_marketList_tradePrice:
                isPriceUp = !isPriceUp;
                changeSortImg(iv_price,isPriceUp);
                Collections.sort(list, new Comparator<PushMarketBean.ListBean>() {
                    @Override
                    public int compare(PushMarketBean.ListBean arg0, PushMarketBean.ListBean arg1) {
                        double price1 = MathUtils.string2Double(arg0.getNewPriceCNY());
                        double price2 = MathUtils.string2Double(arg1.getNewPriceCNY());
                        if (price2 > price1) {
                            return !isPriceUp?1:-1;
                        } else if (price2 == price1) {
                            return 0;
                        } else {
                            return !isPriceUp?-1:1;
                        }
                    }
                });
                adapter.clear();
                adapter.addAll(list);
                break;
            /*涨跌幅*/
            case R.id.tv_fragment_marketList_riseFallRate:
            case R.id.iv_fragment_marketList_riseFallRate:
                isRateUp = !isRateUp;
                changeSortImg(iv_rise,isRateUp);
                Collections.sort(list, new Comparator<PushMarketBean.ListBean>() {
                    @Override
                    public int compare(PushMarketBean.ListBean arg0, PushMarketBean.ListBean arg1) {
                        double rate1 = MathUtils.string2Double(arg0.getChgPrice());
                        double rate2 = MathUtils.string2Double(arg1.getChgPrice());
                        if (rate2 > rate1) {
                            return !isRateUp?1:-1;
                        } else if (rate2 == rate1) {
                            return 0;
                        } else {
                            return !isRateUp?-1:1;
                        }
                    }
                });
                adapter.clear();
                adapter.addAll(list);
                break;
            default:break;
        }
    }

    private void changeSortImg(ImageView view, boolean isUp) {
        iv_volume.setImageResource(R.mipmap.sort);
        iv_price.setImageResource(R.mipmap.sort);
        iv_rise.setImageResource(R.mipmap.sort);
        if (isUp) {
            view.setImageResource(R.mipmap.sort_up);
        }else{
            view.setImageResource(R.mipmap.sort_down);
        }
    }

    /**点击item跳转k线图*/
    @Override
    public void getPosition(int pos) {
        PushMarketBean.ListBean item = adapter.getItem(pos);
        int tradeCoin = item.getOrderCoinType();
        int perCoin = item.getUnitCoinType();
        Intent intent2KLine = new Intent(mContext, KLineActivity.class);
        intent2KLine.putExtra("type",marketType);
        intent2KLine.putExtra("tradeCoin",tradeCoin);
        intent2KLine.putExtra("perCoin",perCoin);
        getActivity().startActivityForResult(intent2KLine, MainActivity.REQUEST_KLINE);
    }


    @Override
    public void onMarketOneListCallback(List<PushMarketBean.ListBean> marketList) {
        if (marketType == TYPE_ONCOIN){
            hideLoading();
            if (marketList != null && marketList.size() > 0) {
                adapter.setData(marketList);
                adapter.notifyDataSetChanged();
            }
        }

    }

    @Override
    public void onMarketMainListCallback(List<PushMarketBean.ListBean> marketList) {
        if (marketType == TYPE_WORLD){
            hideLoading();
            if (marketList != null && marketList.size() > 0) {
                adapter.setData(marketList);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
