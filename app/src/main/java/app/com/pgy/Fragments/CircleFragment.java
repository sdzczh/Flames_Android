package app.com.pgy.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tmall.ultraviewpager.UltraViewPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import app.com.pgy.Activitys.BlockActivity;
import app.com.pgy.Activitys.BlockTradeActviity;
import app.com.pgy.Activitys.ZhuanPanViewActivity;
import app.com.pgy.Activitys.YubibaoActivity;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Fragments.Base.BaseFragment;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Models.Beans.BannerInfo;
import app.com.pgy.Models.Beans.CircleBanner;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Utils.BannerIntentUtils;
import app.com.pgy.Utils.ImageLoaderUtils;
import app.com.pgy.Utils.LoginUtils;
import app.com.pgy.Utils.MathUtils;
import app.com.pgy.Utils.TimeUtils;
import app.com.pgy.Widgets.RatioImageView;
import app.com.pgy.im.widget.DemoGridView;

import static app.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * 创建日期：2018/04/18 0022 on 上午 11:23
 * 描述:生态界面
 *
 * @author 徐庆重
 */
public class CircleFragment extends BaseFragment {
    private final static int AUTO_SCROLL = 4 * 1000;
    private static CircleFragment instance;
    @BindView(R.id.srl_fragment_circle_refresh)
    SmartRefreshLayout srlRefresh;
    @BindView(R.id.rg_fragment_circle_block)
    RadioGroup rgFragmentCircleBlock;
    @BindView(R.id.riv_fragment_circle_img)
    RatioImageView rivFragmentCircleImg;
    @BindView(R.id.uvp_fragment_circle_slide)
    UltraViewPager uvpFragmentCircleSlide;
    @BindView(R.id.dgv_fragment_circle_apps)
    DemoGridView dgvFragmentCircleApps;

    private CircleBanner circleBanner;
    private SlideAdapter slideAdapter;
    private int[] appIcons = {R.mipmap.jiedianwakuang,R.mipmap.jiaoyiwakuang};
    private String[] appTitle = {"节点挖矿","交易挖矿"};

    private List<BannerInfo> slides;
    public CircleFragment() {
    }

    public static Fragment getInstance() {
        if (instance == null){
            instance = new CircleFragment();
        }
        return instance;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_circle;
    }


    @Override
    protected void initData() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        dgvFragmentCircleApps.setAdapter(new AppsGridAdapter(mContext,getAppList()));
        dgvFragmentCircleApps.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent= null;
                switch (i){
                    case 0:
                        if (LoginUtils.isLogin(getActivity())){
                            intent = new Intent(mContext, YubibaoActivity.class);
                        }

                        break;
                    case 1:
//                        showToast("暂未开启 敬请期待");
                        if (LoginUtils.isLogin(getActivity())){
                            intent = new Intent(mContext, BlockTradeActviity.class);
                        }

                        break;
                    case 2:
                        intent = new Intent(mContext,ZhuanPanViewActivity.class);
                        break;
                    case 3:
                        showToast("正在开发中...");
                        break;
                    case 4:
                        showToast("正在开发中...");
                        break;
                    default:
                        showToast("正在开发中...");
                        break;
                }

