package app.com.pgy.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidkun.xtablayout.XTabLayout;

import java.util.ArrayList;
import java.util.List;

import app.com.pgy.Fragments.Base.BaseFragment;
import app.com.pgy.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static app.com.pgy.Constants.StaticDatas.NORMAL;

/**
 * Create by YX on 2019/10/6 0006
 */
public class C2CTradeFragment extends BaseFragment {
    @BindView(R.id.activity_tradeC2C_title_back)
    ImageView activityTradeC2CTitleBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.activity_tradeC2C_title_right)
    TextView activityTradeC2CTitleRight;
    @BindView(R.id.activity_tradeC2C_title_tab)
    XTabLayout activityTradeC2CTitleTab;
    @BindView(R.id.vp_content)
    ViewPager vpContent;

    private List<Integer> c2cCoinTypes;
    private boolean isBuy = true;
    private int role = NORMAL;
    private int coinType;
    public static C2CTradeFragment newInstance() {

        return  new C2CTradeFragment();
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_trade_c2c_new;
    }

    @Override
    protected void initData() {
        c2cCoinTypes = getC2cCoinList();
        coinType = c2cCoinTypes.size() > 0 ? c2cCoinTypes.get(0) : 0;
        tvTitle.setText(getCoinName(coinType)+"/CNY");
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @OnClick(R.id.activity_tradeC2C_title_right)
    public void onViewClicked() {
    }


    private List<Fragment> getFragments() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(C2cTradeBuyOrSaleNormalFragment.newInstance(coinType, isBuy));
        fragments.add(C2cTradeBuyOrSaleBusinessFragment.newInstance(coinType, isBuy));
        return fragments;
    }

    /**
     * 添加所有fragment的标题
     */
    private List<String> getFragmentsNames() {
        List<String> names = new ArrayList<>();
        names.add("买入");
        names.add("卖出");
        names.add("当前委托");
        names.add("历史委托");
        return names;
    }
}
