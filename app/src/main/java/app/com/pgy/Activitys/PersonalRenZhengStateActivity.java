package app.com.pgy.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import app.com.pgy.Activitys.Base.BaseActivity;
import app.com.pgy.Models.Beans.EventBean.EventMainChangeState;
import app.com.pgy.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static app.com.pgy.Models.Beans.EventBean.EventMainChangeState.CHANGE_TO_C2C;

/**
 * Create by Android on 2019/10/31 0031
 */
public class PersonalRenZhengStateActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_state_state)
    TextView tvStateState;
    @BindView(R.id.tv_state_statedesc)
    TextView tvStateStatedesc;
    @BindView(R.id.tv_state_tofabi)
    TextView tvStateTofabi;
    @BindView(R.id.tv_state_tosecond)
    TextView tvStateTosecond;

    private int index = 1;
    private int state = 0;
    @Override
    public int getContentViewId() {
        return R.layout.activity_renzheng_first_state;
    }

    @Override
    protected void initData() {
        index = getIntent().getIntExtra("index",1);
        state = getIntent().getIntExtra("state",0);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (index == 1){
            tvTitle.setText("Lv.1 基础认证");
            tvStateTofabi.setText("去法币交易");
            tvStateTosecond.setText("开始Lv.2 高级认证");
        }else if (index == 2){
            tvTitle.setText("Lv.2 高级认证");
            tvStateTofabi.setText("去法币交易");
            tvStateTosecond.setText("开始Lv.3 视频认证");
        }else if (index == 3){
            tvTitle.setText("Lv.3 视频认证");
            tvStateTofabi.setText("去法币交易");
            tvStateTosecond.setVisibility(View.INVISIBLE);
        }
        if (state == 0){

        }

    }

    @OnClick({R.id.iv_back, R.id.tv_state_tofabi, R.id.tv_state_tosecond})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_state_tofabi:
                EventBus.getDefault().post(new EventMainChangeState(CHANGE_TO_C2C));
                Intent intent1 = new Intent();
                intent1.putExtra("state",0);
                setResult(RESULT_OK,intent1);
                finish();
                break;
            case R.id.tv_state_tosecond:
                Intent intent = new Intent();
                intent.putExtra("state",1);
                setResult(RESULT_OK,intent);
                finish();
                break;
        }
    }
}
