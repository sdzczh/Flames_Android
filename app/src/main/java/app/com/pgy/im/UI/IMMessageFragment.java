package app.com.pgy.im.UI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.com.pgy.Constants.Preferences;
import app.com.pgy.Fragments.Base.BaseFragment;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Utils.TimeUtils;
import app.com.pgy.im.SealAppContext;
import app.com.pgy.im.SealUserInfoManager;
import app.com.pgy.im.adapter.ConversationListAdapterEx;
import app.com.pgy.im.db.Friend;
import app.com.pgy.im.server.broadcast.BroadcastManager;
import app.com.pgy.im.server.response.GetUserInfosResponse;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.message.ContactNotificationMessage;

import static app.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * 创建日期：2017/11/22 0022 on 上午 11:23
 * 描述:信息界面
 *
 * @author 徐庆重
 */
public class IMMessageFragment extends BaseFragment implements RongIMClient.ConnectionStatusListener {
    private static IMMessageFragment instance;
    ConversationListFragment fragmentMessageConversationList;
    private Conversation.ConversationType[] mConversationsTypes;

    public IMMessageFragment() {
    }

    public static IMMessageFragment newInstance() {
        if (instance == null) {
            instance = new IMMessageFragment();
        }
        return instance;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_im_message;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        initListener();
        initView();
        initData();
        initIM();
        return rootView;
    }

