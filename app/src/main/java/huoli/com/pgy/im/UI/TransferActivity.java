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
import huoli.com.pgy.im.SealUserInfoManager;
import huoli.com.pgy.im.db.Friend;
import huoli.com.pgy.im.message.TransferMeassage;
import huoli.com.pgy.im.server.response.GetSendRedPacketResponse;
import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

/**
 * 转账界面
 *
 * @author xuqingzhong
 */

public class TransferActivity extends BaseActivity {
    @BindView(R.id.activity_imTransfer_title)
    TitleView activityIMTransferTitle;
    @BindView(R.id.activity_imTransfer_accountType)
    TextAndNextItem activityIMTransferAccountType;
    @BindView(R.id.activity_imTransfer_coinType)
    TextAndNextItem activityIMTransferCoinType;
    @BindView(R.id.activity_imTransfer_availableBalance)
    TextView activityIMTransferAvailableBalance;
    @BindView(R.id.activity_imTransfer_availableBalance_coinName)
    TextView activityIMTransferAvailableBalanceCoinName;
    @BindView(R.id.activity_imTransfer_amount)
    NumberEditText activityIMTransferAmount;
    @BindView(R.id.activity_imTransfer_amount_coinName)
    TextView activityIMTransferAmountCoinName;
    @BindView(R.id.activity_imTransfer_message)
    EditText activityIMTransferMessage;
    @BindView(R.id.activity_imTransfer_price)
    TextView activityIMTransferPrice;
    @BindView(R.id.activity_imTransfer_submit)
    TextView activityIMTransferSubmit;
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
    private String currentCoinName;
    private String currentPriceOfCNY = "0.00";
    /**汇率*/
    private double exchangeRate = 0.0;
    /**对方用户的id即手机号*/
    private String targetId;
    private String content;

