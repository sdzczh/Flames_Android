package app.com.pgy.Fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidkun.xtablayout.XTabLayout;
import com.tmall.ultraviewpager.UltraViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.com.pgy.Activitys.BannerListActivity;
import app.com.pgy.Activitys.Base.WebDetailActivity;
import app.com.pgy.Activitys.MyAccountActivity;
import app.com.pgy.Activitys.MyWalletRechargeActivity;
import app.com.pgy.Activitys.MyWalletTransferActivity;
import app.com.pgy.Activitys.MyWalletWithdrawActivity;
import app.com.pgy.Activitys.PersonalGroupsActivity;
import app.com.pgy.Adapters.HomeMarketAdapter;
import app.com.pgy.Adapters.HomeNewsAdapter;
import app.com.pgy.Constants.Constants;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Fragments.Base.BaseFragment;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Models.Beans.BannerInfo;
import app.com.pgy.Models.Beans.EventBean.EventLoginState;
import app.com.pgy.Models.Beans.EventBean.EventUserInfoChange;
import app.com.pgy.Models.Beans.HomeInfo;
import app.com.pgy.Models.Beans.HomeMarketBean;
import app.com.pgy.Models.Beans.PushBean.PushData;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Receivers.HomeMarketReceiver;
import app.com.pgy.Utils.BannerIntentUtils;
import app.com.pgy.Utils.ImageLoaderUtils;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Utils.LoginUtils;
import app.com.pgy.Utils.MathUtils;
import app.com.pgy.Utils.TimeUtils;
import app.com.pgy.Widgets.MarqueeTextView;
import app.com.pgy.Widgets.banner.ScalePageTransformer;
import butterknife.BindView;
import butterknife.OnClick;

import static app.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * Create by Android on 2019/10/10 0010
 */
public class HomeFragmentNew extends BaseFragment implements HomeMarketReceiver.onListCallback {
    private final static int AUTO_SCROLL = 4 * 1000;
    private static HomeFragmentNew instance;
    @BindView(R.id.view_home_unlogin)
    View viewHomeUnlogin;
    @BindView(R.id.ll_home_top_1)
    LinearLayout llHomeTop1;
    @BindView(R.id.ll_home_top_2)
    LinearLayout llHomeTop2;
    @BindView(R.id.ll_home_top_3)
    LinearLayout llHomeTop3;
    @BindView(R.id.hsv_home_top_unlogin)
    HorizontalScrollView hsvHomeTopUnlogin;
    @BindView(R.id.tv_home_top_title)
    TextView tvHomeTopTitle;
    @BindView(R.id.tv_fragment_home_unlogin)
    TextView tvFragmentHomeUnlogin;
    @BindView(R.id.iv_home_top_show)
    ImageView iv_show;
    @BindView(R.id.tv_fragment_home_total_asset)
    TextView tvFragmentHomeTotalAsset;
    @BindView(R.id.tv_fragment_home_total_asset1)
    TextView tvFragmentHomeTotalAsset1;
    @BindView(R.id.ll_fragment_home_c2c_asset)
    LinearLayout llFragmentHomeC2cAsset;
    @BindView(R.id.ll_fragment_home_trust)
    LinearLayout llFragmentHomeTrust;
    @BindView(R.id.ll_fragment_home_withdraw)
    LinearLayout llFragmentHomeWithdraw;
    @BindView(R.id.ll_fragment_home_recharge)
    LinearLayout llFragmentHomeRecharge;
    @BindView(R.id.ll_fragment_home_groups)
    LinearLayout llFragmentHomeGroups;
    @BindView(R.id.mtv_fragment_home_notice)
    MarqueeTextView mtv_notice;
    @BindView(R.id.uvp_fragment_home_slide)
    UltraViewPager uvp_slide;
    @BindView(R.id.stab_fragment_home_market)
    XTabLayout stabFragmentHomeMarket;
    @BindView(R.id.rv_fragment_home_market)
    RecyclerView rvMarket;
    @BindView(R.id.rv_fragment_home_market24h)
    RecyclerView rvMarket24H;
    @BindView(R.id.rv_fragment_home_news)
    RecyclerView rvFragmentHomeNews;
    @BindView(R.id.ll_fragment_home_up)
    LinearLayout llFragmentHomeUp;
    @BindView(R.id.tv_fragment_home_up)
    TextView tvFragmentHomeUp;
    @BindView(R.id.tv_fragment_home_down)
    TextView tvFragmentHomeDown;
    @BindView(R.id.view_fragment_home_up)
    View viewFragmentHomeUp;
    @BindView(R.id.view_fragment_home_down)
    View viewFragmentHomeDown;
    @BindView(R.id.ll_fragment_home_down)
    LinearLayout llFragmentHomeDown;
    @BindView(R.id.iv_fragment_home_up)
    ImageView ivMarkerUp;
    @BindView(R.id.iv_fragment_home_down)
    ImageView ivMarkerDown;

