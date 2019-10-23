package app.com.pgy.Activitys;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import app.com.pgy.Activitys.Base.BaseActivity;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Models.Beans.EventBean.EventLoginState;
import app.com.pgy.Models.Beans.User;
import app.com.pgy.Models.Beans.verficationCode;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Utils.NetUtil;
import app.com.pgy.Utils.PasswordUtil;
import app.com.pgy.Utils.TimeUtils;
import app.com.pgy.Utils.ToolsUtils;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

import static app.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * Created by YX on 2018/7/6.
 */

public class LoginActivity extends BaseActivity {
    @BindView(R.id.iv_activity_login_close)
    ImageView iv_close;
    @BindView(R.id.rg_activity_login_logintype)
    RadioGroup rg_loginType;
    @BindView(R.id.edt_activity_login_tel)
    EditText edt_tel;
    @BindView(R.id.ll_activity_login_password)
    LinearLayout ll_pw;
    @BindView(R.id.edt_activity_login_password)
    EditText edt_pw;
    @BindView(R.id.iv_activity_login_pwShow)
    ImageView iv_show;
    @BindView(R.id.ll_activity_login_verificationCode)
    LinearLayout ll_verificationCode;
    @BindView(R.id.edt_activity_login_verificationCode)
    EditText edt_verificationCode;
    @BindView(R.id.tv_activity_login_verificationCode)
    TextView tv_verificationCode;
    @BindView(R.id.tv_activity_login_submit)
    TextView tv_login;
    @BindView(R.id.tv_activity_login_toRegister)
    TextView tv_register;
    @BindView(R.id.tv_activity_login_toForgetPw)
    TextView tv_forgetPw;
    @BindView(R.id.iv_activity_login_clear)
    ImageView iv_clear;
    @BindView(R.id.view_login_by_code)
    View code_line;
    @BindView(R.id.view_login_by_pw)
    View pw_line;

    //密码是否可见
    private boolean ispwShow = false;
    /**
     * 用户输入的登录的手机号、密码、验证码
     */
    private String userPhone;
    private String userPassword;
    private String verificationCode;
    /**
     * 服务器返回的验证码标志
     */
    private String verificationMarkFromNet;

    private boolean loginByPw = true;
    /**
     * 倒计时器,默认60s
     */
    private static final int COUNTDOWN_TIME = 60 * 1000;

    private CountDownTimer timer = new CountDownTimer(COUNTDOWN_TIME,1000) {
        @Override
        public void onTick(long l) {
            tv_verificationCode.setEnabled(false);
            int seconds = (int)(l/1000);
            tv_verificationCode.setText(seconds+"s重发");
        }

        @Override
        public void onFinish() {
            tv_verificationCode.setEnabled(true);
            tv_verificationCode.setText("重发验证码");
        }
    };

