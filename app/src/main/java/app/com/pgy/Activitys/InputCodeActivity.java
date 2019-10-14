package app.com.pgy.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tuo.customview.VerificationCodeView;

import java.util.HashMap;
import java.util.Map;

import app.com.pgy.Activitys.Base.BaseActivity;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Models.Beans.verficationCode;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Utils.NetUtil;
import app.com.pgy.Utils.TimeUtils;
import app.com.pgy.Utils.ToolsUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static app.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * Create by Android on 2019/10/14 0014
 */
public class InputCodeActivity extends BaseActivity {
    @BindView(R.id.tv_activity_register_verificationCode)
    TextView tv_verification;
    @BindView(R.id.icv)
    VerificationCodeView icv;

    private String verificationMarkFromNet;
    private String userPhone;
    /**
     * 倒计时器,默认60s
     */
    private static final int COUNTDOWN_TIME = 60 * 1000;
    private CountDownTimer timer = new CountDownTimer(COUNTDOWN_TIME, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            tv_verification.setEnabled(false);
            long currentSecond = millisUntilFinished / 1000;
            tv_verification.setText(currentSecond + "s");
        }

        @Override
        public void onFinish() {
            tv_verification.setEnabled(true);
            tv_verification.setText("获取验证码");
        }
    };
    @Override
    public int getContentViewId() {
        return R.layout.activity_input_code;
    }

    @Override
    protected void initData() {
        userPhone = getIntent().getStringExtra("tel");
        /*如果输入的是手机号则去验证，开启倒计时*/
        if (TextUtils.isEmpty(userPhone)  || !ToolsUtils.isPhone(userPhone)) {
            showToast(getString(R.string.illegal_phone));
            finish();
            return;
        }
        getVerificationCode();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        icv.setInputCompleteListener(new VerificationCodeView.InputCompleteListener() {
            @Override
            public void inputComplete() {
                if (icv.getInputContent().length() != 6){
                    return;
                }
                if (TextUtils.isEmpty(verificationMarkFromNet)) {
                    showToast(R.string.getVerificationFirst);
                    return;
                }
                Intent intent = new Intent(mContext,SetPasswordActivity.class);
                intent.putExtra("tel",userPhone);
                intent.putExtra("code",icv.getInputContent());
                intent.putExtra("codeNet",verificationMarkFromNet);
                startActivityForResult(intent,1);
            }

            @Override
            public void deleteContent() {

            }
        });
    }
    @OnClick({R.id.iv_activity_register_close, R.id.tv_activity_register_verificationCode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_activity_register_close:
                finish();
                break;
            case R.id.tv_activity_register_verificationCode:
                getVerificationCode();
                break;
        }
    }



    private void getVerificationCode() {
        /*如果输入的是手机号则去验证，开启倒计时*/
        if (!ToolsUtils.isPhone(userPhone)) {
            showToast(getString(R.string.illegal_phone));
            return;
        }
        showToast("获取验证码");
        if (timer != null) {
            timer.start();
        }
        tv_verification.setEnabled(false);
        Map<String, Object> map = new HashMap<>();
        map.put("phone", userPhone);
        map.put("type", 1);
        map.put("userIp", NetUtil.getIpAddress(getApplicationContext()));
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        NetWorks.getVerificationCode(map, new getBeanCallback<verficationCode>() {
            @Override
            public void onSuccess(verficationCode verCode) {
                verificationMarkFromNet = verCode.getCodeId();
                showToast("验证码已发送");
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
                if (timer != null) {
                    timer.cancel();
                    timer.onFinish();
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK){
            setResult(RESULT_OK);
            finish();
        }
    }
}
