package app.com.pgy.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import app.com.pgy.Fragments.Base.BaseFragment;
import app.com.pgy.R;

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
