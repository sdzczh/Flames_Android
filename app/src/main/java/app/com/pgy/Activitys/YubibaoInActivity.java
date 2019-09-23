package app.com.pgy.Activitys;

import android.content.Intent;
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
import app.com.pgy.Activitys.Base.WebDetailActivity;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Constants.StaticDatas;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Interfaces.getStringCallback;
import app.com.pgy.Models.Beans.Configuration;
import app.com.pgy.Models.Beans.EventBean.EventAssetsChange;
import app.com.pgy.Models.Beans.YubibaoAvailbalance;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Utils.MathUtils;
import app.com.pgy.Utils.TimeUtils;
import app.com.pgy.Widgets.NumberEditText;

import static app.com.pgy.Activitys.YubibaoActivity.TRANSFER_SUCCESS;
import static app.com.pgy.Constants.StaticDatas.ACCOUNT_GOODS;

/**
 * Created by YX on 2018/7/16.
 */

public class YubibaoInActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_question)
    ImageView ivQuestion;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.edt_activity_yububao_in_amount)
    NumberEditText edtActivityYububaoInAmount;
    @BindView(R.id.tv_activity_yubibao_in_coinName)
    TextView tvActivityYubibaoInCoinName;
    @BindView(R.id.tv_activity_yubibao_in_chooseAll)
    TextView tvActivityYubibaoInChooseAll;
    @BindView(R.id.tv_activity_yubibao_in_total)
    TextView tvActivityYubibaoInTotal;
    @BindView(R.id.tv_activity_yubibao_in_coinName1)
    TextView tvActivityYubibaoInCoinName1;
    @BindView(R.id.tv_activity_yubibao_in_time)
    TextView tvActivityYubibaoInTime;
    @BindView(R.id.tv_activity_yubibao_in_submit)
    TextView tvActivityYubibaoInSubmit;
    @BindView(R.id.tv_activity_yubibao_in_desc)
    TextView tvActivityYubibaoInCoinDesc;

    private int coinType = 0;
    private Configuration.CoinInfo coinInfo;
    private Double amount;
    private YubibaoAvailbalance availBalance;

    @Override
    public int getContentViewId() {
        return R.layout.activity_yubibao_in;
    }

    @Override
    protected void initData() {
        coinType = getIntent().getIntExtra("coinType",-1);
        coinInfo = getCoinInfo(coinType);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (coinType == -1){
            showToast("币种信息为空");
            finish();
            return;
        }
        if (coinInfo == null){
            showToast("获取币种信息为空");
            finish();
            return;
        }
        edtActivityYububaoInAmount.setDigits(coinInfo.getYubiScale());
        tvTitle.setText(coinInfo.getCoinname()+"-转入");
        ivQuestion.setVisibility(View.VISIBLE);
        tvActivityYubibaoInCoinName.setText(coinInfo.getCoinname()+"");
        tvActivityYubibaoInCoinName1.setText(coinInfo.getCoinname()+"");
        getCoinAvailBalance();
    }
    @OnClick({R.id.iv_back,R.id.iv_question,R.id.tv_activity_yubibao_in_chooseAll,R.id.tv_activity_yubibao_in_submit})
    public void onViewClick(View view){
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_question:
                Intent intent = null;
                intent = new Intent(mContext, WebDetailActivity.class);
                intent.putExtra("title", "常见问题");
                intent.putExtra("url", getConfiguration().getYubibaoHelpUrl());
                startActivityForResult(intent,TRANSFER_SUCCESS);
                break;
            case R.id.tv_activity_yubibao_in_chooseAll:
                if (coinInfo != null){
                    edtActivityYububaoInAmount.setText(MathUtils.formatDoubleNumber(MathUtils.string2Double(availBalance.getAvailBalance()),coinInfo.getYubiScale()));
                }else {
                    edtActivityYububaoInAmount.setText(TextUtils.isEmpty(availBalance.getAvailBalance()) ? "0" : availBalance.getAvailBalance());
                }
//                edtActivityYububaoInAmount.setText(TextUtils.isEmpty(availBalance.getAvailBalance()) ? "0" : availBalance.getAvailBalance());
                int index = edtActivityYububaoInAmount.getText().toString().trim().length();
                edtActivityYububaoInAmount.setSelection(index>0?index:0);
                break;
            case R.id.tv_activity_yubibao_in_submit:
                if (!isLogin()) {
                    showToast(R.string.unlogin);
                    return;
                }
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
        map.put("accountType", ACCOUNT_GOODS);
        map.put("coinType", coinType);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", StaticDatas.SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.getYubibaoAvailbalance(Preferences.getAccessToken(), map, new getBeanCallback<YubibaoAvailbalance>() {
            @Override
            public void onSuccess(YubibaoAvailbalance availbalance) {
                hideLoading();
                availBalance = availbalance;
                if (availBalance == null){
                    availBalance = new YubibaoAvailbalance();
                    showToast("获取数据为空，请稍候重试");
                }
                tvActivityYubibaoInTotal.setText(""+availBalance.getAvailBalance());
                String minTransAmount = availBalance.getMinTransAmount();

                if (TextUtils.isEmpty(minTransAmount)){
                    edtActivityYububaoInAmount.setHint("请输入数量");
                }else{
                    edtActivityYububaoInAmount.setHint("最小转入数量"+availBalance.getMinTransAmount());
                }
                tvActivityYubibaoInTime.setText(availBalance.getProfitReturnDate());
                tvActivityYubibaoInCoinDesc.setText("转入后"+availBalance.getProfitDay()+"天开始计算收益，"+coinInfo.getCoinname()+"最小产息数量"+availBalance.getMinProfitAmount());
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
                /*网络错误*/
                hideLoading();
                if (availBalance == null){
                    availBalance = new YubibaoAvailbalance();
                }
            }
        });
    }

    private void submit() {
        /*获取输入的内容*/
        if (availBalance == null){
            availBalance = new YubibaoAvailbalance();
        }
        String amountStr = edtActivityYububaoInAmount.getText().toString().trim();
        if (TextUtils.isEmpty(amountStr)) {
            showToast("请输入转入数量");
            return;
        }
        amount = MathUtils.string2Double(amountStr);
        if (amount < MathUtils.string2Double(availBalance.getMinTransAmount())) {
            showToast("最小转入数量为"+availBalance.getMinTransAmount());
            return;
        }
        if (amount > MathUtils.string2Double(availBalance.getAvailBalance())){
            showToast("余额不足");
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
        showLoading(tvActivityYubibaoInSubmit);
        Map<String, Object> map = new HashMap<>();
        map.put("coinType", coinType);
        map.put("type", 0);
        map.put("amount", amount);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", StaticDatas.SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        map.put("password", pwd);
        NetWorks.submitYubibaoTransfer(Preferences.getAccessToken(), map, new getBeanCallback() {
            @Override
            public void onSuccess(Object o) {
                showToast("转入成功");
                LogUtils.w(TAG, "成功");
                hideLoading();
                EventBus.getDefault().post(new EventAssetsChange(StaticDatas.ACCOUNT_YUBIBAO));
                Intent intent = getIntent();
                intent.putExtra("coinType",coinType);
                setResult(TRANSFER_SUCCESS,intent);
                YubibaoInActivity.this.finish();
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
                hideLoading();
            }
        });
    }
}