    private List<BannerInfo> slides;
    private boolean isShow = true;
    private SlideAdapter slideAdapter;

    private HomeInfo.NoticeBean noticeBean;
    private HomeInfo mHomeInfo;

    private List<String> fragmentsName;
    private HomeMarketAdapter marketAdapter,market24HAdapter;
    HomeNewsAdapter homeNewsAdapter;
    private String scene = "354";
    private HomeMarketReceiver receiver;
    public static HomeFragmentNew newInstance() {
        if (instance == null) {
            instance = new HomeFragmentNew();
        }
        return instance;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_home_new;
    }

    @Override
    protected void initData() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        switchScene(new PushData(scene));
//        initMarketData();
        if (receiver == null) {
            receiver = new HomeMarketReceiver();
        }
        rvMarket.setVisibility(View.VISIBLE);
        rvMarket24H.setVisibility(View.GONE);
        /*注册监听*/
        IntentFilter filters = new IntentFilter();
        filters.addAction(Constants.SOCKET_ACTION);
        getLocalBroadcastManager().registerReceiver(receiver, filters);
        receiver.setListCallback(this);
        fragmentsName = getFragmentsNames();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        for (String name : fragmentsName) {
            stabFragmentHomeMarket.addTab(stabFragmentHomeMarket.newTab().setText(name));
        }
        stabFragmentHomeMarket.addOnTabSelectedListener(new XTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(XTabLayout.Tab tab) {
                if (tab.getPosition() == 0){
                    rvMarket.setVisibility(View.VISIBLE);
                    rvMarket24H.setVisibility(View.GONE);
                    scene = "354";
                }else {
                    rvMarket.setVisibility(View.GONE);
                    rvMarket24H.setVisibility(View.VISIBLE);
                    scene = "355";
                }
                switchScene(new PushData(scene));
            }

            @Override
            public void onTabUnselected(XTabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(XTabLayout.Tab tab) {

            }
        });

        getHomeInfo();
    }


    private void updateLogin() {
        if (mHomeInfo == null){
            mHomeInfo = new HomeInfo();
        }
        ivMarkerUp.setImageResource(R.mipmap.home_bottom_up);
        ivMarkerDown.setImageResource(R.mipmap.home_bottom_down);
        if (isLogin()) {
            viewHomeUnlogin.setVisibility(View.GONE);
            tvFragmentHomeUnlogin.setVisibility(View.GONE);
            tvHomeTopTitle.setTextColor(getResources().getColor(R.color.black));
            hsvHomeTopUnlogin.setVisibility(View.GONE);
            llFragmentHomeC2cAsset.setVisibility(View.VISIBLE);
            if (Preferences.getUserMarket() == 1){
                ivMarkerUp.setImageResource(R.mipmap.home_bottom_up_sel);
            }else  if (Preferences.getUserMarket() == 0){
                ivMarkerDown.setImageResource(R.mipmap.home_bottom_down_sel);
            }
        } else {
            viewHomeUnlogin.setVisibility(View.VISIBLE);
            tvFragmentHomeUnlogin.setVisibility(View.VISIBLE);
            tvHomeTopTitle.setTextColor(getResources().getColor(R.color.white));
            hsvHomeTopUnlogin.setVisibility(View.VISIBLE);
            llFragmentHomeC2cAsset.setVisibility(View.GONE);
        }
        if (mHomeInfo.getMood() != null){
            tvFragmentHomeUp.setText(mHomeInfo.getMood().getMoodTop()+"%");
            tvFragmentHomeDown.setText(mHomeInfo.getMood().getMoodBottom()+"%");
            float top = Float.parseFloat(mHomeInfo.getMood().getMoodTop());
            float down = Float.parseFloat(mHomeInfo.getMood().getMoodBottom());
            viewFragmentHomeUp.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, top/100));
            viewFragmentHomeDown.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, down/100));
        }else {
            tvFragmentHomeUp.setText("100%");
            tvFragmentHomeDown.setText("0%");
            viewFragmentHomeUp.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
            viewFragmentHomeDown.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0f));
