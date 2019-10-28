package app.com.pgy.Activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import app.com.pgy.Activitys.Base.BaseActivity;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Models.Beans.RenZhengBean;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Utils.MathUtils;
import app.com.pgy.Utils.TimeUtils;
import app.com.pgy.Utils.Utils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static app.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * Create by YX on 2019/9/27 0027
 */
public class PersonalRenZhengActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ll_activity_renzheng_one_finished)
    LinearLayout llActivityRenzhengOneFinished;
    @BindView(R.id.tv_activity_renzheng_to1)
    TextView tvActivityRenzhengTo1;
    @BindView(R.id.ll_activity_renzheng_two_finished)
    LinearLayout llActivityRenzhengTwoFinished;
    @BindView(R.id.tv_activity_renzheng_to2)
    TextView tvActivityRenzhengTo2;
    @BindView(R.id.tv_activity_renzheng_desc2)
    TextView tvActivityRenzhengDesc2;
    @BindView(R.id.ll_activity_renzheng_three_finished)
    LinearLayout llActivityRenzhengThreeFinished;
    @BindView(R.id.tv_activity_renzheng_to3)
    TextView tvActivityRenzhengTo3;
    @BindView(R.id.tv_activity_renzheng_desc3)
    TextView tvActivityRenzhengDesc3;
    @BindView(R.id.tv_activity_renzheng_name)
    TextView tvActivityRenzhengName;
    @BindView(R.id.tv_activity_renzheng_tel)
    TextView tvActivityRenzhengTel;
    @BindView(R.id.ll_activity_renzheng_info)
    LinearLayout llActivityRenzhengInfo;
    @BindView(R.id.tv_activity_renzheng_content1)
    TextView tvActivityRenzhengContent1;
    @BindView(R.id.tv_activity_renzheng_content2)
    TextView tvActivityRenzhengContent2;
    @BindView(R.id.tv_activity_renzheng_content3)
    TextView tvActivityRenzhengContent3;

    @Override
    public int getContentViewId() {
        return R.layout.activity_personal_renzheng;
    }

    @Override
    protected void initData() {
        tvTitle.setText("身份认证");
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        initRezheng();
    }

    @OnClick({R.id.iv_back, R.id.tv_activity_renzheng_to1, R.id.tv_activity_renzheng_to2, R.id.tv_activity_renzheng_to3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_activity_renzheng_to1:
                break;
            case R.id.tv_activity_renzheng_to2:
                break;
            case R.id.tv_activity_renzheng_to3:
                break;
        }
    }

    private void initRezheng() {
        showLoading(null);
        Map<String, Object> map = new HashMap<>();
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.initRenZheng(Preferences.getAccessToken(), map, new getBeanCallback<RenZhengBean>() {
            @Override
            public void onSuccess(RenZhengBean o) {
                hideLoading();
                updateView(o);
            }

            @Override
            public void onError(int errorCode, String reason) {
                hideLoading();
                onFail(errorCode, reason);
                showToast("提交失败");
            }
        });
    }

    private void updateView(RenZhengBean renZhengBean){
        if (renZhengBean != null){
            llActivityRenzhengInfo.setVisibility(View.VISIBLE);
            tvActivityRenzhengName.setText(renZhengBean.getUserName());
            tvActivityRenzhengTel.setText(Utils.getSecretPhoneNum(renZhengBean.getPhone()));
            tvActivityRenzhengContent1.setText("认证后可以提币，24小时限额 "+renZhengBean.getWithdrawQuota1()+" "+"\\n认证后可以法币交易，单笔限额 "+renZhengBean.getC2cQuota1()+" CNY");
            tvActivityRenzhengContent2.setText("增加提币额度，24小时限额 "+renZhengBean.getWithdrawQuota2()+" "+"\\n增加法币交易额度，单笔限额 "+renZhengBean.getC2cQuota2()+" CNY");
            tvActivityRenzhengContent3.setText("增加提币额度，24小时限额 "+renZhengBean.getWithdrawQuota3()+" "+"\\n增加法币交易额度，单笔限额 "+renZhengBean.getC2cQuota3()+" CNY");
            //身份认证等级 0未认证 1一级 2二级 3三级
            tvActivityRenzhengTo1.setVisibility(View.GONE);
            llActivityRenzhengOneFinished.setVisibility(View.GONE);
            tvActivityRenzhengTo2.setVisibility(View.GONE);
            llActivityRenzhengTwoFinished.setVisibility(View.GONE);
            tvActivityRenzhengDesc2.setVisibility(View.GONE);
            tvActivityRenzhengTo3.setVisibility(View.GONE);
            llActivityRenzhengThreeFinished.setVisibility(View.GONE);
            tvActivityRenzhengDesc3.setVisibility(View.GONE);
            if (renZhengBean.getAuthState() == 0){
                tvActivityRenzhengTo1.setVisibility(View.VISIBLE);
                tvActivityRenzhengDesc2.setVisibility(View.VISIBLE);
                tvActivityRenzhengDesc3.setVisibility(View.VISIBLE);
            }else if (renZhengBean.getAuthState() == 1){
                llActivityRenzhengOneFinished.setVisibility(View.VISIBLE);
                tvActivityRenzhengTo2.setVisibility(View.VISIBLE);
                tvActivityRenzhengDesc3.setVisibility(View.VISIBLE);
            }else if (renZhengBean.getAuthState() == 2){
                llActivityRenzhengOneFinished.setVisibility(View.VISIBLE);
                llActivityRenzhengTwoFinished.setVisibility(View.VISIBLE);
                tvActivityRenzhengTo3.setVisibility(View.VISIBLE);
            }else if (renZhengBean.getAuthState() == 3){
                llActivityRenzhengOneFinished.setVisibility(View.VISIBLE);
                llActivityRenzhengTwoFinished.setVisibility(View.VISIBLE);
                llActivityRenzhengThreeFinished.setVisibility(View.VISIBLE);
            }
        }
    }
}
