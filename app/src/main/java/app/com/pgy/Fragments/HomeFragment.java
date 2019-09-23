package app.com.pgy.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tmall.ultraviewpager.UltraViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import app.com.pgy.Activitys.Base.WebDetailActivity;
import app.com.pgy.Activitys.C2CTradeActivity;
import app.com.pgy.Activitys.MyWalletActivity;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Fragments.Base.BaseFragment;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Models.Beans.BannerInfo;
import app.com.pgy.Models.Beans.EventBean.EventLoginState;
import app.com.pgy.Models.Beans.EventBean.EventUserInfoChange;
import app.com.pgy.Models.Beans.HomeInfo;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Utils.BannerIntentUtils;
import app.com.pgy.Utils.ImageLoaderUtils;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Utils.LoginUtils;
import app.com.pgy.Utils.MathUtils;
import app.com.pgy.Utils.TimeUtils;
import app.com.pgy.Utils.Utils;
import app.com.pgy.Widgets.MarqueeTextView;
import app.com.pgy.Widgets.RatioImageView;
import app.com.pgy.Widgets.banner.ScalePageTransformer;
import app.com.pgy.im.UI.IMMainActivity;
import io.rong.imkit.RongIM;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.model.Conversation;

import static app.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * Created by YX on 2018/7/11.
 */

public class HomeFragment extends BaseFragment implements IUnReadMessageObserver {
    private final static int AUTO_SCROLL = 4 * 1000;
    private static HomeFragment instance;
    @BindView(R.id.riv_fragment_home_headerImg)
    RoundedImageView riv_headerImg;
    @BindView(R.id.tv_fragment_home_unlogin)
    TextView tv_unlogin;
    @BindView(R.id.tv_fragment_home_nickname)
    TextView tv_nickName;
    @BindView(R.id.uvp_fragment_home_slide)
    UltraViewPager uvp_slide;
    @BindView(R.id.mtv_fragment_home_notice)
    MarqueeTextView mtv_notice;
    @BindView(R.id.tv_fragment_home_total_asset)
    TextView tv_total;
    @BindView(R.id.iv_fragment_home_asset_show)
    ImageView iv_show;
    @BindView(R.id.tv_fragment_home_goods_amount)
    TextView tv_goods;
    @BindView(R.id.tv_fragment_home_lever_amount)
    TextView tv_lever;
    @BindView(R.id.tv_fragment_home_c2c_amount)
    TextView tv_c2c;
    @BindView(R.id.tv_fragment_home_yubibao_amount)
    TextView tv_yubibao;
    @BindView(R.id.riv_fragment_home_toC2C)
    RatioImageView riv_toC2C;
    @BindView(R.id.view_titleMessage_point)
    View viewTitleMessagePoint;
    @BindView(R.id.view_titleMessage_frame)
    FrameLayout viewTitleMessageFrame;
    @BindView(R.id.srl_fragment_home_refresh)
    SmartRefreshLayout srlRefresh;
//    @BindView(R.id.banner_view)
//    YBannerView bannerView;
    Unbinder unbinder;

    private List<BannerInfo> slides;
    private boolean isShow = true;
    private SlideAdapter slideAdapter;

