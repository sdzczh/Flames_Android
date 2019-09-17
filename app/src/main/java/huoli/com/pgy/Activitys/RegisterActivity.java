package huoli.com.pgy.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import huoli.com.pgy.Activitys.Base.BaseActivity;
import huoli.com.pgy.Activitys.Base.WebDetailActivity;
import huoli.com.pgy.Constants.Preferences;
import huoli.com.pgy.Interfaces.getBeanCallback;
import huoli.com.pgy.Models.Beans.verficationCode;
import huoli.com.pgy.NetUtils.NetWorks;
import huoli.com.pgy.R;
import huoli.com.pgy.Utils.EdittextUtils;
import huoli.com.pgy.Utils.LogUtils;
import huoli.com.pgy.Utils.NetUtil;
import huoli.com.pgy.Utils.PasswordUtil;
import huoli.com.pgy.Utils.TimeUtils;
import huoli.com.pgy.Utils.ToolsUtils;

import static huoli.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * Created by YX on 2018/7/6.
 */

public class RegisterActivity extends BaseActivity {
    @BindView(R.id.iv_activity_register_close)
    ImageView iv_close;
    @BindView(R.id.edt_activity_register_tel)
    EditText edt_tel;
    @BindView(R.id.iv_activity_register_clear)
    ImageView iv_clear;
    @BindView(R.id.edt_activity_register_verificationCode)
    EditText edt_verificationCode;
    @BindView(R.id.tv_activity_register_verificationCode)
    TextView tv_verification;
    @BindView(R.id.edt_activity_register_password)
    EditText edt_password;
    @BindView(R.id.iv_activity_register_pwShow)
    ImageView iv_pwShow;
    @BindView(R.id.edt_activity_register_refereeTel)
    EditText edt_refereeTel;
    @BindView(R.id.tv_activity_register_submit)
    TextView tv_submit;
    @BindView(R.id.tv_activity_register_toAgreement)
    TextView tv_toAgreement;
    @BindView(R.id.ll_activity_register_backLogin)
    LinearLayout llActivityRegisterBackLogin;

    /**
     * 用户输入的手机号、验证码、密码
     */
    private String userPhone;
    private String verificationCode;
    private String password;
    private String referPhoneNumber;
    private boolean isPwdVisible = false;
    /**
     * 服务器返回的验证码标志
     */
    private String verificationMarkFromNet;
    private String agreement;
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
        return R.layout.activity_register;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {
        agreement = getConfiguration().getAgreenmentUrl();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        edt_password.setFilters(new InputFilter[]{EdittextUtils.getNoEmojiNoCh(getApplicationContext())});
        edt_tel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (edt_tel.getText().length() < 1) {
                    iv_clear.setVisibility(View.GONE);
                    tv_submit.setEnabled(false);

                } else if (iv_clear.getVisibility() != View.VISIBLE) {
                    iv_clear.setVisibility(View.VISIBLE);
                    if (checkInupt()){
                        tv_submit.setEnabled(true);
                    }
                }
            }
        });

        edt_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (edt_password.getText().length() < 1) {
                    iv_pwShow.setVisibility(View.GONE);
                    tv_submit.setEnabled(false);

                } else if (iv_pwShow.getVisibility() != View.VISIBLE) {
                    iv_pwShow.setVisibility(View.VISIBLE);
                    if (checkInupt()){
                        tv_submit.setEnabled(true);
                    }
                }
            }
        });
        edt_verificationCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(edt_verificationCode.getText().length() > 0){
                    if (checkInupt()){
                        tv_submit.setEnabled(true);
                    }
                }else {
                    tv_submit.setEnabled(false);
                }
            }
        });
        PasswordUtil.setPasswordShow(edt_password, iv_pwShow, isPwdVisible);
        isPwdVisible = !isPwdVisible;
    }

    private boolean checkInupt(){
        if (edt_tel.getText().length() > 0&&edt_verificationCode.getText().length() > 0 &&
                edt_password.getText().length() > 0){
            return true;
        }
        return false;
    }

    @OnClick({R.id.iv_activity_register_close, R.id.iv_activity_register_clear, R.id.tv_activity_register_verificationCode,
            R.id.iv_activity_register_pwShow, R.id.tv_activity_register_submit, R.id.tv_activity_register_toAgreement,
            R.id.ll_activity_register_backLogin})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.iv_activity_register_close:
                finish();
                break;
            case R.id.ll_activity_register_backLogin:
                finish();
                break;
            case R.id.iv_activity_register_clear:
                edt_tel.setText("");
                break;
            case R.id.tv_activity_register_verificationCode:
                getVerificationCode();
                break;
            case R.id.iv_activity_register_pwShow:
                PasswordUtil.setPasswordShow(edt_password, iv_pwShow, isPwdVisible);
                isPwdVisible = !isPwdVisible;
                break;
            case R.id.tv_activity_register_submit:
                register2Net();
                break;
            case R.id.tv_activity_register_toAgreement:
                Intent intent2Detail = new Intent(mContext, WebDetailActivity.class);
                intent2Detail.putExtra("title", "协议声明");
                intent2Detail.putExtra("url", agreement);
                startActivity(intent2Detail);
                break;
        }
    }


    private void getVerificationCode() {
        userPhone = edt_tel.getText().toString().trim();
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

    /**
     * 去服务器注册
     */
    private void register2Net() {
        userPhone = edt_tel.getText().toString().trim();
        /*如果输入的是手机号则去验证，开启倒计时*/
        if (!ToolsUtils.isPhone(userPhone)) {
            showToast(getString(R.string.illegal_phone));
            return;
        }
        if (TextUtils.isEmpty(verificationMarkFromNet)) {
            showToast(R.string.getVerificationFirst);
            return;
        }
        verificationCode = edt_verificationCode.getText().toString().trim();
        if (!ToolsUtils.isVerificationCode(verificationCode)) {
            showToast(getString(R.string.illegal_verification));
            return;
        }
        password = edt_password.getText().toString().trim();
        if (!ToolsUtils.isDigitalAndWord(password)) {
            showToast(getString(R.string.illegal_password));
            return;
        }
        referPhoneNumber = edt_refereeTel.getText().toString().trim();
        if (TextUtils.isEmpty(referPhoneNumber)){
            showToast("请输入邀请码");
            return;
        }
        showLoading(tv_submit);
        Map<String, Object> map = new HashMap<>();
        map.put("phone", userPhone);
        map.put("password", password);
        map.put("code", verificationCode);
        map.put("codeId", verificationMarkFromNet);
        map.put("referPhone", referPhoneNumber);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        map.put("systemType", SYSTEMTYPE_ANDROID);

        NetWorks.register(map, new getBeanCallback() {
            @Override
            public void onSuccess(Object o) {
                showToast("注册成功");
                LogUtils.w(TAG, "注册成功");
                hideLoading();
                RegisterActivity.this.finish();
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
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
