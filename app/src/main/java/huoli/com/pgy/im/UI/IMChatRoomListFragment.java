package huoli.com.pgy.im.UI;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.jude.easyrecyclerview.EasyRecyclerView;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import huoli.com.pgy.Constants.Preferences;
import huoli.com.pgy.Fragments.Base.BaseFragment;
import huoli.com.pgy.Interfaces.getBeanCallback;
import huoli.com.pgy.Interfaces.getPositionCallback;
import huoli.com.pgy.NetUtils.NetWorks;
import huoli.com.pgy.R;
import huoli.com.pgy.Utils.TimeUtils;
import huoli.com.pgy.im.SealUserInfoManager;
import huoli.com.pgy.im.adapter.ChatRoomListAdapter;
import huoli.com.pgy.im.server.response.GetGroupResponse;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Group;

import static huoli.com.pgy.Constants.StaticDatas.IMGROUP_CHATROOM;
import static huoli.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/***
 * @author xuqingzhong
 * 聊天室列表
 */
public class IMChatRoomListFragment extends BaseFragment implements getPositionCallback {
    private static IMChatRoomListFragment instance;
    @BindView(R.id.fragment_baseList_list)
    EasyRecyclerView recyclerView;
    private ChatRoomListAdapter adapter;

    public IMChatRoomListFragment() {
    }

    public static IMChatRoomListFragment newInstance() {
        if (instance == null) {
            instance = new IMChatRoomListFragment();
        }
        return instance;
    }


    @Override
    public int getContentViewId() {
        return R.layout.fragment_baselist;
    }


    @Override
    protected void initData() {
        if (adapter == null) {
            adapter = new ChatRoomListAdapter(mContext);
        }
        adapter.clear();
        requestData();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        /*添加Layout*/
        recyclerView.setLayoutManager(layoutManager);
        /*添加加载进度条*/
        recyclerView.setAdapter(adapter);
        adapter.setCallback(this);
    }

    @Override
    public void getPosition(int position) {
        GetGroupResponse.GroupEntity item = adapter.getItem(position);
        /*刷新群组信息*/
        Group groupInfo = new Group(item.getGroupId(), item.getName(), Uri.parse(item.getImgUrl()));
        RongIM.getInstance().refreshGroupInfoCache(groupInfo);
        /*开始群聊*/
        RongIM.getInstance().startGroupChat(mContext, item.getGroupId(), item.getName());
    }

    /**请求数据*/
    private void requestData() {
        showLoading(null);
        Map<String, Object> map = new HashMap<>();
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        map.put("type", IMGROUP_CHATROOM);
        NetWorks.getChatRoomList(Preferences.getAccessToken(), map, new getBeanCallback<GetGroupResponse>() {
            @Override
            public void onSuccess(GetGroupResponse response) {
                hideLoading();
                List<GetGroupResponse.GroupEntity> list = response.getList();
                if (list == null || list.size() <= 0) {
                    /*再无更多数据*/
                    recyclerView.showEmpty();
                    return;
                }
                adapter.addAll(list);
                SealUserInfoManager.getInstance().addGroups(list);
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
                /*网络错误*/
                recyclerView.showError();
                hideLoading();
            }
        });
    }

}
