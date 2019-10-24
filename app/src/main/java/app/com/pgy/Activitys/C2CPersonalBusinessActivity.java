package app.com.pgy.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import app.com.pgy.Activitys.Base.BaseActivity;
import app.com.pgy.Adapters.C2CPersonalBusinessAdapter;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Constants.StaticDatas;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Interfaces.getPositionCallback;
import app.com.pgy.Interfaces.getStringCallback;
import app.com.pgy.Models.Beans.C2CPersonalMessage;
import app.com.pgy.Models.Beans.C2cNormalBusiness;
import app.com.pgy.Models.Beans.CoinAvailbalance;
import app.com.pgy.Models.Beans.OrderIdBean;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Utils.ImageLoaderUtils;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Utils.MathUtils;
import app.com.pgy.Utils.TimeUtils;
import app.com.pgy.Widgets.BottomTradeOrderFrame;
import app.com.pgy.Widgets.RoundImageView;
import io.rong.imkit.RongIM;

import static app.com.pgy.Constants.StaticDatas.ACCOUNT_C2C;
import static app.com.pgy.Constants.StaticDatas.NORMAL;
import static app.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/***
 * C2C个人卖家
 * @author xuqingzhong
 */

public class C2CPersonalBusinessActivity extends BaseActivity {
    @BindView(R.id.activity_ccPersonalBusiness_headIcon)
    RoundImageView activityCcPersonalBusinessHeadIcon;
    @BindView(R.id.activity_ccPersonalBusiness_nikeName)
    TextView activityCcPersonalBusinessNikeName;
    @BindView(R.id.activity_ccPersonalBusiness_time)
    TextView activityCcPersonalBusinessTime;
    @BindView(R.id.activity_ccPersonalBusiness_allOrders)
    TextView activityCcPersonalBusinessAllOrders;
    @BindView(R.id.activity_ccPersonalBusiness_c2cOrders)
    TextView activityCcPersonalBusinessC2cOrders;
    @BindView(R.id.activity_ccPersonalBusiness_goodsOrders)
    TextView activityCcPersonalBusinessGoodsOrders;
    @BindView(R.id.activity_ccPersonalBusiness_certification_phone)
    TextView activityCcPersonalBusinessCertificationPhone;
    @BindView(R.id.activity_ccPersonalBusiness_certification_realName)
    TextView activityCcPersonalBusinessCertificationRealName;
    @BindView(R.id.activity_ccPersonalBusiness_buyList)
    RecyclerView buyList;
    @BindView(R.id.activity_ccPersonalBusiness_saleList)
    RecyclerView saleList;
    private C2CPersonalBusinessAdapter buyAdapter;
    private C2CPersonalBusinessAdapter saleAdapter;
    private String phone,nikeName;
    private BottomTradeOrderFrame tradeOrder;
    private String availBalance;

