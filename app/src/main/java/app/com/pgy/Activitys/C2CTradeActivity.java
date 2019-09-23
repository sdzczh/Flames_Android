package app.com.pgy.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import app.com.pgy.Activitys.Base.BaseActivity;
import app.com.pgy.Fragments.C2cTradeBuyOrSaleBusinessFragment;
import app.com.pgy.Fragments.C2cTradeBuyOrSaleNormalFragment;
import app.com.pgy.R;
import app.com.pgy.Utils.LogUtils;

import static app.com.pgy.Constants.StaticDatas.BUSINESS;
import static app.com.pgy.Constants.StaticDatas.NORMAL;

/**
 * 创建日期：2017/11/22 0022 on 上午 11:23
 * 描述:C2C界面，包含购买界面、出售界面
 *
 * @author 徐庆重
 */
public class C2CTradeActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, TabLayout.OnTabSelectedListener {

    @BindView(R.id.activity_tradeC2C_title_group)
    RadioGroup activityTradeC2CTitleGroup;
    @BindView(R.id.activity_tradeC2C_title_roleGroup)
    RadioGroup activityTradeC2CTitleRoleGroup;
    @BindView(R.id.activity_tradeC2C_title_tab)
    TabLayout activityTradeC2CTitleTab;
    @BindView(R.id.activity_tradeC2C_title_tab_check)
    CheckBox activityTradeC2CTitleTabCheck;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private FragmentManager fm;
    private Fragment mContent = null;
    private List<Integer> c2cCoinTypes;
    private boolean isBuy = true;
    private int role = NORMAL;
    private int coinType;

    @Override
    public int getContentViewId() {
        return R.layout.activity_trade_c2c;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.w("C2CTrade", "C2CTradeActivity:onCreate");
    }

    @Override
    protected void initData() {
        c2cCoinTypes = getC2cCoinList();
        coinType = c2cCoinTypes.size() > 0 ? c2cCoinTypes.get(0) : 0;
        tvTitle.setText(getCoinName(coinType)+"/CNY");
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        activityTradeC2CTitleGroup.setOnCheckedChangeListener(this);
        activityTradeC2CTitleRoleGroup.setOnCheckedChangeListener(this);
        activityTradeC2CTitleTab.setOnTabSelectedListener(this);
        /*设置ViewPager标题*/
        for (Integer coinType : c2cCoinTypes) {
            activityTradeC2CTitleTab.addTab(activityTradeC2CTitleTab.newTab().setText(getCoinName(coinType)));
        }
        activityTradeC2CTitleTabCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    role = BUSINESS;
                    refreshFragment();
                } else {
                    role = NORMAL;
                    refreshFragment();
                }
            }
        });
    }

    @OnClick({R.id.activity_tradeC2C_title_back, R.id.activity_tradeC2C_title_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            /*返回*/
            case R.id.activity_tradeC2C_title_back:
                C2CTradeActivity.this.finish();
                break;
            /*跳转订单列表*/
            case R.id.activity_tradeC2C_title_right:
                if (!isLogin()) {
                    showToast(R.string.unlogin);
                    return;
                }
                intent2OrderList();
                break;
            default:
                break;
        }
    }

    private void intent2OrderList() {
        Intent intent = new Intent();
        if (role == NORMAL) {
            intent.setClass(mContext, C2CTradeOrderNormalListActivity.class);
            intent.putExtra("coinType", coinType);
        } else {
            intent.setClass(mContext, C2CTradeOrderBusinessActivity.class);
            intent.putExtra("coinType", coinType);
        }
        startActivity(intent);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            /*购买*/
            case R.id.activity_tradeC2C_title_group_buy:
                isBuy = true;
                refreshFragment();
                break;
            /*出售*/
            case R.id.activity_tradeC2C_title_group_sale:
                isBuy = false;
                refreshFragment();
                break;
            /*普通*/
            case R.id.activity_tradeC2C_title_roleGroup_normal:
                role = NORMAL;
                refreshFragment();
                break;
            /*商家*/
            case R.id.activity_tradeC2C_title_roleGroup_business:
                role = BUSINESS;
                refreshFragment();
                break;
            default:
                break;
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        coinType = c2cCoinTypes.get(tab.getPosition());
        refreshFragment();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private void refreshFragment() {
        Fragment to;
        switch (role) {
            case NORMAL:
                to = C2cTradeBuyOrSaleNormalFragment.newInstance(coinType, isBuy);
                break;
            case BUSINESS:
                to = C2cTradeBuyOrSaleBusinessFragment.newInstance(coinType, isBuy);
                break;
            default:
                to = null;
                break;
        }
        switchContent(to);
    }

    /**
     * 页面跳转
     */
    public void switchContent(Fragment to) {
        if (to == null) {
            return;
        }
        LogUtils.w(TAG, to.toString());
        if (fm == null) {
            fm = getSupportFragmentManager();
        }
        FragmentTransaction ft = fm.beginTransaction();
        if (mContent == null) {
            /*第一次进入，当前content为空，则添加第一个*/
            ft.add(R.id.activity_tradeC2C_content, to);
        } else {
            if (!to.isAdded()) {
                ft.hide(mContent).add(R.id.activity_tradeC2C_content, to);
            } else {
                ft.hide(mContent).show(to);
            }
        }
        ft.commitAllowingStateLoss();
        mContent = to;
    }

}