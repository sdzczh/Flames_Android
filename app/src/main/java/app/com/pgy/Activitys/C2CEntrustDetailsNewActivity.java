package app.com.pgy.Activitys;

import android.Manifest;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.com.pgy.Activitys.Base.PermissionActivity;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Interfaces.DownTimerListener;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Interfaces.getStringCallback;
import app.com.pgy.Interfaces.spinnerSingleChooseListener;
import app.com.pgy.Models.Beans.C2CEntrustDetails;
import app.com.pgy.Models.Beans.EventBean.EventC2cEntrustList;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Utils.FileUtils;
import app.com.pgy.Utils.ImageLoaderUtils;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Utils.TimeUtils;
import app.com.pgy.Widgets.C2cTradeDialog;
import app.com.pgy.Widgets.ExitDialog;
import app.com.pgy.Widgets.MyBottomSpinnerList;
import app.com.pgy.Widgets.PersonalItemView;
import app.com.pgy.im.widget.DownTimer;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;

import static app.com.pgy.Constants.StaticDatas.ALIPAY;
import static app.com.pgy.Constants.StaticDatas.BANKCARD;
import static app.com.pgy.Constants.StaticDatas.BUSINESS;
import static app.com.pgy.Constants.StaticDatas.BUY;
import static app.com.pgy.Constants.StaticDatas.NORMAL;
import static app.com.pgy.Constants.StaticDatas.SALE;
import static app.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;
import static app.com.pgy.Constants.StaticDatas.WECHART;

/**
 * Create by Android on 2019/10/22 0022
 */
