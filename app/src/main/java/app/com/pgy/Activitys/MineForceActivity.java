package app.com.pgy.Activitys;

import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import app.com.pgy.Activitys.Base.BaseActivity;
import app.com.pgy.Adapters.ForceLeverListAdapter;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Models.Beans.Configuration;
import app.com.pgy.Models.Beans.EventBean.EventForceUpBanner;
import app.com.pgy.Models.MyforceInfo;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Utils.ImageLoaderUtils;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Utils.MathUtils;
import app.com.pgy.Utils.TimeUtils;
import app.com.pgy.Utils.Utils;

import static app.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * Created by YX on 2018/7/16.
 */

public class MineForceActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_activity_mine_force_current_score)
    TextView tvCurrentScore;
    @BindView(R.id.tv_activity_mine_force_currentRank)
    TextView tvActivityForceRankCurrentRank;
    @BindView(R.id.tv_activity_mine_force_nextneed)
    TextView tvActivityForceRankNextneed;
    @BindView(R.id.rv_activity_mine_force_list)
    RecyclerView rvActivityForceRankList;
    @BindView(R.id.iv_activity_mine_force_currentIcon)
    ImageView ivActivityMineForceCurrentIcon;
    ForceLeverListAdapter adapter;
    @Override
    public int getContentViewId() {
        return R.layout.activity_mine_force;
    }

    @Override
    protected void initData() {
        if (adapter == null){
            adapter = new ForceLeverListAdapter(mContext);
        }
        /*初始化算力等级划分的Grid布局*/
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        rvActivityForceRankList.setLayoutManager(layoutManager);
        /*获取所有算力等级详情*/
        List<Configuration.CalculateForceLevel> forceLevels = getForceLevels();
        adapter.addAll(forceLevels);
        rvActivityForceRankList.setAdapter(adapter);
        /*获取当前算力等级的详情*/
        int currentLevel = Preferences.getCurrentLevel();
        Configuration.CalculateForceLevel levelDetail = getLevelDetail(currentLevel);
        LogUtils.w(TAG,"levelDetail:"+levelDetail);
        int end = levelDetail.getSoulmaxforce();
        ImageLoaderUtils.display(mContext,ivActivityMineForceCurrentIcon,levelDetail.getRolepicurl(),R.mipmap.level_1);
        /*当前算力*/
        int currentForce = getIntent().getIntExtra("force",0);
        tvCurrentScore.setText(MathUtils.int2String(currentForce));
        tvActivityForceRankNextneed.setText(MathUtils.int2String((end-currentForce)));
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        tvTitle.setText("我的算力");
        getMineForce();
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }


    @OnClick({R.id.iv_back,R.id.tv_activity_mine_force_info,R.id.tv_activity_mine_force_levels
            ,R.id.tv_activity_mine_force_rank,R.id.tv_activity_mine_force_upForce})
    public void onViewClick(View view){
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_activity_mine_force_info:
                Utils.IntentUtils(mContext, ForceInfoListActivity.class);
                break;
            case R.id.tv_activity_mine_force_rank:
                Utils.IntentUtils(mContext, ForceRankActivity.class);
                break;
            case R.id.tv_activity_mine_force_upForce:
                Utils.IntentUtils(mContext, ForceScoreUpActivity.class);
                finish();
                break;
        }
    }

    private void getMineForce() {
        showLoading(null);
        Map<String, Object> map = new HashMap<>();
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.getMyCalculateForce(Preferences.getAccessToken(), map, new getBeanCallback<MyforceInfo>() {
            @Override
            public void onSuccess(MyforceInfo forceDetail) {
                hideLoading();
                if (forceDetail == null) {
                    forceDetail = new MyforceInfo();
                }
                tvActivityForceRankCurrentRank.setText("" + MathUtils.int2String(forceDetail.getRank()));
                tvActivityForceRankNextneed.setText(""+MathUtils.int2String(forceDetail.getDifferForce()));
                tvCurrentScore.setText(""+MathUtils.int2String(forceDetail.getCurrentForce()));
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
                /*网络错误*/
                hideLoading();
                adapter.pauseMore();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void EventForcBanner(EventForceUpBanner event){
        if (event != null && event.getType() > 0){
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }
}
