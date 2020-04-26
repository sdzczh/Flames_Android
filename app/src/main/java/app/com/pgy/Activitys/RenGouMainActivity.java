package app.com.pgy.Activitys;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import app.com.pgy.Activitys.Base.BaseActivity;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Models.RenGouInitBan;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Utils.ImageLoaderUtils;
import app.com.pgy.Utils.MathUtils;
import app.com.pgy.Widgets.NumberEditText;
import app.com.pgy.Widgets.TitleView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 认购界面
 */

public class RenGouMainActivity extends BaseActivity {

    @BindView(R.id.activity_renzheng_main_title)
    TitleView activityRenzhengMainTitle;
    @BindView(R.id.activity_renzheng_main_personNum)
    TextView activityRenzhengMainPersonNum;
    @BindView(R.id.activity_renzheng_main_schedule_progress)
    ProgressBar activityRenzhengMainScheduleProgress;
    @BindView(R.id.activity_renzheng_main_schedule_per)
    TextView activityRenzhengMainSchedulePer;
    @BindView(R.id.activity_renzheng_main_finishTime)
    TextView activityRenzhengMainFinishTime;
    @BindView(R.id.activity_renzheng_main_payAddress)
    TextView activityRenzhengMainPayAddress;
    @BindView(R.id.activity_renzheng_main_udtdAddress)
    NumberEditText activityRenzhengMainUdtdAddress;
    @BindView(R.id.activity_renzheng_main_buyAmt)
    NumberEditText activityRenzhengMainBuyAmt;
    @BindView(R.id.activity_renzheng_main_payAmt)
    TextView activityRenzhengMainPayAmt;
    @BindView(R.id.activity_renzheng_main_playRule)
    TextView activityRenzhengMainPlayRule;
    @BindView(R.id.activity_renzheng_main_submit)
    TextView activityRenzhengMainSubmit;
    @BindView(R.id.activity_renzheng_main_pay_coinName)
    TextView activityRenzhengMainPayCoinName;
    @BindView(R.id.activity_renzheng_main_schedule_title)
    TextView activityRenzhengMainScheduleTitle;
    @BindView(R.id.activity_renzheng_main_defaultRadio)
    TextView activityRenzhengMainDefaultRadio;
    @BindView(R.id.activity_renzheng_main_udtdAddressTitle)
    TextView activityRenzhengMainUdtdAddressTitle;
    private int defaultRadio = 0;
    private String imgUrl;
    private int id;

    @Override
    public int getContentViewId() {
        return R.layout.activity_rengou_main;
    }

    @Override
    protected void initData() {
        getInitInfo();
    }

