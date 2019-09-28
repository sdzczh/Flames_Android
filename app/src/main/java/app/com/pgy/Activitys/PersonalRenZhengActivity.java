package app.com.pgy.Activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import app.com.pgy.Activitys.Base.BaseActivity;
import app.com.pgy.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
}
