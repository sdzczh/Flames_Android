package app.com.pgy.Fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.com.pgy.Constants.Preferences;
import app.com.pgy.Fragments.Base.BaseFragment;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Interfaces.getStringCallback;
import app.com.pgy.Models.Beans.BindInfo;
import app.com.pgy.Models.Beans.C2CBusinessCoinAvail;
import app.com.pgy.Models.Beans.Configuration;
import app.com.pgy.Models.Beans.EventBean.EventC2cCoinChange;
import app.com.pgy.Models.Beans.EventBean.EventC2cTradeCoin;
import app.com.pgy.Models.Beans.EventBean.EventLoginState;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Utils.MathUtils;
import app.com.pgy.Utils.TimeUtils;
import app.com.pgy.Widgets.NumberEditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static app.com.pgy.Constants.StaticDatas.BUY;
import static app.com.pgy.Constants.StaticDatas.SALE;
import static app.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * 创建日期：2018/7/9 0009 on 下午 7:08
 * 描述:      C2C法币交易->购买(出售)->商家
 *
 * @author xu
 */

public class C2cTradeBuyOrSaleBusinessFragment extends BaseFragment implements CheckBox.OnCheckedChangeListener {
    @BindView(R.id.fragment_c2cBusinessPublish_priceTitle)
    TextView fragmentC2cBusinessPublishPriceTitle;
    @BindView(R.id.fragment_c2cBusinessPublish_inputPrice)
    NumberEditText fragmentC2cBusinessPublishInputPrice;
    @BindView(R.id.fragment_c2cBusinessPublish_realTimePrice)
    TextView fragmentC2cBusinessPublishRealTimePrice;
    @BindView(R.id.fragment_c2cBusinessPublish_numberTitle)
    TextView fragmentC2cBusinessPublishNumberTitle;
    @BindView(R.id.fragment_c2cBusinessPublish_inputNumber)
    NumberEditText fragmentC2cBusinessPublishInputNumber;
    @BindView(R.id.fragment_c2cBusinessPublish_amountTitle)
    TextView fragmentC2cBusinessPublishAmountTitle;
    @BindView(R.id.fragment_c2cBusinessPublish_allPrice)
    TextView fragmentC2cBusinessPublishAllPrice;
    @BindView(R.id.fragment_c2cBusinessPublish_limitMinPrice)
    TextView fragmentC2cBusinessPublishLimitMinPrice;
    @BindView(R.id.fragment_c2cBusinessPublish_minPrice)
    NumberEditText fragmentC2cBusinessPublishMinPrice;
    @BindView(R.id.fragment_c2cBusinessPublish_maxPrice)
    NumberEditText fragmentC2cBusinessPublishMaxPrice;
    @BindView(R.id.fragment_c2cBusinessPublish_checkAli)
    CheckBox fragmentC2cBusinessPublishCheckAli;
    @BindView(R.id.fragment_c2cBusinessPublish_checkWx)
    CheckBox fragmentC2cBusinessPublishCheckWx;
    @BindView(R.id.fragment_c2cBusinessPublish_checkCard)
    CheckBox fragmentC2cBusinessPublishCheckCard;
    @BindView(R.id.fragment_c2cBusinessPublish_notice)
    TextView fragmentC2cBusinessPublishNotice;
    @BindView(R.id.fragment_c2cBusinessPublish_publish)
    TextView fragmentC2cBusinessPublishPublish;
    @BindView(R.id.fragment_c2cBusinessPublish_marginFee)
    TextView fragmentC2cBusinessPublishMarginFee;
    @BindView(R.id.fragment_c2cBusinessPublish_serverFee)
    TextView fragmentC2cBusinessPublishServerFee;
    @BindView(R.id.fragment_c2cBusinessPublish_payTitle)
    TextView fragmentC2cBusinessPublishPayTitle;
    @BindView(R.id.fragment_c2cBusinessPublish_availTitle)
    TextView fragmentC2cBusinessPublishAvailTitle;
    @BindView(R.id.fragment_c2cBusinessPublish_avail)
    TextView fragmentC2cBusinessPublishAvail;
    @BindView(R.id.fragment_c2cBusinessPublish_coinName)
    TextView fragmentC2cBusinessPublishCoinName;
    private int coinType;
    private String coinName;
    private boolean isBuy;
    /**
     * 用户输入,价格、数量
     */
    private Double price, amount, tradeAmount;
    private Double minPrice, maxPrice;
    /**
     * 保证金，最小交易额,平台服务费
     */
    private String availBalance,lastedPrice, marginFee, minTradeAmount, serverFee;
    /**
     * 支付方式
     */
    private int payType = -1;
    private int aliPay = 4, wxPay = 2, cardPay = 1;

