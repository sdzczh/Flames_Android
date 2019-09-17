package huoli.com.pgy.im.UI;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import huoli.com.pgy.Activitys.Base.BaseActivity;
import huoli.com.pgy.Constants.Preferences;
import huoli.com.pgy.Constants.StaticDatas;
import huoli.com.pgy.Interfaces.getBeanCallback;
import huoli.com.pgy.Interfaces.getStringCallback;
import huoli.com.pgy.Interfaces.spinnerSingleChooseListener;
import huoli.com.pgy.Models.Beans.CoinAvailbalance;
import huoli.com.pgy.NetUtils.NetWorks;
import huoli.com.pgy.R;
import huoli.com.pgy.Utils.KeyboardUtil;
import huoli.com.pgy.Utils.LogUtils;
import huoli.com.pgy.Utils.MathUtils;
import huoli.com.pgy.Utils.TimeUtils;
import huoli.com.pgy.Widgets.MyCenterSpinnerList;
import huoli.com.pgy.Widgets.NumberEditText;
import huoli.com.pgy.Widgets.TextAndNextItem;
import huoli.com.pgy.Widgets.TitleView;
import huoli.com.pgy.im.message.RedPacketMessage;
import huoli.com.pgy.im.server.response.GetSendRedPacketResponse;
import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

/**
 * 发红包界面
 *
 * @author xuqingzhong
 */

public class SendRedPackageActivity extends BaseActivity {
    @BindView(R.id.activity_sendRedPackage_title)
    TitleView activitySendRedPackageTitle;
    @BindView(R.id.activity_sendRedPackage_accountType)
    TextAndNextItem activitySendRedPackageAccountType;
    @BindView(R.id.activity_sendRedPackage_coinType)
    TextAndNextItem activitySendRedPackageCoinType;
    @BindView(R.id.activity_sendRedPackage_availableBalance)
    TextView activitySendRedPackageAvailableBalance;
    @BindView(R.id.activity_sendRedPackage_availableBalance_coinName)
    TextView activitySendRedPackageAvailableBalanceCoinName;
    @BindView(R.id.activity_sendRedPackage_amount)
    NumberEditText activitySendRedPackageAmount;
    @BindView(R.id.activity_sendRedPackage_amount_coinName)
    TextView activitySendRedPackageAmountCoinName;
    @BindView(R.id.activity_sendRedPackage_message)
    EditText activitySendRedPackageMessage;
    @BindView(R.id.activity_sendRedPackage_price)
    TextView activitySendRedPackagePrice;
    @BindView(R.id.activity_sendRedPackage_submit)
    TextView activitySendRedPackageSubmit;
    /**
     * 标题
     */

    private MyCenterSpinnerList mySpinnerList;
    /**
     * 转账、币种方式Item点击监听
     */
    private spinnerSingleChooseListener accountTypeItemListener, coinTypeItemListener;
    /**
     * 转账、币种数据列表
     */
    private Map<Integer, String> accountMap;
    private List<Integer> accountTypeList;
    private List<Integer> coinTypeList;
    /**
     * 输入的内容
     */
    private int coinType = -1;
    private int accountType = StaticDatas.ACCOUNT_GOODS;
    /**
     * 金额、可用余额
     */
    private Double sendAmount = 0.0;
    private String availBalance = "0.0";
    private String currentPriceOfCNY = "0.00";
    /**汇率*/
    private double exchangeRate = 0.0;
    /**对方用户的id即手机号*/
    private String targetId;
    private String content;

