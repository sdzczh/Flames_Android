package huoli.com.pgy.Activitys;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import com.androidkun.xtablayout.XTabLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;
import huoli.com.pgy.Activitys.Base.BaseActivity;
import huoli.com.pgy.Adapters.ViewPagerAdapter;
import huoli.com.pgy.Fragments.C2CTradeOrderBusinessEntrustListFragment;
import huoli.com.pgy.Fragments.C2CTradeOrderBusinessOrderListFragment;
import huoli.com.pgy.Models.Beans.EventBean.EventC2cEntrustCondition;
import huoli.com.pgy.Models.Beans.EventBean.EventC2cOrderCondition;
import huoli.com.pgy.R;
import huoli.com.pgy.Widgets.ConditionsChooseFrame;

/**
 * 创建日期：2018/7/10 0010 on 下午 4:34
 * 描述: 法币(C2C)交易->顶部导航栏->订单(商家)
 *
 * @author xu
 */

public class C2CTradeOrderBusinessActivity extends BaseActivity {
    @BindView(R.id.activity_tradeC2COrderBusiness_title_tab)
    XTabLayout tabLayout;
    @BindView(R.id.activity_tradeC2COrderBusiness_title_viewPager)
    ViewPager viewPager;
    private List<Fragment> fragments;
    private List<String> fragmentsName;
    /**委托条件*/
    private List<String> entrustTradeTypes,entrustStateTypes;
    /**订单条件*/
    private List<String> orderTradeTypes,orderStateTypes;
    private int coinType;
    private int entrustTradeType = -1,entrustStateType = -1;
    private int orderTradeType = -1,orderStateType = -1;

    @Override
    public int getContentViewId() {
        return R.layout.activity_trade_c2corder_business;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {
        /*初始化所有fragment*/
        fragments = getFragments();
        fragmentsName = getFragmentsNames();
        entrustTradeTypes = getTradeTypes();
        orderTradeTypes = getTradeTypes();
        entrustStateTypes = getEntrustStateTypes();
        orderStateTypes = getOrderStateTypes();
        coinType = getIntent().getIntExtra("coinType",0);
    }

    private List<String> getTradeTypes() {
        List<String> tradeTypes = new ArrayList<>();
        tradeTypes.add("买入");
        tradeTypes.add("卖出");
        return tradeTypes;
    }

    /**
     * 添加所有fragment的标题
     */
    private List<String> getFragmentsNames() {
        List<String> names = new ArrayList<>();
        names.add("委托列表");
        names.add("订单记录");
        return names;
    }

    public List<String> getEntrustStateTypes() {
        List<String> stateTypes = new ArrayList<>();
        stateTypes.add("未完成");
        stateTypes.add("已完成");
        stateTypes.add("已撤销");
        return stateTypes;
    }

    public List<String> getOrderStateTypes() {
        List<String> stateTypes = new ArrayList<>();
        stateTypes.add("待付款");
        stateTypes.add("待确认");
        stateTypes.add("申诉中");
        stateTypes.add("已完成");
        stateTypes.add("已取消");
        stateTypes.add("超时取消");
        return stateTypes;
    }

    private List<Fragment> getFragments() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(C2CTradeOrderBusinessEntrustListFragment.newInstance(coinType));
        fragments.add(C2CTradeOrderBusinessOrderListFragment.newInstance(coinType));
        return fragments;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        /*初始化ViewPager的懒加载，添加几页缓存*/
        viewPager.setOffscreenPageLimit(fragments.size());
        setupViewPager(viewPager);
        /*设置ViewPager标题*/
        for (String name : fragmentsName) {
            tabLayout.addTab(tabLayout.newTab().setText(name));
        }
        tabLayout.setupWithViewPager(viewPager);
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

    @OnClick({R.id.activity_tradeC2COrderBusiness_title_back, R.id.activity_tradeC2COrderBusiness_title_choose})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            /*返回*/
            case R.id.activity_tradeC2COrderBusiness_title_back:
                C2CTradeOrderBusinessActivity.this.finish();
                break;
                /*条件筛选*/
            case R.id.activity_tradeC2COrderBusiness_title_choose:
                int currentItem = viewPager.getCurrentItem();
                switch (currentItem){
                    default:
                        /*委托列表*/
                    case 0:
                        showEntrustConditionChooseDialog();
                        break;
                        /*订单记录*/
                    case 1:
                        showOrderConditionChooseDialog();
                        break;
                }
                break;
                default:break;
        }
    }

    /**委托条件筛选*/
    private void showEntrustConditionChooseDialog() {
        ConditionsChooseFrame conditionChoose = new ConditionsChooseFrame(mContext,"委托列表",entrustTradeTypes,entrustStateTypes,entrustTradeType,entrustStateType);
        conditionChoose.setGetConditionsListener(new ConditionsChooseFrame.ChooseConditionsListener() {
            @Override
            public void getConditionsChoose(int firstChoose, int secondChoose) {
                entrustTradeType = firstChoose;
                entrustStateType = secondChoose;
                EventBus.getDefault().post(new EventC2cEntrustCondition(firstChoose,secondChoose));
            }
        });
        conditionChoose.showUp(tabLayout);
    }

    /**订单条件筛选*/
    private void showOrderConditionChooseDialog() {
        ConditionsChooseFrame conditionChoose = new ConditionsChooseFrame(mContext,"订单记录",orderTradeTypes,orderStateTypes,orderTradeType,orderStateType);
        conditionChoose.setGetConditionsListener(new ConditionsChooseFrame.ChooseConditionsListener() {
            @Override
            public void getConditionsChoose(int firstChoose, int secondChoose) {
                orderTradeType = firstChoose;
                orderStateType = secondChoose;
                EventBus.getDefault().post(new EventC2cOrderCondition(firstChoose,secondChoose));
            }
        });
        conditionChoose.showUp(tabLayout);
    }

}