    public C2cTradeBuyOrSaleBusinessFragment() {
    }

    public static C2cTradeBuyOrSaleBusinessFragment newInstance(int coinType, boolean isBuy) {
        C2cTradeBuyOrSaleBusinessFragment fragment = new C2cTradeBuyOrSaleBusinessFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("coinType", coinType);
        bundle.putBoolean("isBuy", isBuy);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_cc_business_publish_new;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (isBuy) {
            fragmentC2cBusinessPublishPriceTitle.setText("买入价格");
            fragmentC2cBusinessPublishNumberTitle.setText("买入数量");
            fragmentC2cBusinessPublishPublish.setText("立即买入");
            fragmentC2cBusinessPublishPublish.setBackgroundResource(R.mipmap.c2c_trade_buy);
//            fragmentC2cBusinessPublishPayTitle.setText("支付方式");
//            fragmentC2cBusinessPublishPublish.setBackgroundResource(R.drawable.bg_corners_bluesolid);
        } else {
            fragmentC2cBusinessPublishPriceTitle.setText("卖出价格");
            fragmentC2cBusinessPublishNumberTitle.setText("卖出数量");
            fragmentC2cBusinessPublishPublish.setText("立即卖出");
            fragmentC2cBusinessPublishPublish.setBackgroundResource(R.mipmap.c2c_trade_sale);
//            fragmentC2cBusinessPublishPayTitle.setText("收款方式");
//            fragmentC2cBusinessPublishPublish.setBackgroundResource(R.drawable.bg_corners_darkbluesolid);
        }
//        fragmentC2cBusinessPublishNumberTitle.setText("数量(" + coinName + ")");
        fragmentC2cBusinessPublishAvailTitle.setText(coinName+"  余额");
        fragmentC2cBusinessPublishCoinName.setText(coinName);
        /*添加单选多选按钮监听*/
        fragmentC2cBusinessPublishCheckAli.setOnCheckedChangeListener(this);
        fragmentC2cBusinessPublishCheckWx.setOnCheckedChangeListener(this);
        fragmentC2cBusinessPublishCheckCard.setOnCheckedChangeListener(this);
        LimitPriceWatcher limitPriceWatcher = new LimitPriceWatcher();
        fragmentC2cBusinessPublishInputPrice.addTextChangedListener(limitPriceWatcher);
        fragmentC2cBusinessPublishInputNumber.addTextChangedListener(limitPriceWatcher);
        Configuration.CoinInfo coinInfo = getCoinInfo(coinType);
        int c2cNumScale = coinInfo.getC2cNumScale();
        int c2cPriceScale = coinInfo.getC2cPriceScale();
        LogUtils.w(TAG, "coin:" + coinType + "number:" + c2cNumScale + ",price:" + c2cPriceScale);
        /*设置币种价格、数量的限制输入位数*/
        fragmentC2cBusinessPublishInputPrice.setDigits(c2cPriceScale);
        fragmentC2cBusinessPublishInputNumber.setDigits(c2cNumScale);
        fragmentC2cBusinessPublishMinPrice.setDigits(c2cNumScale);
        fragmentC2cBusinessPublishMaxPrice.setDigits(c2cNumScale);
        fragmentC2cBusinessPublishNotice.setText(Html.fromHtml("1、订单有效期为15分钟，请及时付款并点击“我已付款”。<br>" + "2、币由系统锁定托管，请安心下单。"));
        refreshView();

    }

    @Override
    protected void initData() {
        coinType = Preferences.getC2CCoin();
        coinName = getCoinName(coinType);
        isBuy = getArguments().getBoolean("isBuy");
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }


    /**
     * 初始化界面
     */
    private void refreshView() {
        if (coinType <= 0) {
            /*当为KN币的时候，价格定死*/
            fragmentC2cBusinessPublishInputPrice.setEnabled(false);
            fragmentC2cBusinessPublishInputPrice.setText(isBuy ? "0.99" : "1.00");
        }else if (coinType == 9){
            fragmentC2cBusinessPublishInputPrice.setEnabled(false);
            fragmentC2cBusinessPublishInputPrice.setText(isBuy ? "7.02" : "7.00");
        }
        else {
            fragmentC2cBusinessPublishInputPrice.setEnabled(true);
        }
        fragmentC2cBusinessPublishInputNumber.setText("");
        fragmentC2cBusinessPublishMinPrice.setText("");
        fragmentC2cBusinessPublishMaxPrice.setText("");
        fragmentC2cBusinessPublishCheckAli.setChecked(false);
        fragmentC2cBusinessPublishCheckWx.setChecked(false);
        fragmentC2cBusinessPublishCheckCard.setChecked(false);
        /*参考价格、平台服务费、保证金*/
        getCoinAvailBalanceAndPrice();
    }

