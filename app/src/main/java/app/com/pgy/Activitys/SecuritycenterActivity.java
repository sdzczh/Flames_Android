package app.com.pgy.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.security.rp.RPSDK;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import app.com.pgy.Activitys.Base.BaseActivity;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Constants.StaticDatas;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Models.Beans.EventBean.EventRealName;
import app.com.pgy.Models.Beans.RealNameResult;
import app.com.pgy.Models.Beans.StringNameBean;
import app.com.pgy.Models.Beans.User;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Utils.LoginUtils;
import app.com.pgy.Utils.TimeUtils;
import app.com.pgy.Widgets.PersonalItemView;

import static app.com.pgy.Constants.StaticDatas.BANKCARD;
import static app.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * Created by YX on 2018/7/7.
 */

public class SecuritycenterActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.piv_activity_securitycenter_realname)
    PersonalItemView piv_realName;
    @BindView(R.id.piv_activity_securitycenter_loginPw)
    PersonalItemView piv_loginPw;
    @BindView(R.id.piv_activity_securitycenter_tradePw)
    PersonalItemView piv_tradePw;
    @BindView(R.id.piv_activity_securitycenter_bindWx)
    PersonalItemView piv_bindWx;
    @BindView(R.id.piv_activity_securitycenter_bindAli)
    PersonalItemView piv_bindAli;
    @BindView(R.id.piv_activity_securitycenter_bindBank)
    PersonalItemView piv_bindBank;

    private String taskId;
    /**
     * 各种状态
     */
    private boolean isSetTradePwd, isBindAli, isBindWx, isBindCard, isRealName;

    @Override
    public int getContentViewId() {
        return R.layout.activity_securitycenter;
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
        tv_title.setText("安全中心");
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshView();
    }

    /**
     * 根据状态修改界面
     */
    private void refreshView() {
        /*是否设置交易密码*/
        isSetTradePwd = Preferences.getLocalUser().isOrderPwdFlag();
        /*是否绑定支付宝*/
        User.BindInfoModel aliPayInfo = Preferences.getUserPayInfo(StaticDatas.ALIPAY);
        isBindAli = !((aliPayInfo == null) || TextUtils.isEmpty(aliPayInfo.getAccount()));
        /*是否绑定微信*/
        User.BindInfoModel wechartPayInfo = Preferences.getUserPayInfo(StaticDatas.WECHART);
        isBindWx = !((wechartPayInfo == null) || TextUtils.isEmpty(wechartPayInfo.getAccount()));
        /*是否绑定银行卡*/
        User.BindInfoModel cardPayInfo = Preferences.getUserPayInfo(BANKCARD);
        isBindCard = !((cardPayInfo == null) || TextUtils.isEmpty(cardPayInfo.getAccount()));
        /*是否已经实名认证*/
        isRealName = Preferences.getLocalUser().isIdCheckFlag();
        /*根据状态修改界面*/
        piv_realName.setRightTxt(isRealName?"已完成":"");
        piv_loginPw.setRightTxt("修改");
        piv_tradePw.setRightTxt(isSetTradePwd?"修改":"设置");

        piv_bindWx.setRightTxt(isBindWx?"已绑定":" ");
        piv_bindAli.setRightTxt(isBindAli?"已绑定":" ");
        piv_bindBank.setRightTxt(isBindCard?"已绑定":" ");

    }

    @OnClick({R.id.iv_back,R.id.piv_activity_securitycenter_loginPw,R.id.piv_activity_securitycenter_tradePw,
            R.id.piv_activity_securitycenter_realname,R.id.piv_activity_securitycenter_bindWx,R.id.piv_activity_securitycenter_bindAli,
            R.id.piv_activity_securitycenter_bindBank})
    public void onViewClick(View v){
        Intent intent = null;
        Bundle bundle = null;
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.piv_activity_securitycenter_loginPw:
                //跳转修改登录密码
                intent = new Intent(mContext,ChangeLoginPwActivity.class);
                break;
            case R.id.piv_activity_securitycenter_tradePw:
                //跳转修改交易密码
                intent = new Intent(mContext,ChangeTradePwActivity.class);
                break;
            case R.id.piv_activity_securitycenter_realname:
                // 跳转实名认证
                if (LoginUtils.isLogin(this)){
                    if (Preferences.getLocalUser().isIdCheckFlag()) {
                        showToast("您已完成实名认证");
                        return;
                    }
                    start2RealName();
                }
                break;
            case R.id.piv_activity_securitycenter_bindWx:
                // 跳转绑定微信
                intent = new Intent(mContext,BindWeixinActivity.class);
                break;
            case R.id.piv_activity_securitycenter_bindAli:
                //  跳转绑定支付宝
                intent = new Intent(mContext,BindAliActivity.class);
                break;
            case R.id.piv_activity_securitycenter_bindBank:
                intent = new Intent(mContext,BindBankActivity.class);
                //  跳转绑定银行卡
                break;
        }
        if (intent != null && LoginUtils.isLogin(this)){
            if (bundle != null){
                intent.putExtras(bundle);
            }
            startActivity(intent);
        }
    }


    /**
     * 去请求实名认证token
     */
    private void start2RealName() {
        Map<String, Object> map = new HashMap<>();
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.getRealNameToken(Preferences.getAccessToken(), map, new getBeanCallback<RealNameResult>() {
            @Override
            public void onSuccess(RealNameResult realNameResult) {
                if (realNameResult == null) {
                    realNameResult = new RealNameResult();
                }
                LogUtils.w("realName", "start2RealName:" + realNameResult.toString());
                LogUtils.w(TAG, realNameResult.toString());
                String verifyToken = realNameResult.getToken();
                taskId = realNameResult.getTaskId();
                if (TextUtils.isEmpty(verifyToken)) {
                    showToast("获取token失败");
                    return;
                }
                start2Certification(verifyToken);
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
                /*网络错误*/
            }
        });
    }

    /**
     * 开始认证
     */
    private void start2Certification(String verifyToken) {
        RPSDK.start(verifyToken, mContext, new RPSDK.RPCompletedListener() {
            @Override
            public void onAuditResult(RPSDK.AUDIT audit) {
                LogUtils.w("realName", "阿里认证结果：" + audit);
                if (TextUtils.isEmpty(taskId)) {
                    return;
                }
                getRealNameState(taskId);
                if (audit == RPSDK.AUDIT.AUDIT_PASS) { //认证通过
                    //showToast("认证通过");

                } else if (audit == RPSDK.AUDIT.AUDIT_FAIL) { //认证不通过
                    //showToast("认证不通过");
                } else if (audit == RPSDK.AUDIT.AUDIT_NOT) { //未认证，用户取消
                    //showToast("未认证，用户取消");
                }
            }

        });
    }

    private void getRealNameState(String taskId) {
        showLoading(null);
        Map<String, Object> map = new HashMap<>();
        map.put("taskId", taskId);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.getRealNameStatus(Preferences.getAccessToken(), map, new getBeanCallback<StringNameBean>() {
            @Override
            public void onSuccess(StringNameBean realNameStatus) {
                hideLoading();
                showToast("实名认证成功");
                LogUtils.w("realName", "getRealNameState：" + realNameStatus.toString());
                Preferences.saveUserName(realNameStatus.getName());
                LogUtils.w("realName", "userName:" + realNameStatus.getName());
                Preferences.setIsHasRealName(true);
                EventBus.getDefault().post(new EventRealName(true));
            }

            @Override
            public void onError(int errorCode, String reason) {
                hideLoading();
                onFail(errorCode, reason);
                /*网络错误*/
            }
        });

    }

}