public class C2CEntrustDetailsNewActivity extends PermissionActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;

    //单号
    @BindView(R.id.activity_ccEntrustDetails_contentOrder_numId)
    PersonalItemView activityCcEntrustDetailsContentOrderNumId;
    //交易市场
    @BindView(R.id.activity_ccEntrustDetails_contentOrder_coinname)
    PersonalItemView activityCcEntrustDetailsContentOrderCoinname;
    //单价
    @BindView(R.id.activity_ccEntrustDetails_contentTop_priceBg)
    PersonalItemView activityCcEntrustDetailsContentTopPriceBg;
    //交易总额
    @BindView(R.id.activity_ccEntrustDetails_contentTop_numberBg)
    PersonalItemView activityCcEntrustDetailsContentTopNumberBg;
    //交易量
    @BindView(R.id.activity_ccEntrustDetails_contentOrder_number)
    PersonalItemView activityCcEntrustDetailsContentOrderNumber;
    //订单状态
    @BindView(R.id.activity_ccEntrustDetails_top_orderState)
    PersonalItemView activityCcEntrustDetailsTopOrderState;

    //付款方式
    //卖家姓名
    @BindView(R.id.tv_pay_type_account_name)
    TextView tvPayTypeAccountName;
    @BindView(R.id.iv_pay_type_account_icon)
    ImageView ivPayTypeAccountIcon;
    @BindView(R.id.tv_pay_type_account_bank)
    TextView tvPayTypeAccountBank;
    @BindView(R.id.tv_pay_type_account_branch)
    TextView tvPayTypeAccountBranch;
    @BindView(R.id.ll_pay_type_account_bank)
    LinearLayout llPayTypeAccountBank;
    @BindView(R.id.tv_pay_type_account_title)
    TextView tvPayTypeAccountTitle;
    @BindView(R.id.tv_pay_type_account_num)
    TextView tvPayTypeAccountNum;
    @BindView(R.id.iv_pay_type_account_qrcode)
    ImageView ivPayTypeAccountQrcode;
    @BindView(R.id.ll_pay_type_account)
    LinearLayout llPayTypeAccount;
    @BindView(R.id.ll_pay_type_account_bg)
    LinearLayout llPayTypeAccountBg;

    //买家信息
    @BindView(R.id.tv_buyer_name)
    TextView tvBuyerName;
    @BindView(R.id.ll_buyer_info)
    LinearLayout llBuyerInfo;
    //卖家信息
    @BindView(R.id.tv_saler_paytype)
    TextView tvSalerPaytype;
    @BindView(R.id.ll_saler_bg)
    LinearLayout llSalerBg;
    //确认收款
    @BindView(R.id.activity_ccEntrustDetails_contentBottomBtn_receipted)
    TextView activityCcEntrustDetailsContentBottomBtnReceipted;
    //撤销此单
    @BindView(R.id.activity_ccEntrustDetails_contentBottomBtn_cancelOrder)
    TextView activityCcEntrustDetailsContentBottomBtnCancelOrder;
    //我要申诉
    @BindView(R.id.activity_ccEntrustDetails_contentBottomBtn_complaint)
    TextView activityCcEntrustDetailsContentBottomBtnComplaint;
    //待付款说明
    @BindView(R.id.activity_ccEntrustDetails_contentBottomBtn_buyDes)
    TextView activityCcEntrustDetailsContentBottomBtnBuyDes;
    //待付款说明
    @BindView(R.id.activity_ccEntrustDetails_contentBottomBtn_buyDesbg)
    LinearLayout activityCcEntrustDetailsContentBottomBtnBuyDesbg;
    //我已付款
    @BindView(R.id.activity_ccEntrustDetails_contentBottomBtn_payed)
    TextView activityCcEntrustDetailsContentBottomBtnPayed;
    //重新选择付款方式
    @BindView(R.id.activity_ccEntrustDetails_contentPay_payType_switch)
    TextView activityCcEntrustDetailsContentPayPayTypeSwitch;

    //重新选择付款方式
    @BindView(R.id.ll_choose_paytype)
    LinearLayout llChoosePaytype;
    @BindView(R.id.tv_pay_type_ali_name)
    TextView tvPayTypeAliName;
    @BindView(R.id.tv_pay_type_ali_account)
    TextView tvPayTypeAliAccount;
    @BindView(R.id.iv_pay_type_ali_qrcode)
    ImageView ivPayTypeAliQrcode;
    @BindView(R.id.ll_pay_type_ali)
    LinearLayout llPayTypeAli;
    @BindView(R.id.tv_pay_type_wx_name)
    TextView tvPayTypeWxName;
    @BindView(R.id.tv_pay_type_wx_account)
    TextView tvPayTypeWxAccount;
    @BindView(R.id.iv_pay_type_wx_qrcode)
    ImageView ivPayTypeWxQrcode;
    @BindView(R.id.ll_pay_type_wx)
    LinearLayout llPayTypeWx;
    @BindView(R.id.tv_pay_type_card_name)
    TextView tvPayTypeCardName;
    @BindView(R.id.tv_pay_type_card_bank)
    TextView tvPayTypeCardBank;
    @BindView(R.id.tv_pay_type_card_branch)
    TextView tvPayTypeCardBranch;
    @BindView(R.id.tv_pay_type_card_num)
    TextView tvPayTypeCardNum;
    @BindView(R.id.ll_pay_type_card)
    LinearLayout llPayTypeCard;

    private int entrustId;
    private int normalOrBusiness;
    private MyBottomSpinnerList mySpinnerList;
    /**
     * 支付方式Item点击监听
     */
    private spinnerSingleChooseListener payTypeItemListener;
    private int currentPayType;
    private boolean hasPayType = true;
    private Map<Integer, C2CEntrustDetails.PayInfo> payInfo;
    private String bottomOrderNotice;
    private String erwermaUrl;
    private C2CEntrustDetails c2CEntrustDetails;

    /**
     *     private C2CEntrustDetails c2CEntrustDetails;
     * 卖家姓名和联系方式
     */
    private String userNikeName, userPhone;
    private DownTimer downTimer;

    @Override
    public int getContentViewId() {
        return R.layout.activity_c2c_entrust_details_new;
    }

    @Override
    protected void initData() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        /*获取列表页传递过来的委托单号id*/
        entrustId = getIntent().getIntExtra("entrustId", -1);
        normalOrBusiness = getIntent().getIntExtra("normalOrBusiness", NORMAL);
        getEntrustDetails(false, entrustId);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        tvTitle.setText("订单详情");

    }

    @OnClick({R.id.iv_back,R.id.tv_title_right, R.id.iv_pay_type_account_qrcode, R.id.activity_ccEntrustDetails_contentBottomBtn_receipted,
            R.id.activity_ccEntrustDetails_contentBottomBtn_cancelOrder, R.id.activity_ccEntrustDetails_contentBottomBtn_complaint,
            R.id.activity_ccEntrustDetails_contentPay_copyAccount,
            R.id.activity_ccEntrustDetails_contentBottomBtn_payed, R.id.activity_ccEntrustDetails_contentPay_payType_switch,
            R.id.iv_pay_type_ali_qrcode, R.id.ll_pay_type_ali, R.id.iv_pay_type_wx_qrcode, R.id.ll_pay_type_wx, R.id.ll_pay_type_card,
            R.id.tv_pay_type_account_name,R.id.tv_pay_type_account_bank,R.id.tv_pay_type_account_branch,R.id.tv_pay_type_account_num})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_title_right:
                call(userPhone);
                break;
            case R.id.iv_pay_type_account_qrcode:
                /*弹出二维码窗口*/
                showErweiMaDialog();
                break;
            /*确认收款*/
            case R.id.activity_ccEntrustDetails_contentBottomBtn_receipted:
                showPayDialog(new getStringCallback() {
                    @Override
                    public void getString(String string) {
                        confirmReceipt(string);
                    }
                });
                break;
            /*取消订单*/
            case R.id.activity_ccEntrustDetails_contentBottomBtn_cancelOrder:
                showCancelOrderDialog();
                break;
            /*申诉*/
            case R.id.activity_ccEntrustDetails_contentBottomBtn_complaint:
                Intent intent = new Intent(mContext, C2CEntrustComplaintActivity.class);
                intent.putExtra("orderId", entrustId);
                startActivity(intent);
                break;

            case R.id.activity_ccEntrustDetails_contentPay_copyAccount:
                /*复制支付宝、微信、银行账号*/
