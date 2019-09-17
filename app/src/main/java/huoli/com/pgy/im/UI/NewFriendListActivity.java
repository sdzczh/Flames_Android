package huoli.com.pgy.im.UI;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import huoli.com.pgy.Activitys.Base.BaseActivity;
import huoli.com.pgy.Constants.Preferences;
import huoli.com.pgy.Constants.StaticDatas;
import huoli.com.pgy.Interfaces.getBeanCallback;
import huoli.com.pgy.NetUtils.NetWorks;
import huoli.com.pgy.R;
import huoli.com.pgy.Utils.TimeUtils;
import huoli.com.pgy.Widgets.TitleView;
import huoli.com.pgy.im.SealAppContext;
import huoli.com.pgy.im.SealUserInfoManager;
import huoli.com.pgy.im.adapter.NewFriendListAdapter;
import huoli.com.pgy.im.db.Friend;
import huoli.com.pgy.im.server.broadcast.BroadcastManager;
import huoli.com.pgy.im.server.pinyin.CharacterParser;
import huoli.com.pgy.im.server.response.UserRelationshipResponse;

import static huoli.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;


public class NewFriendListActivity extends BaseActivity implements NewFriendListAdapter.OnItemButtonClick {

    @BindView(R.id.activity_newFriend_title)
    TitleView activityNewFriendTitle;
    @BindView(R.id.activity_newFriend_isEmpty)
    TextView activityNewFriendIsEmpty;
    @BindView(R.id.activity_newFriend_list)
    ListView activityNewFriendList;

    private NewFriendListAdapter adapter;

    @Override
    public int getContentViewId() {
        return R.layout.activity_im_new_friendlist;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        activityNewFriendTitle.setBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewFriendListActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {
        if (adapter == null) {
            adapter = new NewFriendListAdapter(mContext);
        }
        getAllNewFriends();
        activityNewFriendList.setAdapter(adapter);
        adapter.setOnItemButtonClick(this);
    }

    /**
     * 从服务器获取新的朋友
     */
    private void getAllNewFriends() {
        if (!isLogin()){
            showToast(R.string.unlogin);
            return;
        }
        showLoading(null);
        Map<String, Object> map = new HashMap<>();
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.getNewFriendList(Preferences.getAccessToken(), map, new getBeanCallback<UserRelationshipResponse>() {
            @Override
            public void onSuccess(UserRelationshipResponse response) {
                hideLoading();
                List<UserRelationshipResponse.ListBean> list = response.getList();
                if (list == null || list.size() <= 0) {
                    /*再无更多数据*/
                    activityNewFriendIsEmpty.setVisibility(View.VISIBLE);
                    return;
                }
                adapter.removeAll();
                adapter.addData(list);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
                /*网络错误*/
                activityNewFriendIsEmpty.setVisibility(View.VISIBLE);
                hideLoading();
            }
        });
    }


    @Override
    public boolean onAgreeClick(int position, View view, int status) {
        switch (status) {
            case 0: //同意好友邀请
                UserRelationshipResponse.ListBean bean = (UserRelationshipResponse.ListBean)adapter.getItem(position);
                agreeFriend(bean,true);
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public boolean onRefuseClick(int position, View view, int status) {
        switch (status) {
            case 0: //拒绝好友邀请
                UserRelationshipResponse.ListBean bean = (UserRelationshipResponse.ListBean)adapter.getItem(position);
                agreeFriend(bean,false);
                break;
            default:
                break;
        }
        return false;
    }


    /**
     * 同意好友请求
     */
    private void agreeFriend(final UserRelationshipResponse.ListBean bean, final boolean isAgree) {
        /*审核好友申请*/
        showLoading(null);
        Map<String, Object> map = new HashMap<>();
        map.put("applyId", bean.getId());
        map.put("state", isAgree?1:2);
        map.put("type", StaticDatas.IMGROUP_CHATROOM);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", StaticDatas.SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.checkIMFriend(Preferences.getAccessToken(), map, new getBeanCallback() {
            @Override
            public void onSuccess(Object o) {
                hideLoading();
                if (!isAgree){
                    showToast("拒绝添加好友");
                    return;
                }
                getAllNewFriends(); //刷新 UI 按钮
                String headPath = bean.getHeadPath();
                SealUserInfoManager.getInstance().addFriend(new Friend(bean.getId()+"",
                        bean.getNickName(),
                        Uri.parse(TextUtils.isEmpty(headPath)?"":headPath),
                        bean.getNickName(),
                        String.valueOf(bean.getState()),
                        null,
                        null,
                        null,
                        CharacterParser.getInstance().getSpelling(bean.getNickName()),
                        CharacterParser.getInstance().getSpelling(bean.getNickName())));
                // 通知好友列表刷新数据
                showToast(R.string.agreed_friend);
                BroadcastManager.getInstance(mContext).sendBroadcast(SealAppContext.UPDATE_FRIEND);
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
                hideLoading();
            }
        });

    }
}