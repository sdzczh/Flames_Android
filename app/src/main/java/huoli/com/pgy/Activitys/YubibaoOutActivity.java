package huoli.com.pgy.Activitys;

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
import huoli.com.pgy.Activitys.Base.BaseActivity;
import huoli.com.pgy.Activitys.Base.WebDetailActivity;
import huoli.com.pgy.Constants.Preferences;
import huoli.com.pgy.Constants.StaticDatas;
import huoli.com.pgy.Interfaces.getBeanCallback;
import huoli.com.pgy.Interfaces.getStringCallback;
import huoli.com.pgy.Models.Beans.Configuration;
import huoli.com.pgy.Models.Beans.EventBean.EventAssetsChange;
import huoli.com.pgy.Models.Beans.YubibaoAvailbalance;
import huoli.com.pgy.NetUtils.NetWorks;
import huoli.com.pgy.R;
import huoli.com.pgy.Utils.LogUtils;
import huoli.com.pgy.Utils.MathUtils;
import huoli.com.pgy.Utils.TimeUtils;
import huoli.com.pgy.Widgets.NumberEditText;

import static huoli.com.pgy.Activitys.YubibaoActivity.TRANSFER_SUCCESS;
import static huoli.com.pgy.Constants.StaticDatas.ACCOUNT_YUBIBAO;

/**
 * Created by YX on 2018/7/16.
 */

public class YubibaoOutActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_question)
    ImageView ivQuestion;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.edt_activity_yububao_out_amount)
    NumberEditText edtActivityYububaoOutAmount;
    @BindView(R.id.tv_activity_yubibao_out_coinName)
    TextView tvActivityYubibaoOutCoinName;
    @BindView(R.id.tv_activity_yubibao_out_chooseAll)
    TextView tvActivityYubibaoOutChooseAll;
    @BindView(R.id.tv_activity_yubibao_out_total)
    TextView tvActivityYubibaoOutTotal;
    @BindView(R.id.tv_activity_yubibao_out_coinName1)
    TextView tvActivityYubibaoOutCoinName1;
    @BindView(R.id.tv_activity_yubibao_out_submit)
    TextView tvActivityYubibaoOutSubmit;
    @BindView(R.id.tv_activity_yubibao_out_coinName2)
    TextView tvActivityYubibaoOutCoinName2;
    @BindView(R.id.tv_activity_yubibao_out_limitmin)
    TextView tvActivityYubibaoOutLimitMin;
    private int coinType = 0;
    private Configuration.CoinInfo coinInfo;
    private YubibaoAvailbalance availBalance;
    private Double amount;
    @Override
    public int getContentViewId() {
        return R.layout.activity_yubibao_out;
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

        edtActivityYububaoOutAmount.setDigits(coinInfo.getYubiScale());
        tvTitle.setText(coinInfo.getCoinname()+"  转出");
        ivQuestion.setVisibility(View.VISIBLE);
        tvActivityYubibaoOutCoinName.setText(coinInfo.getCoinname()+"");
        tvActivityYubibaoOutCoinName1.setText(coinInfo.getCoinname()+"");
        tvActivityYubibaoOutCoinName2.setText(coinInfo.getCoinname()+"");
        getCoinAvailBalance();
    }

    @OnClick({R.id.iv_back,R.id.iv_question,R.id.tv_activity_yubibao_out_chooseAll,R.id.tv_activity_yubibao_out_submit})
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
            case R.id.tv_activity_yubibao_out_chooseAll:
                if (coinInfo != null){
                    edtActivityYububaoOutAmount.setText(MathUtils.formatDoubleNumber(MathUtils.string2Double(availBalance.getAvailBalance()),coinInfo.getYubiScale()));
                }else {
                    edtActivityYububaoOutAmount.setText(TextUtils.isEmpty(availBalance.getAvailBalance()) ? "0" : availBalance.getAvailBalance());
                }
//                edtActivityYububaoOutAmount.setText(TextUtils.isEmpty(availBalance.getAvailBalance()) ? "0" : availBalance.getAvailBalance());
                int index = edtActivityYububaoOutAmount.getText().toString().trim().length();
                edtActivityYububaoOutAmount.setSelection(index>0?index:0);
                break;
            case R.id.tv_activity_yubibao_out_submit:
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
        map.put("accountType", ACCOUNT_YUBIBAO);
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
                }
                tvActivityYubibaoOutTotal.setText(""+availBalance.getAvailBalance());
                String minTransAmount = availBalance.getMinTransAmount();
                if (TextUtils.isEmpty(minTransAmount)){
                    edtActivityYububaoOutAmount.setHint("请输入转出数量");
                    tvActivityYubibaoOutLimitMin.setText(0);
                }else{
                    edtActivityYububaoOutAmount.setHint("最小转出数量"+minTransAmount);
                    tvActivityYubibaoOutLimitMin.setText(""+minTransAmount);
                }

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
        String amountStr = edtActivityYububaoOutAmount.getText().toString().trim();
        if (TextUtils.isEmpty(amountStr)) {
            showToast("请输入转出数量");
            return;
        }
        amount = MathUtils.string2Double(amountStr);
        if (amount < MathUtils.string2Double(availBalance.getMinTransAmount())) {
            showToast("最小转出数量为"+availBalance.getMinTransAmount());
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
        showLoading(tvActivityYubibaoOutSubmit);
        Map<String, Object> map = new HashMap<>();
        map.put("coinType", coinType);
        map.put("type", 1);
        map.put("amount", amount);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", StaticDatas.SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        map.put("password", pwd);
        NetWorks.submitYubibaoTransfer(Preferences.getAccessToken(), map, new getBeanCallback() {
            @Override
            public void onSuccess(Object o) {
                showToast("转出成功");
                LogUtils.w(TAG, "成功");
                hideLoading();
                EventBus.getDefault().post(new EventAssetsChange(StaticDatas.ACCOUNT_YUBIBAO));
                Intent intent = getIntent();
                intent.putExtra("coinType",coinType);
                setResult(TRANSFER_SUCCESS,intent);
                YubibaoOutActivity.this.finish();
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
                hideLoading();
            }
        });
    }
}
