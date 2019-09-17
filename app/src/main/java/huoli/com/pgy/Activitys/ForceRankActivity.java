package huoli.com.pgy.Activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.EasyRecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import huoli.com.pgy.Activitys.Base.BaseListActivity;
import huoli.com.pgy.Adapters.ForceRankListAdapter;
import huoli.com.pgy.Constants.Preferences;
import huoli.com.pgy.Constants.StaticDatas;
import huoli.com.pgy.Interfaces.getBeanCallback;
import huoli.com.pgy.Models.Beans.ForceRankInfo;
import huoli.com.pgy.NetUtils.NetWorks;
import huoli.com.pgy.R;
import huoli.com.pgy.Utils.TimeUtils;

import static huoli.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * Created by YX on 2018/7/16.
 */

public class ForceRankActivity extends BaseListActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rv_activity_force_rank)
    EasyRecyclerView rvActivityForceRank;

    private ForceRankListAdapter mAdapter;

    @Override
    public int getContentViewId() {
        return R.layout.activity_force_rank;
    }

    @Override
    protected void initData() {
        if (mAdapter == null){
            mAdapter = new ForceRankListAdapter(this);
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        tvTitle.setText("算力排行榜");
        init(rvActivityForceRank,mAdapter);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void requestData(int index) {
        showLoading(null);
        Map<String, Object> map = new HashMap<>();
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        map.put("page", index);
        map.put("rows", StaticDatas.PAGE_SIZE);
        NetWorks.getForceRank(Preferences.getAccessToken(), map, new getBeanCallback<ForceRankInfo>() {
            @Override
            public void onSuccess(ForceRankInfo forceDetail) {
                hideLoading();
                if (forceDetail == null) {
                    forceDetail = new ForceRankInfo();
                }
                /*将获取的算力详情设置到界面上*/
                List<ForceRankInfo.ListBean> list = forceDetail.getList();
                if (list == null || list.size() <= 0) {
                    /*再无更多数据*/
                    mAdapter.stopMore();
                    return;
                }
                mAdapter.addAll(list);
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
                /*网络错误*/
                hideLoading();
                mAdapter.pauseMore();
            }
        });
    }
    @Override
    protected void onListItemClick(int position) {

    }

    @Override
    public void onRefresh() {
        mAdapter.clear();
        if (!isLogin()){
            return;
        }
        pageIndex = StaticDatas.PAGE_START;
        if (!checkNetworkState()) {
            showToast(R.string.notHaveNet);
            rvActivityForceRank.setRefreshing(false);
            return;
        }
        requestData(pageIndex);
    }

    @Override
    public void onLoadMore() {
        if (!isLogin()){
            mAdapter.clear();
            return;
        }
        if (!checkNetworkState()) {
            showToast(R.string.notHaveNet);
            rvActivityForceRank.setRefreshing(false);
            return;
        }
        pageIndex++;
        requestData(pageIndex);
    }

}