//                String accountNumber = tvPayTypeAccountNum.getText().toString();
//                copyMessage(accountNumber);
                break;
            /*我已支付*/
            case R.id.activity_ccEntrustDetails_contentBottomBtn_payed:
                showConfirmPayDialog();
                break;
            case R.id.activity_ccEntrustDetails_contentPay_payType_switch:
                //切换支付方式
                llChoosePaytype.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_pay_type_ali_qrcode:
                showErweiMaDialog(payInfo.get(ALIPAY));
                break;
            case R.id.ll_pay_type_ali:
                if(currentPayType != ALIPAY){
                    currentPayType = ALIPAY;
                    switchPayTypeFrame(currentPayType, payInfo);
                }
                llChoosePaytype.setVisibility(View.GONE);
                break;
            case R.id.iv_pay_type_wx_qrcode:
                showErweiMaDialog(payInfo.get(WECHART));
                break;
            case R.id.ll_pay_type_wx:
                if(currentPayType != WECHART){
                    currentPayType = WECHART;
                    switchPayTypeFrame(currentPayType, payInfo);
                }
                llChoosePaytype.setVisibility(View.GONE);
                break;
            case R.id.ll_pay_type_card:
                if(currentPayType != BANKCARD){
                    currentPayType = BANKCARD;
                    switchPayTypeFrame(currentPayType, payInfo);
                }
                llChoosePaytype.setVisibility(View.GONE);
                break;