    private HomeInfo.NoticeBean noticeBean;
    private HomeInfo mHomeInfo;

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        if (instance == null) {
            instance = new HomeFragment();
        }
        return instance;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_home;
    }


    @Override
    protected void initData() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        RongIM.getInstance().addUnReadMessageCountChangedObserver(this, conversationTypes);
        srlRefresh.setEnableLoadMore(false);
        srlRefresh.setRefreshHeader(new ClassicsHeader(getContext()));
        srlRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getHomeInfo();
            }
        });
        srlRefresh.autoRefresh();
    }

    final Conversation.ConversationType[] conversationTypes = {
            Conversation.ConversationType.PRIVATE,
            Conversation.ConversationType.GROUP, Conversation.ConversationType.SYSTEM,
            Conversation.ConversationType.PUBLIC_SERVICE, Conversation.ConversationType.APP_PUBLIC_SERVICE
    };

    @Override
    public void onCountChanged(int i) {
        if (i > 0 || Preferences.isHasNewFriend()) {
            viewTitleMessagePoint.setVisibility(View.VISIBLE);
        } else {
            viewTitleMessagePoint.setVisibility(View.GONE);
        }
    }

    private void updateLogin() {
        if (isLogin()) {
            ImageLoaderUtils.displayCircle(mContext, riv_headerImg, Preferences.getLocalUser().getHeadImg());
            tv_nickName.setText(Preferences.getLocalUser().getName());
            tv_unlogin.setVisibility(View.GONE);
            tv_nickName.setVisibility(View.VISIBLE);
//            viewTitleMessageFrame.setVisibility(View.VISIBLE);
            RongIM.getInstance().addUnReadMessageCountChangedObserver(this, conversationTypes);
        } else {
            riv_headerImg.setImageResource(R.mipmap.icon_unlogin);
            tv_unlogin.setVisibility(View.VISIBLE);
            tv_nickName.setVisibility(View.GONE);
//            viewTitleMessageFrame.setVisibility(View.GONE);
        }
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
                            return isUpdate;
                        }
                    } else {
                        if (!(TextUtils.isEmpty(bannerBean.getAddress()) && TextUtils.isEmpty(bannerBean1.getAddress()))) {
                            isUpdate = true;
                            return isUpdate;
                        }
                    }

                    if (!TextUtils.isEmpty(bannerBean.getImgpath()) && !TextUtils.isEmpty(bannerBean1.getImgpath())) {
                        if (!bannerBean.getImgpath().equals(bannerBean1.getImgpath())) {
                            isUpdate = true;
                            return isUpdate;
                        }
                    } else {
                        if (!(TextUtils.isEmpty(bannerBean.getImgpath()) && TextUtils.isEmpty(bannerBean1.getImgpath()))) {
                            isUpdate = true;
                            return isUpdate;
                        }
                    }
                    if (!TextUtils.isEmpty(bannerBean.getTitle()) && !TextUtils.isEmpty(bannerBean1.getTitle())) {
                        if (!bannerBean.getTitle().equals(bannerBean1.getTitle())) {
                            isUpdate = true;
                            return isUpdate;
                        }
                    } else {
                        if (!(TextUtils.isEmpty(bannerBean.getTitle()) && TextUtils.isEmpty(bannerBean1.getTitle()))) {
                            isUpdate = true;
                            return isUpdate;
                        }
                    }

                    if (bannerBean.getType() != bannerBean1.getType()) {
                        isUpdate = true;
                        return isUpdate;
                    }
                }
            } else {
                return true;
            }
        }
        return isUpdate;
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
        if (slideAdapter == null) {
            uvp_slide.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
            uvp_slide.setMultiScreen(0.9f);
            uvp_slide.setItemRatio(1.0f);
            uvp_slide.setAutoMeasureHeight(true);
            uvp_slide.setAutoScroll(AUTO_SCROLL);
            uvp_slide.setPageTransformer(true, new ScalePageTransformer());
//            uvp_slide.setItemMargin(DensityUtil.dp2px(15f),0,DensityUtil.dp2px(15f),0);
            uvp_slide.initIndicator();
            uvp_slide.getIndicator()
                    .setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM)
                    .setMargin(0, 0, 0, MathUtils.dip2px(mContext, 5))
                    .setRadius(MathUtils.dip2px(mContext, 1))
                    .setOrientation(UltraViewPager.Orientation.HORIZONTAL)
                    .setFocusResId(R.mipmap.home_slide_sel)
                    .setNormalResId(R.mipmap.home_slide_def)
                    .setIndicatorPadding(MathUtils.dip2px(mContext, 5))
                    .build();
        }
        slideAdapter = new SlideAdapter(slides);
        uvp_slide.setAdapter(new SlideAdapter(slides));

    }

    private void updateNoice() {
        if (mHomeInfo == null || mHomeInfo.getNotice() == null) {
            return;
        }
        if (noticeBean == null || mHomeInfo.getNotice().getId() != noticeBean.getId() || !(mHomeInfo.getNotice().getTitle() + "").equals(noticeBean.getTitle() + "")) {
            noticeBean = mHomeInfo.getNotice();
            if (noticeBean == null) {
                noticeBean = new HomeInfo.NoticeBean();
            }
            mtv_notice.setText(noticeBean.getTitle() + "");
            mtv_notice.startScoll();
        }
    }

    private void updateAssetShow() {
        if (!isLogin()) {
            iv_show.setImageResource(R.mipmap.pw_show);
            tv_total.setText("0.00");
            tv_goods.setText("0.00");
            tv_lever.setText("0.00");
            tv_c2c.setText("0.00");
            tv_yubibao.setText("0.00");
            return;
        }
        if (isShow) {
            iv_show.setImageResource(R.mipmap.pw_show);
            if (mHomeInfo == null) {
                mHomeInfo = new HomeInfo();
            }
            tv_total.setText(TextUtils.isEmpty(mHomeInfo.getTotal()) ? "0.00" : mHomeInfo.getTotal());
            tv_goods.setText(TextUtils.isEmpty(mHomeInfo.getSoptAccount()) ? "0.00" : mHomeInfo.getSoptAccount());
            tv_lever.setText(TextUtils.isEmpty(mHomeInfo.getLeverAccount()) ? "0.00" : mHomeInfo.getLeverAccount());
            tv_c2c.setText(TextUtils.isEmpty(mHomeInfo.getC2cAccount()) ? "0.00" : mHomeInfo.getC2cAccount());
            tv_yubibao.setText(TextUtils.isEmpty(mHomeInfo.getYubiAccount()) ? "0.00" : mHomeInfo.getYubiAccount());
        } else {
            iv_show.setImageResource(R.mipmap.pw_unshow);
            tv_total.setText("****");
            tv_goods.setText("****");
            tv_lever.setText("****");
            tv_c2c.setText("****");
            tv_yubibao.setText("****");
        }
    }

    @OnClick({R.id.riv_fragment_home_headerImg, R.id.tv_fragment_home_unlogin, R.id.tv_fragment_home_nickname, R.id.view_titleMessage_frame,
            R.id.iv_fragment_home_asset_show, R.id.mtv_fragment_home_notice, R.id.riv_fragment_home_toC2C, R.id.ll_fragment_home_goods_asset,
            R.id.ll_fragment_home_lever_asset, R.id.ll_fragment_home_c2c_asset, R.id.ll_fragment_home_yubibao_asset})
    public void onViewClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.riv_fragment_home_headerImg:
                if (LoginUtils.isLogin(getActivity())) {
//                    intent = new Intent(mContext,CalanderActivity.class);

                }
                break;
            case R.id.tv_fragment_home_unlogin:
                LoginUtils.isLogin(getActivity());
                break;
            case R.id.tv_fragment_home_nickname:
                if (LoginUtils.isLogin(getActivity())) {
                }
                break;
            case R.id.iv_fragment_home_asset_show:
                if (isLogin()) {
                    isShow = !isShow;
                    updateAssetShow();
                }
                break;
            case R.id.view_titleMessage_frame:
                //  跳转消息
                Utils.IntentUtils(mContext, IMMainActivity.class);
                break;
            case R.id.mtv_fragment_home_notice:
                // 2018/7/11 跳转到页面
                if (mHomeInfo != null && noticeBean != null && !TextUtils.isEmpty(mHomeInfo.getNoticeUrl())) {
                    intent = new Intent(mContext, WebDetailActivity.class);
                    intent.putExtra("title", "");
                    intent.putExtra("url", mHomeInfo.getNoticeUrl());
                }
                break;
            case R.id.riv_fragment_home_toC2C:
                //  跳转到法币交易
                Utils.IntentUtils(mContext, C2CTradeActivity.class);
                break;
            case R.id.ll_fragment_home_goods_asset:
                // 跳转到资产 币币账户
                if (LoginUtils.isLogin(getActivity())) {
                    intent = new Intent(mContext, MyWalletActivity.class);
                    intent.putExtra("index", 0);
                }
                break;
            case R.id.ll_fragment_home_lever_asset:
                //  跳转到资产 杠杆账户
                if (LoginUtils.isLogin(getActivity())) {
                    intent = new Intent(mContext, MyWalletActivity.class);
                    intent.putExtra("index", 1);
                }
                break;
            case R.id.ll_fragment_home_c2c_asset:
                //  跳转到资产 法币账户
                if (LoginUtils.isLogin(getActivity())) {
                    intent = new Intent(mContext, MyWalletActivity.class);
                    intent.putExtra("index", 1);
                }
                break;
            case R.id.ll_fragment_home_yubibao_asset:
                // 2018/7/11 跳转到资产 余币宝账户
                if (LoginUtils.isLogin(getActivity())) {
                    intent = new Intent(mContext, MyWalletActivity.class);
                    intent.putExtra("index", 2);
                }
                break;
            default:
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
                srlRefresh.finishRefresh(true);
//                hideLoading();
                mHomeInfo = homeInfo;
                updateLogin();
                updateSlide();
                updateNoice();
                updateAssetShow();
            }

            @Override
            public void onError(int errorCode, String reason) {
                srlRefresh.finishRefresh(false);
//                hideLoading();
                onFail(errorCode, reason);
                mHomeInfo = null;
                updateLogin();
                updateSlide();
                updateNoice();
                updateAssetShow();
            }
        });
    }

    /**
     * 登录状态监听
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventLoginState loginState) {
        LogUtils.e(TAG, "首页是否登录==" + loginState.isLoged());
        getHomeInfo();
    }

    /**
     * 用户信息改变监听
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void UserInfoEvent(EventUserInfoChange eventUserInfoChange) {
        updateLogin();
    }

    /**
     * 账户金额变动
     */
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void AssetsChangeEvent(EventAssetsChange event) {
//        if (event != null && event.getAccountType() >= 0 && event.getAccountType() != 2){
//            getHomeInfo();
//        }
//    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        unbinder.unbind();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
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
                        BannerIntentUtils.bannerToActivity(getContext(), bannerBean);
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
            switchScene(null);
        }
    }
}