                if (intent != null){
                    startActivity(intent);
                }
            }
        });
        rgFragmentCircleBlock.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rb_fragment_circle_block:
                        break;
                    case R.id.rb_fragment_circle_newblock:
                        showToast("功能开发中");
                        rgFragmentCircleBlock.check(R.id.rb_fragment_circle_block);
                        break;
                }
            }
        });
        rivFragmentCircleImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rgFragmentCircleBlock.getCheckedRadioButtonId() == R.id.rb_fragment_circle_block){
                    Intent intent = new Intent(mContext, BlockActivity.class);
                    startActivity(intent);
                }else {

                }
            }
        });
        switchScene(null);
        srlRefresh.setEnableLoadMore(false);
        srlRefresh.setRefreshHeader(new ClassicsHeader(getContext()));
        srlRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getBanner();
            }
        });
        srlRefresh.autoRefresh();

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            switchScene(null);
        }
    }

    private void getBanner(){
        Map<String,Object> map = new HashMap<>();
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        map.put("bannerType", 1);
        NetWorks.getCircleBanners(map, new getBeanCallback<CircleBanner>() {
            @Override
            public void onSuccess(CircleBanner banners) {
                srlRefresh.finishRefresh(true);
                circleBanner  = banners;
                updateSlide();
            }

            @Override
            public void onError(int errorCode, String reason) {
                srlRefresh.finishRefresh(false);
                onFail(errorCode,reason);
                circleBanner  = null;
            }
        });
    }


    //判断是否需要重新加载banner
    private boolean isUpdateSlide(){
        boolean isUpdate = false;
        if (circleBanner != null && slides != null &&
                circleBanner.getList() != null){
            if ( circleBanner.getList().size() ==slides.size()){
                for (int i = 0;i < slides.size();i++){
                    BannerInfo bannerBean = slides.get(i);
                    BannerInfo bannerBean1 = circleBanner.getList().get(i);
                    if (!TextUtils.isEmpty(bannerBean.getAddress()) && !TextUtils.isEmpty(bannerBean1.getAddress())){
                        if (!bannerBean.getAddress().equals(bannerBean1.getAddress())){
                            isUpdate = true;
                            return isUpdate;
                        }
                    }else {
                        if (!(TextUtils.isEmpty(bannerBean.getAddress()) && TextUtils.isEmpty(bannerBean1.getAddress()))){
                            isUpdate = true;
                            return isUpdate;
                        }
                    }

                    if (!TextUtils.isEmpty(bannerBean.getImgpath()) && !TextUtils.isEmpty(bannerBean1.getImgpath())){
                        if (!bannerBean.getImgpath().equals(bannerBean1.getImgpath())){
                            isUpdate = true;
                            return isUpdate;
                        }
                    }else {
                        if (!(TextUtils.isEmpty(bannerBean.getImgpath()) && TextUtils.isEmpty(bannerBean1.getImgpath()))){
                            isUpdate = true;
                            return isUpdate;
                        }
                    }
                    if (!TextUtils.isEmpty(bannerBean.getTitle()) && !TextUtils.isEmpty(bannerBean1.getTitle())){
                        if (!bannerBean.getTitle().equals(bannerBean1.getTitle())){
                            isUpdate = true;
                            return isUpdate;
                        }
                    }else {
                        if (!(TextUtils.isEmpty(bannerBean.getTitle()) && TextUtils.isEmpty(bannerBean1.getTitle()))){
                            isUpdate = true;
                            return isUpdate;
                        }
                    }

                    if (bannerBean.getType() != bannerBean1.getType()){
                        isUpdate = true;
                        return isUpdate;
                    }
                }
            }else {
                return true;
            }
        }
        return isUpdate;
    }

    private void updateSlide(){
        if (circleBanner == null){
            return;
        }
        if (slides != null && !isUpdateSlide()){
            return;
        }
        slides = circleBanner.getList();
        if (slides == null ||  slides.size() < 1){
            return;
        }
        if (slideAdapter == null){
            uvpFragmentCircleSlide.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
            uvpFragmentCircleSlide.setMultiScreen(1);
            uvpFragmentCircleSlide.setAutoMeasureHeight(true);
            uvpFragmentCircleSlide.setAutoScroll(AUTO_SCROLL);
            uvpFragmentCircleSlide.initIndicator();
            uvpFragmentCircleSlide.getIndicator()
                    .setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM)
                    .setMargin(0, 0,0 , MathUtils.dip2px(mContext,5))
                    .setRadius(MathUtils.dip2px(mContext,1))
                    .setOrientation(UltraViewPager.Orientation.HORIZONTAL)
                    .setFocusResId(R.mipmap.home_slide_sel)
                    .setNormalResId(R.mipmap.home_slide_def)
                    .setIndicatorPadding(MathUtils.dip2px(mContext,5))
                    .build();
        }
        slideAdapter = new SlideAdapter(slides);
        uvpFragmentCircleSlide.setAdapter(slideAdapter);
    }

    private List<CircleAppInfo> getAppList(){
        List<CircleAppInfo> list = new ArrayList<>();
        for (int i = 0;i < appIcons.length;i++){
            CircleAppInfo info = new CircleAppInfo();
            info.setIconRes(appIcons[i]);
            info.setTitle(appTitle[i]);
            info.setType(i);
            if (i == 3 || i == 4){
                info.setState(1);
            }
            list.add(info);
        }

        return list;
    }

    public class SlideAdapter extends PagerAdapter {
        List<BannerInfo> list;

        public SlideAdapter(List<BannerInfo> data) {
            super();
            list = data;
        }
        @Override
        public int getCount() {
            return list == null?0:list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            final BannerInfo bannerBean = list.get(position);
            ImageView riv = new ImageView(mContext);
            riv.setScaleType(ImageView.ScaleType.FIT_XY);
            ImageLoaderUtils.displayWithCache(mContext,riv,bannerBean.getImgpath());
            riv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BannerIntentUtils.bannerToActivity(getContext(),bannerBean);
                }
            });
            container.addView(riv);
            return riv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (object instanceof ImageView){
                ImageView iv = (ImageView) object;
                container.removeView(iv);
            }
        }
    }
    class CircleAppInfo{
        int iconRes;
        String title;
        int state;
        int type;

        public void setIconRes(int iconRes) {
            this.iconRes = iconRes;
        }

        public int getIconRes() {
            return iconRes;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getState() {
            return state;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
    }

    class AppsGridAdapter extends BaseAdapter {
        private List<CircleAppInfo> list;
        Context context;


        public AppsGridAdapter(Context context, List<CircleAppInfo> list) {
            this.list = list;
            this.context = context;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_circle_apps, parent, false);
            }
            CircleAppInfo info = (CircleAppInfo) getItem(position);
            ImageView iv_icon = convertView.findViewById(R.id.iv_item_circle_apps_icon);
            TextView tv_title = convertView.findViewById(R.id.tv_item_circle_apps_name);
            TextView tv_state = convertView.findViewById(R.id.tv_item_circle_apps_state);
            iv_icon.setImageResource(info.getIconRes());
            tv_title.setText(info.getTitle());
            if (info.getState() == 1){
                tv_state.setText("正在开发中");
            }else {
                tv_state.setText("");
            }
            return convertView;
        }

        @Override
        public int getCount() {
            return list != null ? list.size() : 0;
        }

        @Override
        public Object getItem(int position) {
            return list == null || list.size() <= position?null:list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }



}
