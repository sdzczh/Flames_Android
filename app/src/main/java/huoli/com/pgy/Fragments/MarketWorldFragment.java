package huoli.com.pgy.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import huoli.com.pgy.Fragments.Base.BaseFragment;
import huoli.com.pgy.R;

/**
 * Created by YX on 2018/7/13.
 */

public class MarketWorldFragment extends BaseFragment {

    public static Fragment getInstance(){
        MarketWorldFragment fragment = new MarketWorldFragment();
        return fragment;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_market_world;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }
}
