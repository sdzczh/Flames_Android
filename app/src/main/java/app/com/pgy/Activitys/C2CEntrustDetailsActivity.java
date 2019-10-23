package app.com.pgy.Activitys;

import android.Manifest;
import android.app.Dialog;
import android.content.ClipboardManager;
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

import app.com.pgy.Widgets.C2cTradeDialog;
import butterknife.BindView;
import butterknife.OnClick;
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
import app.com.pgy.Widgets.ExitDialog;
import app.com.pgy.Widgets.MyBottomSpinnerList;
import app.com.pgy.Widgets.RoundImageView;
import app.com.pgy.Widgets.TextAndTextItem;
import app.com.pgy.im.widget.DownTimer;
import io.rong.imkit.RongIM;

import static app.com.pgy.Constants.StaticDatas.ALIPAY;
import static app.com.pgy.Constants.StaticDatas.BANKCARD;
import static app.com.pgy.Constants.StaticDatas.BUSINESS;
import static app.com.pgy.Constants.StaticDatas.BUY;
import static app.com.pgy.Constants.StaticDatas.NORMAL;
import static app.com.pgy.Constants.StaticDatas.SALE;
import static app.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;
import static app.com.pgy.Constants.StaticDatas.WECHART;

/***
 * C2C委托详情页
 * @author xuqingzhong
 */

public class C2CEntrustDetailsActivity extends PermissionActivity {
    /**
     * 顶部
     */
    /*顶部订单状态*/
    @BindView(R.id.activity_ccEntrustDetails_top_orderState)
    TextView activityCcEntrustDetailsTopOrderState;
    /*顶部订单状态提示*/
    @BindView(R.id.activity_ccEntrustDetails_top_notice)
    TextView activityCcEntrustDetailsTopNotice;
    /*顶部拨打电话*/
    @BindView(R.id.activity_ccEntrustDetails_top_btn_call)
    TextView activityCcEntrustDetailsTopBtnCall;
    /*顶部联系对方*/
    @BindView(R.id.activity_ccEntrustDetails_top_btn_contact)
    TextView activityCcEntrustDetailsTopBtnContact;
    /**
     * 内容顶部
     */
    /*内容顶部交易总额标题*/
    @BindView(R.id.activity_ccEntrustDetails_contentTop_allAmountTitle)
    TextView activityCcEntrustDetailsContentTopAllAmountTitle;
    @BindView(R.id.activity_ccEntrustDetails_contentTop_priceTitle)
    TextView activityCcEntrustDetailsContentTopPriceTitle;
    /*内容顶部交易数量标题*/
    @BindView(R.id.activity_ccEntrustDetails_contentTop_numberTitle)
    TextView activityCcEntrustDetailsContentTopNumberTitle;
    /*内容顶部成交总额*/
    @BindView(R.id.activity_ccEntrustDetails_contentTop_allAmount)
    TextView activityCcEntrustDetailsContentTopAllAmount;
    /*内容顶部交易单价*/
    @BindView(R.id.activity_ccEntrustDetails_contentTop_price)
    TextView activityCcEntrustDetailsContentTopPrice;
    /*内容顶部交易数量*/
    @BindView(R.id.activity_ccEntrustDetails_contentTop_number)
    TextView activityCcEntrustDetailsContentTopNumber;
    /**
     * 内容支付信息
     */
    @BindView( R.id.activity_ccEntrustDetails_contentPay_payType_switch)
    LinearLayout activityCcEntrustDetailsContentPayPayTypeSwitch;

