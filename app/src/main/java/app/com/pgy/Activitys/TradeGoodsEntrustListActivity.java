package app.com.pgy.Activitys;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidkun.xtablayout.XTabLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import app.com.pgy.Activitys.Base.BaseActivity;
import app.com.pgy.Adapters.ViewPagerAdapter;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Fragments.TradeGoodsCurrentEntrustListFragment;
import app.com.pgy.Fragments.TradeGoodsHistoryListFragment;
import app.com.pgy.Models.Beans.EventBean.EventGoodsToTrade;
import app.com.pgy.Models.Beans.EventBean.EventTradeCancelAll;
import app.com.pgy.R;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Create by YX on 2019/10/6 0006
 */
public class TradeGoodsEntrustListActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.title_tab)
    XTabLayout titleTab;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.vp_content)
    ViewPager vpContent;
    @BindView(R.id.tv_coin_name)
    TextView tvCoinName;

    /**
     * 选中的现货的计价币交易币
     */
    private int goodsPerCoin, goodsTradeCoin;
    private List<Integer> perCoinTypeList;
    //    private Map<Integer, List<Integer>> tradeCoinTypeMap;
    private List<Fragment> fragments;
    private List<String> fragmentsName;
    private int index = 0;

    @Override
    public int getContentViewId() {
        return R.layout.activity_trade_goods_entrustlist;
    }

    @Override
    protected void initData() {
        index = getIntent().getIntExtra("index", 0);
        /*初始化现货中计价币、交易币的位置，从本地获取*/
        goodsPerCoin = Preferences.getGoodsPerCoin();
        goodsTradeCoin = Preferences.getGoodsTradeCoin();
        /*获取计价币种列表，交易币种map*/
        perCoinTypeList = getGoodsPerCoinList();
//        tradeCoinTypeMap = getGoodsCoinMap();
        if (goodsPerCoin < 0) {
            /*如果本地计价币不存在，则默认设置为列表第一个*/
            goodsPerCoin = (perCoinTypeList != null && perCoinTypeList.size() > 0) ? perCoinTypeList.get(0) : 0;
            Preferences.setGoodsPerCoin(goodsPerCoin);
        }
        if (goodsTradeCoin < 0) {
            List<Integer> tradeCoins = getGoodsCoinMap().get(goodsPerCoin);
            goodsTradeCoin = (tradeCoins != null && tradeCoins.size() > 0) ? tradeCoins.get(0) : 0;
            Preferences.setGoodsTradeCoin(goodsTradeCoin);
        }
        tvCoinName.setText(getCoinName(goodsTradeCoin)+"/"+getCoinName(goodsPerCoin));
        /*初始化所有fragment*/
        fragments = getFragments();
        fragmentsName = getFragmentsNames();
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initViewpagerView();
    }

    private void initViewpagerView() {
        /*初始化ViewPager的懒加载，添加几页缓存*/
        vpContent.setOffscreenPageLimit(fragments.size());
        setupViewPager(vpContent);
        /*设置ViewPager标题*/
        for (String name : fragmentsName) {
            titleTab.addTab(titleTab.newTab().setText(name));
        }
        titleTab.setupWithViewPager(vpContent);
        titleTab.addOnTabSelectedListener(new XTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(XTabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    tvTitleRight.setVisibility(View.VISIBLE);
                } else {
                    tvTitleRight.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTabUnselected(XTabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(XTabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    tvTitleRight.setVisibility(View.VISIBLE);
                } else {
                    tvTitleRight.setVisibility(View.GONE);
                }
            }
        });
        if (index != 0) {
            vpContent.setCurrentItem(1);
        }
    }

    /**
     * 将构造出来的不同类别的货币添加到ViewPager的适配器中
     */
    private void setupViewPager(ViewPager mViewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        //往适配器中两个参数，第一个是fragment，第二个是该类在viewpager的标题
        for (int i = 0; i < fragments.size(); i++) {
            adapter.addFragment(fragments.get(i), fragmentsName.get(i));
        }
        mViewPager.setAdapter(adapter);
    }


    @OnClick({R.id.iv_back, R.id.tv_title_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_title_right:
                EventBus.getDefault().post(new EventTradeCancelAll(true));
                break;
        }
    }

    private List<Fragment> getFragments() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(TradeGoodsCurrentEntrustListFragment.newInstance(goodsPerCoin, goodsTradeCoin));
        fragments.add(TradeGoodsHistoryListFragment.newInstance(goodsPerCoin, goodsTradeCoin));
        return fragments;
    }

    /**
     * 添加所有fragment的标题
     */
    private List<String> getFragmentsNames() {
        List<String> names = new ArrayList<>();
        names.add("当前委托");
        names.add("历史委托");
        return names;
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void EventGoodsToTrade(EventGoodsToTrade event){
        finish();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }
}