    @Override
    public int getContentViewId() {
        return R.layout.activity_c2c_personalbusiness;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {
        if (buyAdapter == null) {
            buyAdapter = new C2CPersonalBusinessAdapter(mContext,true);
        }
        if (saleAdapter == null) {
            saleAdapter = new C2CPersonalBusinessAdapter(mContext,false);
        }
        /*获取列表页传递过来的用户id*/
        phone = getIntent().getStringExtra("phone");
        getC2cPersonalBusiness(phone);
    }

    private void getC2cPersonalBusiness(String phone) {
        Map<String, Object> map = new HashMap<>();
        map.put("userPhone", phone);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.getC2CPersonalBusiness(Preferences.getAccessToken(), map, new getBeanCallback<C2CPersonalMessage>() {
            @Override
            public void onSuccess(C2CPersonalMessage entrustDetails) {
                if (entrustDetails == null) {
                    entrustDetails = new C2CPersonalMessage();
                }
                LogUtils.w(TAG, entrustDetails.toString());
                initDetailViews(entrustDetails);
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
                /*网络错误*/
            }
        });
    }

    private void initDetailViews(C2CPersonalMessage bean) {
        ImageLoaderUtils.displayCircle(mContext,activityCcPersonalBusinessHeadIcon,bean.getHeadPath());
        nikeName = bean.getNickName();
        activityCcPersonalBusinessNikeName.setText(nikeName);
        activityCcPersonalBusinessTime.setText("注册时间："+bean.getRegisterTime());
        activityCcPersonalBusinessAllOrders.setText(MathUtils.int2String(bean.getTotalNum()));
        activityCcPersonalBusinessGoodsOrders.setText(MathUtils.int2String(bean.getSpotNum()));
        activityCcPersonalBusinessC2cOrders.setText(MathUtils.int2String(bean.getC2cNum()));
        activityCcPersonalBusinessCertificationPhone.setVisibility(bean.isPhoneAuthFlag()?View.VISIBLE:View.INVISIBLE);
        activityCcPersonalBusinessCertificationRealName.setVisibility(bean.isRealNameAuthFlag()?View.VISIBLE:View.INVISIBLE);
        initRecyclerView();
        buyAdapter.clear();
        buyAdapter.addAll(bean.getBuyList());
        saleAdapter.clear();
        saleAdapter.addAll(bean.getSaleList());
        addHeader();
    }

    private void addHeader() {
        /*添加买入列表头部*/
        if (buyAdapter.getCount() > 0) {
            buyAdapter.addHeader(new RecyclerArrayAdapter.ItemView() {
                @Override
                public View onCreateView(ViewGroup parent) {
                    View headView = LayoutInflater.from(mContext).inflate(R.layout.layout_head_ccpersonal, parent, false);
                    return headView;
                }

                @Override
                public void onBindView(View headerView) {
                /*给头部设置数据*/
                    TextView headTitle = headerView.findViewById(R.id.layout_head_ccPersonal_title);
                    headTitle.setText("在线购买");
                }
            });
        }else{
            buyAdapter.removeAllHeader();
        }

        /*添加买入列表头部*/
        if (saleAdapter.getCount() > 0) {
            saleAdapter.addHeader(new RecyclerArrayAdapter.ItemView() {
                @Override
                public View onCreateView(ViewGroup parent) {
                    View headView = LayoutInflater.from(mContext).inflate(R.layout.layout_head_ccpersonal, parent, false);
                    return headView;
                }

                @Override
                public void onBindView(View headerView) {
                /*给头部设置数据*/
                    TextView headTitle = headerView.findViewById(R.id.layout_head_ccPersonal_title);
                    headTitle.setText("在线出售");
                }
            });
        }else{
            saleAdapter.removeAllHeader();
        }
    }

    private void initRecyclerView() {
        /*初始化买入列表*/
        LinearLayoutManager buyManager = new LinearLayoutManager(mContext);
        buyList.setHasFixedSize(true);
        buyList.setNestedScrollingEnabled(false);
        buyList.setLayoutManager(buyManager);
        buyList.setAdapter(buyAdapter);
        buyAdapter.setPositionCallback(new getPositionCallback() {
            @Override
            public void getPosition(int pos) {
                //购买当前
                int buyHeaderCount = buyAdapter.getHeaderCount();
                C2CPersonalMessage.ListBean item = buyAdapter.getItem(pos-buyHeaderCount);
                if (item == null){
                    return;
                }
                C2cNormalBusiness.ListBean bean = new C2cNormalBusiness.ListBean();
                bean.setId(item.getId());
                bean.setTotalMax(item.getTotalMax());
                bean.setTotalMin(item.getTotalMin());
                bean.setRemain(item.getAmount());
                bean.setPrice(item.getPrice()+"");
                showOrderChooseDialog(bean,true,item.getCoinType());
            }
        });
        /*初始化卖出列表*/
        final LinearLayoutManager saleManager = new LinearLayoutManager(mContext);
        saleList.setHasFixedSize(true);
        saleList.setNestedScrollingEnabled(false);
        saleList.setLayoutManager(saleManager);
        saleList.setAdapter(saleAdapter);
        /*点击事件*/
        saleAdapter.setPositionCallback(new getPositionCallback() {
            @Override
            public void getPosition(int pos) {
                //出售当前
                int saleHeaderCount = saleAdapter.getHeaderCount();
                C2CPersonalMessage.ListBean item = saleAdapter.getItem(pos-saleHeaderCount);
                if (item == null){
                    return;
                }
                C2cNormalBusiness.ListBean bean = new C2cNormalBusiness.ListBean();
                bean.setId(item.getId());
                bean.setTotalMax(item.getTotalMax());
                bean.setTotalMin(item.getTotalMin());
                bean.setRemain(item.getAmount());
                bean.setPrice(item.getPrice()+"");
                getCoinAvailBalance(bean,item.getCoinType());
            }
        });
    }

    /**从服务器获取该币种的可用余额
     * @param item
     * @param cointype*/
    private void getCoinAvailBalance(final C2cNormalBusiness.ListBean item, final int cointype) {
        showLoading(null);
        Map<String, Object> map = new HashMap<>();
        map.put("accountType",ACCOUNT_C2C);
        map.put("coinType",cointype);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", StaticDatas.SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.getCoinAvailbalance(Preferences.getAccessToken(), map, new getBeanCallback<CoinAvailbalance>() {
            @Override
            public void onSuccess(CoinAvailbalance availbalance) {
                availBalance = TextUtils.isEmpty(availbalance.getAvailBalance())?"0.0":availbalance.getAvailBalance();
                showOrderChooseDialog(item,false,cointype);
                hideLoading();
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
                /*网络错误*/
                hideLoading();
            }
        });
    }

    /**弹出下单对话框*/
    private void showOrderChooseDialog(final C2cNormalBusiness.ListBean bean, final boolean isBuy, final int coinType) {
        tradeOrder = new BottomTradeOrderFrame(mContext,bean,isBuy,coinType,availBalance);
        tradeOrder.setGetTradeOrderCallback(new BottomTradeOrderFrame.GetTradeOrderCallback() {
            @Override
            public void getInputDoubleAmount(final Double amount) {
                if (isBuy) {
                    ccNormalBuy(bean,amount,coinType);
                }else{
                 /*弹出输入交易密码对话框*/
                    showPayDialog(new getStringCallback() {
                        @Override
                        public void getString(String string) {
                            ccNormalSale(string,bean,amount,coinType);
                        }
                    });
                }
            }
        });
        tradeOrder.showUp(buyList);
    }


    private void ccNormalBuy(C2cNormalBusiness.ListBean item, Double amount, int coinType) {
        /*获取用户输入委托价格、委托数量*/
        showLoading(null);
        Map<String, Object> map = new HashMap<>();
        map.put("coinType", coinType);
        map.put("amount", amount);
        map.put("orderId", item.getId());
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        NetWorks.c2cNormalBuy(Preferences.getAccessToken(), map, new getBeanCallback<OrderIdBean>() {
            @Override
            public void onSuccess(OrderIdBean info) {
                hideLoading();
                showToast("购买成功");
                LogUtils.w(TAG, "购买成功");
                if (info != null){
                    Intent intent = new Intent(mContext, C2CEntrustDetailsActivity.class);
                    intent.putExtra("normalOrBusiness",NORMAL);
                    intent.putExtra("entrustId",info.getId());
                    startActivity(intent);
                }
            }

            @Override
            public void onError(int errorCode, String reason) {
                hideLoading();
                onFail(errorCode, reason);
            }
        });
    }

    private void ccNormalSale(String pwd, C2cNormalBusiness.ListBean item, Double amount, int coinType) {
        /*获取用户输入委托价格、委托数量*/
        showLoading(null);
        Map<String, Object> map = new HashMap<>();
        map.put("coinType", coinType);
        map.put("amount", amount);
        map.put("password", pwd);
        map.put("orderId", item.getId());
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        NetWorks.c2cNormalSale(Preferences.getAccessToken(), map, new getBeanCallback<OrderIdBean>() {
            @Override
            public void onSuccess(OrderIdBean info) {
                hideLoading();
                showToast("出售成功");
                LogUtils.w(TAG, "出售成功");
                if (info != null){
                    Intent intent = new Intent(mContext, C2CEntrustDetailsNewActivity.class);
                    intent.putExtra("normalOrBusiness",NORMAL);
                    intent.putExtra("entrustId",info.getId());
                    startActivity(intent);
                }
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
                hideLoading();
            }
        });
    }


    /**
     * 获取详情，初始化界面
     */
    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    /**
     * 开始聊天
     */
    public void startChat() {
        if (!isLogin()){
            showToast(R.string.unlogin);
            return;
        }
        try{
            if (Preferences.getLocalUser().getPhone().equals(phone)) {
                showToast("不能与自己聊天");
                return;
            }
            RongIM.getInstance().startPrivateChat(mContext, phone, nikeName);
        }catch (Exception e){
            e.printStackTrace();
            showToast("系统异常,请重试");
        }
    }

    @OnClick({R.id.iv_back, R.id.activity_ccPersonalBusiness_contact})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            /*返回*/
            case R.id.iv_back:
                C2CPersonalBusinessActivity.this.finish();
                break;
                /*联系对方*/
            case R.id.activity_ccPersonalBusiness_contact:
//                startChat();
                break;
                default:break;
        }
    }
}
