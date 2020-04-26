package app.com.pgy.Activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.decoration.DividerDecoration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.com.pgy.Activitys.Base.BaseListActivity;
import app.com.pgy.Adapters.RenGouRecordAdapter;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Constants.StaticDatas;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Models.RenGouRecord;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Utils.MathUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 认购记录
 */
public class RenGouListActivity extends BaseListActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.activity_baselist_list)
    EasyRecyclerView easyRecyclerView;
    @BindView(R.id.view_line)
    View viewLine;
    private RenGouRecordAdapter adapter;
    private int id;

    @Override
    public int getContentViewId() {
        return R.layout.activity_base_list;
    }

    @Override
    protected void initData() {
        if (adapter == null) {
            adapter = new RenGouRecordAdapter(mContext);
        }
        id = getIntent().getIntExtra("id", 0);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        tvTitle.setText("认购记录");
        viewLine.setVisibility(View.GONE);
        init(easyRecyclerView, adapter);
        easyRecyclerView.addItemDecoration(new DividerDecoration(getResources().getColor(R.color.divider_line), MathUtils.dip2px(mContext, 1)));
    }

    @Override
    protected void onListItemClick(int position) {

    }

    @Override
    public void onRefresh() {
        adapter.clear();
        if (!isLogin()) {
            return;
        }
        pageIndex = StaticDatas.PAGE_START;
        if (!checkNetworkState()) {
            showToast(R.string.notHaveNet);
            easyRecyclerView.setRefreshing(false);
            return;
        }
        requestData(pageIndex);
    }

    private void requestData(int index) {
        String accessToken = Preferences.getAccessToken();
        Map<String, Object> map = new HashMap<>();
        map.put("buyingId", id);//期数
        map.put("page", index);
        map.put("rows", StaticDatas.PAGE_SIZE);
        NetWorks.getRenGouRecord(accessToken, map, new getBeanCallback<List<RenGouRecord>>() {
            @Override
            public void onSuccess(List<RenGouRecord> list) {
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
                adapter.pauseMore();
            }
        });
    }

    @Override
    public void onLoadMore() {
        if (!isLogin()) {
            return;
        }
        if (!checkNetworkState()) {
            showToast(R.string.notHaveNet);
            easyRecyclerView.setRefreshing(false);
            return;
        }
        pageIndex++;
        requestData(pageIndex);
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        RenGouListActivity.this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
