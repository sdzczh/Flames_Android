package app.com.pgy.Activitys;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import app.com.pgy.Activitys.Base.BaseActivity;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Models.Beans.verficationCode;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Utils.NetUtil;
import app.com.pgy.Utils.PasswordUtil;
import app.com.pgy.Utils.TimeUtils;
import app.com.pgy.Utils.ToolsUtils;
import app.com.pgy.Utils.Utils;

import static app.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * Created by YX on 2018/7/7.
 */

public class ChangeTradePwActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_activity_change_trade_pw_tel)
    TextView tv_tel;
    @BindView(R.id.tv_activity_change_trade_pw_verificationCode)
    TextView tv_verification;
    @BindView(R.id.edt_activity_change_trade_pw_verificationCode)
    EditText edt_verification;
    @BindView(R.id.edt_activity_change_trade_pw_oldpw)
    EditText edt_oldPw;
    @BindView(R.id.edt_activity_change_trade_pw_newpw)
    EditText edt_newPw;
    @BindView(R.id.iv_activit_change_trade_pw_pwShow)
    ImageView iv_pwShow;
    @BindView(R.id.edt_activity_change_trade_pw_newpw1)
    EditText edt_newPw1;
    @BindView(R.id.iv_activit_change_trade_pw_pwShow1)
    ImageView iv_pwShow1;
    @BindView(R.id.tv_activity_change_trade_pw_submit)
    TextView tv_submit;

    /**
     * 用户输入的手机号、验证码
     */
    private String userPhoneNumber;
    private String verificationCode;
    private String password;
    private boolean isSetTradePwd;
    private boolean isPwdVisible = false;
    /**
     * 服务器返回的验证码标志
     */
    private String verificationMarkFromNet;
    /**
     * 倒计时器,默认60s
     */
    private static final int COUNTDOWN_TIME = 60 * 1000;
    private CountDownTimer timer = new CountDownTimer(COUNTDOWN_TIME, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            tv_verification.setEnabled(false);
            long currentSecond = millisUntilFinished / 1000;
            tv_verification.setText(currentSecond + "秒后可重发");
        }

        @Override
        public void onFinish() {
            tv_verification.setEnabled(true);
            tv_verification.setText("获取验证码");
        }
    };

    @Override
    public int getContentViewId() {
        return R.layout.activity_change_trade_password;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        isSetTradePwd = Preferences.getLocalUser().isOrderPwdFlag();
        tv_title.setText(isSetTradePwd?"修改交易密码":"设置交易密码");
        userPhoneNumber = Preferences.getLocalUser().getPhone();
        tv_tel.setText(Utils.getSecretPhoneNum(userPhoneNumber));
        edt_newPw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (edt_newPw.getText().length() < 1){
                    iv_pwShow.setVisibility(View.GONE);
                }else if (iv_pwShow.getVisibility() != View.VISIBLE){
                    iv_pwShow.setVisibility(View.VISIBLE);
                }
            }
        });
        PasswordUtil.setPasswordShow(edt_newPw,iv_pwShow,isPwdVisible);
        isPwdVisible = !isPwdVisible;
    }

    @OnClick({R.id.iv_back,R.id.tv_activity_change_trade_pw_verificationCode,R.id.iv_activit_change_trade_pw_pwShow,
            R.id.tv_activity_change_trade_pw_submit})
    public void onViewClick(View view){
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_activity_change_trade_pw_verificationCode:
                getVerificationCode();
                break;
            case R.id.iv_activit_change_trade_pw_pwShow:
                PasswordUtil.setPasswordShow(edt_newPw,iv_pwShow,isPwdVisible);
                isPwdVisible = !isPwdVisible;
                break;
            case R.id.tv_activity_change_trade_pw_submit:
                submitTradePwd2Net();
                break;
        }
    }

    private void getVerificationCode(){
        /*如果输入的是手机号则去验证，开启倒计时*/
        if (!ToolsUtils.isPhone(userPhoneNumber)) {
            showToast(getString(R.string.illegal_phone));
            return;
        }
        showToast("获取验证码");
        if (timer != null) {
            timer.start();
        }
        tv_verification.setEnabled(false);
        Map<String, Object> map = new HashMap<>();
        map.put("phone", userPhoneNumber);
        map.put("type", 2);
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

    /**
     * 去服务器修改
     */
    private void submitTradePwd2Net() {
//        if (TextUtils.isEmpty(edt_oldPw.getText().toString().trim())){
//            showToast("请输入原密码");
//            return;
//        }
        if (!ToolsUtils.isPhone(userPhoneNumber)) {
            showToast(getString(R.string.illegal_phone));
            return;
        }
        if (TextUtils.isEmpty(verificationMarkFromNet)) {
            showToast(R.string.getVerificationFirst);
            return;
        }
        verificationCode = edt_verification.getText().toString().trim();
        if (!ToolsUtils.isVerificationCode(verificationCode)) {
            showToast(getString(R.string.illegal_verification));
            return;
        }
        password = edt_newPw.getText().toString().trim();
        if (!ToolsUtils.isTradePwd(password)) {
            showToast(getString(R.string.illegal_password));
            return;
        }
        if (!edt_newPw1.getText().toString().trim().equals(password)){
            showToast("确认密码不一致");
            return;
        }
        showLoading(tv_submit);
        Map<String, Object> map = new HashMap<>();
//        map.put("old", edt_oldPw.getText().toString().trim());
        map.put("password", password);
        map.put("code", verificationCode);
        map.put("codeId", verificationMarkFromNet);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        map.put("systemType", SYSTEMTYPE_ANDROID);

        NetWorks.setWithdrawPwd(Preferences.getAccessToken(), map, new getBeanCallback() {
            @Override
            public void onSuccess(Object o) {
                hideLoading();
                showToast(isSetTradePwd?"修改交易密码成功":"设置交易密码成功");
                LogUtils.w(TAG, isSetTradePwd?"修改交易密码成功":"设置交易密码成功");
                /*修改交易密码后，将本地存储的交易密码清空*/
                Preferences.setLocalTradePwd("");
                Preferences.setIsHasTradePwd(true);
                ChangeTradePwActivity.this.finish();
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
                hideLoading();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null){
            timer.cancel();
            timer = null;
        }
    }
}
