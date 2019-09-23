package app.com.pgy.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tmall.ultraviewpager.UltraViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import butterknife.OnClick;
import app.com.pgy.Activitys.Base.BaseActivity;
import app.com.pgy.Adapters.MyWalletListAdapter;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Constants.StaticDatas;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Interfaces.getPositionCallback;
import app.com.pgy.Models.Beans.EventBean.EventAssetsChange;
import app.com.pgy.Models.Beans.MyWallet;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Utils.TimeUtils;
import app.com.pgy.Widgets.banner.ScalePageTransformer;

import static app.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * Created by YX on 2018/7/11.
 */

public class MyWalletActivity extends BaseActivity implements getPositionCallback {
    @BindView(R.id.iv_activity_my_wallet_back)
    ImageView iv_back;
    @BindView(R.id.tv_activity_my_wallet_title)
    TextView tv_title;
    @BindView(R.id.uvp_activity_my_wallet_type)
    UltraViewPager uvp_type;
    @BindView(R.id.rv_activity_my_wallet_list)
    RecyclerView rv_list;
    @BindView(R.id.ll_error_content)
    LinearLayout ll_error;
    @BindView(R.id.tv_error_content)
    TextView tv_error;
    @BindView(R.id.btn_error_reload)
    Button btn_reload;

    private int mIndex = 0;
    private MyWalletListAdapter mAdapter;
    private WalletTypeAdapter typeAdapter;
    private List<String> accountCny;
    private MyWallet myWallet;

    private Map<Integer,TextView> viewMap;

    private int currentAccount = StaticDatas.ACCOUNT_GOODS;

    @Override
    public int getContentViewId() {
        return R.layout.activity_my_wallet;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewpage();
    }