    @Override
    public int getContentViewId() {
        return R.layout.activity_im_transfer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        /*点击标题返回按钮*/
        activityIMTransferTitle.setBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransferActivity.this.finish();
            }
        });
        
        //activityIMTransferAmount.setDigits(8);
        activityIMTransferAmount.addTextChangedListener(new LimitPriceTradeWatcher());
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
        coinTypeList = getConfiguration().getTalkTransferCoin();
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
        activityIMTransferAccountType.setRightText(accountMap.get(accountType));
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
                activityIMTransferAvailableBalance.setText(availBalance);
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
                activityIMTransferAmount.setText("");
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
        activityIMTransferAmount.setText("");
        currentCoinName = getCoinName(coinType);
        activityIMTransferCoinType.setRightText(currentCoinName);
        activityIMTransferAvailableBalanceCoinName.setText(currentCoinName);
        activityIMTransferAmountCoinName.setText(currentCoinName);
        activityIMTransferAvailableBalanceCoinName.setText(currentCoinName);
        /*切换底部矿工费、可用余额、实际到账的币名*/
        getCoinAvailBalance();
    }

    /**
     * 根据不同账户切换界面
     */
    private void switchAccountFrameText(int accountType) {
        activityIMTransferAmount.setText("");
        activityIMTransferAccountType.setRightText(accountMap.get(accountType));
        getCoinAvailBalance();
    }

    @OnClick({R.id.activity_imTransfer_accountType, R.id.activity_imTransfer_coinType, R.id.activity_imTransfer_submit, R.id.activity_imTransfer_record})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            /*选择账户类型*/
            case R.id.activity_imTransfer_accountType:
                //showChooseAccountTypeDialog();
                break;
                /*选择充值币种*/
            case R.id.activity_imTransfer_coinType:
                KeyboardUtil.hideSoftKeyboard(this);
                List<String> coinNameList = new ArrayList<>();
                for (Integer coin : coinTypeList) {
                    coinNameList.add(getCoinName(coin));
                }
                mySpinnerList = new MyCenterSpinnerList(mContext, "选择币种", coinNameList, activityIMTransferCoinType);
                mySpinnerList.setMySpinnerListener(coinTypeItemListener);
                mySpinnerList.showUp();
                break;
                /*提交*/
            case R.id.activity_imTransfer_submit:
                if (!isLogin()) {
                    showToast(R.string.unlogin);
                    return;
                }
                submit();
                break;
                /*转账记录*/
            case R.id.activity_imTransfer_record:
                Intent intent2Record = new Intent(mContext, RedPackageRecordsActivity.class);
                intent2Record.putExtra("isRedPacket",false);
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
        mySpinnerList = new MyCenterSpinnerList(mContext, "选择账户", accountTypeNameList, activityIMTransferCoinType);
        mySpinnerList.setMySpinnerListener(accountTypeItemListener);
        mySpinnerList.showUp();
    }

    /**
     * 提交三方提现
     */
    private void submit() {
        /*判断输入是否有空*/
        String amountStr = activityIMTransferAmount.getText().toString().trim();
        if (TextUtils.isEmpty(amountStr)) {
            showToast("请输入数量");
            return;
        }
        sendAmount = MathUtils.string2Double(amountStr);
        if (sendAmount > MathUtils.string2Double(availBalance)) {
            showToast("可用余额不足");
            return;
        }
        content = activityIMTransferMessage.getText().toString().trim();
         /*弹出输入交易密码对话框*/
        showPayDialog(new getStringCallback() {
            @Override
            public void getString(String string) {
                imTransfer2Net(string);
            }
        });
    }
    /**
     * 转账
     */
    private void imTransfer2Net(String pwd) {
        showLoading(activityIMTransferSubmit);
        Map<String, Object> map = new HashMap<>();
        map.put("coinType", coinType);
        map.put("accountType", accountType);
        map.put("amount", sendAmount);
        map.put("note", content);
        map.put("phone", targetId);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", StaticDatas.SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        map.put("password", pwd);
        NetWorks.sendTransfer(Preferences.getAccessToken(), map, new getBeanCallback<GetSendRedPacketResponse>() {
            @Override
            public void onSuccess(GetSendRedPacketResponse response) {
                showToast("转账成功");
                LogUtils.w(TAG, "转账成功");
                sendTransferMessage(response.getId()+"");
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
                hideLoading();
            }
        });
    }

    /**发送自定义红包消息*/
    private void sendTransferMessage(String packetId) {
        if (TextUtils.isEmpty(content)){
            content = activityIMTransferMessage.getHint().toString().trim();
        }
        String targetName;
        Friend targetUser = SealUserInfoManager.getInstance().getFriendByID(targetId);
        if (targetUser == null){
            /*陌生人*/
            targetName = "他/她";
        }else if (TextUtils.isEmpty(targetUser.getName())){
            /*好友无名*/
            targetName = "好友";
        }else{
            targetName = targetUser.getName();
        }
        TransferMeassage redPacketMessage = TransferMeassage.obain(packetId,targetId,targetName, MathUtils.formatdoubleNumber(sendAmount),getCoinName(coinType),content,"");
        Message message = Message.obtain(targetId, Conversation.ConversationType.PRIVATE,redPacketMessage);
        RongIM.getInstance().sendMessage(message, "收到转账", "点击查看", new IRongCallback.ISendMessageCallback() {
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
        hideLoading();
        TransferActivity.this.finish();
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
            String inputAmount = activityIMTransferAmount.getText().toString().trim();
            if (!TextUtils.isEmpty(inputAmount)) {
                sendAmount = MathUtils.string2Double(inputAmount);
                if (sendAmount > MathUtils.string2Double(availBalance)){
                    activityIMTransferAmount.setText(availBalance);
                }
                currentPriceOfCNY = MathUtils.formatdoubleNumber(sendAmount*exchangeRate);
                activityIMTransferPrice.setText("¥"+currentPriceOfCNY);
            } else {
                sendAmount = 0.0;
                activityIMTransferPrice.setText("¥0.00");
            }
        }
    }
}
