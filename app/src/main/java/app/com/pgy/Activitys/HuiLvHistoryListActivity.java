package app.com.pgy.Activitys;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.EasyRecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.com.pgy.Activitys.Base.BaseListActivity;
import app.com.pgy.Adapters.HuiLvHistoryAdapter;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Constants.StaticDatas;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Models.Beans.OdinHistoryRankBean;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Utils.TimeUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static app.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * Create by YX on 2019/9/28 0028
 */
public class HuiLvHistoryListActivity extends BaseListActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.activity_baselist_list)
    EasyRecyclerView recyclerView;

    HuiLvHistoryAdapter adapter;


    @Override
    public int getContentViewId() {
        return R.layout.activity_huilv_history;
    }

    @Override
    protected void initData() {
        if (adapter == null){
            adapter = new HuiLvHistoryAdapter(this);
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        tvTitle.setText("历史汇率");
        init(recyclerView,adapter);
    }

    @Override
    protected void onListItemClick(int position) {

    }

    @Override
    public void onRefresh() {
        adapter.clear();
        pageIndex = StaticDatas.PAGE_START;
        if (!checkNetworkState()) {
            showToast(R.string.notHaveNet);
            recyclerView.setRefreshing(false);
            return;
        }
        requestData();
    }


    @Override
    public void onLoadMore() {
        if (!checkNetworkState()) {
            showToast(R.string.notHaveNet);
            recyclerView.setRefreshing(false);
            return;
        }
        pageIndex++;
        requestData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }

    private void requestData(){
        showLoading(null);
        Map<String, Object> map = new HashMap<>();
        map.put("page", pageIndex);
        map.put("rows", StaticDatas.PAGE_SIZE);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());

        NetWorks.getOdinHistoryRankList(Preferences.getAccessToken(), map, new getBeanCallback<List<OdinHistoryRankBean>>() {
            @Override
            public void onSuccess(List<OdinHistoryRankBean> odinHistoryRankBean) {
                hideLoading();
                if (odinHistoryRankBean == null || odinHistoryRankBean.size() <= 0) {
                    /*再无更多数据*/
                    adapter.stopMore();
                    return;
                }
//                adapter.addAll(odinHistoryRankBean);
            }

            @Override
            public void onError(int errorCode, String reason) {
                hideLoading();
                onFail(errorCode,reason);
                adapter.pauseMore();
            }
        });
    }
}
