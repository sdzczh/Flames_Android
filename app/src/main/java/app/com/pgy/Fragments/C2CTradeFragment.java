package app.com.pgy.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import app.com.pgy.Adapters.ViewPagerAdapter;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Fragments.Base.BaseFragment;
import app.com.pgy.Interfaces.spinnerSingleChooseListener;
import app.com.pgy.Models.Beans.EventBean.EventC2cTradeCoin;
import app.com.pgy.R;
import app.com.pgy.Widgets.CoinTypeListPopupwindow;
import app.com.pgy.im.utils.Resource;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static app.com.pgy.Constants.StaticDatas.BUSINESS;
import static app.com.pgy.Constants.StaticDatas.NORMAL;

/**
 * Create by YX on 2019/10/6 0006
 */
public class C2CTradeFragment extends BaseFragment {
    private static C2CTradeFragment instance;
    @BindView(R.id.activity_tradeC2C_title_back)
    ImageView activityTradeC2CTitleBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.activity_tradeC2C_title_right)
    TextView activityTradeC2CTitleRight;
    @BindView(R.id.view_line)
    View viewLine;
    @BindView(R.id.vp_content)
    ViewPager vpContent;
    @BindView(R.id.vp_content2)
    ViewPager vpContent2;
    @BindView(R.id.rb_fragment_trade_c2c_tab1)
    RadioButton rbFragmentTradeC2cTab1;
    @BindView(R.id.rb_fragment_trade_c2c_tab2)
    RadioButton rbFragmentTradeC2cTab2;
    @BindView(R.id.rb_fragment_trade_c2c_tab3)
    RadioButton rbFragmentTradeC2cTab3;
    @BindView(R.id.rb_fragment_trade_c2c_tab4)
    RadioButton rbFragmentTradeC2cTab4;
    @BindView(R.id.rg_fragment_trade_c2c_tab)
    RadioGroup rgFragmentTradeC2cTab;
    @BindView(R.id.view_fragment_trade_c2c_tab1)
    View viewFragmentTradeC2cTab1;
    @BindView(R.id.view_fragment_trade_c2c_tab2)
    View viewFragmentTradeC2cTab2;
    @BindView(R.id.view_fragment_trade_c2c_tab3)
    View viewFragmentTradeC2cTab3;
    @BindView(R.id.view_fragment_trade_c2c_tab4)
    View viewFragmentTradeC2cTab4;
    @BindView(R.id.tv_tradeC2C_normal)
    TextView tvTradeC2CNormal;
    @BindView(R.id.tv_tradeC2C_business)
    TextView tvTradeC2CBusiness;
    @BindView(R.id.ll_tradeC2C_role)
    LinearLayout llTradeC2CRole;
    private List<Integer> c2cCoinTypes;
    private boolean isBuy = true;
    private int role = NORMAL;
    private int coinType;
    private ViewPagerAdapter nAdapter, bAdapter;

    public static C2CTradeFragment newInstance() {
        if (instance == null) {
            instance = new C2CTradeFragment();
        }
        return instance;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_trade_c2c_new;
    }

    @Override
    protected void initData() {

        coinType = Preferences.getC2CCoin();
        if (coinType < 0) {
            c2cCoinTypes = getC2cCoinList();
            coinType = c2cCoinTypes.size() > 0 ? c2cCoinTypes.get(0) : 0;
        }

        tvTitle.setText(getCoinName(coinType) + "/CNY");
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        rgFragmentTradeC2cTab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_fragment_trade_c2c_tab1:
                        hideLineView(0);
                        break;
                    case R.id.rb_fragment_trade_c2c_tab2:
                        hideLineView(1);
                        break;
                    case R.id.rb_fragment_trade_c2c_tab3:
                        hideLineView(2);
                        break;
                    case R.id.rb_fragment_trade_c2c_tab4:
                        hideLineView(3);
                        break;
                }
            }
        });
        vpContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        rbFragmentTradeC2cTab1.setChecked(true);
                        break;
                    case 1:
                        rbFragmentTradeC2cTab2.setChecked(true);
                        break;
                    case 2:
                        rbFragmentTradeC2cTab3.setChecked(true);
                        break;
                    case 3:
                        rbFragmentTradeC2cTab4.setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        vpContent2.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        rbFragmentTradeC2cTab1.setChecked(true);
                        break;
                    case 1:
                        rbFragmentTradeC2cTab2.setChecked(true);
                        break;
                    case 2:
                        rbFragmentTradeC2cTab3.setChecked(true);
                        break;
                    case 3:
                        rbFragmentTradeC2cTab4.setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        updateNormal();
    }

    private void updateNormal() {
        vpContent.setVisibility(View.VISIBLE);
        vpContent2.setVisibility(View.GONE);
        viewFragmentTradeC2cTab3.setBackgroundResource(R.color.txt_3075EE);
        viewFragmentTradeC2cTab4.setBackgroundResource(R.color.txt_3075EE);
        rbFragmentTradeC2cTab1.setText("买入");
        rbFragmentTradeC2cTab2.setText("卖出");
        rbFragmentTradeC2cTab3.setText("当前委托");
        rbFragmentTradeC2cTab4.setText("历史委托");
        rbFragmentTradeC2cTab3.setTextColor(getResources().getColorStateList(R.color.selector_font_appbg_99));
        rbFragmentTradeC2cTab4.setTextColor(getResources().getColorStateList(R.color.selector_font_appbg_99));
        if (nAdapter == null) {
            nAdapter = new ViewPagerAdapter(getChildFragmentManager());
            List<Fragment> normalFragment = new ArrayList<>();
            normalFragment = getNormalFragments();
            for (int i = 0; i < normalFragment.size(); i++) {
                nAdapter.addFragment(normalFragment.get(i), "");
            }
            vpContent.setAdapter(nAdapter);
        }
        vpContent.setCurrentItem(0);
        rbFragmentTradeC2cTab1.setChecked(true);
    }

    private void updateBusiness() {
        vpContent.setVisibility(View.GONE);
        vpContent2.setVisibility(View.VISIBLE);
        viewFragmentTradeC2cTab3.setBackgroundResource(R.color.txt_4DB872);
        viewFragmentTradeC2cTab4.setBackgroundResource(R.color.txt_4DB872);
        rbFragmentTradeC2cTab1.setText("商家买入");
        rbFragmentTradeC2cTab2.setText("商家卖出");
        rbFragmentTradeC2cTab3.setText("商家接单");
        rbFragmentTradeC2cTab4.setText("当前委托");
        rbFragmentTradeC2cTab3.setTextColor(getResources().getColorStateList(R.color.selector_font_green_99));
        rbFragmentTradeC2cTab4.setTextColor(getResources().getColorStateList(R.color.selector_font_green_99));
        if (bAdapter == null) {
            bAdapter = new ViewPagerAdapter(getChildFragmentManager());
            List<Fragment> businessFragments = getBusinessFragments();
            for (int i = 0; i < businessFragments.size(); i++) {
                bAdapter.addFragment(businessFragments.get(i), "");
            }
            vpContent2.setAdapter(bAdapter);
        }
        vpContent2.setCurrentItem(0);
        rbFragmentTradeC2cTab1.setChecked(true);
    }

    private void hideLineView(int tab) {
        if (role == NORMAL){
            vpContent.setCurrentItem(tab);

        }else
        {
            vpContent2.setCurrentItem(tab);
        }
        viewFragmentTradeC2cTab1.setVisibility(View.INVISIBLE);
        viewFragmentTradeC2cTab2.setVisibility(View.INVISIBLE);
        viewFragmentTradeC2cTab3.setVisibility(View.INVISIBLE);
        viewFragmentTradeC2cTab4.setVisibility(View.INVISIBLE);
        switch (tab) {
            case 0:
                viewFragmentTradeC2cTab1.setVisibility(View.VISIBLE);
                break;
            case 1:
                viewFragmentTradeC2cTab2.setVisibility(View.VISIBLE);
                break;
            case 2:
                viewFragmentTradeC2cTab3.setVisibility(View.VISIBLE);
                break;
            case 3:
                viewFragmentTradeC2cTab4.setVisibility(View.VISIBLE);
                break;
        }
    }

    private List<Fragment> getNormalFragments() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(C2cTradeBuyOrSaleNormalFragment.newInstance(coinType, true));
        fragments.add(C2cTradeBuyOrSaleNormalFragment.newInstance(coinType, false));
        fragments.add(C2CTradeOrderNormalListFragment.newInstance(coinType, true));
        fragments.add(C2CTradeOrderNormalListFragment.newInstance(coinType, false));
        return fragments;
    }

    private List<Fragment> getBusinessFragments() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(C2cTradeBuyOrSaleBusinessFragment.newInstance(coinType, true));
        fragments.add(C2cTradeBuyOrSaleBusinessFragment.newInstance(coinType, false));
        fragments.add(C2CTradeOrderBusinessOrderListFragment.newInstance(coinType));
        fragments.add(C2CTradeOrderBusinessEntrustListFragment.newInstance(coinType));
        return fragments;
    }

    @OnClick({R.id.tv_title,R.id.activity_tradeC2C_title_right,R.id.tv_tradeC2C_normal, R.id.tv_tradeC2C_business,R.id.ll_tradeC2C_role})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_title:
                showSpinner();
                break;
            case R.id.activity_tradeC2C_title_right:
                showRoleLayout(llTradeC2CRole.getVisibility() == View.GONE);
                break;
            case R.id.tv_tradeC2C_normal:
                showRoleLayout(false);
                if (role != NORMAL){
                    tvTradeC2CBusiness.setSelected(false);
                    tvTradeC2CNormal.setSelected(true);
                    role = NORMAL;
                    activityTradeC2CTitleRight.setText("普通");
                    updateNormal();
                }
                break;
            case R.id.tv_tradeC2C_business:
                showRoleLayout(false);
                if (role != BUSINESS){
                    role = BUSINESS;
                    tvTradeC2CBusiness.setSelected(true);
                    tvTradeC2CNormal.setSelected(false);
                    activityTradeC2CTitleRight.setText("商家");
                    updateBusiness();
                }
                break;
            case R.id.ll_tradeC2C_role:
                showRoleLayout(false);
                break;
        }
    }

    private void showRoleLayout(boolean isShow){
        if (isShow){
            llTradeC2CRole.setVisibility(View.VISIBLE);
            if (role == NORMAL){
                tvTradeC2CBusiness.setSelected(false);
                tvTradeC2CNormal.setSelected(true);
            }else {
                tvTradeC2CBusiness.setSelected(true);
                tvTradeC2CNormal.setSelected(false);
            }
        }else {
            llTradeC2CRole.setVisibility(View.GONE);
        }
    }


    private CoinTypeListPopupwindow coinspinner;
    private void showSpinner() {
        if ( getC2cCoinList() == null ||  getC2cCoinList().size() <= 0) {
            return;
        }
        if (coinspinner == null) {
            coinspinner = new CoinTypeListPopupwindow(getActivity(), getC2cCoinList(), new spinnerSingleChooseListener() {
                @Override
                public void onItemClickListener(int position) {
                    coinspinner.dismiss();
                    if (getC2cCoinList().get(position) == coinType) {
                        return;
                    }
                    coinType = getC2cCoinList().get(position);
                    tvTitle.setText(getCoinName(coinType) + "/CNY");
                    EventBus.getDefault().post(new EventC2cTradeCoin(coinType));
                }
            });
        }
        coinspinner.setSelectCoin(coinType);
        if (!coinspinner.isShowing()) {
            coinspinner.showDown(viewLine);
        }
    }
}
