package app.com.pgy.Activitys;

import android.os.Bundle;
import android.widget.TextView;

import com.jude.easyrecyclerview.EasyRecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import app.com.pgy.Activitys.Base.BaseListActivity;
import app.com.pgy.Adapters.OdinHistoryRankListAdapter;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Constants.StaticDatas;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Models.Beans.OdinHistoryRankBean;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Utils.TimeUtils;

import static app.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * *  @Description:描述
 * *  @Author: EDZ
 * *  @CreateDate: 2019/7/22 8:42
 */
public class InVitationHistoryActivity extends BaseListActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.activity_baselist_list)
    EasyRecyclerView recyclerView;

    OdinHistoryRankListAdapter adapter;

//    /**
//     * 请求数据的页码，从0开始即第一页，每页的数据由后台定
//     */
//    protected int pageIndex = StaticDatas.PAGE_START;

    @Override
    protected void initData() {
        if (adapter == null){
            adapter = new OdinHistoryRankListAdapter(mContext);
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        tvTitle.setText("历史邀请支付榜");
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
    public int getContentViewId() {
        return R.layout.activity_base_list;
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
                adapter.addAll(odinHistoryRankBean);
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
