package app.com.pgy.Activitys;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputFilter;
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
import app.com.pgy.Utils.EdittextUtils;
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

public class ChangeLoginPwActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_activity_change_login_pw_tel)
    TextView tv_tel;
    @BindView(R.id.tv_activity_change_login_pw_verificationCode)
    TextView tv_verification;
    @BindView(R.id.edt_activity_change_login_pw_verificationCode)
    EditText edt_verification;
    @BindView(R.id.edt_activity_change_login_pw_oldPw)
    EditText edt_oldPw;
    @BindView(R.id.iv_activity_change_login_pw_oldpwShow)
    ImageView iv_oldpwShow;
    @BindView(R.id.edt_activity_change_login_pw_newpw)
    EditText edt_newPw;
    @BindView(R.id.iv_activity_change_login_pw_pwShow)
    ImageView iv_pwShow;
    @BindView(R.id.edt_activity_change_login_pw_newpw1)
    EditText edt_newPw1;
    @BindView(R.id.iv_activity_change_login_pw_pwShow1)
    ImageView iv_pwShow1;
    @BindView(R.id.tv_activity_change_login_pw_submit)
    TextView tv_submit;

    /**
     * 用户输入的手机号、验证码
     */
    private String userPhoneNumber;
    private String verificationCode;
    private String password;
    private boolean isOldVisible = false;
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
        return R.layout.activity_change_login_password;
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
        tv_title.setText("修改登录密码");
        userPhoneNumber = Preferences.getLocalUser().getPhone();
        tv_tel.setText(Utils.getSecretPhoneNum(userPhoneNumber));
        edt_oldPw.setFilters(new InputFilter[]{EdittextUtils.getNoEmojiNoCh(getApplicationContext())});
        edt_oldPw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (edt_oldPw.getText().length() < 1){
                    iv_oldpwShow.setVisibility(View.GONE);
                }else if (iv_oldpwShow.getVisibility() != View.VISIBLE){
                    iv_oldpwShow.setVisibility(View.VISIBLE);
                }
            }
        });
        PasswordUtil.setPasswordShow(edt_oldPw,iv_oldpwShow,isOldVisible);
        isOldVisible = !isOldVisible;

        edt_newPw.setFilters(new InputFilter[]{EdittextUtils.getNoEmojiNoCh(getApplicationContext())});
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
        edt_newPw1.setFilters(new InputFilter[]{EdittextUtils.getNoEmojiNoCh(getApplicationContext())});

    }

    @OnClick({R.id.iv_back,R.id.tv_activity_change_login_pw_verificationCode,R.id.iv_activity_change_login_pw_pwShow,
            R.id.tv_activity_change_login_pw_submit,R.id.iv_activity_change_login_pw_oldpwShow})
    public void onViewClick(View view){
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_activity_change_login_pw_verificationCode:
                getVerificationCode();
                break;
            case R.id.iv_activity_change_login_pw_pwShow:
                PasswordUtil.setPasswordShow(edt_newPw,iv_pwShow,isPwdVisible);
                isPwdVisible = !isPwdVisible;
                break;
            case R.id.tv_activity_change_login_pw_submit:
                submitPwd2Net();
                break;
            case R.id.iv_activity_change_login_pw_oldpwShow:
                PasswordUtil.setPasswordShow(edt_oldPw,iv_oldpwShow,isOldVisible);
                isOldVisible = !isOldVisible;
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
    private void submitPwd2Net() {
//        if (!ToolsUtils.isPhone(userPhoneNumber)) {
//            showToast(getString(R.string.illegal_phone));
//            return;
//        }
        password = edt_newPw.getText().toString().trim();
        if (!ToolsUtils.isDigitalAndWord(password)) {
            showToast(getString(R.string.illegal_password));
            return;
        }
        if (!edt_newPw1.getText().toString().trim().equals(password)){
            showToast("确认密码不正确");
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

        showLoading(tv_submit);
        Map<String, Object> map = new HashMap<>();
        map.put("password", password);
        map.put("code", verificationCode);
        map.put("codeId", verificationMarkFromNet);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        map.put("systemType", SYSTEMTYPE_ANDROID);

        NetWorks.updatePwd(Preferences.getAccessToken(),map, new getBeanCallback() {
            @Override
            public void onSuccess(Object o) {
                showToast("修改密码成功");
                LogUtils.w(TAG, "修改成功");
                hideLoading();
                ChangeLoginPwActivity.this.finish();
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