    /*内容支付信息界面*/
    @BindView(R.id.activity_ccEntrustDetails_contentPay_frame)
    LinearLayout activityCcEntrustDetailsContentPayFrame;
    /*内容支付信息支付方式图标*/
    @BindView(R.id.activity_ccEntrustDetails_contentPay_payType_icon)
    ImageView activityCcEntrustDetailsContentPayPayTypeIcon;
    /*内容支付信息支付方式文本*/
    @BindView(R.id.activity_ccEntrustDetails_contentPay_payType_txt)
    TextView activityCcEntrustDetailsContentPayPayTypeTxt;
    /*内容支付信息收款人*/
    @BindView(R.id.activity_ccEntrustDetails_contentPay_payerName)
    TextAndTextItem activityCcEntrustDetailsContentPayPayerName;
    /*内容支付信息账号*/
    @BindView(R.id.activity_ccEntrustDetails_contentPay_payAccount)
    TextAndTextItem activityCcEntrustDetailsContentPayPayAccount;
    /*内容支付信息收款二维码*/
    @BindView(R.id.activity_ccEntrustDetails_contentPay_payErweima)
    TextAndTextItem activityCcEntrustDetailsContentPayPayErweima;
    /**
     * 内容订单信息
     */
    /*内容订单信息界面*/
    @BindView(R.id.activity_ccEntrustDetails_contentOrder_frame)
    LinearLayout activityCcEntrustDetailsContentOrderFrame;
    /*内容订单信息卖家或卖家名字*/
    @BindView(R.id.activity_ccEntrustDetails_contentOrder_userName)
    TextAndTextItem activityCcEntrustDetailsContentOrderUserName;
    /*内容订单信息单价*/
    @BindView(R.id.activity_ccEntrustDetails_contentOrder_perPrice)
    TextAndTextItem activityCcEntrustDetailsContentOrderPerPrice;
    /*内容订单信息数量*/
    @BindView(R.id.activity_ccEntrustDetails_contentOrder_number)
    TextAndTextItem activityCcEntrustDetailsContentOrderNumber;
    /*内容订单信息下单时间*/
    @BindView(R.id.activity_ccEntrustDetails_contentOrder_time)
    TextAndTextItem activityCcEntrustDetailsContentOrderTime;
    /*内容订单信息订单号*/
    @BindView(R.id.activity_ccEntrustDetails_contentOrder_numId)
    TextAndTextItem activityCcEntrustDetailsContentOrderNumId;
    /*内容参考号*/
    @BindView(R.id.activity_ccEntrustDetails_content_referenceId)
    TextAndTextItem activityCcEntrustDetailsContentReferenceId;
    /**
     * 内容底部
     */
    /*内容底部蓝色提示*/
    @BindView(R.id.activity_ccEntrustDetails_contentBottomNotice_blue)
    TextView activityCcEntrustDetailsContentBottomNoticeBlue;
    /**
     * 内容底部按钮界面
     */
    /*内容底部按钮申诉*/
    @BindView(R.id.activity_ccEntrustDetails_contentBottomBtn_complaint)
    TextView activityCcEntrustDetailsContentBottomBtnComplaint;
    /*内容底部按钮取消订单*/
    @BindView(R.id.activity_ccEntrustDetails_contentBottomBtn_cancelOrder)
    TextView activityCcEntrustDetailsContentBottomBtnCancelOrder;
    /*内容底部按钮我已支付*/
    @BindView(R.id.activity_ccEntrustDetails_contentBottomBtn_payed)
    TextView activityCcEntrustDetailsContentBottomBtnPayed;
    /*内容底部按钮确认收款*/
    @BindView(R.id.activity_ccEntrustDetails_contentBottomBtn_receipted)
    TextView activityCcEntrustDetailsContentBottomBtnReceipted;
    /*内容底部黄色提示*/
    @BindView(R.id.activity_ccEntrustDetails_contentBottomNotice_orange)
    TextView activityCcEntrustDetailsContentBottomNoticeOrange;