    @Override
    protected void initData(){
        mIndex = getIntent().getIntExtra("index",0);
        if (mAdapter == null) {
            mAdapter = new MyWalletListAdapter(mContext);
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        /*添加Layout*/
        rv_list.setLayoutManager(layoutManager);
        /*添加加载进度条*/
        rv_list.setAdapter(mAdapter);
        mAdapter.setCallback(this);
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    private void initViewpage(){
        if (accountCny == null){
            accountCny = new ArrayList<>();
            for (int i = 0;i < 3;i++){
                accountCny.add("0.00");
            }
        }
        uvp_type.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
        uvp_type.setMultiScreen(0.9f);
        uvp_type.setItemRatio(1.0f);
        uvp_type.setAutoMeasureHeight(false);
        uvp_type.setInfiniteLoop(false);
        uvp_type.setPageTransformer(true,new ScalePageTransformer());
//        uvp_type.initIndicator();
//        uvp_type.getIndicator()
//                .setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM)
//                .setMargin(0, 0,0 , MathUtils.dip2px(mContext,3))
//                .setRadius(MathUtils.dip2px(mContext,1))
//                .setOrientation(UltraViewPager.Orientation.HORIZONTAL)
//                .setFocusResId(R.mipmap.asset_indicator_sel)
//                .setNormalResId(R.mipmap.asset_indicator_def)
//                .setIndicatorPadding(MathUtils.dip2px(mContext,5))
//                .build();
        typeAdapter = new WalletTypeAdapter(accountCny);
        uvp_type.setAdapter(typeAdapter);
        uvp_type.setOnPageChangeListener(new MyPageChangeListen());
        if (mIndex == 0){
            tv_title.setText("币币账户");
            getMyWalletFromNet();
        }else {
            uvp_type.setCurrentItem(mIndex);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void AssetsChangeEvent(EventAssetsChange event){
        if (event != null){
            getMyWalletFromNet();
        }
    }


    @Override
    protected void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    @OnClick({R.id.iv_activity_my_wallet_back,R.id.btn_error_reload})
    public void OnViewClick(View view){
        switch (view.getId()){
            case R.id.iv_activity_my_wallet_back:
                finish();
                break;
            case R.id.btn_error_reload:
                // 重新加载
                getMyWalletFromNet();
                break;
        }
    }

    private void updateAccount(MyWallet wallets){
        if (currentAccount != wallets.getAccountType()){
            return;
        }
        mAdapter.clear();
        myWallet = wallets;
        int position = 0;
        switch (wallets.getAccountType()){
            case StaticDatas.ACCOUNT_GOODS:
                position = 0;
                break;
//            case StaticDatas.ACCOUNT_LEVER:
//                position = 1;
//                break;
            case StaticDatas.ACCOUNT_C2C:
                position = 1;
                break;
            case StaticDatas.ACCOUNT_YUBIBAO:
                position = 2;
                break;
        }


//        if (position == 1){//杠杆暂未开发，直接跳过
//            ll_error.setVisibility(View.VISIBLE);
//            btn_reload.setVisibility(View.GONE);
//            tv_error.setText("正在开发中");
//            return;
//        }
        TextView tv_total = viewMap.get(position);
        if (tv_total != null){//修改账户总资产
            if (myWallet != null){
                if (TextUtils.isEmpty(myWallet.getTotalSumOfCny())){
                    tv_total.setText("0.00");

                }else {
                    tv_total.setText(""+ myWallet.getTotalSumOfCny());
                }
            }else {
                tv_total.setText("0.00");
            }

        }
        //刷新列表
        if (myWallet != null && myWallet.getList() != null && myWallet.getList().size() > 0){
            ll_error.setVisibility(View.GONE);
            mAdapter.addAll(myWallet.getList());
        }else if (myWallet != null){
            ll_error.setVisibility(View.VISIBLE);
            btn_reload.setVisibility(View.GONE);
            tv_error.setText("账户还没有币种");
        }else {
            ll_error.setVisibility(View.VISIBLE);
            btn_reload.setVisibility(View.VISIBLE);
            tv_error.setText("加载失败");
        }
    }

    private void updateList(int position){
        switch (position){
            case 0:
                tv_title.setText("币币账户");

                currentAccount = StaticDatas.ACCOUNT_GOODS;
                break;
//            case 1:
//                currentAccount = StaticDatas.ACCOUNT_LEVER;
//                break;
            case 1:
                tv_title.setText("法币账户");
                currentAccount = StaticDatas.ACCOUNT_C2C;

                break;
            case 2:
                tv_title.setText("节点账户");
                currentAccount = StaticDatas.ACCOUNT_YUBIBAO;
                break;
        }

        getMyWalletFromNet();
    }

    @Override
    public void getPosition(int pos) {
        MyWallet.ListBean info = mAdapter.getItem(pos);
        if (currentAccount == StaticDatas.ACCOUNT_GOODS ||
                currentAccount == StaticDatas.ACCOUNT_C2C){
            Intent intent = new Intent(mContext,MyWalletCoinInfoActivity.class);
            intent.putExtra("accountType",currentAccount);
            intent.putExtra("coinType",info.getCoinType());
            startActivity(intent);
        }else if (currentAccount == StaticDatas.ACCOUNT_YUBIBAO){
            Intent intent = new Intent(mContext,MyWalletYbbCoinInfoActivity.class);
            intent.putExtra("coinType",info.getCoinType());
            startActivity(intent);
        }else {

        }

    }

    /**
     * 从服务器获取我的钱包
     */
    private void getMyWalletFromNet() {
        showLoading(null);
        Map<String, Object> map = new HashMap<>();
        map.put("accountType", currentAccount);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.getMyWalletList(Preferences.getAccessToken(), map, new getBeanCallback<MyWallet>() {
            @Override
            public void onSuccess(MyWallet myWalletFromNet) {
                if (myWalletFromNet == null) {
                    myWalletFromNet = new MyWallet();
                }
                updateAccount(myWalletFromNet);
                hideLoading();
            }

            @Override
            public void onError(int errorCode, String reason) {
                updateAccount(new MyWallet());
                onFail(errorCode, reason);
                /*网络错误*/
                hideLoading();

            }
        });
    }
    public class WalletTypeAdapter extends PagerAdapter {
        List<String> list;
        String[] titles = {"币币账户","法币账户","节点账户"};//"杠杆账户",
        int[] bg = {R.drawable.shape_home_goods_asset_bg,
                R.drawable.shape_home_c2c_asset_bg,R.drawable.shape_home_yubibao_bg};//R.drawable.shape_home_lever_asset_bg,
        public WalletTypeAdapter( List<String> data) {
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
        public Object instantiateItem(ViewGroup container,int position) {
            View view = View.inflate(mContext,R.layout.item_my_wallet_type_view,null);
            LinearLayout ll_bg = view.findViewById(R.id.ll_item_my_wallet_type_bg);
            TextView tv_title = view.findViewById(R.id.tv_item_my_wallet_type_name);
            TextView tv_total = view.findViewById(R.id.tv_item_my_wallet_type_total);
            ll_bg.setBackgroundResource(bg[position%3]);
            tv_title.setText(""+titles[position%3]);
            tv_total.setText(""+list.get(position));
            if (viewMap == null){
                viewMap = new HashMap<>();
            }
            if (!viewMap.containsKey(position)){
                viewMap.put(position,tv_total);
            }
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (object instanceof RoundedImageView){
                RoundedImageView iv = (RoundedImageView) object;
                container.removeView(iv);
            }
        }
    }

    private class MyPageChangeListen implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            updateList(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
