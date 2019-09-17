package huoli.com.pgy.Fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.RadioGroup;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import huoli.com.pgy.Fragments.Base.BaseFragment;
import huoli.com.pgy.R;
import huoli.com.pgy.Widgets.MyViewPagerAdapter;

/**
 * 创建日期：2018/7/9 0009 on 下午 5:50
 * 描述:  C2C法币交易出售界面，包括普通和商家
 *
 * @author xu
 */

public class C2CTradeSaleFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener{
    private static C2CTradeSaleFragment instance;
    @BindView(R.id.fragment_tradeC2c_tab)
    TabLayout fragmentTradeC2cTab;
    @BindView(R.id.fragment_tradeC2c_group)
    RadioGroup fragmentTradeC2cGroup;
    @BindView(R.id.fragment_tradeC2c_viewpager)
    ViewPager fragmentTradeC2cViewpager;
    private List<Integer> c2cCoinTypes;
    private PagerAdapter adapter;
    private List<Fragment> fragments;


    public C2CTradeSaleFragment() {
    }

    public static C2CTradeSaleFragment newInstance() {
        if (instance == null) {
            instance = new C2CTradeSaleFragment();
        }
        return instance;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_trade_c2c;
    }

    @Override
    protected void initData() {
        c2cCoinTypes = new ArrayList<>();
        fragments=new ArrayList<>();
        c2cCoinTypes.add(0);
        c2cCoinTypes.add(1);
        if (adapter == null) {
            adapter = new MyViewPagerAdapter(getChildFragmentManager(),fragments,c2cCoinTypes);
        }
        fragmentTradeC2cViewpager.setAdapter(adapter);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            /*选择普通*/
            case R.id.fragment_tradeC2c_group_normal:
                setupNormalViewPager();
                break;
                /*选择商家*/
            case R.id.fragment_tradeC2c_group_business:
                setupBusinessViewPager();
                break;
            default:
                break;
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        fragmentTradeC2cGroup.setOnCheckedChangeListener(this);
        fragmentTradeC2cViewpager.setOffscreenPageLimit(c2cCoinTypes.size());
        setupNormalViewPager();
        /*设置ViewPager标题*/
        for (Integer coinType : c2cCoinTypes) {
            fragmentTradeC2cTab.addTab(fragmentTradeC2cTab.newTab().setText(getCoinName(coinType)));
        }
        fragmentTradeC2cTab.setupWithViewPager(fragmentTradeC2cViewpager);
    }

    /**
     * 将构造出来的不同类别的货币添加到ViewPager的适配器中
     */
    private void setupNormalViewPager() {
        fragments.clear();
        //往适配器中两个参数，第一个是fragment，第二个是该类在viewpager的标题
        for (int i = 0; i < c2cCoinTypes.size(); i++) {
            Integer coinType = c2cCoinTypes.get(i);
            fragments.add(C2cTradeBuyOrSaleNormalFragment.newInstance(coinType,false));
        }
        adapter.notifyDataSetChanged();
    }

    private void setupBusinessViewPager() {
        fragments.clear();
        //往适配器中两个参数，第一个是fragment，第二个是该类在viewpager的标题
        for (int i = 0; i < c2cCoinTypes.size(); i++) {
            Integer coinType = c2cCoinTypes.get(i);
            fragments.add(C2cTradeBuyOrSaleBusinessFragment.newInstance(coinType,false));
        }
        adapter.notifyDataSetChanged();
    }


}
