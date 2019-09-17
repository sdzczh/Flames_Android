package huoli.com.pgy.im.UI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Switch;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import huoli.com.pgy.Activitys.Base.BaseActivity;
import huoli.com.pgy.Constants.Preferences;
import huoli.com.pgy.Constants.StaticDatas;
import huoli.com.pgy.Interfaces.getBeanCallback;
import huoli.com.pgy.Models.Beans.MuteResult;
import huoli.com.pgy.NetUtils.NetWorks;
import huoli.com.pgy.R;
import huoli.com.pgy.Utils.ImageLoaderUtils;
import huoli.com.pgy.Utils.LogUtils;
import huoli.com.pgy.Utils.TimeUtils;
import huoli.com.pgy.Widgets.RoundImageView;
import huoli.com.pgy.Widgets.TitleView;
import huoli.com.pgy.im.SealAppContext;
import huoli.com.pgy.im.server.broadcast.BroadcastManager;
import huoli.com.pgy.im.server.response.GetGroupDetailsResponse;
import huoli.com.pgy.im.server.utils.OperationRong;
import huoli.com.pgy.im.server.utils.RongGenerate;
import huoli.com.pgy.im.server.utils.json.JsonMananger;
import huoli.com.pgy.im.widget.DemoGridView;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;

import static huoli.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * Created by AMing on 16/1/27.
 * Company RongCloud
 */
public class GroupDetailActivity extends BaseActivity {
    public static final int EXIT_GROUP = 0x1000;
    @BindView(R.id.activity_groupDetail_title)
    TitleView activityGroupDetailTitle;
    @BindView(R.id.activity_groupDetail_icon)
    RoundImageView activityGroupDetailIcon;
    @BindView(R.id.activity_groupDetail_groupName)
    TextView activityGroupDetailGroupName;
    @BindView(R.id.activity_groupDetail_desc)
    TextView activityGroupDetailDesc;
    @BindView(R.id.activity_groupDetail_number)
    TextView activityGroupDetailNumber;
    @BindView(R.id.activity_groupDetail_list)
    DemoGridView mGridView;
    @BindView(R.id.activity_groupDetail_switch)
    Switch activityGroupDetailSwitch;
    @BindView(R.id.activity_groupDetail_exit)
    TextView activityGroupDetailExit;
    private String groupId;
    private String headImg;
    private boolean isMuted;