    @Override
    public int getContentViewId() {
        return R.layout.activity_im_send_red_package;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        /*点击标题返回按钮*/
        activitySendRedPackageTitle.setBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendRedPackageActivity.this.finish();
            }
        });
        
        activitySendRedPackageAmount.addTextChangedListener(new LimitPriceTradeWatcher());
        /*设置监听*/
        setSpinnerListener();
    }


    /**
     * 获取用户的可用余额和绑定的支付宝账号
     */
    @Override
    protected void initData() {
        targetId = getIntent().getStringExtra("targetId");
        /*获取提现币种列表*/
        coinTypeList = getConfiguration().getRedPacketCoin();
        if (coinTypeList == null) {
            coinTypeList = new ArrayList<>();
        }
        accountMap = getAccountMap();
        accountTypeList = getAccountTypeList();
        /*获取传递过来的货币类型和转账类型,默认为列表第一个*/
        int firstListCoinType = coinTypeList.size() > 0 ? coinTypeList.get(0) : 0;
        int getCoinType = getIntent().getIntExtra("coinType", firstListCoinType);
        coinType = coinTypeList.contains(getCoinType) ? getCoinType : firstListCoinType;
        accountType = getIntent().getIntExtra("accountType", StaticDatas.ACCOUNT_GOODS);
        /*切换币种名称*/
        switchCoinFrameText(coinType);
        activitySendRedPackageAccountType.setRightText(accountMap.get(accountType));
    }

    /**
     * 从服务器获取该币种的可用余额
     */
    private void getCoinAvailBalance() {
        showLoading(null);
        Map<String, Object> map = new HashMap<>();
        map.put("accountType", accountType);
        map.put("coinType", coinType);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", StaticDatas.SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.getCoinAvailbalance(Preferences.getAccessToken(), map, new getBeanCallback<CoinAvailbalance>() {
            @Override
            public void onSuccess(CoinAvailbalance availbalance) {
                exchangeRate = availbalance.getExchangeRate();
                availBalance = TextUtils.isEmpty(availbalance.getAvailBalance()) ? "0.0" : availbalance.getAvailBalance();
                activitySendRedPackageAvailableBalance.setText(availBalance);
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

    /**
     * 转账方式、币种的列表监听
     */
    private void setSpinnerListener() {
        accountTypeItemListener = new spinnerSingleChooseListener() {
            @Override
            public void onItemClickListener(int position) {
                //点击Item时的操作,存储选中的位置、更改文字
                if (accountType == accountTypeList.get(position)) {
                    return;
                }
                accountType = accountTypeList.get(position);
                switchAccountFrameText(accountType);
            }
        };
        coinTypeItemListener = new spinnerSingleChooseListener() {
            @Override
            public void onItemClickListener(int position) {
                //点击Item时的操作,存储选中的位置、更改文字
                if (coinType == coinTypeList.get(position)) {
                    return;
                }
                coinType = coinTypeList.get(position);
                switchCoinFrameText(coinType);
            }
        };
    }

    /**
     * 根据不同币种切换界面
     */
    private void switchCoinFrameText(int coinType) {
        activitySendRedPackageAmount.setText("");
        String currentCoinName = getCoinName(coinType);
        activitySendRedPackageCoinType.setRightText(currentCoinName);
        activitySendRedPackageAvailableBalanceCoinName.setText(currentCoinName);
        activitySendRedPackageAmountCoinName.setText(currentCoinName);
        activitySendRedPackageAvailableBalanceCoinName.setText(currentCoinName);
        /*切换底部矿工费、可用余额、实际到账的币名*/
        getCoinAvailBalance();
    }

    /**
     * 根据不同账户切换界面
     */
    private void switchAccountFrameText(int accountType) {
        activitySendRedPackageAmount.setText("");
        activitySendRedPackageAccountType.setRightText(accountMap.get(accountType));
        getCoinAvailBalance();
    }

    @OnClick({R.id.activity_sendRedPackage_accountType, R.id.activity_sendRedPackage_coinType, R.id.activity_sendRedPackage_submit, R.id.activity_sendRedPackage_record})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            /*选择账户类型*/
            case R.id.activity_sendRedPackage_accountType:
                //showChooseAccountTypeDialog();
                break;
                /*选择充值币种*/
            case R.id.activity_sendRedPackage_coinType:
                KeyboardUtil.hideSoftKeyboard(this);
                List<String> coinNameList = new ArrayList<>();
                for (Integer coin : coinTypeList) {
                    coinNameList.add(getCoinName(coin));
                }
                mySpinnerList = new MyCenterSpinnerList(mContext, "选择币种", coinNameList, activitySendRedPackageCoinType);
                mySpinnerList.setMySpinnerListener(coinTypeItemListener);
                mySpinnerList.showUp();
                break;
                /*提交*/
            case R.id.activity_sendRedPackage_submit:
                if (!isLogin()) {
                    showToast(R.string.unlogin);
                    return;
                }
                submit();
                break;
                /*红包记录*/
            case R.id.activity_sendRedPackage_record:
                Intent intent2Record = new Intent(mContext, RedPackageRecordsActivity.class);
                intent2Record.putExtra("isRedPacket",true);
                startActivity(intent2Record);
                break;
                default:break;
        }
    }

    /**弹出选择账户类型对话框*/
    private void showChooseAccountTypeDialog() {
        List<String> accountTypeNameList = new ArrayList<>();
        for (Integer accountType : accountTypeList) {
            accountTypeNameList.add(accountMap.get(accountType));
        }
        mySpinnerList = new MyCenterSpinnerList(mContext, "选择账户", accountTypeNameList, activitySendRedPackageCoinType);
        mySpinnerList.setMySpinnerListener(accountTypeItemListener);
        mySpinnerList.showUp();
    }

    /**
     * 提交三方提现
     */
    private void submit() {
        /*判断输入是否有空*/
        String amountStr = activitySendRedPackageAmount.getText().toString().trim();
        if (TextUtils.isEmpty(amountStr)) {
            showToast("请输入数量");
            return;
        }
        sendAmount = MathUtils.string2Double(amountStr);
        if (sendAmount > MathUtils.string2Double(availBalance)) {
            showToast("可用余额不足");
            return;
        }
        content = activitySendRedPackageMessage.getText().toString().trim();
        if (TextUtils.isEmpty(content)){
            content = activitySendRedPackageMessage.getHint().toString().trim();
        }
         /*弹出输入交易密码对话框*/
        showPayDialog(new getStringCallback() {
            @Override
            public void getString(String string) {
                sendRedPackageFromNet(string);
            }
        });
    }

    /**
     * 发红包
     */
    private void sendRedPackageFromNet(String pwd) {
        showLoading(activitySendRedPackageSubmit);
        Map<String, Object> map = new HashMap<>();
        map.put("coinType", coinType);
        map.put("accountType", accountType);
        map.put("amount", sendAmount);
        map.put("password", pwd);
        map.put("num", "1");
        map.put("note", content);
        map.put("phone", targetId);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", StaticDatas.SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.sendRedPacket(Preferences.getAccessToken(), map, new getBeanCallback<GetSendRedPacketResponse>() {
            @Override
            public void onSuccess(GetSendRedPacketResponse response) {
                if (response == null){
                    response = new GetSendRedPacketResponse();
                }
                showToast("发红包成功");
                LogUtils.w(TAG, "发红包成功");
                int packetId = response.getId();
                sendRedPackageMessage(packetId+"");
                hideLoading();
                SendRedPackageActivity.this.finish();
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
                hideLoading();
            }
        });
    }

    /**发送自定义红包消息*/
    private void sendRedPackageMessage(String packetId) {
        RedPacketMessage redPacketMessage = RedPacketMessage.obain(packetId,0,content, RongIM.getInstance().getCurrentUserId(), Preferences.getLocalUser().getName());
        Message message = Message.obtain(targetId, Conversation.ConversationType.PRIVATE,redPacketMessage);
//        RongIMClient.getInstance().sendMessage(conversationType,targetId,redPacketMessage,"收到一个红包","点击查看红",(IRongCallback.ISendMediaMessageCallback)null);
        RongIM.getInstance().sendMessage(message, "收到一个红包", "点击查看", new IRongCallback.ISendMessageCallback() {
            @Override
            public void onAttached(Message message) {

            }

            @Override
            public void onSuccess(Message message) {

            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    /**
     * 输入提现金额的输入监听
     */
    private class LimitPriceTradeWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String inputAmount = activitySendRedPackageAmount.getText().toString().trim();
            if (!TextUtils.isEmpty(inputAmount)) {
                sendAmount = MathUtils.string2Double(inputAmount);
                if (sendAmount > MathUtils.string2Double(availBalance)){
                    activitySendRedPackageAmount.setText(availBalance);
                }
                currentPriceOfCNY = MathUtils.formatdoubleNumber(sendAmount*exchangeRate);
                activitySendRedPackagePrice.setText("¥"+currentPriceOfCNY);
            } else {
                sendAmount = 0.0;
                activitySendRedPackagePrice.setText("¥0.00");
            }
        }
    }
}
