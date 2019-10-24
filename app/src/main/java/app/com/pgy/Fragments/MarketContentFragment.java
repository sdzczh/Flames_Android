package app.com.pgy.Fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import app.com.pgy.Adapters.ViewPagerAdapter;
import app.com.pgy.Fragments.Base.BaseFragment;
import app.com.pgy.R;
import app.com.pgy.Widgets.NoScrollViewPager;

import static app.com.pgy.Fragments.MarketFragment.TYPE_ONCOIN;

/**
 * Created by YX on 2018/7/13.
 */

public class MarketContentFragment extends BaseFragment {

    @BindView(R.id.tab_fragment_market_content)
    TabLayout tab_percoins;
    @BindView(R.id.nvp_fragment_market_content_list)
    NoScrollViewPager nvp_market_list;
    private List<Fragment> fragments;
    private List<String> fragmentsName;
    private int marketType = 0;

    public static Fragment getInstance(int type){
        MarketContentFragment fragment = new MarketContentFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type",type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_market_content;
    }


    @Override
    protected void initData() {
        if (getArguments() != null){
            marketType = getArguments().getInt("type",0);
        }
        fragments = getFragments();
        fragmentsName = getFragmentsNames();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        /*初始化ViewPager的懒加载，添加几页缓存*/
        nvp_market_list.setOffscreenPageLimit(fragments.size());
        setupViewPager(nvp_market_list);
        /*设置ViewPager标题*/
        for (String name : fragmentsName) {
            tab_percoins.addTab(tab_percoins.newTab().setText(name));
        }
        tab_percoins.setupWithViewPager(nvp_market_list);
    }

    /**
     * 添加所有fragment
     */
    private List<Fragment> getFragments() {
        List<Fragment> fragments = new ArrayList<>();
//        fragments.add(MarketListFragment.getInstance(MarketListFragment.TYPE_AUTO,marketType));
        fragments.add(MarketListFragment.getInstance(MarketListFragment.COIN_TYPE_KN,marketType));
//        fragments.add(MarketListFragment.getInstance(MarketListFragment.TYPE_DK,marketType));
        return fragments;
    }
    /**
     * 添加所有fragment的标题
     */
    private List<String> getFragmentsNames() {
        List<String> names = new ArrayList<>();
//        names.add("自选");
        if (marketType == TYPE_ONCOIN){
            names.add("CNHT");
        }else {
            names.add("USDT");
        }
//        names.add("DK");
        return names;
    }

    /**
     * 将构造出来的不同类别的货币添加到ViewPager的适配器中
     */
    private void setupViewPager(ViewPager mViewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        //往适配器中两个参数，第一个是fragment，第二个是该类在viewpager的标题
        for (int i = 0; i < fragments.size(); i++) {
            adapter.addFragment(fragments.get(i), fragmentsName.get(i));
        }
        mViewPager.setAdapter(adapter);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            /*当GoodsFragment显示时，激活当前viewpager中的fragment*/
            if (fragments == null){
                fragments = getFragments();
            }
            /*获取当前显示的viewpager中的fragment*/
            int currentItem = nvp_market_list.getCurrentItem();
            if (currentItem < 0 || currentItem >= fragments.size()){
                currentItem = 0;
            }
            Fragment fragment = fragments.get(currentItem);
            if (fragment != null) {
                fragment.setUserVisibleHint(true);
            }
        }else{
            switchScene(null);
        }
    }

}
