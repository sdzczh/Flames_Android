package app.com.pgy.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import app.com.pgy.Widgets.MyLeftSecondarySpinnerList;
import app.com.pgy.Widgets.MyTradeLeftSpinnerPopupWondow;
import butterknife.BindView;
import butterknife.OnClick;
import app.com.pgy.Adapters.ViewPagerAdapter;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Fragments.Base.BaseFragment;
import app.com.pgy.Models.Beans.EventBean.EventGoodsChange;
import app.com.pgy.Models.Beans.EventBean.EventKLineSceneDestroy;
import app.com.pgy.Models.Beans.EventBean.EventLoginState;
import app.com.pgy.Models.Beans.EventBean.EventMarketScene;
import app.com.pgy.R;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Utils.Utils;
import app.com.pgy.Widgets.NoScrollViewPager;
import app.com.pgy.im.UI.IMMainActivity;
import io.rong.imkit.RongIM;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.model.Conversation;

/**
 * 创建日期：2018/04/18 0022 on 上午 11:23
 * 描述:交易界面
 *
 * @author 徐庆重
 */
public class TradeFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener, IUnReadMessageObserver {
    private static TradeFragment instance;
    @BindView(R.id.fragment_trade_group)
    RadioGroup fragmentTradeGroup;
    @BindView(R.id.fragment_trade_viewpager)
    NoScrollViewPager fragmentTradeViewpager;
    @BindView(R.id.view_titleMessage_point)
    View viewTitleMessagePoint;
    @BindView(R.id.view_titleMessage_frame)
    FrameLayout viewTitleMessageFrame;
    @BindView(R.id.fragment_trade_group_c2c)
    RadioButton fragmentTradeGroupC2c;
    private List<Fragment> fragments;

    public TradeFragment() {
    }

    public static TradeFragment newInstance() {
        if (instance == null) {
            instance = new TradeFragment();
        }
        return instance;
    }

    final Conversation.ConversationType[] conversationTypes = {
            Conversation.ConversationType.PRIVATE,
            Conversation.ConversationType.GROUP, Conversation.ConversationType.SYSTEM,
            Conversation.ConversationType.PUBLIC_SERVICE, Conversation.ConversationType.APP_PUBLIC_SERVICE
    };

    @Override
    public int getContentViewId() {
        return R.layout.fragment_trade;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    private void updateLogin() {
        if (isLogin()) {
//            viewTitleMessageFrame.setVisibility(View.VISIBLE);
            RongIM.getInstance().addUnReadMessageCountChangedObserver(this, conversationTypes);
        } else {
//            viewTitleMessageFrame.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCountChanged(int i) {
        if (i > 0 || Preferences.isHasNewFriend()) {
            viewTitleMessagePoint.setVisibility(View.VISIBLE);
        } else {
            viewTitleMessagePoint.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initData() {
        //获取所有fragment
        fragments = getFragments();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        RongIM.getInstance().addUnReadMessageCountChangedObserver(this, conversationTypes);
        fragmentTradeViewpager.setOffscreenPageLimit(fragments.size());
        setupViewPager(fragmentTradeViewpager);
        fragmentTradeGroup.setOnCheckedChangeListener(this);
        updateLogin();
        //获取默认值
        fragmentTradeViewpager.setCurrentItem(0);
    }

    /**
     * 添加所有fragment
     */
    private List<Fragment> getFragments() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(TradeGoodsFragment.newInstance());
        if ( type != 0 && fragments.get(0) instanceof TradeGoodsFragment){
            ((TradeGoodsFragment)fragments.get(0)).setTypes(type);
        }
        //fragments.add(TradeLeverFragment.newInstance());
        return fragments;
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
            int currentItem = fragmentTradeViewpager.getCurrentItem();
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

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            /*币币*/
            case R.id.fragment_trade_group_c2c:
                if (fragmentTradeViewpager.getCurrentItem() != 0){
                    fragmentTradeViewpager.setCurrentItem(0);
                }
                break;
                /*杠杆*/
            case R.id.fragment_trade_group_lever:
                //fragmentTradeViewpager.setCurrentItem(1);
                fragmentTradeGroupC2c.setChecked(true);
                showToast("玩命开发中");
                break;
            default:
                break;
        }
    }

    /**
     * 登录状态监听
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventLoginState loginState) {
        updateLogin();
    }

    /**
     * 将构造出来的不同类别的货币添加到ViewPager的适配器中
     */
    private void setupViewPager(ViewPager mViewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        //往适配器中两个参数，第一个是fragment，第二个是该类在viewpager的标题
        for (int i = 0; i < fragments.size(); i++) {
            adapter.addFragment(fragments.get(i), "");
        }
        mViewPager.setAdapter(adapter);
    }


    @OnClick({R.id.view_titleMessage_frame,R.id.iv_fragment_trade_more})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.view_titleMessage_frame:
                Utils.IntentUtils(mContext, IMMainActivity.class);
                break;
            case R.id.iv_fragment_trade_more:
                MyTradeLeftSpinnerPopupWondow spinnerCoin = new MyTradeLeftSpinnerPopupWondow(getActivity(), Preferences.getGoodsPerCoin());
                spinnerCoin.showLeft();
                break;
        }

    }

    //防止第一次加载时，接收不到广播现象
    int type = 0;
    public void setTypes(int t){
        type = t;
        if ( type != 0 && fragments != null && fragments.size() > 0){
            if (fragments.get(0) instanceof TradeGoodsFragment){
                ((TradeGoodsFragment)fragments.get(0)).setTypes(type);
            }
        }
    }

    //广播改变现实页面
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void goodsFragmentChangeEvent(EventGoodsChange event){
        LogUtils.e(TAG,"type=接收到改变广播");
        if (event != null){
            if (fragmentTradeViewpager.getCurrentItem() != 0){
                fragmentTradeViewpager.setCurrentItem(0);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void kLineSceneDestory(EventKLineSceneDestroy event) {
        if (event != null && event.isDestory()) {
            LogUtils.w("switch","TradeFragment接收----event:"+isHidden());
            if (!isHidden()){
                if (fragmentTradeViewpager.getCurrentItem() == 0){
                    EventBus.getDefault().post(new EventMarketScene(EventMarketScene.TYPE_TRADE_GOODS));
                }else if (fragmentTradeViewpager.getCurrentItem() == 1){
                    EventBus.getDefault().post(new EventMarketScene(EventMarketScene.TYPE_TRADE_LEVER));
                }
            }
        }
    }
}
