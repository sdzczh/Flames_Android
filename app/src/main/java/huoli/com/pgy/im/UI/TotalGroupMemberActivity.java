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
import huoli.com.pgy.Constants.Preferences;
import huoli.com.pgy.Constants.StaticDatas;
import huoli.com.pgy.Interfaces.getBeanCallback;
import huoli.com.pgy.NetUtils.NetWorks;
import huoli.com.pgy.R;
import huoli.com.pgy.Utils.TimeUtils;
import huoli.com.pgy.Widgets.TitleView;
import huoli.com.pgy.im.adapter.GroupMemberListAdapter;
import huoli.com.pgy.im.server.response.GetGroupMemberResponse;

import static huoli.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * Created by AMing on 16/7/1.
 * Company RongCloud
 */
public class TotalGroupMemberActivity extends BaseListActivity {
    @BindView(R.id.activity_baselist_title)
    TitleView title;
    @BindView(R.id.activity_baselist_list)
    EasyRecyclerView recyclerView;
    private GroupMemberListAdapter adapter;
    private String mGroupID;

    @Override
    public int getContentViewId() {
        return R.layout.activity_baselist;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        title.setTitle("群成员");
        title.setBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TotalGroupMemberActivity.this.finish();
            }
        });
        /*继承BaseListFragment，初始化*/
        init(recyclerView, adapter);
    }

    @Override
    protected void initData() {
        mGroupID = getIntent().getStringExtra("targetId");
        if (adapter == null) {
            adapter = new GroupMemberListAdapter(mContext);
        }
    }

    @Override
    protected void onListItemClick(int position) {
        /*点击成员，查看用户信息*/
        GetGroupMemberResponse.UserEntity item = adapter.getItem(position);
        Intent intent = new Intent(mContext, UserDetailActivity.class);
        intent.putExtra("friendPhone", item.getPhone());
        //intent.putExtra("type", CLICK_CONVERSATION_USER_PORTRAIT);
        //intent.putExtra("conversationType", Conversation.ConversationType.GROUP.getValue());
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        adapter.clear();
        if (!isLogin()){
            return;
        }
        pageIndex = StaticDatas.PAGE_START;
        if (!checkNetworkState()){
            showToast(R.string.notHaveNet);
            recyclerView.setRefreshing(false);
            return;
        }
        requestData(pageIndex);
    }

    @Override
    public void onLoadMore() {
        if (!isLogin()){
            return;
        }
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
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        map.put("groupId", mGroupID);
        map.put("page", index);
        map.put("rows", StaticDatas.PAGE_SIZE);
        NetWorks.getChatRoomMembers(Preferences.getAccessToken(), map, new getBeanCallback<GetGroupMemberResponse>() {
            @Override
            public void onSuccess(GetGroupMemberResponse response) {
                List<GetGroupMemberResponse.UserEntity> list = response.getList();
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
