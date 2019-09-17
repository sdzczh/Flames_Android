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
import huoli.com.pgy.Adapters.ForceInfoListAdapter;
import huoli.com.pgy.Constants.Preferences;
import huoli.com.pgy.Constants.StaticDatas;
import huoli.com.pgy.Interfaces.getBeanCallback;
import huoli.com.pgy.Models.Beans.CalculateDetail;
import huoli.com.pgy.NetUtils.NetWorks;
import huoli.com.pgy.R;
import huoli.com.pgy.Utils.TimeUtils;

import static huoli.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * Created by YX on 2018/7/16.
 */

public class ForceInfoListActivity extends BaseListActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rv_activity_force_info_list)
    EasyRecyclerView rvActivityForceInfoList;

    ForceInfoListAdapter adapter;
    @Override
    public int getContentViewId() {
        return R.layout.activity_force_info_list;
    }

    @Override
    protected void initData() {
        if (adapter == null){
            adapter = new ForceInfoListAdapter(mContext);
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        tvTitle.setText("算力详情");
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
 /*继承BaseListFragment，初始化*/
        init(rvActivityForceInfoList, adapter);
    }

    @Override
    protected void onListItemClick(int position) {

    }
    /**下拉刷新*/
    @Override
    public void onRefresh() {
        adapter.clear();
        if (!isLogin()){
            return;
        }
        pageIndex = StaticDatas.PAGE_START;
        if (!checkNetworkState()){
            showToast(R.string.notHaveNet);
            rvActivityForceInfoList.setRefreshing(false);
            return;
        }
        requestData(pageIndex);
    }

    /**上拉加载*/
    @Override
    public void onLoadMore() {
        if (!isLogin()){
            return;
        }
        if (!checkNetworkState()){
            showToast(R.string.notHaveNet);
            rvActivityForceInfoList.setRefreshing(false);
            return;
        }
        pageIndex++;
        requestData(pageIndex);
    }

    /**请求数据*/
    private void requestData(int index) {
        Map<String, Object> map = new HashMap<>();
        map.put("page", index);
        map.put("rows", StaticDatas.PAGE_SIZE);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.getCalculateDetailList(Preferences.getAccessToken(),map, new getBeanCallback<CalculateDetail>() {
            @Override
            public void onSuccess(CalculateDetail calculateDetail) {
                List<CalculateDetail.ListBean> list = calculateDetail.getList();
                if (list == null || list.size() <= 0) {
                    /*再无更多数据*/
                    adapter.stopMore();
                    return;
                }
                adapter.addAll(list);
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
                /*网络错误*/
                adapter.pauseMore();
            }
        });
    }
}