    private void initListener() {
        LogUtils.w(TAG, "initListener");
        RongIM.getInstance().setMessageAttachedUserInfo(false);
        RongIM.setConnectionStatusListener(this);//连接状态监听
        BroadcastManager.getInstance(mContext).addAction(SealAppContext.UPDATE_FRIEND, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String command = intent.getAction();
                if (!TextUtils.isEmpty(command)) {
                    updateFriends();
                }
            }
        });
    }

    /**
     * 连接成功，初始化IM
     */
    private void initIM() {
        String cacheToken = Preferences.getTalkToken();
        LogUtils.w(TAG, "cacheToken:" + cacheToken);
        if (!TextUtils.isEmpty(cacheToken)) {
            updateFriends();
            if (!RongIM.getInstance().getCurrentConnectionStatus().equals(ConnectionStatus.CONNECTED)) {
                /*im重连*/
                RongIM.connect(cacheToken, new RongIMClient.ConnectCallback() {
                    @Override
                    public void onTokenIncorrect() {
                        SealUserInfoManager.getInstance().reGetToken();
                        LogUtils.w(TAG, "重连，获取token失败");
                    }

                    @Override
                    public void onSuccess(String s) {
                        LogUtils.w(TAG, "重连，连接成功");
                        if (fragmentMessageConversationList != null) {
                            Uri uri = fragmentMessageConversationList.getUri();
                            LogUtils.w(TAG, "onRestoreUI时的Uri：" + uri.toString());
                            fragmentMessageConversationList.onRestoreUI();
                        } else {
                            initListener();
                            initView();
                            initData();
                        }
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode e) {
                        LogUtils.w(TAG, "重连，连接失败");
                    }
                });
            } else {
                /*im连接成功*/
                LogUtils.w(TAG, "初始化连接成功");
                if (fragmentMessageConversationList != null) {
                    //fragmentMessageConversationList.onRestoreUI();
                }
            }
        } else {
            LogUtils.w(TAG, "未登录");
            if (fragmentMessageConversationList != null) {
                fragmentMessageConversationList.onRestoreUI();
            }
        }
    }

    @Override
    protected void initData() {
        LogUtils.w(TAG, "initData");
        String talkToken = Preferences.getTalkToken();
        if (TextUtils.isEmpty(talkToken)) {
            return;
        }
        RongIM.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                if (conversations != null && conversations.size() > 0) {
                    LogUtils.w(TAG, "initData:会话列表数:" + conversations.size());
                    for (Conversation c : conversations) {
                        RongIM.getInstance().clearMessagesUnreadStatus(c.getConversationType(), c.getTargetId(), null);
                    }
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode e) {
            }
        }, mConversationsTypes);
        getConversationPush();// 获取 push 的 id 和 target
        getPushMessage();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    private void getConversationPush() {
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra("PUSH_CONVERSATIONTYPE") && intent.hasExtra("PUSH_TARGETID")) {
            final String conversationType = intent.getStringExtra("PUSH_CONVERSATIONTYPE");
            final String targetId = intent.getStringExtra("PUSH_TARGETID");
            RongIM.getInstance().getConversation(Conversation.ConversationType.valueOf(conversationType), targetId, new RongIMClient.ResultCallback<Conversation>() {
                @Override
                public void onSuccess(Conversation conversation) {
                    if (conversation != null) {
                        if (conversation.getLatestMessage() instanceof ContactNotificationMessage) { //好友消息的push
                            startActivity(new Intent(mContext, NewFriendListActivity.class));
                        } else {
                            Uri uri = Uri.parse("rong://" + mContext.getApplicationInfo().packageName).buildUpon().appendPath("conversation")
                                    .appendPath(conversationType).appendQueryParameter("targetId", targetId).build();
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(uri);
                            startActivity(intent);
                        }
                    }
                }

                @Override
                public void onError(RongIMClient.ErrorCode e) {

                }
            });
        }
    }

    /**
     * 得到不落地 push 消息
     */
    private void getPushMessage() {
        Intent intent = getActivity().getIntent();
        if (intent == null || intent.getData() == null) {
            return;
        }
        LogUtils.w(TAG, "Data:" + intent.getData().getScheme());
        if (intent.getData().getScheme().equals("rong")) {
            String path = intent.getData().getPath();
            if (path.contains("push_message")) {
                String cacheToken = Preferences.getTalkToken();
                if (TextUtils.isEmpty(cacheToken)) {
                    showToast(R.string.unlogin);
                    //startActivity(new Intent(mContext, LoginActivity.class));
                } else {
                    LogUtils.w(TAG, "connectStatus:" + RongIM.getInstance().getCurrentConnectionStatus());
                    if (!RongIM.getInstance().getCurrentConnectionStatus().equals(ConnectionStatus.CONNECTED)) {
                        showLoading(null);
                        RongIM.connect(cacheToken, new RongIMClient.ConnectCallback() {
                            @Override
                            public void onTokenIncorrect() {
                                hideLoading();
                            }

                            @Override
                            public void onSuccess(String s) {
                                hideLoading();
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode e) {
                                hideLoading();
                            }
                        });
                    }
                }
            }
        }
    }

    private void initView() {
        LogUtils.w(TAG, "initView");
        String talkToken = Preferences.getTalkToken();
        if (TextUtils.isEmpty(talkToken)) {
            return;
        }
        fragmentMessageConversationList = (ConversationListFragment) getChildFragmentManager().findFragmentById(R.id.fragment_message_conversationList);
        fragmentMessageConversationList.setAdapter(new ConversationListAdapterEx(RongContext.getInstance()));
        Uri uri = Uri.parse("rong://" + mContext.getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//设置群组会话是否聚合显示
                .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//设置公共服务号是否聚合显示
                .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//设置订阅号是否聚合显示
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")//设置系统是否聚合显示
                .build();
        mConversationsTypes = new Conversation.ConversationType[]{
                Conversation.ConversationType.PRIVATE,
                Conversation.ConversationType.GROUP,
                Conversation.ConversationType.PUBLIC_SERVICE,
                Conversation.ConversationType.APP_PUBLIC_SERVICE,
                Conversation.ConversationType.SYSTEM
        };
        fragmentMessageConversationList.setUri(uri);
        LogUtils.w(TAG, "initViewUri:" + uri.toString());
    }

    /**
     * 从服务器获取好友列表,并保存在本地
     */
    private void updateFriends() {
        LogUtils.w(TAG, "更新好友列表");
        Map<String, Object> map = new HashMap<>();
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.getMyFriendList(Preferences.getAccessToken(), map, new getBeanCallback<GetUserInfosResponse>() {
            @Override
            public void onSuccess(GetUserInfosResponse response) {
                List<GetUserInfosResponse.ResultEntity> list = response.getList();
                /*更新到本地数据库*/
                SealUserInfoManager.getInstance().openDB();
                List<Friend> friends = SealUserInfoManager.getInstance().addFriends(list);
                for (Friend friend :friends) {
                    RongIM.getInstance().refreshUserInfoCache(friend);
                }
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
            }
        });
    }

    @Override
    public void onDestroy() {
        BroadcastManager.getInstance(mContext).destroy(SealAppContext.UPDATE_FRIEND);
        super.onDestroy();
    }

    @Override
    public void onChanged(ConnectionStatus connectionStatus) {
        LogUtils.w(TAG, "ConnectionStatus onChanged = " + connectionStatus.getMessage());
        if (fragmentMessageConversationList != null) {
            //fragmentMessageConversationList.onRestoreUI();
        }
        switch (connectionStatus) {
            case CONNECTED://连接成功。

                break;
            case DISCONNECTED://断开连接。

                break;
            case CONNECTING://连接中。

                break;
            case NETWORK_UNAVAILABLE://网络不可用。

                break;
            case KICKED_OFFLINE_BY_OTHER_CLIENT:
                //用户账户在其他设备登录，本机会被踢掉线,账号异地登录
                LogUtils.w("exit","seal异地登录");
                //quit(true);
                /*if (Preferences.isLogin() && Preferences.clearAllUserData()) {
                    EventBus.getDefault().post(new EventLoginState(false));
                    MyWebSocket.getMyWebSocket().stopSocket();
                }*/
                break;
            default:
                break;
        }
    }

}