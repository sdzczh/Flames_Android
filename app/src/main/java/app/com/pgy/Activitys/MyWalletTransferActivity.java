package app.com.pgy.Activitys;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import app.com.pgy.Activitys.Base.BaseActivity;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Constants.StaticDatas;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Interfaces.getStringCallback;
import app.com.pgy.Models.Beans.CoinAvailbalance;
import app.com.pgy.Models.Beans.Configuration;
import app.com.pgy.Models.Beans.EventBean.EventAssetsChange;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Utils.MathUtils;
import app.com.pgy.Utils.TimeUtils;
import app.com.pgy.Widgets.NumberEditText;

/**
 * Created by YX on 2018/7/12.
 * 我的钱包资产划转
 */

public class MyWalletTransferActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_activity_mywallet_transfer_from)
    TextView tv_from;
    @BindView(R.id.tv_activity_mywallet_transfer_to)
    TextView tv_to;
    @BindView(R.id.iv_activity_mywallet_transfer_transferAccount)
    ImageView iv_transferAccount;
    @BindView(R.id.edt_activity_mywallet_transfer_amount)
    NumberEditText edt_amount;
    @BindView(R.id.tv_activity_mywallet_transfer_coinName)
    TextView tv_coinName;
    @BindView(R.id.tv_activity_mywallet_transfer_chooseAll)
    TextView tv_chooseAll;
    @BindView(R.id.tv_activity_mywallet_transfer_avail)
    TextView tv_avail;
    @BindView(R.id.tv_activity_mywallet_transfer_coinName1)
    TextView tv_coinName1;
    @BindView(R.id.tv_activity_mywallet_transfer_submit)
    TextView tv_submit;
    @BindView(R.id.iv_activity_mywallet_transfer_from_icon)
    ImageView iv_from;
    @BindView(R.id.iv_activity_mywallet_transfer_to_icon)
    ImageView iv_to;
    /**
     * 是否从c2c到现货，为true时表示从C2C到现货，为false时表示从现货到C2C
     */
    private boolean isFromC2c;
    private int currentAccount;
    private int coinType;
    private Configuration.CoinInfo coinInfo;
    private String availBalance;
    private Double amount;

    @Override
    public int getContentViewId() {
        return R.layout.activity_mywallet_transfer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        /*从币种详情获取当前账户*/
        currentAccount = getIntent().getIntExtra("accountType", StaticDatas.ACCOUNT_GOODS);
        coinType = getIntent().getIntExtra("coinType", -1);
        if (coinType == -1) {
            showToast("没有币种信息");
            finish();
            return;
        }
        isFromC2c = currentAccount != StaticDatas.ACCOUNT_GOODS;
        coinInfo = getCoinInfo(coinType);
        edt_amount.setDigits(coinInfo.getCalculScale());
        /*初始化界面*/
        switchTransferType(isFromC2c);
        tv_coinName.setText(coinInfo.getCoinname());
        tv_coinName1.setText(coinInfo.getCoinname());
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        tv_title.setText(coinInfo.getCoinname() + "  划转");
    }

    /**
     * 转账方式切换
     */
    private void switchTransferType(boolean isFromC2c) {
        String from, to;
        if (isFromC2c) {
            currentAccount = StaticDatas.ACCOUNT_C2C;
            from = "法币账户";
            to = "币币账户";
            iv_from.setImageResource(R.mipmap.fabi);
            iv_to.setImageResource(R.mipmap.bibi);
        } else {
            currentAccount = StaticDatas.ACCOUNT_GOODS;
            from = "币币账户";
            to = "法币账户";
            iv_to.setImageResource(R.mipmap.fabi);
            iv_from.setImageResource(R.mipmap.bibi);
        }
        tv_from.setText(from);
        tv_to.setText(to);
        edt_amount.setText("");
        getCoinAvailBalance();
    }

    @OnClick({R.id.iv_back, R.id.iv_activity_mywallet_transfer_transferAccount, R.id.tv_activity_mywallet_transfer_chooseAll,
            R.id.tv_activity_mywallet_transfer_submit})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_activity_mywallet_transfer_transferAccount:
                isFromC2c = !isFromC2c;
                switchTransferType(isFromC2c);
                break;
            case R.id.tv_activity_mywallet_transfer_chooseAll:
                edt_amount.setText(TextUtils.isEmpty(availBalance) ? "0" : availBalance);
                int index = edt_amount.getText().toString().trim().length();
                edt_amount.setSelection(index > 0 ? index : 0);
                break;
            case R.id.tv_activity_mywallet_transfer_submit:
                submit();
                break;
        }
    }


    /**
     * 从服务器获取该币种的可用余额
     */
    private void getCoinAvailBalance() {
        showLoading(null);
        Map<String, Object> map = new HashMap<>();
        map.put("accountType", currentAccount);
        map.put("coinType", coinType);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", StaticDatas.SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.getCoinAvailbalance(Preferences.getAccessToken(), map, new getBeanCallback<CoinAvailbalance>() {
            @Override
            public void onSuccess(CoinAvailbalance availbalance) {
                availBalance = TextUtils.isEmpty(availbalance.getAvailBalance()) ? "0.0" : availbalance.getAvailBalance();
                tv_avail.setText("" + availBalance);
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
     * 提交划转
     */
    private void submit() {
        /*获取输入的内容*/
        if (coinType < 0) {
            showToast("请选择转出币种");
            return;
        }
        String amountStr = edt_amount.getText().toString().trim();
        if (TextUtils.isEmpty(amountStr)) {
            showToast("请输入转出数量");
            return;
        }
        amount = MathUtils.string2Double(amountStr);
        if (amount <= 0) {
            showToast("请输入转出数量(>0)");
            return;
        }
        if (amount > MathUtils.string2Double(availBalance)) {
            showToast("可用余额不足");
            return;
        }
        /*弹出输入交易密码对话框*/
        showPayDialog(new getStringCallback() {
            @Override
            public void getString(String string) {
                submitTransfer2Net(string);
            }
        });
    }

    /**
     * 去服务器转账
     */
    private void submitTransfer2Net(String pwd) {
        showLoading(tv_submit);
        Map<String, Object> map = new HashMap<>();
        map.put("coinType", coinType);
        map.put("type", isFromC2c ? 1 : 2);
        map.put("amount", amount);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", StaticDatas.SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        map.put("password", pwd);
        NetWorks.submitAssetsTransfer(Preferences.getAccessToken(), map, new getBeanCallback() {
            @Override
            public void onSuccess(Object o) {
                showToast("资金划转成功");
                LogUtils.w(TAG, "资金划转成功");
                hideLoading();
                EventBus.getDefault().post(new EventAssetsChange(isFromC2c ? StaticDatas.ACCOUNT_C2C : StaticDatas.ACCOUNT_GOODS));
                MyWalletTransferActivity.this.finish();
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
                hideLoading();
            }
        });
    }

}
