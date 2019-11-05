package app.com.pgy.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import app.com.pgy.Activitys.Base.BaseActivity;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Models.Beans.EventBean.EventRealName;
import app.com.pgy.Models.Beans.User;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Utils.TimeUtils;
import app.com.pgy.Widgets.PersonalItemInputView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static app.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * Create by Android on 2019/10/31 0031
 */
public class PersonalRenZhengFirstActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.piiv_activity_renzheng_name)
    PersonalItemInputView piivActivityRenzhengName;
    @BindView(R.id.piiv_activity_renzheng_code)
    PersonalItemInputView piivActivityRenzhengCode;
    @BindView(R.id.tv_activity_renzheng_submit)
    TextView tvActivityRenzhengSubmit;

    @Override
    public int getContentViewId() {
        return R.layout.activity_renzheng_first;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        tvTitle.setText("Lv.1 基础认证");
    }

    @OnClick({R.id.iv_back, R.id.tv_activity_renzheng_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_activity_renzheng_submit:
                submit();
                break;
        }
    }

    private void submit() {
        /*获取输入的内容*/
        final String userName = piivActivityRenzhengName.getRightTxt();
        if (TextUtils.isEmpty(userName)) {
            showToast("请输入姓名");
            return;
        }
        String cardNum = piivActivityRenzhengCode.getRightTxt();
        if (TextUtils.isEmpty(cardNum)) {
            showToast("请输入身份证号");
            return;
        }
        showLoading(tvTitle);
        Map<String, Object> map = new HashMap<>();
        map.put("userName", userName);
        map.put("idCardNumber", cardNum);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.renzhengFirst(Preferences.getAccessToken(), map, new getBeanCallback() {
            @Override
            public void onSuccess(Object o) {
                Preferences.saveUserRealName(userName);
                if (Preferences.getUserIdStatus() < 1){
                    Preferences.saveUserIdStatus(1);
                }
                EventBus.getDefault().post(new EventRealName(true));
                toStateActivity(true);

            }

            @Override
            public void onError(int errorCode, String reason) {
                hideLoading();
//                onFail(errorCode, reason);
                showToast(reason);

                if (errorCode == 30039){
//                    toStateActivity(false);
//                    showToast(reason);
                }else {

                }
            }
        });
    }

    private void toStateActivity(boolean success){
        Intent intent = new Intent(mContext,PersonalRenZhengStateActivity.class);
        intent.putExtra("index",1);
        intent.putExtra("state",success?1:0);
        startActivityForResult(intent,600);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 600 && resultCode == RESULT_OK){
            setResult(RESULT_OK,data == null?null:data);
            finish();
        }
    }
}
