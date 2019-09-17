package huoli.com.pgy.Activitys;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import huoli.com.pgy.Activitys.Base.BaseActivity;
import huoli.com.pgy.Constants.Preferences;
import huoli.com.pgy.Interfaces.getBeanCallback;
import huoli.com.pgy.Models.Beans.EventBean.EventC2cEntrustList;
import huoli.com.pgy.NetUtils.NetWorks;
import huoli.com.pgy.R;
import huoli.com.pgy.Utils.LogUtils;
import huoli.com.pgy.Utils.TimeUtils;

import static huoli.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/***
 * C2C委托详情申诉界面
 * @author xuqingzhong
 */

public class C2CEntrustComplaintActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.activity_ccEntrustComplaint_input)
    EditText activityCcEntrustComplaintInput;
    @BindView(R.id.activity_ccEntrustComplaint_submit)
    TextView activityCcEntrustComplaintSubmit;
    private int orderId;
    private String content;

    @Override
    public int getContentViewId() {
        return R.layout.activity_c2c_entrust_complaint;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {
        orderId = getIntent().getIntExtra("orderId",-1);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        tvTitle.setText("申诉");
    }

    /**
     * 申诉客服
     */
    private void appeal2Net() {
        showLoading(activityCcEntrustComplaintSubmit);
        Map<String, Object> map = new HashMap<>();
        map.put("orderId",orderId);
        map.put("reason",content);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.c2cEntrustAppeal(Preferences.getAccessToken(), map, new getBeanCallback() {
            @Override
            public void onSuccess(Object o) {
                hideLoading();
                showToast("申诉成功");
                LogUtils.w(TAG, "申诉成功");
                EventBus.getDefault().post(new EventC2cEntrustList(true));
                C2CEntrustComplaintActivity.this.finish();
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
                hideLoading();
            }
        });
    }

    @OnClick({R.id.iv_back, R.id.activity_ccEntrustComplaint_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            /*返回*/
            case R.id.iv_back:
                C2CEntrustComplaintActivity.this.finish();
                break;
                /*提交申诉*/
            case R.id.activity_ccEntrustComplaint_submit:
                if (!isLogin()){
                    showToast(R.string.unlogin);
                    return;
                }
                if (orderId < 0){
                    showToast("订单不存在");
                    return;
                }
                content = activityCcEntrustComplaintInput.getText().toString();
                if (TextUtils.isEmpty(content)){
                    showToast("请输入申诉理由");
                    return;
                }
                appeal2Net();
                break;
                default:break;
        }
    }
}