    @Override
    public int getContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initEdittext();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        rg_loginType.check(R.id.rb_activity_login_by_password);
        loginByPw = true;
        updateLoginType();
        rg_loginType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rb_activity_login_by_code:
                        loginByPw = false;
                        updateLoginType();
                        break;
                    case R.id.rb_activity_login_by_password:
                        loginByPw = true;
                        updateLoginType();
                        break;
                }
            }
        });
        PasswordUtil.setPasswordShow(edt_pw,iv_show,ispwShow);
        ispwShow = !ispwShow;
    }

    private void initEdittext(){
        edt_tel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (edt_tel.getText().length() < 1){
                    iv_clear.setVisibility(View.GONE);
                    tv_login.setEnabled(true);
                }else if (iv_clear.getVisibility() != View.VISIBLE){
                    iv_clear.setVisibility(View.VISIBLE);
                    if (edt_pw.getText().length() > 0){
                        tv_login.setEnabled(true);
                    }
                }
            }
        });

        edt_pw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (edt_pw.getText().length() < 1){
                    iv_show.setVisibility(View.GONE);
                    tv_login.setEnabled(true);
                }else if (iv_show.getVisibility() != View.VISIBLE){
                    iv_show.setVisibility(View.VISIBLE);
                    if (edt_tel.getText().length() > 0){
                        tv_login.setEnabled(true);
                    }
                }
            }
        });
    }

    @OnClick({R.id.iv_activity_login_close,R.id.iv_activity_login_pwShow,R.id.tv_activity_login_verificationCode,
            R.id.tv_activity_login_toRegister,R.id.tv_activity_login_toForgetPw,R.id.iv_activity_login_clear,
            R.id.tv_activity_login_submit})
    public void onViewClick(View view){
        switch (view.getId()){
            case R.id.iv_activity_login_close:
                finish();
                break;
            case R.id.iv_activity_login_pwShow:
                PasswordUtil.setPasswordShow(edt_pw,iv_show,ispwShow);
                ispwShow = !ispwShow;
                break;
            case R.id.tv_activity_login_verificationCode:
                getVerificationCode();
                break;
            case R.id.tv_activity_login_toRegister:
                  //跳转注册页面
                Intent intent2register = new Intent(mContext,RegisterActivity.class);
                startActivity(intent2register);
                break;
            case R.id.tv_activity_login_toForgetPw:
                 //跳转忘记密码页
                Intent intent2forget = new Intent(mContext,ForgetPwActivity.class);
                startActivity(intent2forget);
                break;
            case R.id.iv_activity_login_clear:
                edt_tel.setText("");
                break;
            case R.id.tv_activity_login_submit:
                if (loginByPw){
                    loginByPw();
                }else {
                    loginByCode();
                }
                break;
        }
    }

    private void updateLoginType(){
        if (loginByPw){
            ll_pw.setVisibility(View.VISIBLE);
            ll_verificationCode.setVisibility(View.GONE);
            code_line.setVisibility(View.GONE);
            pw_line.setVisibility(View.VISIBLE);
        }else {
            ll_verificationCode.setVisibility(View.VISIBLE);
            ll_pw.setVisibility(View.GONE);
            code_line.setVisibility(View.VISIBLE);
            pw_line.setVisibility(View.GONE);
        }
    }

    private void getVerificationCode(){
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
        tv_verificationCode.setEnabled(false);
        Map<String, Object> map = new HashMap<>();
        map.put("phone", userPhone);
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

    private void loginByPw(){
        userPhone = edt_tel.getText().toString().trim();
                /*如果输入的是手机号则去验证，开启倒计时*/
        if (!ToolsUtils.isPhone(userPhone)) {
            showToast(getString(R.string.illegal_phone));
            return;
        }
        userPassword = edt_pw.getText().toString().trim();
        if (!ToolsUtils.isDigitalAndWord(userPassword)) {
            showToast(getString(R.string.illegal_password));
            return;
        }
        showLoading(tv_login);
        Map<String, Object> map = new HashMap<>();
        map.put("phone", userPhone);
        map.put("password", userPassword);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.login(map, new getBeanCallback<User>() {
            @Override
            public void onSuccess(User user) {
                LogUtils.w(TAG,"user:"+user.toString());
                /*登录成功，将用户保存在本地*/
                if (Preferences.setLocalUser(user)) {
                    LogUtils.w(TAG,"talkToken:"+Preferences.getTalkToken());
                    sendLoginMessage(user);
                    /*进行umeng用户统计*/
                    MobclickAgent.onProfileSignIn("ANDROID", user.getPhone());
                    hideLoading();
                    LoginActivity.this.finish();
                }
            }

            @Override
            public void onError(int errorCode, String reason) {
                /*如果登录失败则清空本地key和个人信息*/
                //Preferences.clearAllUserData();
                onFail(errorCode, reason);
                hideLoading();

            }
        });
    }
    private void loginByCode(){
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

        showLoading(tv_login);
        Map<String, Object> map = new HashMap<>();
        map.put("phone", userPhone);
        map.put("code", verificationCode);
        map.put("codeId", verificationMarkFromNet);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        NetWorks.loginByCode(map, new getBeanCallback<User>() {
            @Override
            public void onSuccess(User user) {
                /*登录成功，将用户保存在本地*/
                if (Preferences.setLocalUser(user)) {
                    sendLoginMessage(user);
                    /*进行umeng用户统计*/
                    MobclickAgent.onProfileSignIn("ANDROID", user.getPhone());
                    hideLoading();
                    LoginActivity.this.finish();
                }
            }

            @Override
            public void onError(int errorCode, String reason) {
                /*如果登录失败则清空本地key和个人信息*/
                onFail(errorCode, reason);
                hideLoading();
            }
        });
    }

    /**发送登录成功广播，在baseFragment和baseActivity中接收
     * @param user*/
    private void sendLoginMessage(User user) {
        /*同步个人消息*/
        RongIM.getInstance().refreshUserInfoCache(new UserInfo(user.getPhone(),user.getName(), Uri.parse(user.getHeadImg())));
        if (RongIM.getInstance() != null) {
            RongIM.getInstance().setCurrentUserInfo(new UserInfo(user.getPhone(), user.getName(), Uri.parse(user.getHeadImg())));
        }
        LogUtils.w("receiver","Login发送action");
        EventBus.getDefault().post(new EventLoginState(true));
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