    @Override
    public int getContentViewId() {
        return R.layout.activity_im_groupdetail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        activityGroupDetailTitle.setBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupDetailActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {
        //群组会话界面点进群组详情
        groupId = getIntent().getStringExtra("groupId");
        if (!TextUtils.isEmpty(groupId)) {
            //群组会话页进入
            showLoading(null);
            /*获取群信息和群成员列表*/
            getGroupDetails();
        }
        /*添加监听*/
        setGroupsInfoChangeListener();
    }

    private void getGroupDetails() {
        showLoading(null);
        Map<String, Object> map = new HashMap<>();
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        map.put("groupId", groupId);
        NetWorks.getChatRoomDetails(Preferences.getAccessToken(), map, new getBeanCallback<GetGroupDetailsResponse>() {
            @Override
            public void onSuccess(GetGroupDetailsResponse response) {
                hideLoading();
                initGroupData(response);
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
                /*网络错误*/
                hideLoading();
            }
        });
    }

    private void initGroupData(GetGroupDetailsResponse response) {
        headImg = response.getImgUrl();
        ImageLoaderUtils.displayCircle(mContext, activityGroupDetailIcon, headImg);
        activityGroupDetailGroupName.setText(response.getName());
        activityGroupDetailDesc.setText(response.getDecription());
        int memberNum = response.getNum();
        activityGroupDetailNumber.setText(memberNum + "人");
        mGridView.setAdapter(new GridAdapter(mContext, response.getList()));
        /*消息免打扰是否打开*/
        //isMuted = response.isMuted();
        /*初始化消息免打扰*/
        setGroupMute();
    }

    private void setGroupMute() {
        RongIM.getInstance().getConversationNotificationStatus(Conversation.ConversationType.GROUP, groupId, new RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus>() {
            @Override
            public void onSuccess(Conversation.ConversationNotificationStatus conversationNotificationStatus) {
                isMuted = conversationNotificationStatus == Conversation.ConversationNotificationStatus.DO_NOT_DISTURB;
                LogUtils.w(TAG,"setGroupMute:"+isMuted);
                activityGroupDetailSwitch.setChecked(isMuted);
                /*if (conversationNotificationStatus == Conversation.ConversationNotificationStatus.DO_NOT_DISTURB) {
                    activityGroupDetailSwitch.setChecked(true);
                } else {
                    activityGroupDetailSwitch.setChecked(false);
                }*/
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                isMuted = false;
                LogUtils.w(TAG,"setGroupMute:error");
            }
        });
    }

    @Override
    protected void onDestroy() {
        BroadcastManager.getInstance(this).destroy(SealAppContext.UPDATE_GROUP_NAME);
        BroadcastManager.getInstance(this).destroy(SealAppContext.UPDATE_GROUP_MEMBER);
        super.onDestroy();
    }


    class GridAdapter extends BaseAdapter {

        private List<GetGroupDetailsResponse.ListBean> list;
        Context context;


        public GridAdapter(Context context, List<GetGroupDetailsResponse.ListBean> list) {
            if (list.size() >= 10) {
                this.list = list.subList(0, 10);
            } else {
                this.list = list;
            }

            this.context = context;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_chatroom_detail_members, parent, false);
            }
            RoundImageView iv_avatar = convertView.findViewById(R.id.iv_avatar);
            TextView tv_username = convertView.findViewById(R.id.tv_username);

            final GetGroupDetailsResponse.ListBean bean = list.get(position);
            tv_username.setText(bean.getName());
            ImageLoaderUtils.displayCircle(mContext, iv_avatar, bean.getHeadPath());
            iv_avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UserDetailActivity.class);
                    intent.putExtra("friendPhone", bean.getPhone());
                    context.startActivity(intent);
                }

            });

            return convertView;
        }

        @Override
        public int getCount() {
            return list != null ? list.size() : 0;
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

    }

    private void setGroupsInfoChangeListener() {
        //有些权限只有群主有,比如修改群名称等,已经更新UI不需要再更新
        BroadcastManager.getInstance(this).addAction(SealAppContext.UPDATE_GROUP_NAME, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    String result = intent.getStringExtra("result");
                    if (result != null) {
                        try {
                            List<String> nameList = JsonMananger.jsonToBean(result, List.class);
                            if (nameList.size() != 3) {
                                return;
                            }
                            String groupID = nameList.get(0);
                            if (groupID != null && !groupID.equals(groupId)) {
                                return;
                            }
                            String groupName = nameList.get(1);
                            String operationName = nameList.get(2);
                            activityGroupDetailGroupName.setText(groupName);
                            showToast(operationName + context.getString(R.string.rc_item_change_group_name)
                                    + "\"" + groupName + "\"");
                            RongIM.getInstance().refreshGroupInfoCache(new Group(groupId, groupName, TextUtils.isEmpty(headImg) ? Uri.parse(RongGenerate.generateDefaultAvatar(groupName, groupID)) : Uri.parse(headImg)));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        });
        BroadcastManager.getInstance(this).addAction(SealAppContext.UPDATE_GROUP_MEMBER, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    String groupID = intent.getStringExtra("String");
                    if (groupID != null && groupID.equals(groupId)) {
                        getGroupDetails();
                    }
                }
            }
        });

    }

    @OnClick({R.id.activity_groupDetail_number, R.id.activity_groupDetail_switch, R.id.activity_groupDetail_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.activity_groupDetail_number:
                Intent intent = new Intent(mContext, TotalGroupMemberActivity.class);
                intent.putExtra("targetId", groupId);
                startActivity(intent);
                break;
            case R.id.activity_groupDetail_switch:
                if (!isLogin()) {
                    showToast(R.string.unlogin);
                    activityGroupDetailSwitch.setChecked(false);
                    return;
                }
                //changeSwitch();
                LogUtils.w(TAG,"changeClick:"+isMuted);
                OperationRong.setConverstionNotif(mContext, Conversation.ConversationType.GROUP, groupId, !isMuted);
                setGroupMute();
                break;
                /*退出群组*/
            case R.id.activity_groupDetail_exit:
                exitGroup();
                break;
            default:
                break;
        }
    }

    /**
     * 退出群组聊天室
     */
    private void exitGroup() {
        Map<String, Object> map = new HashMap<>();
        map.put("groupId", groupId);
        map.put("type", StaticDatas.IMGROUP_CHATROOM);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", StaticDatas.SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.exitGroup(Preferences.getAccessToken(), map, new getBeanCallback() {
            @Override
            public void onSuccess(Object o) {
                showToast("退出成功");
                RongIM.getInstance().removeConversation(Conversation.ConversationType.GROUP, groupId, new RongIMClient.ResultCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        Log.e("SealAppContext", "Conversation remove successfully.");
                        Intent intent = getIntent();
                        intent.putExtra("isExitGroup",true);
                        setResult(EXIT_GROUP,intent);
                        GroupDetailActivity.this.finish();
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode e) {

                    }
                });
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
            }
        });
    }

    /**
     * 消息免打扰关闭或开启
     */
    private void changeSwitch() {
        showLoading(null);
        Map<String, Object> map = new HashMap<>();
        map.put("groupId", groupId);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());

        NetWorks.imTalkMute(Preferences.getAccessToken(), map, new getBeanCallback<MuteResult>() {
            @Override
            public void onSuccess(MuteResult muteResult) {
                hideLoading();
                if (muteResult == null) {
                    muteResult = new MuteResult();
                }
                isMuted = muteResult.isMuted();
                activityGroupDetailSwitch.setChecked(isMuted);
            }

            @Override
            public void onError(int errorCode, String reason) {
                hideLoading();
                activityGroupDetailSwitch.setChecked(isMuted);
                onFail(errorCode, reason);
            }
        });
    }
}
