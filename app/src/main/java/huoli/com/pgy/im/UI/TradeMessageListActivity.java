package huoli.com.pgy.im.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.jude.easyrecyclerview.EasyRecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import huoli.com.pgy.Activitys.Base.BaseListActivity;
import huoli.com.pgy.Activitys.Base.WebDetailActivity;
import huoli.com.pgy.Constants.Preferences;
import huoli.com.pgy.Constants.StaticDatas;
import huoli.com.pgy.Interfaces.getBeanCallback;
import huoli.com.pgy.Models.Beans.ImSystemMessage;
import huoli.com.pgy.NetUtils.NetWorks;
import huoli.com.pgy.R;
import huoli.com.pgy.Utils.TimeUtils;
import huoli.com.pgy.Widgets.TitleView;
import huoli.com.pgy.im.adapter.TradeMessageListAdapter;

import static huoli.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;


/**
 * @author xuqingzhong
 * 交易消息列表详情
 */
public class TradeMessageListActivity extends BaseListActivity{
    /**标题*/
    @BindView(R.id.activity_baselist_title)
    TitleView titleView;
    /**初始化适配器和RecyclerView*/
    private TradeMessageListAdapter adapter;
    @BindView(R.id.activity_baselist_list)
    EasyRecyclerView recyclerView;

    @Override
    public int getContentViewId() {
        return R.layout.activity_baselist;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {
        if (adapter == null) {
            adapter = new TradeMessageListAdapter(mContext);
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        titleView.setTitle("交易通知");
        titleView.setBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TradeMessageListActivity.this.finish();
            }
        });
        /*继承BaseListFragment，初始化*/
        init(recyclerView, adapter);
    }

    /**点击某Item时候的操作*/
    @Override
    public void onListItemClick(int position) {
        ImSystemMessage.ListBean notice = adapter.getItem(position);
        Intent intent2Detail = new Intent(mContext, WebDetailActivity.class);
        intent2Detail.putExtra("title", "消息详情");
        intent2Detail.putExtra("url",notice.getUrl());
        startActivity(intent2Detail);
    }

    /**下拉刷新*/
    @Override
    public void onRefresh() {
        adapter.clear();
        pageIndex = StaticDatas.PAGE_START;
        if (!checkNetworkState()){
            showToast(R.string.notHaveNet);
            recyclerView.setRefreshing(false);
            return;
        }
        requestData(pageIndex);
    }

    /**上拉加载*/
    @Override
    public void onLoadMore() {
        if (!checkNetworkState()){
            showToast(R.string.notHaveNet);
            recyclerView.setRefreshing(false);
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
        NetWorks.getImSystemMessageList(Preferences.getAccessToken(),map, new getBeanCallback<ImSystemMessage>() {
            @Override
            public void onSuccess(ImSystemMessage notice) {
                List<ImSystemMessage.ListBean> list = notice.getList();
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