//            /*查看对方详情*/
//            case R.id.activity_ccEntrustDetails_bottomContact_frame:
//                Intent intent2Business = new Intent(mContext, C2CPersonalBusinessActivity.class);
//                intent2Business.putExtra("phone", userPhone);
//                startActivity(intent2Business);
//                break;
            case R.id.tv_pay_type_account_num:
                /*复制支付宝、微信、银行账号*/
                copyMessage(tvPayTypeAccountNum.getText().toString());
                break;
            case R.id.tv_pay_type_account_name:
                /*复制支付宝、微信、银行账号*/
                copyMessage(tvPayTypeAccountName.getText().toString());
                break;
            case R.id.tv_pay_type_account_bank:
                /*复制支付宝、微信、银行账号*/
                copyMessage(tvPayTypeAccountBank.getText().toString());
                break;
            case R.id.tv_pay_type_account_branch:
                /*复制支付宝、微信、银行账号*/
                copyMessage(tvPayTypeAccountBranch.getText().toString());
                break;
        }
    }


    /**
     * 去服务器获取详情页
     * isRefreshState 是否刷新了该订单的状态，如果是，则发送刷新广播
     */
    private void getEntrustDetails(final boolean isRefreshState, int id) {
        if (id < 0) {
            showToast("订单号错误");
            return;
        }
        if (!isLogin()) {
            showToast(R.string.unlogin);
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", id);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.getC2CEntrustDetails(Preferences.getAccessToken(), map, new getBeanCallback<C2CEntrustDetails>() {
            @Override
            public void onSuccess(C2CEntrustDetails entrustDetails) {
                if (entrustDetails == null) {
                    entrustDetails = new C2CEntrustDetails();
                }
                LogUtils.w(TAG, entrustDetails.toString());
                initDetailViews(entrustDetails);
                if (isRefreshState) {
                    toRefreshOrderList();
                }
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
                /*网络错误*/
                resetDefaultFrame();
            }
        });
    }

    /**
     * 初始化界面，隐藏掉所有内容
     */
    private void resetDefaultFrame() {
        if (downTimer != null) {
            downTimer.stopDown();
            downTimer = null;
        }
        llPayTypeAccountBg.setVisibility(View.GONE);
        activityCcEntrustDetailsContentBottomBtnCancelOrder.setVisibility(View.GONE);
        activityCcEntrustDetailsContentBottomBtnComplaint.setVisibility(View.GONE);
        activityCcEntrustDetailsContentPayPayTypeSwitch.setVisibility(View.GONE);
        activityCcEntrustDetailsContentBottomBtnReceipted.setVisibility(View.GONE);
        activityCcEntrustDetailsContentBottomBtnPayed.setVisibility(View.GONE);
        activityCcEntrustDetailsContentBottomBtnBuyDesbg.setVisibility(View.GONE);
        tvTitleRight.setVisibility(View.GONE);
        llSalerBg.setVisibility(View.GONE);
        llChoosePaytype.setVisibility(View.GONE);
    }

    /**
     * 根据买卖和订单状态切换界面
     */
    private void initDetailViews(C2CEntrustDetails details) {
        c2CEntrustDetails = details;
        resetDefaultFrame();

        if (downTimer == null) {
            downTimer = new DownTimer();
        }
        userPhone = details.getOtherPhone();
        userNikeName = details.getOtherNickName();
        String coinName = getCoinName(details.getCoinType());
        /*内容顶部交易单价、数量、成交量*/
        activityCcEntrustDetailsContentOrderNumId.setLeftTxt("单号："+details.getOrderNum());
        activityCcEntrustDetailsContentOrderNumId.setRightTxt(details.getCreateTime());
        activityCcEntrustDetailsContentOrderCoinname.setRightTxt(coinName+"/CNY");
        activityCcEntrustDetailsContentTopPriceBg.setRightTxt(details.getPrice()+" CNY");
        activityCcEntrustDetailsContentTopNumberBg.setRightTxt( details.getTotal()+" CNY");
        activityCcEntrustDetailsContentOrderNumber.setRightTxt(details.getAmount()+" "+ coinName);

        payInfo = details.getPayInfo();
        currentPayType = ALIPAY;

        /*获取默认的支付类型0支付宝 1微信 2银行卡，若没有则设为支付宝支付*/
        if (payInfo != null && payInfo.size() > 0){
            for (int key : payInfo.keySet()){
                currentPayType = payInfo.get(key).getType();
                if (currentPayType == ALIPAY){
                    break;
                }
            }
        }
        initPayAccount(payInfo);
        /*交易类型 0买入 1卖出*/
        int buyOrSale = details.getOrderType();
        /*订单状态，0代付款 1待确认 2冻结 3已完成 4已取消 5超时取消*/
        int state = details.getState();
        switchPayTypeFrame(currentPayType, payInfo);
        switch (state) {
                /*代付款状态*/
                case 0:
                /*显示支付信息、底部联系对方、底部交易提醒*/
                tvTitleRight.setVisibility(View.GONE);
                activityCcEntrustDetailsTopOrderState.setLeftTxt("未付款");
                int cutDownSeconds = details.getInactiveTimeInterval() * 1000;
                if (downTimer != null) {
                    downTimer.stopDown();
                }
                downTimer.startDown(cutDownSeconds > 0 ? cutDownSeconds : 0);
                downTimer.setListener(new DownTimerListener() {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        int seconds = (int) (millisUntilFinished / 1000);
                        String currentTime = TimeUtils.seconds2CountDownTime(seconds);
//                                String countDownStr = currentTime + "内未付款，订单将自动取消，打款请备注参考号";
                        activityCcEntrustDetailsTopOrderState.setRightTxt(currentTime);
                    }

                    @Override
                    public void onFinish() {
                        /*倒计时完成，刷新页面*/
                        getEntrustDetails(true, entrustId);
                    }
                });
                llPayTypeAccountBg.setVisibility(View.VISIBLE);

                switch (buyOrSale) {
                    default:
                    case BUY:
                        activityCcEntrustDetailsTopOrderState.setLeftTxt("未付款");
                        /*显示蓝色信息、取消订单我已支付按钮、黄色按钮*/
                        activityCcEntrustDetailsContentBottomBtnBuyDesbg.setVisibility(View.VISIBLE);
                        activityCcEntrustDetailsContentBottomBtnCancelOrder.setVisibility(View.VISIBLE);
                        activityCcEntrustDetailsContentBottomBtnPayed.setVisibility(View.VISIBLE);
                        activityCcEntrustDetailsContentPayPayTypeSwitch.setVisibility(View.VISIBLE);


                        break;
                    case SALE:
                        /*显示取消订单*/
                        activityCcEntrustDetailsContentBottomBtnCancelOrder.setVisibility(View.GONE);
                        break;
                }
                break;
            /*待确认状态*/
            case 1:
                activityCcEntrustDetailsTopOrderState.setLeftTxt("待确认");
                activityCcEntrustDetailsTopOrderState.setRightTxt(details.getInactiveTime());
                /*显示拨打电话、订单信息、黄色提示信息、交易提醒*/
//                tvTitleRight.setVisibility(View.VISIBLE);

                switch (buyOrSale) {
                    default:
                    case BUY:
                        llPayTypeAccountBg.setVisibility(View.VISIBLE);
                        break;
                    case SALE:
                        llSalerBg.setVisibility(View.VISIBLE);
                        llBuyerInfo.setVisibility(View.VISIBLE);
                        tvBuyerName.setText(details.getBuyerName());


                        activityCcEntrustDetailsContentBottomBtnComplaint.setVisibility(View.VISIBLE);
                        activityCcEntrustDetailsContentBottomBtnReceipted.setVisibility(View.VISIBLE);
                        break;
                }
                break;
            /*冻结状态,申诉中*/
            case 2:
                /*显示支付信息、底部信息*/
                activityCcEntrustDetailsTopOrderState.setLeftTxt("申诉中");
                activityCcEntrustDetailsTopOrderState.setRightTxt("");
                switch (buyOrSale) {
                    default:
                    case BUY:
                        llPayTypeAccountBg.setVisibility(View.VISIBLE);
                        break;
                    case SALE:
                        llSalerBg.setVisibility(View.VISIBLE);
                        llBuyerInfo.setVisibility(View.VISIBLE);
                        tvBuyerName.setText(details.getBuyerName());
                        break;
                }
                break;
            /*已完成状态*/
            case 3:
                activityCcEntrustDetailsTopOrderState.setLeftTxt("已完成");
                activityCcEntrustDetailsTopOrderState.setRightTxt("");

                switch (buyOrSale) {
                    default:
                    case BUY:
                        llPayTypeAccountBg.setVisibility(View.VISIBLE);
                        break;
                    case SALE:
                        llSalerBg.setVisibility(View.VISIBLE);
                        llBuyerInfo.setVisibility(View.VISIBLE);
                        tvBuyerName.setText(details.getOtherName());
                        break;
                }
                break;
            /*已取消状态*/
            case 4:
                /*显示支付信息*/
                activityCcEntrustDetailsTopOrderState.setLeftTxt("已取消");
                activityCcEntrustDetailsTopOrderState.setRightTxt("0s");
                llPayTypeAccountBg.setVisibility(View.VISIBLE);
                activityCcEntrustDetailsContentBottomBtnBuyDes.setText("此订单已关闭，切勿再次给卖家付款。当前信息仅供浏览。");
                switch (buyOrSale) {
                    default:
                    case BUY:
                        llPayTypeAccountBg.setVisibility(View.VISIBLE);
                        break;
                    case SALE:
                        llSalerBg.setVisibility(View.VISIBLE);
                        llBuyerInfo.setVisibility(View.VISIBLE);
                        tvBuyerName.setText(details.getBuyerName());
                        break;
                }
                break;
            /*超时取消状态*/
            case 5:
                activityCcEntrustDetailsTopOrderState.setLeftTxt("超时取消");
                activityCcEntrustDetailsTopOrderState.setRightTxt("0s");
                llPayTypeAccountBg.setVisibility(View.VISIBLE);
                activityCcEntrustDetailsContentBottomBtnBuyDes.setText("此订单已关闭，切勿再次给卖家付款。当前信息仅供浏览。");
                switch (buyOrSale) {
                    default:
                    case BUY:
                        llPayTypeAccountBg.setVisibility(View.VISIBLE);
                        break;
                    case SALE:
                        llSalerBg.setVisibility(View.VISIBLE);
                        llBuyerInfo.setVisibility(View.VISIBLE);
                        tvBuyerName.setText(details.getBuyerName());
                        break;
                }
                break;
            default:
                break;
        }

    }

    private void initPayAccount(Map<Integer, C2CEntrustDetails.PayInfo> payInfo){
        if (payInfo == null || payInfo.isEmpty()){
            hasPayType = false;
        }
        llPayTypeAli.setVisibility(View.GONE);
        llPayTypeWx.setVisibility(View.GONE);
        llPayTypeCard.setVisibility(View.GONE);
        if (payInfo.containsKey(ALIPAY)){
            C2CEntrustDetails.PayInfo aliPayInfo = payInfo.get(ALIPAY);
            tvPayTypeAliName.setText(aliPayInfo.getName());
            tvPayTypeAliAccount.setText(aliPayInfo.getAccount());
            llPayTypeAli.setVisibility(View.VISIBLE);
            if (aliPayInfo.getType() == 0){
                tvSalerPaytype.setText("支付宝");
            }

        }
        if (payInfo.containsKey(WECHART)){
            C2CEntrustDetails.PayInfo wxPayInfo = payInfo.get(WECHART);
            llPayTypeWx.setVisibility(View.VISIBLE);
            tvPayTypeWxName.setText(wxPayInfo.getName());
            tvPayTypeWxAccount.setText(wxPayInfo.getAccount());
            if (wxPayInfo.getType() == 1){
                tvSalerPaytype.setText("微信");
            }
        }
        if (payInfo.containsKey(BANKCARD)){
            C2CEntrustDetails.PayInfo cardPayInfo = payInfo.get(BANKCARD);
            llPayTypeCard.setVisibility(View.VISIBLE);
            tvPayTypeCardName.setText(cardPayInfo.getName());
            tvPayTypeCardBank.setText(cardPayInfo.getBankName());
            tvPayTypeCardBranch.setText(cardPayInfo.getBranchName());
            tvPayTypeCardNum.setText(cardPayInfo.getAccount());
            if (cardPayInfo.getType() == 2){
                tvSalerPaytype.setText("银行卡");
            }
        }
    }

    /**
     * 根据支付类型，设置界面显示文本
     */
    private void switchPayTypeFrame(int payType, Map<Integer, C2CEntrustDetails.PayInfo> payInfo) {
        /*填充支付详情*/
        C2CEntrustDetails.PayInfo currentPayInfo = payInfo.get(payType);
        if (currentPayInfo == null) {
            currentPayInfo = new C2CEntrustDetails.PayInfo();
        }
        switch (payType) {
            case ALIPAY:
                tvPayTypeAccountTitle.setText("支付宝账号");
                ivPayTypeAccountIcon.setImageResource(R.mipmap.ali_pay_white);
                tvPayTypeAccountName.setText(currentPayInfo.getName());
                tvPayTypeAccountNum.setText(currentPayInfo.getAccount());
                ivPayTypeAccountQrcode.setVisibility(View.VISIBLE);
                llPayTypeAccountBank.setVisibility(View.GONE);
                erwermaUrl = currentPayInfo.getImgurl();
                llPayTypeAccount.setBackgroundResource(R.mipmap.ali_pay_bg);
                break;
            case WECHART:
                tvPayTypeAccountTitle.setText("微信账号");
                ivPayTypeAccountIcon.setImageResource(R.mipmap.wx_pay_white);
                tvPayTypeAccountName.setText(currentPayInfo.getName());
                tvPayTypeAccountNum.setText(currentPayInfo.getAccount());
                ivPayTypeAccountQrcode.setVisibility(View.VISIBLE);
                llPayTypeAccountBank.setVisibility(View.GONE);
                erwermaUrl = currentPayInfo.getImgurl();
                llPayTypeAccount.setBackgroundResource(R.mipmap.wx_pay_bg);
                break;
            case BANKCARD:
                tvPayTypeAccountTitle.setText("银行卡号");
                ivPayTypeAccountIcon.setImageResource(R.mipmap.card_pay_white);
                tvPayTypeAccountName.setText(currentPayInfo.getName());
                tvPayTypeAccountNum.setText(currentPayInfo.getAccount());
                tvPayTypeAccountBank.setText(currentPayInfo.getBankName());
                tvPayTypeAccountBranch.setText(currentPayInfo.getBranchName());
                ivPayTypeAccountQrcode.setVisibility(View.GONE);
                llPayTypeAccountBank.setVisibility(View.VISIBLE);
                llPayTypeAccount.setBackgroundResource(R.mipmap.card_pay_bg);
                break;
            default:
                break;
        }
    }

    /**
     * 确认支付提示对话框
     */
    private void showConfirmPayDialog() {
//        final ExitDialog.Builder builder = new ExitDialog.Builder(mContext);
//        builder.setTitle("支付确认");
//        builder.setMessage("确认已经完成付款了吗？ 若恶意提交会造成账号封禁。");
//        builder.setCancelable(true);
//
//        builder.setPositiveButton("确定",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        /*弹出输入交易密码对话框*/
//                        showPayDialog(new getStringCallback() {
//                            @Override
//                            public void getString(String string) {
//                                confirmPayed(string);
//                            }
//                        });
//                        dialog.dismiss();
//                    }
//                });
//        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        builder.create().show();


        C2cTradeDialog.Builder builder1 = new C2cTradeDialog.Builder(mContext);
        builder1.setOrderNo(c2CEntrustDetails.getOrderNum());
        builder1.setCoinName(getCoinName(c2CEntrustDetails.getCoinType()));
        builder1.setAmount(c2CEntrustDetails.getAmount());
        builder1.setAction("支付确认");
        builder1.setConfirmDes("确认已经完成付款, 若恶意提交会造成账号封禁。");

        builder1.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /*弹出输入交易密码对话框*/
                        showPayDialog(new getStringCallback() {
                            @Override
                            public void getString(String string) {
                                confirmPayed(string);
                            }
                        });
                        dialog.dismiss();
                    }
                });
        builder1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder1.create().show();
    }

    /**
     * 确定支付
     */
    private void confirmPayed(String pwd) {
        showLoading(null);
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", entrustId);
        map.put("password", pwd);
        map.put("payType", currentPayType);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.c2cEntrustConfirmPayed(Preferences.getAccessToken(), map, new getBeanCallback() {
            @Override
            public void onSuccess(Object o) {
                hideLoading();
                showToast("成功");
                LogUtils.w(TAG, "成功");
                getEntrustDetails(true, entrustId);
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
                hideLoading();
            }
        });
    }

    /**
     * 调用拨号界面
     *
     * @param phone 电话号码
     */
    private void call(String phone) {
        if (TextUtils.isEmpty(phone)) {
            showToast("未获取到联系电话");
            return;
        }
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * 复制到剪切板
     */
    private void copyMessage(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
// 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("simple text", str);
// 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
        showToast("复制成功");
    }

    /**
     * 确定收款，放币
     */
    private void confirmReceipt(String pwd) {
        showLoading(null);
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", entrustId);
        map.put("password", pwd);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.c2cEntrustConfirmReceipt(Preferences.getAccessToken(), map, new getBeanCallback() {
            @Override
            public void onSuccess(Object o) {
                hideLoading();
                showToast("成功");
                LogUtils.w(TAG, "成功");
                getEntrustDetails(true, entrustId);
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
                hideLoading();
            }
        });
    }

    /**
     * 确认支付提示对话框
     */
    private void showCancelOrderDialog() {
//        final ExitDialog.Builder builder = new ExitDialog.Builder(mContext);
        String count;
        switch (normalOrBusiness) {
            default:
            case NORMAL:
                count = getConfiguration().getMaxCancelOfTaker();
                break;
            case BUSINESS:
                count = getConfiguration().getMaxCancelOfMaker();
                break;
        }
//        if (!TextUtils.isEmpty(count)) {
//            builder.setTitle("确认取消订单");
//        }
//        String content = "1、如已向卖家付款，千万不要取消订单。<br>" +
//                "2、每日取消" + count + "笔交易会限制当日买入功能。";
//        builder.setMessage(content);
//        builder.setCancelable(true);
//        builder.setPositiveButton("取消订单",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        cancelOrder();
//                        dialog.dismiss();
//                    }
//                });
//        builder.setNegativeButton("暂不取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        builder.create().show();


        C2cTradeDialog.Builder builder1 = new C2cTradeDialog.Builder(mContext);
        builder1.setOrderNo(c2CEntrustDetails.getOrderNum());
        builder1.setCoinName(getCoinName(c2CEntrustDetails.getCoinType()));
        builder1.setAmount(c2CEntrustDetails.getAmount());
        builder1.setAction("取消订单");
        if (!TextUtils.isEmpty(count)) {
            builder1.setConfirmDes("1、如已向卖家付款，千万不要取消订单。\n"+
                    "2、每日取消" + count + "笔交易会限制当日买入功能。");
        }else {
            builder1.setConfirmDes("如已向卖家付款，千万不要取消订单。");
        }


        builder1.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancelOrder();
                        dialog.dismiss();
                    }
                });
        builder1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder1.create().show();
    }

    /**
     * 取消订单
     */
    private void cancelOrder() {
        showLoading(null);
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", entrustId);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.c2cEntrustCancelOrder(Preferences.getAccessToken(), map, new getBeanCallback() {
            @Override
            public void onSuccess(Object o) {
                hideLoading();
                showToast("成功");
                LogUtils.w(TAG, "成功");
                getEntrustDetails(true, entrustId);
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
                hideLoading();
            }
        });
    }

    /**
     * 订单状态改变，去刷新列表
     */
    private void toRefreshOrderList() {
        LogUtils.w("EventC2cEntrustList", "post:");
        EventBus.getDefault().post(new EventC2cEntrustList(true));
    }

    /**
     * 订单详情状态改变，刷新列表
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventC2cEntrustList entrustList) {
        getEntrustDetails(false, entrustId);
    }

    /**
     * 显示二维码的对话框
     */
    private void showErweiMaDialog() {
        if (TextUtils.isEmpty(erwermaUrl)) {
            showToast("二维码为空");
            return;
        }
        View view = getLayoutInflater().inflate(R.layout.show_erweima_dialog, null);
        Dialog showErweiMa = new Dialog(mContext);
        showErweiMa.setContentView(view);
        ImageView imageView = view.findViewById(R.id.dialog_erweima_iv);
        TextView textView = view.findViewById(R.id.dialog_erweima_tv);
        //"http://s.jiathis.com/qrcode.php?url=" + shareUrl
        new DownLoadAsy(imageView).execute(erwermaUrl);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission(new CheckPermListener() {
                    @Override
                    public void superPermission() {
                        LogUtils.w("permission", "BaseUploadPicActivity:读写权限已经获取");
                        String picName = "PGY收款人" + tvPayTypeAccountName.getText() + "的二维码";
                        FileUtils.saveBmp2Gallery(mContext, erweimaBitmap, picName);
                    }
                }, R.string.storage, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        });
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.8f;
        getWindow().setAttributes(lp);
        // 设置点击外围解散
        showErweiMa.setCanceledOnTouchOutside(true);
        if (!showErweiMa.isShowing()) {
            showErweiMa.show();
        }
    }


    /**
     * 显示二维码的对话框
     */
    private void showErweiMaDialog(final C2CEntrustDetails.PayInfo payInfo) {
        if (payInfo == null || TextUtils.isEmpty(payInfo.getImgurl())) {
            showToast("二维码为空");
            return;
        }
        View view = getLayoutInflater().inflate(R.layout.show_erweima_dialog, null);
        Dialog showErweiMa = new Dialog(mContext);
        showErweiMa.setContentView(view);
        ImageView imageView = view.findViewById(R.id.dialog_erweima_iv);
        TextView textView = view.findViewById(R.id.dialog_erweima_tv);
        //"http://s.jiathis.com/qrcode.php?url=" + shareUrl
        new DownLoadAsy(imageView).execute(payInfo.getImgurl());
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission(new CheckPermListener() {
                    @Override
                    public void superPermission() {
                        LogUtils.w("permission", "BaseUploadPicActivity:读写权限已经获取");
                        String picName = "PGY收款人" + payInfo.getName() + "的二维码";
                        FileUtils.saveBmp2Gallery(mContext, erweimaBitmap, picName);
                    }
                }, R.string.storage, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        });
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.8f;
        getWindow().setAttributes(lp);
        // 设置点击外围解散
        showErweiMa.setCanceledOnTouchOutside(true);
        if (!showErweiMa.isShowing()) {
            showErweiMa.show();
        }
    }

    private Bitmap erweimaBitmap = null;

    /**
     * 下载二维码图片到本地
     */
    private class DownLoadAsy extends AsyncTask<String, Void, Bitmap> {
        private ImageView imageview;

        DownLoadAsy(ImageView imageview) {
            this.imageview = imageview;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String url = params[0];
            try {
                erweimaBitmap = Glide.with(mContext).load(url).asBitmap().centerCrop().into(380, 380).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return erweimaBitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageview.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        /*停止倒计时*/
        if (downTimer != null) {
            downTimer.stopDown();
            downTimer = null;
        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
