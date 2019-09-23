package app.com.pgy.Activitys;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.HashMap;
import java.util.Map;
import butterknife.BindView;
import butterknife.OnClick;
import app.com.pgy.Activitys.Base.BaseActivity;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Constants.StaticDatas;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Interfaces.getStringCallback;
import app.com.pgy.Models.Beans.User;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Utils.EdittextUtils;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Utils.TimeUtils;
import app.com.pgy.Widgets.PersonalItemInputView;

import static app.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * Created by YX on 2018/7/9.
 */

public class BindBankActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.piiv_activity_bind_bankcard_bankName)
    PersonalItemInputView piiv_bankName;
    @BindView(R.id.piiv_activity_bind_bankcard_branchBankName)
    PersonalItemInputView piiv_branchName;
    @BindView(R.id.piiv_activity_bind_bankcard_userName)
    PersonalItemInputView piiv_userName;
    @BindView(R.id.piiv_activity_bind_bankcard_cardNum)
    PersonalItemInputView piiv_cardNum;
    @BindView(R.id.tv_activity_bind_bankcard_submit)
    TextView tv_submit;

    private boolean isBindCard;

    /**银行卡信息，开户行、支行、开户姓名、银行卡号*/
    private String bankName, branchName, userName, cardNum;

    @Override
    public int getContentViewId() {
        return R.layout.activity_bind_bankcard;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        tv_title.setText("绑定银行卡");
        piiv_bankName.getEdt().setFilters(new InputFilter[]{EdittextUtils.getNoEmoji(getApplicationContext())});
        piiv_branchName.getEdt().setFilters(new InputFilter[]{EdittextUtils.getNoEmoji(getApplicationContext())});
        piiv_cardNum.getEdt().setInputType(InputType.TYPE_CLASS_NUMBER);
        piiv_userName.getEdt().setFilters(new InputFilter[]{EdittextUtils.getNoEmoji(getApplicationContext()),new InputFilter.LengthFilter(10)});

    }

    @Override
    protected void initData() {
        /*是否绑定支付宝*/
        User.BindInfoModel cardPayInfo = Preferences.getUserPayInfo(StaticDatas.BANKCARD);
        if (cardPayInfo == null){
            return;
        }
        /*如果有绑定信息，则设置在界面上*/
        bankName = cardPayInfo.getBankName();
        piiv_bankName.setRightTxt(bankName);
        branchName = cardPayInfo.getBranchName();
        piiv_branchName.setRightTxt(branchName);
        userName = cardPayInfo.getName();
        piiv_userName.setRightTxt(userName);
        cardNum = cardPayInfo.getAccount();
        piiv_cardNum.setRightTxt(cardNum);
        isBindCard = !TextUtils.isEmpty(cardPayInfo.getAccount());
        tv_submit.setText(isBindCard ?"修改绑定信息":"确认绑定");

    }

    @OnClick({R.id.iv_back,R.id.tv_activity_bind_bankcard_submit})
    public void onViewClick(View v){
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_activity_bind_bankcard_submit:
                submit();
                break;
        }
    }
    /**
     * 提交三方充值
     */
    private void submit() {
        /*获取输入的内容*/
        bankName = piiv_bankName.getRightTxt();
        if (TextUtils.isEmpty(bankName)) {
            showToast("请输入开户行");
            return;
        }
        branchName = piiv_branchName.getRightTxt();
        if (TextUtils.isEmpty(branchName)) {
            showToast("请输入所在支行信息");
            return;
        }
        userName = piiv_userName.getRightTxt();
        if (TextUtils.isEmpty(userName)) {
            showToast("请填写开户姓名");
            return;
        }
        cardNum = piiv_cardNum.getRightTxt();
        if (TextUtils.isEmpty(cardNum)) {
            showToast("请输入银行卡号");
            return;
        }
        if (cardNum.length() < 16) {
            showToast("银行卡号长度不能小于16位");
            return;
        }
         /*弹出输入交易密码对话框*/
        showPayDialog(new getStringCallback() {
            @Override
            public void getString(String string) {
                submitBind2Net(string);
            }
        });
    }

    /**
     * 去服务器添加银行卡
     */
    private void submitBind2Net(String tradePwd) {
        showLoading(tv_submit);
        Map<String, Object> map = new HashMap<>();
        map.put("name", userName);
        map.put("account", cardNum);
        map.put("bankName", bankName);
        map.put("password", tradePwd);
        map.put("branchName", branchName);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.bindCard(Preferences.getAccessToken(), map, new getBeanCallback() {
            @Override
            public void onSuccess(Object o) {
                showToast("绑定银行卡成功");
                LogUtils.w(TAG, "绑定银行卡成功");
                User.BindInfoModel infoModel = new User.BindInfoModel();
                infoModel.setAccount(cardNum);
                infoModel.setBankName(bankName);
                infoModel.setBranchName(branchName);
                infoModel.setName(userName);
                Preferences.saveUserPay_Card(infoModel);
                hideLoading();
                BindBankActivity.this.finish();
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
    }
}