    /**
     * 底部
     */
    /*底部联系对方界面*/
    @BindView(R.id.activity_ccEntrustDetails_bottomContact_frame)
    LinearLayout activityCcEntrustDetailsBottomContactFrame;
    /*底部联系对方头像*/
    @BindView(R.id.activity_ccEntrustDetails_bottomContact_icon)
    RoundImageView activityCcEntrustDetailsBottomContactIcon;
    /*底部联系对方昵称*/
    @BindView(R.id.activity_ccEntrustDetails_bottomContact_nikeName)
    TextView activityCcEntrustDetailsBottomContactNikeName;
    /*底部交易提醒界面*/
    @BindView(R.id.activity_ccEntrustDetails_bottomNotice_frame)
    LinearLayout activityCcEntrustDetailsBottomNoticeFrame;
    /*底部交易提醒内容*/
    @BindView(R.id.activity_ccEntrustDetails_bottomNotice_notice)
    TextView activityCcEntrustDetailsBottomNoticeNotice;
    @BindView(R.id.activity_ccEntrustDetails_contentTop_priceBg)
    LinearLayout activityCcEntrustDetailsContentTopPriceBg;
    @BindView(R.id.activity_ccEntrustDetails_contentTop_numberBg)
    LinearLayout activityCcEntrustDetailsContentTopNumberBg;
    private int entrustId;
    private int normalOrBusiness;
    private MyBottomSpinnerList mySpinnerList;
    /**
     * 支付方式Item点击监听
     */
    private spinnerSingleChooseListener payTypeItemListener;
    private List<Integer> payTypes;
    private int currentPayType;

    private Map<Integer, C2CEntrustDetails.PayInfo> payInfo;
    private String bottomOrderNotice;
    private String erwermaUrl;
    /**
     * 卖家姓名和联系方式
     */
    private String userNikeName, userPhone;
    private DownTimer downTimer;