//                tvFragmentHomeDown.
        }
        if (mHomeInfo.getNewsList() == null || mHomeInfo.getNewsList().size() < 1){
            rvFragmentHomeNews.setVisibility(View.GONE);
        }else {
            rvFragmentHomeNews.setVisibility(View.VISIBLE);
            rvFragmentHomeNews.setLayoutManager(new LinearLayoutManager(mContext){
                @Override
                public boolean canScrollVertically() {
                    //解决ScrollView里存在多个RecyclerView时滑动卡顿的问题
                    //如果你的RecyclerView是水平滑动的话可以重写canScrollHorizontally方法
                    return false;
                }
            });
        //解决数据加载不完的问题
            rvFragmentHomeNews.setNestedScrollingEnabled(false);
            rvFragmentHomeNews.setHasFixedSize(true);
        //解决数据加载完成后, 没有停留在顶部的问题
            rvFragmentHomeNews.setFocusable(false);
            if (homeNewsAdapter == null){
                homeNewsAdapter = new HomeNewsAdapter(mContext);
                rvFragmentHomeNews.setAdapter(homeNewsAdapter);
            }
            homeNewsAdapter.clear();
            homeNewsAdapter.addAll(mHomeInfo.getNewsList());
        }


    }

    private void initMarket(){
        if (mHomeInfo == null){
            mHomeInfo = new HomeInfo();
        }

        if (marketAdapter == null){
            rvMarket.setLayoutManager(new LinearLayoutManager(mContext){
                @Override
                public boolean canScrollVertically() {
                    //解决ScrollView里存在多个RecyclerView时滑动卡顿的问题
                    //如果你的RecyclerView是水平滑动的话可以重写canScrollHorizontally方法
                    return false;
                }
            });
            //解决数据加载不完的问题
            rvMarket.setNestedScrollingEnabled(false);
            rvMarket.setHasFixedSize(true);
//解决数据加载完成后, 没有停留在顶部的问题
            rvMarket.setFocusable(false);
            marketAdapter = new HomeMarketAdapter(mContext);

            rvMarket.setAdapter(marketAdapter);
            marketAdapter.addAll(mHomeInfo.getMarketMain());
        }

        if (market24HAdapter == null){
            rvMarket24H.setLayoutManager(new LinearLayoutManager(mContext){
                @Override
                public boolean canScrollVertically() {
                    //解决ScrollView里存在多个RecyclerView时滑动卡顿的问题
                    //如果你的RecyclerView是水平滑动的话可以重写canScrollHorizontally方法
                    return false;
                }
            });
//解决数据加载不完的问题
            rvMarket24H.setNestedScrollingEnabled(false);
            rvMarket24H.setHasFixedSize(true);
//解决数据加载完成后, 没有停留在顶部的问题
            rvMarket24H.setFocusable(false);
            market24HAdapter = new HomeMarketAdapter(mContext);
            rvMarket24H.setAdapter(market24HAdapter);
            market24HAdapter.addAll(mHomeInfo.getMarket24h());
        }
    }

    private void updateNoice() {
        if (mHomeInfo == null || TextUtils.isEmpty(mHomeInfo.getNoticeTitle())) {
            return;
        }
        mtv_notice.setText(mHomeInfo.getNoticeTitle()+ "");
        mtv_notice.startScoll();
    }

    //判断是否需要重新加载banner
    private boolean isUpdateSlide() {
        boolean isUpdate = false;
        if (mHomeInfo != null && slides != null &&
                mHomeInfo.getBanner() != null) {
            if (mHomeInfo.getBanner().size() == slides.size()) {
                for (int i = 0; i < slides.size(); i++) {
                    BannerInfo bannerBean = slides.get(i);
                    BannerInfo bannerBean1 = mHomeInfo.getBanner().get(i);
                    if (!TextUtils.isEmpty(bannerBean.getAddress()) && !TextUtils.isEmpty(bannerBean1.getAddress())) {
                        if (!bannerBean.getAddress().equals(bannerBean1.getAddress())) {
                            isUpdate = true;
                            return true;
                        }
                    } else {
                        if (!(TextUtils.isEmpty(bannerBean.getAddress()) && TextUtils.isEmpty(bannerBean1.getAddress()))) {
                            isUpdate = true;
                            return true;
                        }
                    }

                    if (!TextUtils.isEmpty(bannerBean.getImgpath()) && !TextUtils.isEmpty(bannerBean1.getImgpath())) {
                        if (!bannerBean.getImgpath().equals(bannerBean1.getImgpath())) {
                            isUpdate = true;
                            return true;
                        }
                    } else {
                        if (!(TextUtils.isEmpty(bannerBean.getImgpath()) && TextUtils.isEmpty(bannerBean1.getImgpath()))) {
                            isUpdate = true;
                            return true;
                        }
                    }
                    if (!TextUtils.isEmpty(bannerBean.getTitle()) && !TextUtils.isEmpty(bannerBean1.getTitle())) {
                        if (!bannerBean.getTitle().equals(bannerBean1.getTitle())) {
                            isUpdate = true;
                            return true;
                        }
                    } else {
                        if (!(TextUtils.isEmpty(bannerBean.getTitle()) && TextUtils.isEmpty(bannerBean1.getTitle()))) {
                            isUpdate = true;
                            return true;
                        }
                    }

                    if (bannerBean.getType() != bannerBean1.getType()) {
                        isUpdate = true;
                        return true;
                    }
                }
            } else {
                return true;
            }
        }
        return false;
    }


    private void updateSlide() {
        if (mHomeInfo == null){
            return;
        }
        if (slides != null && !isUpdateSlide()){
            return;
        }
        slides = mHomeInfo.getBanner();
        if (slides == null ||  slides.size() < 1){
            return;
        }
//        if (slideAdapter == null) {
            uvp_slide.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
            uvp_slide.setMultiScreen(1f);
            uvp_slide.setItemRatio(1.0f);
            uvp_slide.setAutoMeasureHeight(true);
            uvp_slide.setAutoScroll(AUTO_SCROLL);
            uvp_slide.setPageTransformer(true, new ScalePageTransformer());
//            uvp_slide.setItemMargin(DensityUtil.dp2px(15f),0,DensityUtil.dp2px(15f),0);
//            uvp_slide.initIndicator();
//            uvp_slide.getIndicator()
//                    .setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM)
//                    .setMargin(0, 0, 0, MathUtils.dip2px(mContext, 5))
//                    .setRadius(MathUtils.dip2px(mContext, 1))
//                    .setOrientation(UltraViewPager.Orientation.HORIZONTAL)
//                    .setFocusResId(R.mipmap.home_slide_sel)
//                    .setNormalResId(R.mipmap.home_slide_def)
//                    .setIndicatorPadding(MathUtils.dip2px(mContext, 5))
//                    .build();
//        }
//        slideAdapter = new SlideAdapter(slides);
        uvp_slide.setAdapter(new SlideAdapter(slides));

    }

    private void updateAssetShow() {
        if (!isLogin()) {
            return;
        }
        if (isShow) {
            iv_show.setImageResource(R.mipmap.pw_show);
            if (mHomeInfo == null) {
                mHomeInfo = new HomeInfo();
            }
            tvFragmentHomeTotalAsset.setText(mHomeInfo.getAccountBalanceCny());
            tvFragmentHomeTotalAsset1.setText(mHomeInfo.getAccountBalanceCny());
        } else {
            iv_show.setImageResource(R.mipmap.pw_unshow);
            tvFragmentHomeTotalAsset.setText("****");
            tvFragmentHomeTotalAsset1.setText("****");
        }
    }



    @OnClick({R.id.ll_home_top_1, R.id.ll_home_top_2, R.id.ll_home_top_3, R.id.tv_fragment_home_unlogin,
            R.id.iv_home_top_show, R.id.ll_fragment_home_c2c_asset, R.id.ll_fragment_home_trust,
            R.id.ll_fragment_home_withdraw, R.id.ll_fragment_home_recharge, R.id.ll_fragment_home_groups,
            R.id.mtv_fragment_home_notice, R.id.ll_fragment_home_up, R.id.ll_fragment_home_down})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.ll_home_top_1:
                intent = new Intent(mContext, WebDetailActivity.class);
                intent.putExtra("title", "买币指南");
                intent.putExtra("url", mHomeInfo.getBuyCoinGuide());
                break;
            case R.id.ll_home_top_2:
                intent = new Intent(mContext, WebDetailActivity.class);
                intent.putExtra("title", "账户指南");
                intent.putExtra("url", mHomeInfo.getAccountGuide());
                break;
            case R.id.ll_fragment_home_groups:
            case R.id.ll_home_top_3:
                //  跳转加入社群
                intent = new Intent(mContext, PersonalGroupsActivity.class);
                break;
            case R.id.tv_fragment_home_unlogin:
                if (LoginUtils.isLogin(getActivity())) {
                }
                break;
            case R.id.iv_home_top_show:
                isShow = !isShow;
                updateAssetShow();
                break;
            case R.id.ll_fragment_home_c2c_asset:
                if (LoginUtils.isLogin(getActivity())) {
                    intent = new Intent(mContext, MyAccountActivity.class);

                }
                break;
            case R.id.ll_fragment_home_trust:
                if (LoginUtils.isLogin(getActivity())) {
                    intent = new Intent(mContext, MyWalletTransferActivity.class);

                }
                break;
            case R.id.ll_fragment_home_withdraw:
                if (LoginUtils.isLogin(getActivity())) {
                    intent = new Intent(mContext, MyWalletRechargeActivity.class);

                }
                break;
            case R.id.ll_fragment_home_recharge:
                if (LoginUtils.isLogin(getActivity())) {
                    intent = new Intent(mContext, MyWalletWithdrawActivity.class);

                }
                break;

            case R.id.mtv_fragment_home_notice:
                // 2018/7/11 跳转到页面
                if (mHomeInfo != null && !TextUtils.isEmpty(mHomeInfo.getNoticeUrl())) {
                    intent = new Intent(mContext, WebDetailActivity.class);
                    intent.putExtra("title", mHomeInfo.getNoticeTitle());
                    intent.putExtra("url", mHomeInfo.getNoticeUrl());
                }
                break;
            case R.id.ll_fragment_home_up:
                if (LoginUtils.isLogin(getActivity()) && Preferences.getUserMarket() <1){
                    submitChangeMarket(1);
                }
                break;
            case R.id.ll_fragment_home_down:
                if (LoginUtils.isLogin(getActivity())&& Preferences.getUserMarket() <1){
                    submitChangeMarket(0);
                }
                break;
        }

        if (intent != null) {
            startActivity(intent);
        }
    }


    private void getHomeInfo() {
//        showLoading(null);
        Map<String, Object> map = new HashMap<>();
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.getHome(Preferences.getAccessToken(), map, new getBeanCallback<HomeInfo>() {
            @Override
            public void onSuccess(HomeInfo homeInfo) {
                mHomeInfo = homeInfo;
                initMarket();
                updateLogin();
                updateNoice();
                updateSlide();
                updateAssetShow();
            }

            @Override
            public void onError(int errorCode, String reason) {

                onFail(errorCode, reason);
                mHomeInfo = null;
                initMarket();
                updateLogin();
                updateNoice();
                updateSlide();
                updateAssetShow();
            }
        });
    }

    private void submitChangeMarket(final int state){
        showLoading(null);
        Map<String, Object> map = new HashMap<>();
        map.put("moodState", state);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.submitChangeMarket(Preferences.getAccessToken(), map, new getBeanCallback<HomeInfo.MoodBean>() {
            @Override
            public void onSuccess(HomeInfo.MoodBean o) {
                hideLoading();
                showToast("提交成功");
                Preferences.setUserMarket(state);
                if (state == 1){
                    ivMarkerUp.setImageResource(R.mipmap.home_bottom_up_sel);
                }else  if (state == 0){
                    ivMarkerDown.setImageResource(R.mipmap.home_bottom_down_sel);
                }
                if (o != null){
                    tvFragmentHomeUp.setText(o.getMoodTop()+"%");
                    tvFragmentHomeDown.setText(o.getMoodBottom()+"%");
                    float top = Float.parseFloat(o.getMoodTop());
                    float down = Float.parseFloat(o.getMoodBottom());
                    viewFragmentHomeUp.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, top/100));
                    viewFragmentHomeDown.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, down/100));
                }
            }

            @Override
            public void onError(int errorCode, String reason) {
                hideLoading();
                onFail(errorCode,reason);
                showToast("提交失败");
            }
        });
    }



    /**
     * 添加所有fragment的标题
     */
    private List<String> getFragmentsNames() {
        List<String> names = new ArrayList<>();
        names.add("PGY指数");
        names.add("24H涨幅榜");
        return names;
    }

    /**
     * 登录状态监听
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventLoginState loginState) {
        LogUtils.e(TAG, "首页是否登录==" + loginState.isLoged());
        getHomeInfo();
    }

//    /**
//     * 用户信息改变监听
//     */
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void UserInfoEvent(EventUserInfoChange eventUserInfoChange) {
//        updateLogin();
//    }

    @Override
    public void onDestroyView() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroyView();
    }

    @Override
    public void onMarketPgyCallback(List<HomeMarketBean> marketList) {
        if (marketList != null && marketList.size() > 0){
            if (marketAdapter == null){
                rvMarket.setLayoutManager(new LinearLayoutManager(mContext){
                    @Override
                    public boolean canScrollVertically() {
                        //解决ScrollView里存在多个RecyclerView时滑动卡顿的问题
                        //如果你的RecyclerView是水平滑动的话可以重写canScrollHorizontally方法
                        return false;
                    }
                });
                //解决数据加载不完的问题
                rvMarket.setNestedScrollingEnabled(false);
                rvMarket.setHasFixedSize(true);
//解决数据加载完成后, 没有停留在顶部的问题
                rvMarket.setFocusable(false);
                marketAdapter = new HomeMarketAdapter(mContext);

                rvMarket.setAdapter(marketAdapter);
                marketAdapter.addAll(marketList);
            }else {
                marketAdapter.clear();
                marketAdapter.addAll(marketList);
            }
        }
    }

    @Override
    public void onMarket24HCallback(List<HomeMarketBean> marketList) {
        if (marketList != null && marketList.size() > 0){
            if (market24HAdapter == null){
                rvMarket24H.setLayoutManager(new LinearLayoutManager(mContext){
                    @Override
                    public boolean canScrollVertically() {
                        //解决ScrollView里存在多个RecyclerView时滑动卡顿的问题
                        //如果你的RecyclerView是水平滑动的话可以重写canScrollHorizontally方法
                        return false;
                    }
                });
//解决数据加载不完的问题
                rvMarket24H.setNestedScrollingEnabled(false);
                rvMarket24H.setHasFixedSize(true);
//解决数据加载完成后, 没有停留在顶部的问题
                rvMarket24H.setFocusable(false);
                market24HAdapter = new HomeMarketAdapter(mContext);
                rvMarket24H.setAdapter(market24HAdapter);
                market24HAdapter.addAll(marketList);
            }else {
                market24HAdapter.clear();
                market24HAdapter.addAll(marketList);
            }

        }
    }

    public class SlideAdapter extends PagerAdapter {
        List<BannerInfo> list;
        int[] imgs = {R.mipmap.aslide_test, R.mipmap.aslide_test1, R.mipmap.aslide_test2, R.mipmap.aslide_test3};

        public SlideAdapter(List<BannerInfo> data) {
            super();
            list = data;
        }

        @Override
        public int getCount() {
            return list == null ? 4 : list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageView riv = new ImageView(mContext);
            riv.setScaleType(ImageView.ScaleType.FIT_XY);
            if (list == null) {
                riv.setImageResource(imgs[position % 4]);
            } else {
                final BannerInfo bannerBean = list.get(position);
                //  2018/7/20 显示banner
                ImageLoaderUtils.displayWithCache(mContext, riv, bannerBean.getImgpath());
                riv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //2018/7/20 跳转 banner
//                        BannerIntentUtils.bannerToActivity(getContext(), bannerBean);
//                        if (LoginUtils.isLogin(mContext)){
                            Intent intent = new Intent(mContext, BannerListActivity.class);
                            startActivity(intent);
//                        }
                    }
                });

            }
            container.addView(riv);
            return riv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (object instanceof ImageView) {
                ImageView iv = (ImageView) object;
                container.removeView(iv);
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        /*切换底部行情页面的时候*/
        LogUtils.w("home", "home----onHiddenChanged:" + hidden);
        if (!hidden) {
            switchScene(new PushData(scene));
        }
    }

}
