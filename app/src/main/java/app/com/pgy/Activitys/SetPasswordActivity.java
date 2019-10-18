package app.com.pgy.Activitys;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Map;

import app.com.pgy.Activitys.Base.BaseActivity;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Utils.PasswordUtil;
import app.com.pgy.Utils.TimeUtils;
import app.com.pgy.Utils.ToolsUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static app.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * Create by Android on 2019/10/14 0014
 */
public class SetPasswordActivity extends BaseActivity {
    @BindView(R.id.edt_activity_register_password)
    EditText edtActivityRegisterPassword;
    @BindView(R.id.iv_activity_register_pwShow)
    ImageView ivActivityRegisterPwShow;
    @BindView(R.id.edt_activity_register_password1)
    EditText edtActivityRegisterPassword1;
    @BindView(R.id.iv_activity_register_pwShow1)
    ImageView ivActivityRegisterPwShow1;

    private boolean isPwdVisible = false;
    private boolean isPwdVisible1 = false;

    private String userPhone;
    private String verificationCode;
    private String verificationMarkFromNet;
    private String password;
    private String referPhoneNumber;
    @Override
    public int getContentViewId() {
        return R.layout.activity_set_password;
    }

    @Override
    protected void initData() {
        userPhone = getIntent().getStringExtra("tel");
        verificationCode = getIntent().getStringExtra("code");
        verificationMarkFromNet = getIntent().getStringExtra("codeNet");
        referPhoneNumber  = getIntent().getStringExtra("num");
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }


    @OnClick({R.id.iv_activity_register_close, R.id.iv_activity_register_pwShow, R.id.iv_activity_register_pwShow1, R.id.tv_activity_set_password_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_activity_register_close:
                finish();
                break;
            case R.id.iv_activity_register_pwShow:
                PasswordUtil.setPasswordShow(edtActivityRegisterPassword, ivActivityRegisterPwShow, isPwdVisible);
                isPwdVisible = !isPwdVisible;
                break;
            case R.id.iv_activity_register_pwShow1:
                PasswordUtil.setPasswordShow(edtActivityRegisterPassword1, ivActivityRegisterPwShow1, isPwdVisible1);
                isPwdVisible1 = !isPwdVisible1;
                break;
            case R.id.tv_activity_set_password_submit:
                register2Net();
                break;
        }
    }

    /**
     * 去服务器注册
     */
    private void register2Net() {
        password = edtActivityRegisterPassword.getText().toString().trim();
        if (!ToolsUtils.isDigitalAndWord(password)) {
            showToast(getString(R.string.illegal_password));
            return;
        }
        if (!password.equals(edtActivityRegisterPassword1.getText().toString().trim())){
            showToast("密码与确认密码不一致");
            return;
        }
        /*如果输入的是手机号则去验证，开启倒计时*/
        if (!ToolsUtils.isPhone(userPhone)) {
            showToast(getString(R.string.illegal_phone));
            return;
        }
        if (TextUtils.isEmpty(verificationMarkFromNet)) {
            showToast(R.string.getVerificationFirst);
            return;
        }
        if (!ToolsUtils.isVerificationCode(verificationCode)) {
            showToast(getString(R.string.illegal_verification));
            return;
        }



        showLoading(edtActivityRegisterPassword1);
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
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
                hideLoading();
            }
        });
    }
}