    @Override
    public int getContentViewId() {
        return R.layout.activity_c2c_entrust_details;
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

    /**
     * 获取详情，初始化界面
     */
    @Override
    protected void initView(Bundle savedInstanceState) {
        /*设置监听*/
        setSpinnerListener();
        /*复制支付宝、微信、银行账号*/
        activityCcEntrustDetailsContentPayPayAccount.setCopyClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String accountNumber = activityCcEntrustDetailsContentPayPayAccount.getRightText();
                copyMessage(accountNumber);
            }
        });
        /*弹出二维码窗口*/
        activityCcEntrustDetailsContentPayPayErweima.setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showErweiMaDialog();
            }
        });
        /*复制订单号*/
        activityCcEntrustDetailsContentOrderNumId.setCopyClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String orderId = activityCcEntrustDetailsContentOrderNumId.getRightText();
                copyMessage(orderId);
            }
        });
        /*订单号解释*/
        activityCcEntrustDetailsContentReferenceId.setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReferenceQuestionDialog();
            }
        });
        /*复制参考号*/
        activityCcEntrustDetailsContentReferenceId.setCopyClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String referenceId = activityCcEntrustDetailsContentReferenceId.getRightText();
                copyMessage(referenceId);
            }
        });

    }

    /**
     * 币种的列表监听
     */
    private void setSpinnerListener() {
        payTypeItemListener = new spinnerSingleChooseListener() {
            @Override
            public void onItemClickListener(int position) {
                //点击Item时的操作,存储选中的位置、更改文字
                if (currentPayType == payTypes.get(position)) {
                    return;
                }
                currentPayType = payTypes.get(position);
                switchPayTypeFrame(currentPayType, payInfo);
            }
        };
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
        activityCcEntrustDetailsTopBtnCall.setVisibility(View.GONE);
        activityCcEntrustDetailsContentPayFrame.setVisibility(View.GONE);
        activityCcEntrustDetailsContentPayPayTypeSwitch.setVisibility(View.GONE);
        activityCcEntrustDetailsContentOrderFrame.setVisibility(View.GONE);
        activityCcEntrustDetailsContentBottomNoticeBlue.setVisibility(View.GONE);
        activityCcEntrustDetailsContentBottomBtnCancelOrder.setVisibility(View.GONE);
        activityCcEntrustDetailsContentBottomBtnComplaint.setVisibility(View.GONE);
        activityCcEntrustDetailsContentBottomBtnReceipted.setVisibility(View.GONE);
        activityCcEntrustDetailsContentBottomBtnPayed.setVisibility(View.GONE);
        activityCcEntrustDetailsContentBottomNoticeOrange.setVisibility(View.GONE);
        activityCcEntrustDetailsBottomContactFrame.setVisibility(View.GONE);
        activityCcEntrustDetailsBottomNoticeFrame.setVisibility(View.GONE);
        activityCcEntrustDetailsContentTopNumberBg.setVisibility(View.VISIBLE);
        activityCcEntrustDetailsContentTopPriceBg.setVisibility(View.VISIBLE);
    }

    /**
     * 根据买卖和订单状态切换界面
     */
    private void initDetailViews(C2CEntrustDetails details) {
        resetDefaultFrame();
        bottomOrderNotice = "1、您的汇款将直接进入卖家账户，交易过程中卖家出售的数字资产由平台托管保护。" +
                "<br>" +
                "2、请在规定时间内完成付款，并务必点击“我已付款”，卖家确认收款后，系统会将数字资产划转到您的账户。" +
                "<br>" +
                "3、如果买家当日取消订单达3次，将会被限制当日的买入功能。";
        activityCcEntrustDetailsBottomNoticeNotice.setText(Html.fromHtml(bottomOrderNotice));
        if (downTimer == null) {
            downTimer = new DownTimer();
        }
        userPhone = details.getOtherPhone();
        userNikeName = details.getOtherNickName();
        String coinName = getCoinName(details.getCoinType());
        /*内容顶部交易单价、数量、成交量*/
        activityCcEntrustDetailsContentTopAllAmount.setText("¥" + details.getTotal());
        activityCcEntrustDetailsContentTopPrice.setText(details.getPrice());
        activityCcEntrustDetailsContentTopNumberTitle.setText("交易数量(" + coinName + ")");
        activityCcEntrustDetailsContentTopNumber.setText(details.getAmount());
        /*内容订单信息*/
        activityCcEntrustDetailsContentOrderUserName.setRightText(details.getOtherNickName());
        activityCcEntrustDetailsContentOrderPerPrice.setRightText(details.getPrice());
        activityCcEntrustDetailsContentOrderNumber.setRightText(details.getAmount() + coinName);
        activityCcEntrustDetailsContentOrderTime.setRightText(details.getCreateTime());
        activityCcEntrustDetailsContentOrderNumId.setRightText(details.getOrderNum());
        activityCcEntrustDetailsContentReferenceId.setRightText(details.getReferNum());
        payInfo = details.getPayInfo();
        payTypes = new ArrayList<>();
        if (payInfo != null) {
            /*若支付信息不为空，则获取支付类型列表*/
            payTypes.addAll(payInfo.keySet());
        }
        /*获取默认的支付类型0支付宝 1微信 2银行卡，若没有则设为支付宝支付*/
        currentPayType = payTypes.size() > 0 ? payTypes.get(0) : ALIPAY;
        /*交易类型 0买入 1卖出*/
        int buyOrSale = details.getOrderType();
        /*订单状态，0代付款 1待确认 2冻结 3已完成 4已取消 5超时取消*/
        int state = details.getState();
        switch (state) {
            /*代付款状态*/
            case 0:
                /*显示支付信息、底部联系对方、底部交易提醒*/
                activityCcEntrustDetailsContentPayPayTypeSwitch.setVisibility(View.VISIBLE);
                activityCcEntrustDetailsContentPayFrame.setVisibility(View.VISIBLE);
                activityCcEntrustDetailsBottomContactFrame.setVisibility(View.VISIBLE);
                ImageLoaderUtils.displayCircle(mContext, activityCcEntrustDetailsBottomContactIcon, details.getOtherHeadPath());
                activityCcEntrustDetailsBottomContactNikeName.setText(userNikeName);
                activityCcEntrustDetailsBottomNoticeFrame.setVisibility(View.VISIBLE);
                switchPayTypeFrame(currentPayType, payInfo);
                switch (buyOrSale) {
                    default:
                    case BUY:
                        activityCcEntrustDetailsTopOrderState.setText("请付款");
                        activityCcEntrustDetailsTopNotice.setText("您已成功下单，请及时付款");
                        activityCcEntrustDetailsContentTopAllAmountTitle.setText("请向以下账户付款");
                        /*显示蓝色信息、取消订单我已支付按钮、黄色按钮*/
                        activityCcEntrustDetailsContentBottomNoticeBlue.setVisibility(View.VISIBLE);
                        activityCcEntrustDetailsContentBottomNoticeBlue.setText("如您已向卖家转账付款，请务必点击右下角“我已付款”按钮，否则有可能造成资金损失。");
                        activityCcEntrustDetailsContentBottomBtnCancelOrder.setVisibility(View.VISIBLE);
                        activityCcEntrustDetailsContentBottomBtnPayed.setVisibility(View.VISIBLE);
                        activityCcEntrustDetailsContentBottomNoticeOrange.setVisibility(View.VISIBLE);
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
                                String countDownStr = currentTime + "内未付款，订单将自动取消，打款请备注参考号";
                                activityCcEntrustDetailsContentBottomNoticeOrange.setText(countDownStr);
                            }

                            @Override
                            public void onFinish() {
                                /*倒计时完成，刷新页面*/
                                getEntrustDetails(true, entrustId);
                            }
                        });

                        break;
                    case SALE:
                        /*显示取消订单*/
                        activityCcEntrustDetailsTopOrderState.setText("待付款");
                        activityCcEntrustDetailsTopNotice.setText("您已下单等待买家付款");
                        activityCcEntrustDetailsContentTopAllAmountTitle.setText("交易金额");
                        activityCcEntrustDetailsContentBottomNoticeBlue.setVisibility(View.GONE);
                        activityCcEntrustDetailsContentBottomBtnCancelOrder.setVisibility(View.GONE);
                        break;
                }
                break;
            /*待确认状态*/
            case 1:
                activityCcEntrustDetailsTopOrderState.setText("待确认");
                /*显示拨打电话、订单信息、黄色提示信息、交易提醒*/
                activityCcEntrustDetailsTopBtnCall.setVisibility(View.VISIBLE);
                activityCcEntrustDetailsContentOrderFrame.setVisibility(View.VISIBLE);
                activityCcEntrustDetailsContentOrderUserName.setLeftText(buyOrSale == BUY ? "卖家" : "买家");
                activityCcEntrustDetailsContentOrderPerPrice.setVisibility(View.GONE);
                activityCcEntrustDetailsContentOrderNumber.setVisibility(View.GONE);
                activityCcEntrustDetailsContentBottomNoticeOrange.setVisibility(View.VISIBLE);
                activityCcEntrustDetailsBottomNoticeFrame.setVisibility(View.VISIBLE);
                switch (buyOrSale) {
                    default:
                    case BUY:
                        activityCcEntrustDetailsTopNotice.setText("已付款，等待卖家确认并放币");
                        activityCcEntrustDetailsContentTopAllAmountTitle.setText("已付款");
                        activityCcEntrustDetailsContentBottomNoticeOrange.setText(details.getInactiveTime() + "自动完成订单");
                        break;
                    case SALE:
                        activityCcEntrustDetailsTopNotice.setText("买家已付款待确认收款");
                        activityCcEntrustDetailsContentTopAllAmountTitle.setText("已收到买家付款");
                        activityCcEntrustDetailsContentBottomBtnComplaint.setVisibility(View.VISIBLE);
                        activityCcEntrustDetailsContentBottomBtnReceipted.setVisibility(View.VISIBLE);
                        activityCcEntrustDetailsContentBottomNoticeOrange.setText("请在" + details.getInactiveTime() + "前完成确认，超时自动确认");
                        bottomOrderNotice = "1、交易过程中卖家出售的数字资产由平台进行托管，确认前请确保已经收到买家打款，一经确认数字资产将自动转入买家账户。" +
                                "<br>2、请在规定时间内完成确认，如超时系统将自动放币。";
                        activityCcEntrustDetailsBottomNoticeNotice.setText(Html.fromHtml(bottomOrderNotice));
                        break;
                }
                break;
            /*冻结状态,申诉中*/
            case 2:
                /*显示支付信息、底部信息*/
                activityCcEntrustDetailsTopOrderState.setText("申诉中");
                activityCcEntrustDetailsTopNotice.setText("卖家已申诉客服处理中");
                activityCcEntrustDetailsContentTopAllAmountTitle.setText("交易总额");
                /*显示订单信息*/
                activityCcEntrustDetailsContentOrderFrame.setVisibility(View.VISIBLE);
                activityCcEntrustDetailsContentOrderUserName.setLeftText(buyOrSale == BUY ? "卖家" : "买家");
                break;
            /*已完成状态*/
            case 3:
                activityCcEntrustDetailsTopOrderState.setText("已完成");
                /*显示订单信息*/
                activityCcEntrustDetailsContentOrderFrame.setVisibility(View.VISIBLE);
                activityCcEntrustDetailsContentOrderUserName.setLeftText(buyOrSale == BUY ? "卖家" : "买家");
                switch (buyOrSale) {
                    default:
                    case BUY:
                        activityCcEntrustDetailsTopNotice.setText("卖家已确认收款交易完成");
                        activityCcEntrustDetailsContentTopAllAmountTitle.setText("您已收到(" + coinName + ")");
                        activityCcEntrustDetailsContentTopAllAmount.setText(details.getAmount());
                        activityCcEntrustDetailsContentTopNumberTitle.setText("交易总额");
                        activityCcEntrustDetailsContentTopNumber.setText("¥" + details.getTotal());
                        break;
                    case SALE:
                        activityCcEntrustDetailsTopNotice.setText("您已确认收款交易完成");
                        activityCcEntrustDetailsContentTopAllAmountTitle.setText("您已收到");
                        break;
                }
                break;
            /*已取消状态*/
            case 4:
                /*显示支付信息*/
                activityCcEntrustDetailsTopOrderState.setText("已取消");
                activityCcEntrustDetailsTopNotice.setText("订单已取消，无法查看支付信息");
                activityCcEntrustDetailsContentTopAllAmountTitle.setText("交易总额");
                activityCcEntrustDetailsContentTopPriceTitle.setVisibility(View.INVISIBLE);
                activityCcEntrustDetailsContentTopPrice.setVisibility(View.INVISIBLE);
                activityCcEntrustDetailsContentTopNumberTitle.setVisibility(View.INVISIBLE);
                activityCcEntrustDetailsContentTopNumber.setVisibility(View.INVISIBLE);
                /*显示订单信息*/
                activityCcEntrustDetailsContentOrderFrame.setVisibility(View.VISIBLE);
                activityCcEntrustDetailsContentOrderUserName.setLeftText(buyOrSale == BUY ? "卖家" : "买家");
                activityCcEntrustDetailsContentTopNumberBg.setVisibility(View.GONE);
                activityCcEntrustDetailsContentTopPriceBg.setVisibility(View.GONE);
                break;
            /*超时取消状态*/
            case 5:
                activityCcEntrustDetailsTopOrderState.setText("超时取消");
                activityCcEntrustDetailsTopNotice.setText("订单已取消，无法查看支付信息");
                activityCcEntrustDetailsContentTopAllAmountTitle.setText("交易总额");
                activityCcEntrustDetailsContentTopPriceTitle.setVisibility(View.INVISIBLE);
                activityCcEntrustDetailsContentTopPrice.setVisibility(View.INVISIBLE);
                activityCcEntrustDetailsContentTopNumberTitle.setVisibility(View.INVISIBLE);
                activityCcEntrustDetailsContentTopNumber.setVisibility(View.INVISIBLE);
                /*显示订单信息*/
                activityCcEntrustDetailsContentOrderFrame.setVisibility(View.VISIBLE);
                activityCcEntrustDetailsContentOrderUserName.setLeftText(buyOrSale == BUY ? "卖家" : "买家");
                activityCcEntrustDetailsContentTopNumberBg.setVisibility(View.GONE);
                activityCcEntrustDetailsContentTopPriceBg.setVisibility(View.GONE);
                break;
            default:
                break;
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
                activityCcEntrustDetailsContentPayPayTypeIcon.setImageResource(R.mipmap.pay_ali);
                activityCcEntrustDetailsContentPayPayTypeTxt.setText("支付宝支付");
                activityCcEntrustDetailsContentPayPayAccount.setLeftText("支付宝账号");
                activityCcEntrustDetailsContentPayPayErweima.setLeftText("收款二维码");
                activityCcEntrustDetailsContentPayPayErweima.setRightImageVisible(View.VISIBLE);
                erwermaUrl = currentPayInfo.getImgurl();
                break;
            case WECHART:
                activityCcEntrustDetailsContentPayPayTypeIcon.setImageResource(R.mipmap.pay_wx);
                activityCcEntrustDetailsContentPayPayTypeTxt.setText("微信支付");
                activityCcEntrustDetailsContentPayPayAccount.setLeftText("微信账号");
                activityCcEntrustDetailsContentPayPayErweima.setLeftText("收款二维码");
                activityCcEntrustDetailsContentPayPayErweima.setRightImageVisible(View.VISIBLE);
                erwermaUrl = currentPayInfo.getImgurl();
                break;
            case BANKCARD:
                activityCcEntrustDetailsContentPayPayTypeIcon.setImageResource(R.mipmap.pay_card);
                activityCcEntrustDetailsContentPayPayTypeTxt.setText("银行卡支付");
                activityCcEntrustDetailsContentPayPayAccount.setLeftText("银行卡号");
                activityCcEntrustDetailsContentPayPayErweima.setLeftText("银行卡信息");
                activityCcEntrustDetailsContentPayPayErweima.setRightImageVisible(View.GONE);
                activityCcEntrustDetailsContentPayPayErweima.setRightText(currentPayInfo.getBankName() + "-" + currentPayInfo.getBranchName());
                break;
            default:
                break;
        }
        activityCcEntrustDetailsContentPayPayerName.setRightText(currentPayInfo.getName());
        activityCcEntrustDetailsContentPayPayAccount.setRightText(currentPayInfo.getAccount());
    }

    private String getPayName(int payType) {
        String payName;
        switch (payType) {
            case ALIPAY:
                payName = "支付宝支付";
                break;
            case WECHART:
                payName = "微信支付";
                break;
            case BANKCARD:
                payName = "银行支付";
                break;
            default:
                payName = "支付方式";
        }
        return payName;
    }

    @OnClick({R.id.activity_ccEntrustDetails_back, R.id.activity_ccEntrustDetails_top_btn_call, R.id.activity_ccEntrustDetails_top_btn_contact, R.id.activity_ccEntrustDetails_contentPay_payType_switch, R.id.activity_ccEntrustDetails_contentBottomBtn_complaint, R.id.activity_ccEntrustDetails_contentBottomBtn_cancelOrder, R.id.activity_ccEntrustDetails_contentBottomBtn_payed, R.id.activity_ccEntrustDetails_contentBottomBtn_receipted, R.id.activity_ccEntrustDetails_bottomContact_frame})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            /*返回*/
            case R.id.activity_ccEntrustDetails_back:
                C2CEntrustDetailsActivity.this.finish();
                break;
            /*拨打电话*/
            case R.id.activity_ccEntrustDetails_top_btn_call:
                call(userPhone);
                break;
            /*联系对方*/
            case R.id.activity_ccEntrustDetails_top_btn_contact:
                startChat();
                break;
            /*切换支付方式*/
            case R.id.activity_ccEntrustDetails_contentPay_payType_switch:
                List<String> payNameList = new ArrayList<>();
                if (payTypes == null || payTypes.size() <= 0) {
                    return;
                }
                for (Integer coin : payTypes) {
                    payNameList.add(getPayName(coin));
                }
                mySpinnerList = new MyBottomSpinnerList(mContext, payNameList);
                mySpinnerList.setMySpinnerListener(payTypeItemListener);
                mySpinnerList.showUp(activityCcEntrustDetailsContentPayPayTypeSwitch);
                break;
            /*申诉*/
            case R.id.activity_ccEntrustDetails_contentBottomBtn_complaint:
                Intent intent = new Intent(mContext, C2CEntrustComplaintActivity.class);
                intent.putExtra("orderId", entrustId);
                startActivity(intent);
                break;
            /*取消订单*/
            case R.id.activity_ccEntrustDetails_contentBottomBtn_cancelOrder:
                showCancelOrderDialog();
                break;
            /*我已支付*/
            case R.id.activity_ccEntrustDetails_contentBottomBtn_payed:
                showConfirmPayDialog();
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
            /*查看对方详情*/
            case R.id.activity_ccEntrustDetails_bottomContact_frame:
                Intent intent2Business = new Intent(mContext, C2CPersonalBusinessActivity.class);
                intent2Business.putExtra("phone", userPhone);
                startActivity(intent2Business);
                break;
            default:
                break;
        }
    }

    /**
     * 开始聊天
     */
    public void startChat() {
        if (!isLogin()) {
            showToast(R.string.unlogin);
            return;
        }
        try {
            if (Preferences.getLocalUser().getPhone().equals(userPhone)) {
                showToast("不能与自己聊天");
                return;
            }
            RongIM.getInstance().startPrivateChat(mContext, userPhone, userNikeName);
        } catch (Exception e) {
            e.printStackTrace();
            showToast("系统异常");
        }
    }

    /**
     * 确认支付提示对话框
     */
    private void showConfirmPayDialog() {
        final ExitDialog.Builder builder = new ExitDialog.Builder(mContext);
        builder.setTitle("支付确认");
        builder.setMessage("确认已经完成付款了吗？ 若恶意提交会造成账号封禁。");
        builder.setCancelable(true);

        builder.setPositiveButton("确定",
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
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
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
     * 弹出参考号解释
     */
    private void showReferenceQuestionDialog() {
        final ExitDialog.Builder builder = new ExitDialog.Builder(mContext);
        builder.setTitle("请在付款信息中备注付款参考号");
        builder.setCancelable(true);

        builder.setPositiveButton("知道了",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    /**
     * 复制到剪切板
     */
    private void copyMessage(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        ClipboardManager cms = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        cms.setText(str);
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
        final ExitDialog.Builder builder = new ExitDialog.Builder(mContext);
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
        if (!TextUtils.isEmpty(count)) {
            builder.setTitle("确认取消订单");
        }
        String content = "1、如已向卖家付款，千万不要取消订单。<br>" +
                "2、每日取消" + count + "笔交易会限制当日买入功能。";
        builder.setMessage(content);
        builder.setCancelable(true);
        builder.setPositiveButton("取消订单",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancelOrder();
                        dialog.dismiss();
                    }
                });
        builder.setNegativeButton("暂不取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();

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
                        String picName = "COIN收款人" + activityCcEntrustDetailsContentPayPayerName.getRightText() + "的二维码";
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
