package app.com.pgy.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.RadioGroup;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import app.com.pgy.Adapters.ViewPagerAdapter;
import app.com.pgy.Fragments.Base.BaseFragment;
import app.com.pgy.Models.Beans.EventBean.EventKLineSceneDestroy;
import app.com.pgy.Models.Beans.EventBean.EventLoginState;
import app.com.pgy.Models.Beans.EventBean.EventMarketScene;
import app.com.pgy.R;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Widgets.NoScrollViewPager;

/**
 * 创建日期：2018/04/18 0022 on 上午 11:23
 * 描述:行情界面
 *
 * @author 徐庆重
 */
public class MarketFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener{
    public static final int TYPE_ONCOIN = 0;
    public static final int TYPE_WORLD = 1;

    private static MarketFragment instance;
    @BindView(R.id.rg_fragment_market_title)
    RadioGroup rg_title;
    @BindView(R.id.nvp_fragment_market_content)
    NoScrollViewPager nvp_content;
    @BindView(R.id.view_titleMessage_point)
    View viewTitleMessagePoint;
    private List<Fragment> fragments;
    private List<String> fragmentsName;

    public static Fragment getInstance() {
        if (instance == null) {
            instance = new MarketFragment();
        }
        return instance;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_market;
    }

    @Override
    protected void initData() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        fragments = getFragments();
        fragmentsName = getFragmentsNames();
    }

    /**
     * 登录状态监听
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventLoginState loginState) {
        updateLogin();
    }

    private void updateLogin(){
        if (isLogin()){
//            viewTitleMessageFrame.setVisibility(View.VISIBLE);
//            RongIM.getInstance().addUnReadMessageCountChangedObserver(this, conversationTypes);
        }else {
//            viewTitleMessageFrame.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
//        RongIM.getInstance().addUnReadMessageCountChangedObserver(this, conversationTypes);
        /*初始化ViewPager的懒加载，添加几页缓存*/
        nvp_content.setOffscreenPageLimit(fragments.size());
        setupViewPager(nvp_content);
        rg_title.setOnCheckedChangeListener(this);
        updateLogin();
    }

    /**
     * 添加所有fragment
     */
    private List<Fragment> getFragments() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(MarketContentFragment.getInstance(TYPE_ONCOIN));
        fragments.add(MarketContentFragment.getInstance(TYPE_WORLD));
        return fragments;
    }

    /**
     * 添加所有fragment的标题
     */
    private List<String> getFragmentsNames() {
        List<String> names = new ArrayList<>();
        names.add("ANT");
        names.add("全球");
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
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.rb_fragment_market_oneCoin:
                if (nvp_content.getCurrentItem() != 0){
                    nvp_content.setCurrentItem(0);
                }
                rg_title.setBackgroundResource(R.mipmap.button_left);
                break;
            case R.id.rb_fragment_market_world:
                nvp_content.setCurrentItem(1);
//                showToast("玩命开发中");
//                rg_title.check(R.id.rb_fragment_market_oneCoin);
                rg_title.setBackgroundResource(R.mipmap.btn_qq);
                break;
            default:
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            if (nvp_content.getCurrentItem() == 0){
                EventBus.getDefault().post(new EventMarketScene(EventMarketScene.TYPE_MARKET_YIBI));
            }else if (nvp_content.getCurrentItem() == 1){
                EventBus.getDefault().post(new EventMarketScene(EventMarketScene.TYPE_MARKET_WORlD));
            }
        }
    }

    /**
     * ViewPager中fragment的声明周期，切换显示不显示，刷新界面和数据接收
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (!isViewCreated) {
            return;
        }
        LogUtils.w("switch","marketOne----setUserVisibleHint:"+isVisibleToUser+isViewCreated);
        /*切换COIN和主流界面时*/
        if (isVisibleToUser) {
            if (nvp_content.getCurrentItem() == 0){
                EventBus.getDefault().post(new EventMarketScene(EventMarketScene.TYPE_MARKET_YIBI));
            }else if (nvp_content.getCurrentItem() == 1){
                EventBus.getDefault().post(new EventMarketScene(EventMarketScene.TYPE_MARKET_WORlD));
            }
        }
//        else {
//            switchScene(null);
//        }
        super.setUserVisibleHint(isVisibleToUser);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void kLineSceneDestory(EventKLineSceneDestroy event) {
        if (event != null && event.isDestory()) {
            LogUtils.w("switch","MarketFragment接收----event:"+isHidden());
            if (!isHidden()){
                if (nvp_content.getCurrentItem() == 0){
                    EventBus.getDefault().post(new EventMarketScene(EventMarketScene.TYPE_MARKET_YIBI));
                }else if (nvp_content.getCurrentItem() == 1){
                    EventBus.getDefault().post(new EventMarketScene(EventMarketScene.TYPE_MARKET_WORlD));
                }

            }
        }
    }
}