    /**
     * 从服务器获取该币种的可用余额
     */
    private void getCoinAvailBalanceAndPrice() {
        showLoading(null);
        Map<String, Object> map = new HashMap<>();
        map.put("coinType", coinType);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.getCoinAvail(Preferences.getAccessToken(), map, new getBeanCallback<C2CBusinessCoinAvail>() {
            @Override
            public void onSuccess(C2CBusinessCoinAvail avail) {
                hideLoading();
                initAvailFrame(avail);
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
                /*网络错误*/
                hideLoading();
                initAvailFrame(null);
            }
        });
    }

    private void initAvailFrame(C2CBusinessCoinAvail avail) {
        if (avail == null) {
            avail = new C2CBusinessCoinAvail();
        }
        /*参考价格KN为固定*/
        lastedPrice = (TextUtils.isEmpty(avail.getLatestPrice()) ? "0" : avail.getLatestPrice());
        if (coinType <= 0) {
            lastedPrice = isBuy ? "0.99" : "1.00";
        }
        if (coinType == 9){
            lastedPrice = isBuy ? "7.02" : "7.00";

        }
        availBalance = (TextUtils.isEmpty(avail.getAvailBalance()) ? "0" : avail.getAvailBalance());
        fragmentC2cBusinessPublishAvail.setText(availBalance);
        fragmentC2cBusinessPublishRealTimePrice.setText(lastedPrice + " CNY");
        minTradeAmount = (TextUtils.isEmpty(avail.getMinDealAmt()) ? "0" : avail.getMinDealAmt());
        fragmentC2cBusinessPublishLimitMinPrice.setText("最小交易额" + minTradeAmount);
        marginFee = (TextUtils.isEmpty(avail.getDeposit()) ? "0" : avail.getDeposit());
        fragmentC2cBusinessPublishMarginFee.setText("保证金：" + marginFee + coinName);
        serverFee = (TextUtils.isEmpty(avail.getPlatFee()) ? "0" : avail.getPlatFee());
        fragmentC2cBusinessPublishServerFee.setText(serverFee + "%");
    }

