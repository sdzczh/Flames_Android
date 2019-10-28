package app.com.pgy.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import app.com.pgy.Activitys.Base.BaseActivity;
import app.com.pgy.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Create by YX on 2019/9/28 0028
 */
public class HuiLvActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.tv_activity_huilv_value)
    TextView tvActivityHuilvValue;

    @Override
    public int getContentViewId() {
        return R.layout.activity_huilv;
    }

    @Override
    protected void initData() {
        tvTitle.setText("汇率概览");
//        tvTitleRight.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }
    @OnClick({R.id.iv_back,R.id.tv_title_right})
    public void onViewClicked(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_title_right:
                Intent intent = new Intent(mContext,HuiLvHistoryListActivity.class);
                startActivity(intent);
                break;
        }
    }
}