    private void getInitInfo() {
        String accessToken = Preferences.getAccessToken();
        NetWorks.getRenGouInitInfo(accessToken, new getBeanCallback<RenGouInitBan>() {
            @Override
            public void onSuccess(RenGouInitBan renGouInitBan) {
                if (renGouInitBan == null) {
                    return;
                }
                id = renGouInitBan.getId();
                int countUsers = renGouInitBan.getCountUsers();
                activityRenzhengMainPersonNum.setText(MathUtils.int2String(countUsers));   //当前参与人数

                imgUrl = renGouInitBan.getImgUrl();

                int percentage = renGouInitBan.getPercentage();
                activityRenzhengMainScheduleProgress.setProgress(percentage);
                activityRenzhengMainSchedulePer.setText(percentage + "%");

                String buyingTotal = renGouInitBan.getBuyingTotal();
                Integer buyTotal = MathUtils.string2Integer(buyingTotal);
                BigDecimal current = new BigDecimal(buyingTotal).multiply(new BigDecimal(percentage));
                String coinName = renGouInitBan.getCoinName();
                int coinType = renGouInitBan.getCoinType();

                String str1 = "<font color='#F0CB7A'>" + current.toPlainString().concat(" ").concat(coinName) + "</font>";
                String str2 = "<font color='#E6E6E6'> / " + buyingTotal.concat(" ").concat(coinName) + "</font>";
                activityRenzhengMainScheduleTitle.setText(Html.fromHtml(str1.concat(str2)));

                defaultRadio = renGouInitBan.getDefaultRadio();
                activityRenzhengMainDefaultRadio.setText("当前比例  1" + coinName + "=" + defaultRadio + "Flames");

                String address = renGouInitBan.getAddress();
                activityRenzhengMainPayAddress.setText(address);//付款地址

                String endTime = renGouInitBan.getEndTime();
                activityRenzhengMainFinishTime.setText(endTime);


                activityRenzhengMainUdtdAddressTitle.setText("我的" + coinName + "地址");
                activityRenzhengMainUdtdAddress.setHint("请输入" + coinName + "地址");
                activityRenzhengMainPayCoinName.setText(coinName);

                String explain = renGouInitBan.getExplain();
                activityRenzhengMainPlayRule.setText(explain);
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
            }
        });
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        activityRenzhengMainTitle.setBackClickListener(v -> RenGouMainActivity.this.finish());
        activityRenzhengMainTitle.setRightClickListener(v -> {
            //认购记录
            if (id <= 0) {
                showToast("id为空");
                return;
            }
            Intent intent = new Intent(mContext, RenGouListActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        });
        activityRenzhengMainBuyAmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (defaultRadio <= 0) {
                    return;
                }
                String inputTxt = s.toString();
                Integer count = MathUtils.string2Integer(inputTxt);
                BigDecimal divide = new BigDecimal(count).divide(new BigDecimal(defaultRadio));
                activityRenzhengMainPayAmt.setText(divide.toPlainString());
            }
        });
    }

    @OnClick({R.id.activity_renzheng_main_payArUrl, R.id.activity_renzheng_main_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.activity_renzheng_main_payArUrl:
                showErweiMaDialog();
                break;
            case R.id.activity_renzheng_main_submit:
                submit();
                break;
        }
    }

    /**
     * 显示二维码的对话框
     */
    private void showErweiMaDialog() {
        if (TextUtils.isEmpty(imgUrl)) {
            showToast("数据为空");
            return;
        }
        View view = getLayoutInflater().inflate(R.layout.show_erweima_dialog, null);
        Dialog showErweiMa = new Dialog(mContext);
        showErweiMa.setContentView(view);
        ImageView imageView = view.findViewById(R.id.dialog_erweima_iv);
        ImageLoaderUtils.display(mContext, imageView, imgUrl);
        TextView textView = view.findViewById(R.id.dialog_erweima_tv);
        textView.setVisibility(View.GONE);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.8f;
        getWindow().setAttributes(lp);
        // 设置点击外围解散
        showErweiMa.setCanceledOnTouchOutside(true);
        if (!showErweiMa.isShowing()) {
            showErweiMa.show();
        }
    }

    private void submit() {
        Editable ustdAddress = activityRenzhengMainUdtdAddress.getText();
        if (ustdAddress == null || TextUtils.isEmpty(ustdAddress.toString())) {
            showToast(activityRenzhengMainUdtdAddress.getHint().toString());
            return;
        }

        Editable buyAmt = activityRenzhengMainBuyAmt.getText();
        if (buyAmt == null || TextUtils.isEmpty(buyAmt.toString())) {
            showToast(activityRenzhengMainBuyAmt.getHint().toString());
            return;
        }

        String payAmtStr = activityRenzhengMainPayAmt.getText().toString();
        if (TextUtils.isEmpty(payAmtStr)) {
            showToast("支付金额为空");
            return;
        }
        showLoading(activityRenzhengMainSubmit);
        Map<String, Object> map = new HashMap<>();
        map.put("buyingId", id);
        map.put("address", ustdAddress);
        map.put("amount", buyAmt);
        map.put("payAmount", payAmtStr);
        String accessToken = Preferences.getAccessToken();
        NetWorks.submitRengou(accessToken, map, new getBeanCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                hideLoading();
                showToast("提交成功");
                RenGouMainActivity.this.finish();
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
                hideLoading();
            }
        });
    }

}