    /**
     * 登录状态监听
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventLoginState loginState) {
        LogUtils.w("login", "C2CBusinessEntrustListFragment:" + loginState.isLoged());
        refreshView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void EventCoin(EventC2cTradeCoin eventC2cTradeCoin) {
        LogUtils.e("C2cTradeBuyOrSaleBusinessFragment",isBuy+"收到广播");
        if (eventC2cTradeCoin.getCoinType() != coinType){
           coinType = eventC2cTradeCoin.getCoinType();
           coinName = getCoinName(coinType);
           fragmentC2cBusinessPublishAvailTitle.setText(coinName+"  余额");
           fragmentC2cBusinessPublishCoinName.setText(coinName);
           Configuration.CoinInfo coinInfo = getCoinInfo(coinType);
           int c2cNumScale = coinInfo.getC2cNumScale();
           int c2cPriceScale = coinInfo.getC2cPriceScale();
           LogUtils.w(TAG, "coin:" + coinType + "number:" + c2cNumScale + ",price:" + c2cPriceScale);
           /*设置币种价格、数量的限制输入位数*/
           fragmentC2cBusinessPublishInputPrice.setDigits(c2cPriceScale);
           fragmentC2cBusinessPublishInputNumber.setDigits(c2cNumScale);
           fragmentC2cBusinessPublishMinPrice.setDigits(c2cNumScale);
           fragmentC2cBusinessPublishMaxPrice.setDigits(c2cNumScale);
           refreshView();
       }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @OnClick(R.id.fragment_c2cBusinessPublish_publish)
    public void onViewClicked() {
        /*发布交易*/
        if (!isLogin()) {
            showToast(R.string.unlogin);
            return;
        }
        getBindList();
    }

    public void getBindList(){
        showLoading(null);
        Map<String, Object> map = new HashMap<>();

        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.getBindList(Preferences.getAccessToken(),map, new getBeanCallback<List<BindInfo>>() {
            @Override
            public void onSuccess(List<BindInfo> list) {
                hideLoading();
              if (list == null || list.size() < 1){
                  showToast("请先绑定收款方式");
                  return;
              }
              payType = 0;
              for (BindInfo bindInfo : list){
                  if (bindInfo.getType() == 0){
                      payType += aliPay;
                  }
                  if (bindInfo.getType() == 1){
                      payType +=wxPay;
                  }

                  if (bindInfo.getType() == 2){
                      payType +=cardPay;
                  }
              }
              submit();
            }

            @Override
            public void onError(int errorCode, String reason) {
                hideLoading();
                onFail(errorCode, reason);
                /*List<C2cNormalEntrust.ListBean> c2CTradeOrder = DefaultData.getC2CTradeOrder(tradeType, stateType);
                adapter.addAll(c2CTradeOrder);*/
            }
        });
    }


    /**
     * 提交三方充值
     */
    private void submit() {
        /*获取输入的内容*/
        if (coinType < 0) {
            showToast("请选择币种");
            return;
        }
        if (price == null || price <= 0) {
            showToast("请输入价格");
            return;
        }
        if (amount == null || amount <= 0) {
            showToast("请输入数量");
            return;
        }
        if (tradeAmount < MathUtils.string2Double(minTradeAmount)) {
            showToast("最小交易额" + minTradeAmount);
            return;
        }
        String minPriceStr = fragmentC2cBusinessPublishMinPrice.getText().toString().trim();
        minPrice = MathUtils.string2Double(minPriceStr);
        if (minPrice <= 0) {
            showToast("请输入最小总价");
            return;
        }
        String maxPriceStr = fragmentC2cBusinessPublishMaxPrice.getText().toString().trim();
        maxPrice = MathUtils.string2Double(maxPriceStr);
        if (maxPrice <= 0) {
            showToast("请输入最大总价");
            return;
        }
        if (maxPrice < minPrice) {
            showToast("最大总价不能小于最小总价");
            return;
        }
//        payType = aliPay + wxPay + cardPay;
        if (payType <= 0) {
            showToast("请至少选择一种支付方式");
            return;
        }
        /*弹出输入交易密码对话框*/
        showPayDialog(new getStringCallback() {
            @Override
            public void getString(String string) {
                publishTrade2Net(string);
            }
        });
    }

    /**
     * 发布交易
     */
    private void publishTrade2Net(String pwd) {
        showLoading(fragmentC2cBusinessPublishPublish);
        Map<String, Object> map = new HashMap<>();
        map.put("coinType", coinType);
        map.put("price", price);
        map.put("amount", amount);
        map.put("password", pwd);
        map.put("orderType", isBuy ? BUY : SALE);
        map.put("totalMin", minPrice);
        map.put("totalMax", maxPrice);
        map.put("payType", payType);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.c2cPublishTrade(Preferences.getAccessToken(), map, new getBeanCallback() {
            @Override
            public void onSuccess(Object o) {
                showToast("发布交易成功");
                LogUtils.w(TAG, "发布交易成功");
                refreshView();
                hideLoading();
            }

            @Override
            public void onError(int errorCode, String reason) {
                hideLoading();
                onFail(errorCode, reason);
            }
        });
    }

    /**
     * 总价（单价*数量）>200
     */
    private class LimitPriceWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            /*数量*/
            String limitPriceEntrustAmountStr = fragmentC2cBusinessPublishInputNumber.getText().toString().trim();
            /*获取输入的数量*/
            amount = MathUtils.string2Double(limitPriceEntrustAmountStr);
            String priceStr = fragmentC2cBusinessPublishInputPrice.getText().toString().trim();
            price = MathUtils.string2Double(priceStr);
            /*得到总价*/
            tradeAmount = price * amount;
            if (tradeAmount > 0) {
                String allAmount = String.valueOf(MathUtils.formatDoubleNumber(tradeAmount));
                LogUtils.w(TAG, "总价：" + allAmount);
                fragmentC2cBusinessPublishAllPrice.setText(allAmount);
            } else {
                fragmentC2cBusinessPublishAllPrice.setText("0.00");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }

    /*支付方式多选*/
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        /*选中支付宝*/
        if (fragmentC2cBusinessPublishCheckAli.isChecked()) {
            aliPay = 4;
        } else {
            aliPay = 0;
        }
        /*选中微信支付*/
        if (fragmentC2cBusinessPublishCheckWx.isChecked()) {
            wxPay = 2;
        } else {
            wxPay = 0;
        }
        /*选中银行卡*/
        if (fragmentC2cBusinessPublishCheckCard.isChecked()) {
            cardPay = 1;
        } else {
            cardPay = 0;
        }
    }

}
