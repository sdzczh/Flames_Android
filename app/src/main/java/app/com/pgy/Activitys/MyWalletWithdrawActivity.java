package app.com.pgy.Activitys;

import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import app.com.pgy.Interfaces.spinnerSingleChooseListener;
import app.com.pgy.Widgets.YubibaoCoinspinner;
import butterknife.BindView;
import butterknife.OnClick;
import app.com.pgy.Activitys.Base.ScannerQRCodeActivity;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Constants.StaticDatas;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Interfaces.getStringCallback;
import app.com.pgy.Models.Beans.Configuration;
import app.com.pgy.Models.Beans.EventBean.EventAssetsChange;
import app.com.pgy.Models.Beans.WithdrawBean;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Utils.LoginUtils;
import app.com.pgy.Utils.MathUtils;
import app.com.pgy.Utils.TimeUtils;
import app.com.pgy.Widgets.NumberEditText;

/**
 * Created by YX on 2018/7/12.
 */

public class MyWalletWithdrawActivity extends ScannerQRCodeActivity implements getStringCallback{
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.view_line)
    View viewLine;
    @BindView(R.id.tv_activity_mywallet_withdraw_avail)
    TextView tv_avail;
    @BindView(R.id.tv_activity_mywallet_withdraw_coinName1)
    TextView tv_coinName1;
    @BindView(R.id.edt_activity_mywallet_withdraw_toAddress)
    EditText edt_toAddress;
    @BindView(R.id.iv_activity_mywallet_withdraw_scan)
    ImageView iv_scan;
    @BindView(R.id.edt_activity_mywallet_withdraw_amount)
    NumberEditText edt_amount;
    @BindView(R.id.tv_activity_mywallet_withdraw_coinName2)
    TextView tv_coinName2;
    @BindView(R.id.tv_activity_mywallet_withdraw_chooseAll)
    TextView tv_chooseAll;
    @BindView(R.id.tv_activity_mywallet_withdraw_fee)
    TextView tv_fee;
    @BindView(R.id.tv_activity_mywallet_withdraw_coinName3)
    TextView tv_coinName3;
    @BindView(R.id.tv_activity_mywallet_withdraw_realAmount)
    TextView tv_realAmount;
    @BindView(R.id.tv_activity_mywallet_withdraw_coinName4)
    TextView tv_coinName4;
    @BindView(R.id.tv_activity_mywallet_withdraw_submit)
    TextView tv_submit;
    @BindView(R.id.tv_activity_mywallet_withdraw_desc)
    TextView tv_desc;
    @BindView(R.id.tv_activity_mywallet_transfer_coin)
    TextView tvActivityMywalletTransferCoin;
    //币种、账户
    private int coinType = -1;
    private int accountType = StaticDatas.ACCOUNT_GOODS;
    private Configuration.CoinInfo coinInfo;
    /**
     * 提现金额、矿工费、可用余额
     */
    private Double withdrawAmount = 0.0;
    private Double fee = 0.0;
    private String availBalance = "0.0";
    private String coinName;
    private String walletAddress;

    @Override
    public int getContentViewId() {
        return R.layout.activity_mywallet_withdraw;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    protected void initView(Bundle savedInstanceState) {
        tv_title.setText(coinName+"  提币");
        tvActivityMywalletTransferCoin.setText(coinName);
        /*添加扫描二维码回调监听*/
        setStringCallback(this);
        String notice = "温馨提示<br> 1.提币时支付的网络手续费为网络收取<br> 2.可提币金额≤账户可用余额-冻结金额";
        tv_desc.setText(Html.fromHtml(notice));
        edt_amount.addTextChangedListener(new LimitPriceTradeWatcher());
    }


    /**
     * 获取用户的可用余额和绑定的支付宝账号
     */
    @Override
    protected void initData() {
        coinType = getIntent().getIntExtra("coinType", -1);
        accountType = getIntent().getIntExtra("accountType", StaticDatas.ACCOUNT_GOODS);
        coinInfo = getCoinInfo(coinType);
        if (coinType == -1){
            showToast("没有币种信息");
            finish();
            return;
        }
        if (coinInfo == null){
            showToast("获取币种信息失败");
            finish();
            return;
        }
        LogUtils.e(TAG,"数量："+coinInfo.getWithdrawNum()+",小数："+coinInfo.getWithdrawScale());
        edt_amount.setHint("最小提币数量"+coinInfo.getWithdrawNum());
        edt_amount.setDigits(coinInfo.getWithdrawScale());
        /*切换币种名称*/
        switchCoinFrameText(coinType);
    }

    private void switchCoinFrameText(int coinType) {
        edt_amount.setText("");
        edt_toAddress.setText("");
        tv_realAmount.setText("0.00");
        coinName = getCoinName(coinType);
        tv_coinName1.setText(coinName);
        tv_coinName2.setText(coinName);
        tv_coinName3.setText(coinName);
        tv_coinName4.setText(coinName);
        updateView(null);
        getWithdrawBean();
    }

    @OnClick({R.id.iv_back,R.id.iv_activity_mywallet_withdraw_scan,R.id.tv_activity_mywallet_withdraw_chooseAll,
            R.id.tv_activity_mywallet_withdraw_submit})
    public void onViewClick(View view){
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_activity_mywallet_withdraw_scan:
                goScanner();
                break;
            case R.id.tv_activity_mywallet_withdraw_chooseAll:
                if (coinInfo != null){
                    edt_amount.setText(MathUtils.formatDoubleNumber(MathUtils.string2Double(availBalance),coinInfo.getWithdrawScale()));
                }else {
                    edt_amount.setText(TextUtils.isEmpty(availBalance) ? "0" : availBalance);
                }
                int index = edt_amount.getText().toString().trim().length();
                edt_amount.setSelection(index>0?index:0);
                break;
            case R.id.tv_activity_mywallet_withdraw_submit:
                if (LoginUtils.isLogin(this)){
                    submit();
                }
                break;
            case R.id.ll_activity_mywallet_transfer_coin:
                showSpinner();
                break;
        }
    }

    private void updateView(WithdrawBean withdrawBean){
        if (withdrawBean != null){
            availBalance = TextUtils.isEmpty(withdrawBean.getAvailBalance()) ? "0.00" : withdrawBean.getAvailBalance();
            tv_avail.setText(availBalance);
            tv_fee.setText(withdrawBean.getFee());
            tv_desc.setText(withdrawBean.getInfo());
            fee = MathUtils.string2Double(withdrawBean.getFee());
        }else {
            availBalance = "0.00";
            tv_avail.setText(availBalance);
            tv_fee.setText("0.00");
            tv_desc.setText("");
        }
    }

    /**
     * 提现页面信息
     */
    private void getWithdrawBean() {
        showLoading(null);
        Map<String, Object> map = new HashMap<>();
        map.put("accountType", accountType);
        map.put("coinType", coinType);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", StaticDatas.SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.getWithdrawBean(Preferences.getAccessToken(), map, new getBeanCallback<WithdrawBean>() {
            @Override
            public void onSuccess(WithdrawBean withdrawBean) {
                updateView(withdrawBean);
                hideLoading();
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
                updateView(null);
                /*网络错误*/
                hideLoading();
            }
        });
    }
    /**
     * 提交三方提现
     */
    private void submit() {
        /*判断输入是否有空*/
        walletAddress = edt_toAddress.getText().toString().trim();
        if (TextUtils.isEmpty(walletAddress)) {
            showToast("请输入提币地址");
            return;
        }
        String amountStr = edt_amount.getText().toString().trim();
        if (TextUtils.isEmpty(amountStr)) {
            showToast("请输入提币金额");
            return;
        }
        withdrawAmount = MathUtils.string2Double(amountStr);
        if (withdrawAmount > MathUtils.string2Double(availBalance)) {
            showToast("可用余额不足");
            return;
        }

        if (withdrawAmount < MathUtils.string2Double(coinInfo.getWithdrawNum())){
            showToast("最小提币数量"+coinInfo.getWithdrawNum());
            return;
        }
         /*弹出输入交易密码对话框*/
        showPayDialog(new getStringCallback() {
            @Override
            public void getString(String string) {
                submitWithdraw2Net(string);
            }
        });
    }

    /**
     * 去服务器提现
     */
    private void submitWithdraw2Net(String pwd) {
        showLoading(tv_submit);
        Map<String, Object> map = new HashMap<>();
        map.put("coinType", coinType);
        map.put("type", accountType);
        map.put("amount", withdrawAmount);
        map.put("accountNum", walletAddress);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", StaticDatas.SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        map.put("password", pwd);
        NetWorks.submitWithdraw(Preferences.getAccessToken(), map, new getBeanCallback() {
            @Override
            public void onSuccess(Object o) {
                showToast("提币申请已提交");
                LogUtils.w(TAG, "提币成功");
                hideLoading();
                EventBus.getDefault().post(new EventAssetsChange(accountType));
                MyWalletWithdrawActivity.this.finish();
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
                hideLoading();
            }
        });
    }

    @Override
    public void getString(String string) {
        edt_toAddress.setText(string);
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
            String inputAmount = edt_amount.getText().toString().trim();
            if (!TextUtils.isEmpty(inputAmount)) {
                withdrawAmount = MathUtils.string2Double(inputAmount);

                Double actualAmount = 0.00;
                if (withdrawAmount != null && fee >= 0) {
                    actualAmount = withdrawAmount - fee;
                }
                String actualAmountStr = MathUtils.formatDoubleNumber(actualAmount);
                tv_realAmount.setText(actualAmountStr);
            } else {
                tv_realAmount.setText("0.0");
                withdrawAmount = 0.0;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }

    private YubibaoCoinspinner coinspinner;
    private void showSpinner() {
        if (getRechAndWithCoinTypeList() == null || getRechAndWithCoinTypeList().size() <= 0) {
            return;
        }
        if (coinspinner == null) {
            coinspinner = new YubibaoCoinspinner(getApplicationContext(), getRechAndWithCoinTypeList(), new spinnerSingleChooseListener() {
                @Override
                public void onItemClickListener(int position) {
                    coinspinner.dismiss();
                    if (getRechAndWithCoinTypeList().get(position) == coinType) {
                        return;
                    }
                    coinType = getRechAndWithCoinTypeList().get(position);
                    coinInfo = getCoinInfo(coinType);
                    tvActivityMywalletTransferCoin.setText(coinInfo.getCoinname());
                    LogUtils.e(TAG,"数量："+coinInfo.getWithdrawNum()+",小数："+coinInfo.getWithdrawScale());
                    edt_amount.setHint("最小提币数量"+coinInfo.getWithdrawNum());
                    edt_amount.setDigits(coinInfo.getWithdrawScale());
                    /*切换币种名称*/
                    switchCoinFrameText(coinType);
                    tv_title.setText(coinName+"  提币");

                }
            });
        }
        if (!coinspinner.isShowing()) {
            coinspinner.showDown(viewLine);
        }
    }

}
